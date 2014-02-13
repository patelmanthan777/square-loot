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
	static Shader ambiantShader;
	static FBO[][] staticLightsFBO = new FBO[Map.textureNb][Map.textureNb];
	static FBO shadows = new FBO(Map.textureSize * Map.textureNb,
			Map.textureSize * Map.textureNb);
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
		lightShaderProgram = new Shader("light");
		laserShaderProgram = new Shader("laser");
		ambiantShader = new Shader("ambiant");
	}

	static public void reinit() {
		for (int i = 0; i < Map.textureNb; i++) {
			for (int j = 0; j < Map.textureNb; j++) {
				staticLightsFBO[i][j] = new FBO(Map.textureSize,
						Map.textureSize);
				shouldBeRendered[i][j] = true;
			}
		}				
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
			updateLightShadows(l);
		} else {
			activatedStaticLights.put(name, l);
			updateLightShadows(l);
		}

		return l;
	}

	static public void activateLight(String name, boolean dynamic) {
		Light l = deactivatedLights.remove(name);
		if (l != null) {
			if (dynamic) {
				activatedDynamicLights.put(name, l);
				updateLightShadows(l);
			} else {
				activatedStaticLights.put(name, l);
				updateLightShadows(l);
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

	static public Laser addLaser(String name, Vector2f p, Vector3f color,
			Vector2f dir) {
		Laser laser = new Laser(p, color, dir);

		ShadowBuffer[] shadows = new ShadowBuffer[Map.maxLayer];
		for (int i = 0; i < Map.maxLayer; i++) {
			shadows[i] = new ShadowBuffer();
		}

		lightShadows.put(laser, shadows);
		activatedDynamicLights.put(name, laser);
		updateLightShadows(laser);
		return laser;
	}

	static public void updateLaserIntersect(Laser l) {
		for (ShadowCaster sc : shadowCasters) {
			if (sc instanceof Map)
				((Map) sc).laserIntersect(l);
		}
	}

	static public void updateAllShadows() {
		for (Light l : activatedStaticLights.values()) {
			updateLightShadows(l);
		}
	}

	static public void updateLightShadows(Light l) {
		/* Set to 0 the pointer to the last shadow */
		ShadowBuffer[] shadows = lightShadows.get(l);
		for (int i = 0; i < Map.maxLayer; i++) {
			shadows[i].lastShadow = 0;
		}
		for (ShadowCaster sc : shadowCasters) {
			sc.computeShadow(l, lightShadows.get(l));
		}
	}

	static public void render() {
		renderAmbiantLight();
		renderStaticLights();

		for (int i = 0; i < Map.textureNb; i++) {
			for (int j = 0; j < Map.textureNb; j++) {
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				getFBO(i, j).use();
				glClearColor(0.0f, 0.0f, 0.0f, 0f);
				drawQuad(Map.currentBufferPosition.x + i * Map.textureSize,
						Map.currentBufferPosition.y + j * Map.textureSize,
						Map.textureSize, Map.textureSize);
				getFBO(i, j).unUse();
				glDisable(GL_BLEND);
			}
		}
		renderDynamicLights();
	}

	private static void drawMap(int i, int j, int textureId) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
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
		float posy = dynamic ? -l.getY() + camPos.y
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
				Vector2f inter = ((Laser) l).getIntersection();
				Vector2f pos = l.getPosition();
				laserShaderProgram.setUniform1f(
						"threshold",
						(float) Math.sqrt((inter.x - pos.x) * (inter.x - pos.x)
								+ (inter.y - pos.y) * (inter.y - pos.y)));
			}
		}
	}

	private static void drawShadows(Light l, int layer) {
		glColorMask(false, false, false, false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glBegin(GL_QUADS);
		ShadowBuffer[] shadows = lightShadows.get(l);
		for (int shadowLayer = layer; shadowLayer < Map.maxLayer; shadowLayer++) {
			if (shadows[shadowLayer] != null) {
				for (int i = 0; i < shadows[shadowLayer].lastShadow; i++) {
					Shadow s = shadows[shadowLayer].get(i);
					s.draw();
				}
			}
		}
		glEnd();
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		glStencilFunc(GL_EQUAL, 0, 1);
		glColorMask(true, true, true, true);
	}

	private static void renderAmbiantLight() {
		for (int i = 0; i < Map.textureNb; i++) {
			for (int j = 0; j < Map.textureNb; j++) {
				if (shouldBeRendered[i][j]) {
					// shouldBeRendered[i][j] = false;
					getFBO(i, j).bind();

					getFBO(i, j).unbind();
				}
			}
		}
	}

	private static void renderStaticLights() {
		for (int i = 0; i < Map.textureNb; i++) {
			for (int j = 0; j < Map.textureNb; j++) {
				if (shouldBeRendered[i][j]) {
					shouldBeRendered[i][j] = false;
					getFBO(i, j).bind();
					glPushMatrix();
					glLoadIdentity();
					glTranslatef(
							-(Map.currentBufferPosition.x + i * Map.textureSize),
							-(Map.currentBufferPosition.y + j * Map.textureSize),
							0);

					for (int layer = 0; layer < Map.maxLayer; layer++) {
						ambiantShader.use();
						ambiantShader.setUniform3f("color", 1.0f, 1.0f, 1.0f);
						ambiantShader.setUniform1f("power", 0.05f);
						ambiantShader.setUniform1i("texture", 0);

						drawMap(i, j, Map.getTextureID(i, j, layer));

						for (Light l : activatedStaticLights.values()) {

							bufferToLight.x = (Map.currentBufferPosition.x + i
									* Map.textureSize + Map.textureSize / 2)
									- l.getX();
							bufferToLight.y = (Map.currentBufferPosition.y + j
									* Map.textureSize + Map.textureSize / 2)
									- l.getY();

							if ((l instanceof Laser || bufferToLight.length()
									- ((PointLight) l).getMaxDst() < diagonal)) {

								drawShadows(l, layer);

								setUniforms(l, false, i, j);
								drawMap(i, j, Map.getTextureID(i, j, layer));
								glClear(GL_STENCIL_BUFFER_BIT);

							}
						}
					}
					glPopMatrix();
					getFBO(i, j).unbind();
				}
			}
		}
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

				drawShadows(l, layer);

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
		camPos.x = pos.x*ConfigManager.unitPixelSize;
		camPos.y = pos.y*ConfigManager.unitPixelSize;
	}

	private static void computeTextureDiagonal() {
		diagonal = (float) Math.sqrt(2 * Map.textureSize * Map.textureSize);
	}

	public static void needUpdate(int dx, int dy) {
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
		glColor3f(1, 1, 1);
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
		glVertex2f(x, y + sizey);
		glTexCoord2f(1.0f, 0.0f);
		glVertex2f(x + sizex, y + sizey);
		glTexCoord2f(1.0f, 1.0f);
		glVertex2f(x + sizex, y);
		glTexCoord2f(0.0f, 1.0f);
		glVertex2f(x, y);
		glEnd();
	}

	public static FBO getFBO(int i, int j) {
		return staticLightsFBO[(i + indx) % Map.textureNb][(j + indy)
				% Map.textureNb];
	}

	public static void needFBOUpdate(int i, int j) {
		shouldBeRendered[i][j] = true;
	}

}
