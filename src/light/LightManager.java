package light;

import environment.Map;

import java.util.HashMap;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import rendering.FBO;
import rendering.Shader;
import rendering.ShadowCaster;

public class LightManager {

	private static HashMap<String, Light> activatedDynamicLights = new HashMap<String, Light>();
	private static HashMap<String, Light> activatedStaticLights = new HashMap<String, Light>();
	private static HashMap<String, Light> deactivatedLights = new HashMap<String, Light>();

	private static LinkedList<ShadowCaster> shadowCasters = new LinkedList<ShadowCaster>();

	/**
	 * Stores the shadows to be drawn.
	 */
	private static HashMap<Light, ShadowBuffer[]> lightShadows = new HashMap<Light, ShadowBuffer[]>();
	private static Vector2f camPos = new Vector2f();

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static float diagonal = 0;

	static int lightShaderProgram;
	static int laserShaderProgram;
	static FBO staticLightsFBO;

	static boolean refreshStaticFBO = true;
	/* Avoid dynamic allocation in rendering methods */
	private static Vector2f camToLight = new Vector2f();
	private static Vector2f laserDirection = new Vector2f();

	/* --------------------------------------------- */

	static public void init() {
		staticLightsFBO = new FBO(Map.textureSize,Map.textureSize);
	}

	static public void addShadowCaster(ShadowCaster sc) {
		shadowCasters.add(sc);

		for (Light l : activatedDynamicLights.values()) {
			sc.computeShadow(l, lightShadows.get(l));
		}
		for (Light l : activatedStaticLights.values()) {
			if (sc instanceof Map) {
				boolean save = ((Map) sc).getFullRender();
				((Map) sc).setFullRender(true);
				sc.computeShadow(l, lightShadows.get(l));
				((Map) sc).setFullRender(save);
			}
		}
	}

	static public Light addLight(String name, Vector2f p, Vector3f color,
			float radius, float maxDst, boolean dynamic) {
		Light l = new Light(p, color, radius, maxDst, dynamic);
		l.setName(name);

		ShadowBuffer[] shadows = new ShadowBuffer[Map.maxLayer];
		for (int i = 0; i < Map.maxLayer; i++) {
			shadows[i] = new ShadowBuffer();
		}

		lightShadows.put(l, shadows);
		if (dynamic) {
			activatedDynamicLights.put(name, l);
			updateLightShadows(l, true);
		} else {
			activatedStaticLights.put(name, l);
			updateLightShadows(l, false);
		}

		return l;
	}

	static public void activateLight(String name, boolean dynamic) {
		Light l = deactivatedLights.remove(name);
		if (l != null) {
			if (dynamic) {
				activatedDynamicLights.put(name, l);
				updateLightShadows(l, true);
			} else {
				activatedStaticLights.put(name, l);
				updateLightShadows(l, false);
			}
		}
	}

	static public void deactivateLight(String name, boolean dynamic) {

		Light l;
		if (dynamic) {
			l = activatedDynamicLights.remove(name);
		} else {
			l = activatedStaticLights.remove(name);
		}
		if (l != null) {
			deactivatedLights.put(name, l);
		}
	}

	static public void deleteLight(String name) {
		Light l = activatedDynamicLights.remove(name);
		if (l == null) {
			l = deactivatedLights.remove(name);
		}
		if (l == null) {
			l = activatedStaticLights.remove(name);
		}
		if (l != null)
			lightShadows.remove(l);
	}

	static public void initLightShaders() {
		lightShaderProgram = glCreateProgram();
		Shader s = new Shader("light");
		s.loadCode();
		s.compile();
		s.link(lightShaderProgram);
	}

	static public void initLaserShader() {
		laserShaderProgram = glCreateProgram();
		Shader s = new Shader("laser");
		s.loadCode();
		s.compile();
		s.link(laserShaderProgram);
	}

