package light;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Camera;
import rendering.LightTaker;
import rendering.Shader;
import rendering.ShadowCaster;

public class LightManager {

	private static HashMap<String, Light> activatedLight = new HashMap<String, Light>();
	private static HashMap<String, Light> desactivatedLight = new HashMap<String, Light>();

	private static LinkedList<ShadowCaster> shadowCasters = new LinkedList<ShadowCaster>();
	private static LinkedList<LightTaker> lightTakers = new LinkedList<LightTaker>();

	private static HashMap<Light, LinkedList<Shadow>> lightShadows = new HashMap<Light, LinkedList<Shadow>>();

	private static HashMap<String, Laser> activatedLasers = new HashMap<String, Laser>();
	private static HashMap<String, Laser> desactivatedLasers = new HashMap<String, Laser>();
	private static HashMap<Laser, LinkedList<Shadow>> laserShadows = new HashMap<Laser, LinkedList<Shadow>>();

	static int lightShaderProgram;
	static int laserShaderProgram;


	static public void addShadowCaster(ShadowCaster sc) {
		shadowCasters.add(sc);
		for (Light l : activatedLight.values()) {
			lightShadows.put(l, sc.computeShadow(l));
		}
		for (Laser l : activatedLasers.values()) {
			laserShadows.put(l, sc.computeShadow(l));
		}
	}

	static public Light addActivatedLight(String name, Vector2f p, Vector3f color,
			float radius) {
		Light l = new Light(p, color, radius);
		l.setName(name);
		activatedLight.put(name, l);
		updateLightShadows(l);
		return l;
	}

	static public Light addDesactivatedLight(String name, Vector2f p, Vector3f color,
			float radius) {
		Light l = new Light(p, color, radius);
		l.setName(name);
		desactivatedLight.put(name, l);
		return l;
	}

	static public void activateLight(String name) {
		Light l = desactivatedLight.remove(name);
		if (l != null) {
			activatedLight.put(name, l);
		}
	}

	static public void desactivateLight(String name) {
		Light l = activatedLight.remove(name);
		if (l != null) {
			desactivatedLight.put(name, l);
		}
		lightShadows.remove(l);
	}

	static public void deleteLight(String name) {
		if (activatedLight.remove(name) == null) {
			lightShadows.remove(desactivatedLight.remove(name));
		} else {
		}

	}

	static public void initLightShaders() {
		lightShaderProgram = glCreateProgram();
		Shader s = new Shader("light");
		s.loadCode();
		s.compile();
		s.link(lightShaderProgram);
		/* on defini notre program actif */
	}
	
	static public void initLaserShader(){
		laserShaderProgram = glCreateProgram();
		Shader s = new Shader("laser");
		s.loadCode();
		s.compile();
		s.link(laserShaderProgram);
	}

	/*
	 * public void setLightPosition(String lightName, Vector2f position) { Light
	 * l; if ((l = activatedLight.get(lightName)) != null) {
	 * l.setPosition(position); updateShadows(l); } else if ((l =
	 * desactivatedLight.get(lightName)) != null) { l.setPosition(position); }
	 * 
	 * }
	 */

	/*********** LASERS ************/

	static public Laser addActivatedLaser(String name, Vector2f p, Vector3f color,
			Vector2f dir) {
		Laser laser = new Laser(p, color, dir);
		activatedLasers.put(name, laser);
		updateLaserShadows(laser);
		return laser;
	}

	static public Laser addDesactivatedLaser(String name, Vector2f p, Vector3f color,
			Vector2f dir) {
		Laser laser = new Laser(p, color, dir);
		desactivatedLasers.put(name, laser);
		return laser;
	}

	static public void activateLaser(String name) {
		Laser l = desactivatedLasers.remove(name);
		if (l != null) {
			activatedLasers.put(name, l);
		}
	}

	static public void desactivateLaser(String name) {
		Laser l = activatedLasers.remove(name);
		if (l != null) {
			desactivatedLasers.put(name, l);
		}
		laserShadows.remove(l);
	}

	/*
	 * public void addShadow(String lightName, Shadow s) { Light l; if ((l =
	 * activatedLight.get(lightName)) != null) { LinkedList<Shadow> sl =
	 * lightShadows.get(l); if (sl != null) { sl.add(s); updateShadows(l); } } }
	 */

	static public Collection<Light> getActivatedLight() {
		return activatedLight.values();
	}

	/*public int getShaderProgram() {
		return lightShaderProgram;
	}*/

	static public void addLightTaker(LightTaker lt) {
		lightTakers.add(lt);
	}

	static public void updateLightShadows(Light l) {
		lightShadows.remove(l);
		LinkedList<Shadow> sl = new LinkedList<Shadow>();
		for (ShadowCaster sc : shadowCasters) {
			sl.addAll(sc.computeShadow(l));
		}
		lightShadows.put(l, sl);
	}

	static public void updateLaserShadows(Laser l) {
		laserShadows.remove(l);
		LinkedList<Shadow> sl = new LinkedList<Shadow>();
		for (ShadowCaster sc : shadowCasters) {
			sl.addAll(sc.computeShadow(l));
		}
		laserShadows.put(l, sl);
	}

	static public void render() {

		glClear(GL_COLOR_BUFFER_BIT);
		for (Light l : activatedLight.values()) {

			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

			LinkedList<Shadow> lsc = lightShadows.get(l);
			if (lsc != null) {
				for (Shadow s : lsc) {
					Vector2f[] points = s.points;
					// glColor3f(0,0,0);
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

			// glUniform2f(glGetUniformLocation(shaderProgram,
			// "cameraPosition"), cam.getX(), cam.getY());

			glUseProgram(lightShaderProgram);
			glUniform1f(glGetUniformLocation(lightShaderProgram, "light.radius"),
					l.getRadius());
			glUniform2f(glGetUniformLocation(lightShaderProgram, "light.position"),
					l.getX(), l.getY());
			glUniform3f(glGetUniformLocation(lightShaderProgram, "light.color"),
					l.getColor().x, l.getColor().y, l.getColor().z);

			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			// glColor3f(0, 0, 0);

			for (LightTaker lt : lightTakers) {
				lt.draw();
			}

			glDisable(GL_BLEND);

			glUseProgram(0);
			glClear(GL_STENCIL_BUFFER_BIT);

		}
		for (Laser l : activatedLasers.values()) {

			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

			LinkedList<Shadow> lsc = laserShadows.get(l);
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

			// glUniform2f(glGetUniformLocation(shaderProgram,
			// "cameraPosition"), cam.getX(), cam.getY());

			glUseProgram(laserShaderProgram);
			if(l.getDirection().length() != 0){
			Vector2f direction = (Vector2f) l.getDirection().normalise();
			glUniform2f(glGetUniformLocation(laserShaderProgram, "laser.direction"),
					direction.x,direction.y);
			glUniform2f(glGetUniformLocation(laserShaderProgram, "laser.position"),
					l.getX(), l.getY());
			glUniform3f(glGetUniformLocation(laserShaderProgram, "laser.color"),
					l.getColor().x, l.getColor().y, l.getColor().z);

			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			// glColor3f(0, 0, 0);

			for (LightTaker lt : lightTakers) {
				lt.draw();
			}

			glDisable(GL_BLEND);
			}
			glUseProgram(0);
			glClear(GL_STENCIL_BUFFER_BIT);

		}
	}
}
