package light;

import environment.Map;

import java.util.HashMap;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import rendering.FBO;
import rendering.LightTaker;
import rendering.Shader;
import rendering.ShadowCaster;

public class LightManager {

	private static HashMap<String, Light> activatedDynamicLights = new HashMap<String, Light>();
	private static HashMap<String, Light> activatedStaticLights = new HashMap<String, Light>();
	private static HashMap<String, Light> desactivatedLights = new HashMap<String, Light>();

	private static LinkedList<ShadowCaster> shadowCasters = new LinkedList<ShadowCaster>();
	private static LinkedList<LightTaker> lightTakers = new LinkedList<LightTaker>();

	private static HashMap<Light, LinkedList<Shadow>> lightShadows = new HashMap<Light, LinkedList<Shadow>>();

	private static Vector2f camPos = null;
	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static float diagonal = 0;
	
	static int lightShaderProgram;
	static int laserShaderProgram;
	static FBO staticLightsFBO;

	static public void init() {
		staticLightsFBO = new FBO();
	}

	static public void addShadowCaster(ShadowCaster sc) {
		shadowCasters.add(sc);

		for (Light l : activatedDynamicLights.values()) {
			
			lightShadows.put(l, sc.computeShadow(l));
		}
		for (Light l : activatedStaticLights.values()) {
			if (sc instanceof Map) {
				boolean save = ((Map) sc).getFullRender();
				((Map) sc).setFullRender(true);
				lightShadows.put(l, sc.computeShadow(l));
				((Map) sc).setFullRender(save);
			}
		}
	}

	static public Light addLight(String name, Vector2f p, Vector3f color,
			float radius, float maxDst, boolean dynamic) {
		Light l = new Light(p, color, radius, maxDst,dynamic);
		l.setName(name);
		if (dynamic) {
			activatedDynamicLights.put(name, l);
			updateLightShadows(l,true);
		} else {
			activatedStaticLights.put(name, l);
			updateLightShadows(l,false);
		}
		
		return l;
	}

	static public void activateLight(String name, boolean dynamic) {
		Light l = desactivatedLights.remove(name);
		if (l != null) {
			if (dynamic) {
				activatedDynamicLights.put(name, l);
				updateLightShadows(l,true);
			} else {
				activatedStaticLights.put(name, l);
				updateLightShadows(l,false);
			}
		}
	}

	static public void desactivateLight(String name, boolean dynamic) {

		Light l;
		if (dynamic) {
			l = activatedDynamicLights.remove(name);
		} else {
			l = activatedStaticLights.remove(name);
		}
		if (l != null) {
			desactivatedLights.put(name, l);
			lightShadows.remove(l);
		}
	}