	static public Laser addActivatedLaser(String name, Vector2f p,
			Vector3f color, Vector2f dir) {
		Laser laser = new Laser(p, color, dir);

		ShadowBuffer[] shadows = new ShadowBuffer[Map.maxLayer];
		for (int i = 0; i < Map.maxLayer; i++) {
			shadows[i] = new ShadowBuffer();
		}

		lightShadows.put(laser, shadows);
		activatedDynamicLights.put(name, laser);
		updateLightShadows(laser, true);
		return laser;
	}

	static public void updateLightShadows(Light l, boolean dynamic) {
		/* Set to 0 the pointer to the last shadow */
		ShadowBuffer[] shadows = lightShadows.get(l);
		for (int i = 0; i < Map.maxLayer; i++)
			shadows[i].lastShadow = 0;
		for (ShadowCaster sc : shadowCasters) {
			if (sc instanceof Map && !dynamic) {
				boolean save = ((Map) sc).getFullRender();
				((Map) sc).setFullRender(true);
				sc.computeShadow(l, lightShadows.get(l));
				((Map) sc).setFullRender(save);
			} else {
				sc.computeShadow(l, lightShadows.get(l));
			}
		}
	}

	/**
	 * Compute the FBO resulting from the static lights
	 */
	static private void renderStaticsToFrameBuffer() {

		staticLightsFBO.bind();
		
		renderStaticLights();
		
		staticLightsFBO.setUpdated(true);

		staticLightsFBO.unbind();
		
	}

	static public void render() {
		if (!staticLightsFBO.isUpdated()) {
			renderStaticsToFrameBuffer();
		}
		staticLightsFBO.use();
		glColor3f(1,1,1);
		glBegin(GL_QUADS);

		glTexCoord2f(0.0f, 0.0f);
		glVertex2f(Map.currentBufferPosition.x, Map.currentBufferPosition.y + Map.textureSize);
		glTexCoord2f(1.0f, 0.0f);
		glVertex2f(Map.currentBufferPosition.x + Map.textureSize, Map.currentBufferPosition.y + Map.textureSize);
		glTexCoord2f(1.0f, 1.0f);
		glVertex2f(Map.currentBufferPosition.x + Map.textureSize, Map.currentBufferPosition.y);
		glTexCoord2f(0.0f, 1.0f);
		glVertex2f(Map.currentBufferPosition.x, Map.currentBufferPosition.y);
		glEnd();

		renderDynamicLights();
		staticLightsFBO.unUse();
	}

