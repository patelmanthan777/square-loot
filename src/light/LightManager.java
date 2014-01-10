package light;

import environment.Map;

import java.util.HashMap;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
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
	private static float diagonal = 0;

	static Shader lightShaderProgram;
	static Shader laserShaderProgram;
	static FBO[][] staticLightsFBO = new FBO[Map.textureNb][Map.textureNb];
	private static boolean shouldBeRendered[][] = new boolean[Map.textureNb][Map.textureNb];

	static boolean refreshStaticFBO = true;
	/* Avoid dynamic allocation in rendering methods */
	private static Vector2f bufferToLight = new Vector2f();
	private static Vector2f laserDirection = new Vector2f();

	private static int indx = 0;
	private static int indy = 0;

	/* --------------------------------------------- */

	static public void init() {
		for (int i = 0; i < Map.textureNb; i++) {
			for (int j = 0; j < Map.textureNb; j++) {
				staticLightsFBO[i][j] = new FBO(Map.textureSize,
						Map.textureSize);
				shouldBeRendered[i][j] = true;
			}
		}
		computeTextureDiagonal();
	}

	static public void addShadowCaster(ShadowCaster sc) {
		shadowCasters.add(sc);

		for (Light l : activatedDynamicLights.values()) {
			sc.computeShadow(l, lightShadows.get(l));
		}
		for (Light l : activatedStaticLights.values()) {
			if (sc instanceof Map) {
				sc.computeShadow(l, lightShadows.get(l));
			}
		}
	}

	static public Light addPointLight(String name, Vector2f p, Vector3f color,
			float radius, float maxDst, boolean dynamic) {
		Light l = new PointLight(p, color, radius, maxDst, dynamic);
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
		lightShaderProgram = new Shader("light");
	}

	static public void initLaserShader() {
		laserShaderProgram = new Shader("laser");
	}

	static public Laser addLaser(String name, Vector2f p, Vector3f color,
			Vector2f dir) {
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
				sc.computeShadow(l, lightShadows.get(l));
			} else {
				sc.computeShadow(l, lightShadows.get(l));
			}
		}
	}

	/**
	 * Compute the FBO resulting from the static lights
	 */
	static private void renderStaticsToFrameBuffer(int i, int j) {

		getFBO(i, j).bind();

		renderStaticLights(i, j);

		getFBO(i, j).unbind();

	}

	static public void render() {
		for (int i = 0; i < Map.textureNb; i++) {
			for (int j = 0; j < Map.textureNb; j++) {

				if (shouldBeRendered[i][j]) {
					shouldBeRendered[i][j] = false;
					renderStaticsToFrameBuffer(i, j);
				}
				getFBO(i, j).use();
				glClearColor(0.0f, 0.0f, 0.0f, 1f);
				drawQuad(Map.currentBufferPosition.x + i * Map.textureSize,
						Map.currentBufferPosition.y + j * Map.textureSize,
						Map.textureSize, Map.textureSize);

				getFBO(i, j).unUse();

			}
		}
		renderDynamicLights();
	}

	private static void drawMap(int i, int j, int textureId) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glClearColor(0.0f, 0.0f, 0.0f, 1f);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glActiveTexture(GL_TEXTURE0);
		drawQuad(Map.currentBufferPosition.x + i * Map.textureSize,
				Map.currentBufferPosition.y + j * Map.textureSize,
				Map.textureSize, Map.textureSize);
		Shader.unuse();
		glBindTexture(GL_TEXTURE_2D, 0);
		glActiveTexture(GL_TEXTURE0);
		glDisable(GL_BLEND);

	}

	private static void setUniforms(Light l, boolean dynamic, int i, int j) {

		float posx = dynamic ? l.getX() - camPos.x
				+ (int) ConfigManager.resolution.x / 2 : l.getX()
				- (Map.currentBufferPosition.x + i * Map.textureSize);
		float posy = dynamic ? l.getY() - camPos.y
				+ (int) ConfigManager.resolution.y / 2 : -l.getY()
				+ Map.currentBufferPosition.y + j * Map.textureSize
				+ Map.textureSize;

		if (l instanceof PointLight) {
			lightShaderProgram.use();
			lightShaderProgram.setUniform1f("light.maxDst",
					((PointLight) l).getMaxDst());
			lightShaderProgram.setUniform1f("light.radius",
					((PointLight) l).getRadius());
			lightShaderProgram.setUniform2f("light.position", posx, posy);
			lightShaderProgram.setUniform3f("light.color", l.getColor().x,
					l.getColor().y, l.getColor().z);
			lightShaderProgram.setUniform1i("texture", 0);
		}
		if (l instanceof Laser) {
			laserShaderProgram.use();
			if (((Laser) l).getDirection().length() != 0) {
				laserDirection = ((Laser) l).getDirection();
				laserDirection.normalise(laserDirection);
				laserShaderProgram.setUniform2f("laser.direction",
						laserDirection.x, -laserDirection.y);
				laserShaderProgram.setUniform2f("laser.position", posx, posy);
				laserShaderProgram.setUniform3f("laser.color", l.getColor().x,
						l.getColor().y, l.getColor().z);
			}
		}
	}

	private static void initShadowDrawing() {
		glColorMask(false, false, false, false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glBegin(GL_QUADS);
	}

	private static void endShadowDrawing() {
		glEnd();
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		glStencilFunc(GL_EQUAL, 0, 1);
		glColorMask(true, true, true, true);
	}

	private static void drawShadows(Light l, int layer) {
		ShadowBuffer[] shadows = lightShadows.get(l);
		for (int shadowLayer = layer; shadowLayer < Map.maxLayer - 1; shadowLayer++) {
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

	private static void renderStaticLights(int i, int j) {
		glPushMatrix();
		glLoadIdentity();
		glTranslatef(-(Map.currentBufferPosition.x + i * Map.textureSize), -(Map.currentBufferPosition.y + j * Map.textureSize), 0);
		for (int layer = 0; layer < Map.maxLayer; layer++) {
			for (Light l : activatedStaticLights.values()) {
				bufferToLight.x = (Map.currentBufferPosition.x + i
						* Map.textureSize + Map.textureSize / 2)
						- l.getX();
				bufferToLight.y = (Map.currentBufferPosition.y + j
						* Map.textureSize + Map.textureSize / 2)
						- l.getY();

				if (l instanceof Laser
						|| bufferToLight.length()
								- ((PointLight) l).getMaxDst() < diagonal) {
					initShadowDrawing();
					drawShadows(l, layer);
					endShadowDrawing();
					setUniforms(l, false, i, j);
					drawMap(i, j, Map.getTextureID(i, j, layer));
					glClear(GL_STENCIL_BUFFER_BIT);
				}
			}
		}
		glPopMatrix();
	}

	private static void renderDynamicLights() {

		int layer = 0;

		for (Light l : activatedDynamicLights.values()) {
			bufferToLight.x = (Map.currentBufferPosition.x + (Map.textureNb
					* Map.textureSize / 2))
					- l.getX();
			bufferToLight.y = (Map.currentBufferPosition.y + (Map.textureNb
					* Map.textureSize / 2))
					- l.getY();

			if (l instanceof Laser
					|| bufferToLight.length() - ((PointLight) l).getMaxDst() < diagonal) {
				initShadowDrawing();
				drawShadows(l, layer);
				endShadowDrawing();
				for (int i = 0; i < Map.textureNb; i++) {
					for (int j = 0; j < Map.textureNb; j++) {
						setUniforms(l, true, 0, 0);
						drawMap(i, j, Map.getTextureID(i, j, layer));
					}
				}
			}
			glClear(GL_STENCIL_BUFFER_BIT);
		}
		
	}

	public static void setCamPosition(Vector2f pos) {
		camPos.x = pos.x;
		camPos.y = pos.y;
	}

	private static void computeTextureDiagonal() {
		diagonal = (float) Math.sqrt(2 * Map.textureSize * Map.textureSize);
	}

	public static void needStaticUpdate(int dx, int dy) {
		if (dx == -1) {
			indx = (indx - 1 + Map.textureNb) % Map.textureNb;
			for (int i = 0; i < Map.textureNb; i++) {
				shouldBeRendered[0][i] = true;
			}
		} else if (dx == 1) {

			for (int i = 0; i < Map.textureNb; i++) {
				shouldBeRendered[Map.textureNb - 1][i] = true;
			}
			indx = (indx + 1 + Map.textureNb) % Map.textureNb;
		}
		if (dy == -1) {
			indy = (indy - 1 + Map.textureNb) % Map.textureNb;
			for (int i = 0; i < Map.textureNb; i++) {
				shouldBeRendered[i][0] = true;
			}
		} else if (dy == 1) {
			for (int i = 0; i < Map.textureNb; i++) {
				shouldBeRendered[i][Map.textureNb - 1] = true;
			}
			indy = (indy + 1 + Map.textureNb) % Map.textureNb;
		}
	}

	public static void drawQuad(float x, float y, float sizex, float sizey) {
		//System.out.println("" + x + " " + y);
		glColor3f(1, 1, 1);
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
		glVertex2f(x, y + sizey);
		glTexCoord2f(1.0f, 0.0f);
		glVertex2f(x + sizex, y + sizey);
		glTexCoord2f(1.0f, 1.0f);
		glVertex2f(x + sizex,y);
		glTexCoord2f(0.0f, 1.0f);
		glVertex2f(x, y);
		glEnd();
	}

	public static FBO getFBO(int i, int j) {
		return staticLightsFBO[(i + indx) % Map.textureNb][(j + indy)
				% Map.textureNb];
	}

}