	static public void deleteLight(String name) {
		Light l = activatedDynamicLights.remove(name);
		if (l == null) {
			l = desactivatedLights.remove(name);
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
		activatedDynamicLights.put(name, laser);
		updateLightShadows(laser,true);
		return laser;
	}

	static public void addLightTaker(LightTaker lt) {
		lightTakers.add(lt);
	}

	static public void updateLightShadows(Light l, boolean dynamic) {
		lightShadows.remove(l);
		LinkedList<Shadow> sl = new LinkedList<Shadow>();
		for (ShadowCaster sc : shadowCasters) {
			if (sc instanceof Map && !dynamic) {
				boolean save = ((Map) sc).getFullRender();
				((Map) sc).setFullRender(true);
				sl.addAll(sc.computeShadow(l));
				((Map) sc).setFullRender(save);
			}else{
				sl.addAll(sc.computeShadow(l));
			}
		}
		lightShadows.put(l, sl);
	}

	static private void renderStaticsToFrameBuffer() {
		staticLightsFBO.bind();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, (int) Map.mapPixelSize.x, (int) Map.mapPixelSize.y, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glPushAttrib(GL11.GL_VIEWPORT_BIT);
		glViewport(0, 0, (int) Map.mapPixelSize.x, (int) Map.mapPixelSize.y);
		glPushMatrix();
		glLoadIdentity();
		glClearColor(0.0f, 0.0f, 0.0f, 1f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		renderStaticLights();
		
		glPopMatrix();
		glPopAttrib();
		staticLightsFBO.setUpdated(true);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, ConfigManager.resolution.x, ConfigManager.resolution.y, 0, 1, -1);
		staticLightsFBO.unbind();
		glMatrixMode(GL_MODELVIEW);
	}

	static public void render() {
		if (!staticLightsFBO.isUpdated()) {
			renderStaticsToFrameBuffer();
		}
		staticLightsFBO.use();
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
		glVertex2f(0f, Map.mapPixelSize.y);
		glTexCoord2f(1.0f, 0.0f);
		glVertex2f(Map.mapPixelSize.x, Map.mapPixelSize.y);
		glTexCoord2f(1.0f, 1.0f);
		glVertex2f(Map.mapPixelSize.x, 0f);
		glTexCoord2f(0.0f, 1.0f);
		glVertex2f(0f, 0f);
		glEnd();
		renderDynamicLights();
		staticLightsFBO.unUse();
		
	}

	private static void renderStaticLights() {
		for (Light l : activatedStaticLights.values()) {
			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

			LinkedList<Shadow> lsc = lightShadows.get(l);
			if (lsc != null) {
				for (Shadow s : lsc) {
					Vector2f[] points = s.points;
					glBegin(GL_TRIANGLE_STRIP);
					{
						glVertex2f(points[0].x, points[0].y);
						glVertex2f(points[1].x, points[1].y);
						glVertex2f(points[2].x, points[2].y);
						glVertex2f(points[3].x, points[3].y);
					}
					glEnd();
				}
			}

			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			glStencilFunc(GL_EQUAL, 0, 1);
			glColorMask(true, true, true, true);

			if (l instanceof Light) {
				glUseProgram(lightShaderProgram);
				glUniform1f(
						glGetUniformLocation(lightShaderProgram, "light.maxDst"),
						l.getMaxDst());
				glUniform1f(
						glGetUniformLocation(lightShaderProgram, "light.radius"),
						l.getRadius());
				glUniform2f(
						glGetUniformLocation(lightShaderProgram,
								"light.position"), l.getX(), -l.getY()
								+ Map.mapPixelSize.y);
				glUniform3f(
						glGetUniformLocation(lightShaderProgram, "light.color"),
						l.getColor().x, l.getColor().y, l.getColor().z);
				glUniform1i(
						glGetUniformLocation(lightShaderProgram, "texture"),
						0);
			}
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			glClearColor(0.0f, 0.0f, 0.0f, 1f);
			int tex_save =  glGetInteger(GL_TEXTURE_BINDING_2D);
			glBindTexture(GL_TEXTURE_2D, Map.getTextureID());
			glActiveTexture(GL_TEXTURE0);
			glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2f(0f, Map.mapPixelSize.y);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2f(Map.mapPixelSize.x, Map.mapPixelSize.y);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2f(Map.mapPixelSize.x, 0f);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2f(0f, 0f);
			glEnd();
			glBindTexture(GL_TEXTURE_2D, tex_save);
			glDisable(GL_BLEND);
			glUseProgram(0);
			glClear(GL_STENCIL_BUFFER_BIT);
		}
	}

	private static void renderDynamicLights() {
		for (Light l : activatedDynamicLights.values()) {
			if (Vector2f.sub(camPos, l.getPosition(), null).length()
					- l.getMaxDst() < diagonal / 4) {
				glColorMask(false, false, false, false);
				glStencilFunc(GL_ALWAYS, 1, 1);
				glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

				LinkedList<Shadow> lsc = lightShadows.get(l);
				if (lsc != null) {
					for (Shadow s : lsc) {
						Vector2f[] points = s.points;
						glBegin(GL_TRIANGLE_STRIP);
						{
							glVertex2f(points[0].x, points[0].y);
							glVertex2f(points[1].x, points[1].y);
							glVertex2f(points[2].x, points[2].y);
							glVertex2f(points[3].x, points[3].y);
						}
						glEnd();
					}
				}

				glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
				glStencilFunc(GL_EQUAL, 0, 1);
				glColorMask(true, true, true, true);

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
									"light.position"), l.getX() - camPos.x
									+ (int)ConfigManager.resolution.x / 2,
							(-l.getY() + camPos.y) + (int)ConfigManager.resolution.y / 2);
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
						Vector2f direction = (Vector2f) ((Laser) l)
								.getDirection().normalise();
						glUniform2f(
								glGetUniformLocation(laserShaderProgram,
										"laser.direction"), -direction.x,
								direction.y);
						glUniform2f(
								glGetUniformLocation(laserShaderProgram,
										"laser.position"), l.getX() - camPos.x
										+ (int)ConfigManager.resolution.x / 2,
								(-l.getY() + camPos.y) + (int)ConfigManager.resolution.y / 2);
						glUniform3f(
								glGetUniformLocation(laserShaderProgram,
										"laser.color"), l.getColor().x,
								l.getColor().y, l.getColor().z);
					}
				}

				glEnable(GL_BLEND);
				glBlendFunc(GL_ONE, GL_ONE);
				glClearColor(0.0f, 0.0f, 0.0f, 1f);
				int tex_save =  glGetInteger(GL_TEXTURE_BINDING_2D);
				glBindTexture(GL_TEXTURE_2D, Map.getTextureID());
				glActiveTexture(GL_TEXTURE0);
				glBegin(GL_QUADS);
				glTexCoord2f(0.0f, 0.0f);
				glVertex2f(0f, Map.mapPixelSize.y);
				glTexCoord2f(1.0f, 0.0f);
				glVertex2f(Map.mapPixelSize.x, Map.mapPixelSize.y);
				glTexCoord2f(1.0f, 1.0f);
				glVertex2f(Map.mapPixelSize.x, 0f);
				glTexCoord2f(0.0f, 1.0f);
				glVertex2f(0f, 0f);
				glEnd();
				glBindTexture(GL_TEXTURE_2D, tex_save);
				glDisable(GL_BLEND);
				glUseProgram(0);
				glClear(GL_STENCIL_BUFFER_BIT);

				glUseProgram(0);
				glClear(GL_STENCIL_BUFFER_BIT);
			}
		}
	}

	public static void setCamPosition(Vector2f pos) {
		camPos = pos;
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

}