	private static void drawMap(int textureId){
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glClearColor(0.0f, 0.0f, 0.0f, 1f);
		int tex_save = glGetInteger(GL_TEXTURE_BINDING_2D);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glActiveTexture(GL_TEXTURE0);
		glColor3f(1,1,1);
		glBegin(GL_QUADS);

		glTexCoord2f(0.0f, 0.0f);
		glVertex2f(Map.currentBufferPosition.x, Map.currentBufferPosition.y + Map.textureSize);
		glTexCoord2f(1.0f, 0.0f);
		glVertex2f(Map.currentBufferPosition.x + Map.textureSize, Map.currentBufferPosition.y + Map.textureSize);
		glTexCoord2f(1.0f, 1.0f);
		glVertex2f(Map.currentBufferPosition.x + Map.textureSize, Map.currentBufferPosition.y);
		glTexCoord2f(0.0f, 1.0f);
		glVertex2f(Map.currentBufferPosition.x, Map.currentBufferPosition.y);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, tex_save);
		glActiveTexture(GL_TEXTURE0);
		glDisable(GL_BLEND);
		glUseProgram(0);
		glClear(GL_STENCIL_BUFFER_BIT);
	}
	
	private static void setUniforms(Light l, boolean dynamic){
		/*
			float posx = dynamic ? l.getX() - camPos.x + (int) ConfigManager.resolution.x / 2 : l.getX();
			float posy = dynamic ? l.getY() - camPos.y  + (int) ConfigManager.resolution.y / 2 : -l.getY() + Map.mapPixelSize.y;
		*/
		
			float posx = dynamic ? l.getX() - camPos.x + (int) ConfigManager.resolution.x / 2 : l.getX() - Map.currentBufferPosition.x;
			float posy = dynamic ? l.getY() - camPos.y  + (int) ConfigManager.resolution.y / 2 : -l.getY() + Map.currentBufferPosition.y + Map.textureSize;
		
		if (l instanceof Light) {
			glUseProgram(lightShaderProgram);
			glUniform1f(
					glGetUniformLocation(lightShaderProgram,
							"light.maxDst"), l.getMaxDst());
			glUniform1f(
					glGetUniformLocation(lightShaderProgram,
							"light.radius"), l.getRadius());
			glUniform2f(
					glGetUniformLocation(lightShaderProgram,
							"light.position"), posx, posy);
			glUniform3f(
					glGetUniformLocation(lightShaderProgram,
							"light.color"), l.getColor().x,
					l.getColor().y, l.getColor().z);
			glUniform1i(
					glGetUniformLocation(lightShaderProgram, "texture"),
					0);
		}
		if (l instanceof Laser) {
			glUseProgram(laserShaderProgram);
			if (((Laser) l).getDirection().length() != 0) {

				laserDirection = ((Laser) l).getDirection();
				laserDirection.normalise(laserDirection);

				glUniform2f(
						glGetUniformLocation(laserShaderProgram,
								"laser.direction"), laserDirection.x,
						-laserDirection.y);
				glUniform2f(
						glGetUniformLocation(laserShaderProgram,
								"laser.position"), posx,
						posy);
				glUniform3f(
						glGetUniformLocation(laserShaderProgram,
								"laser.color"), l.getColor().x,
						l.getColor().y, l.getColor().z);
			}
		}
	}
	
	private static void initShadowDrawing(){
		glColorMask(false, false, false, false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glBegin(GL_QUADS);
	}
	
	private static void endShadowDrawing(){
		glEnd();
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		glStencilFunc(GL_EQUAL, 0, 1);
		glColorMask(true, true, true, true);
	}
	private static void drawShadows(Light l, int layer){
		ShadowBuffer[] shadows = lightShadows.get(l);
		for (int shadowLayer = layer; shadowLayer < Map.maxLayer-1; shadowLayer++) {
			if (shadows[shadowLayer] != null) {
				for (int i = 0; i < shadows[shadowLayer].lastShadow; i++) {
					Shadow s = shadows[shadowLayer].get(i);
					Vector2f[] points = s.points;
					{
						glVertex2f(points[0].x, points[0].y);
						glVertex2f(points[1].x, points[1].y);
						glVertex2f(points[3].x, points[3].y);
						glVertex2f(points[2].x, points[2].y);

					}
				}
			}
		}
	}
	private static void renderStaticLights() {
		glPushMatrix();
		glLoadIdentity();
		glTranslatef(-(Map.currentBufferPosition.x),-(Map.currentBufferPosition.y),0);
		for (int layer = 0; layer < Map.maxLayer; layer++) {
			for (Light l : activatedStaticLights.values()) {
				initShadowDrawing();
				drawShadows(l,layer);
				endShadowDrawing();
				setUniforms(l,false);
				drawMap(Map.getTextureID(layer));
			}
		}
		glPopMatrix();
	}
	private static void renderDynamicLights() {
		int layer = 0;
		for (Light l : activatedDynamicLights.values()) {
			Vector2f.sub(camPos, l.getPosition(), camToLight);

			if (camToLight.length() - l.getMaxDst() < diagonal / 4) {
				initShadowDrawing();
				drawShadows(l,layer);
				endShadowDrawing();
				setUniforms(l,true);
				drawMap(Map.getTextureID(layer));

			}
		}
	}

	public static void setCamPosition(Vector2f pos) {
		camPos.x = pos.x;
		camPos.y = pos.y;
	}

	public static void setScreenWidth(int width) {
		screenWidth = width;
		computeDiagonal();
	}

	public static void setScreenHeight(int height) {
		screenHeight = height;
		computeDiagonal();
	}

	private static void computeDiagonal() {
		diagonal = (float) Math.sqrt(screenHeight * screenHeight + screenWidth
				* screenWidth);
	}
	
	public static void needStaticUpdate(){
		staticLightsFBO.setUpdated(false);
	}
}
