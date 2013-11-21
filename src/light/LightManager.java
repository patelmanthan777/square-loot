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
	private static HashMap<Light, LinkedList<Shadow>> shadows = new HashMap<Light, LinkedList<Shadow>>();

	int shaderProgram;

	private Camera cam;

	public LightManager(Camera cam) {
		this.cam = cam;
	}

	public void addShadowCaster(ShadowCaster sc) {
		shadowCasters.add(sc);
		for (Light l : activatedLight.values()) {
			shadows.put(l, sc.computeShadow(l));
		}
	}

	public void addActivatedLight(String name, Vector2f p, Vector3f color,
			float radius) {
		Light l = new Light(p, color, radius);
		activatedLight.put(name, l);
		updateShadows(l);
	}

	public void addDesactivatedLight(String name, Vector2f p, Vector3f color,
			float radius) {
		Light l = new Light(p, color, radius);
		desactivatedLight.put(name, l);
	}

	public void activateLight(String name) {
		Light l = desactivatedLight.remove(name);
		if (l != null) {
			activatedLight.put(name, l);
		}
	}

	public void desactivateLight(String name) {
		Light l = activatedLight.remove(name);
		if (l != null) {
			desactivatedLight.put(name, l);
		}
		shadows.remove(l);
	}

	public void deleteLight(String name) {
		if (activatedLight.remove(name) == null) {
			shadows.remove(desactivatedLight.remove(name));
		} else {
		}

	}

	public void initLightShaders() {
		shaderProgram = glCreateProgram();
		Shader s = new Shader("light");
		s.loadCode();
		s.compile();
		s.link(shaderProgram);
		/* on defini notre program actif */

	}

	public void setLightPosition(String lightName, Vector2f position) {
		Light l;
		if ((l = activatedLight.get(lightName)) != null) {
			l.setPosition(position);
			updateShadows(l);
		} else if ((l = desactivatedLight.get(lightName)) != null) {
			l.setPosition(position);
		}

	}

	public void addShadow(String lightName, Shadow s) {
		Light l;
		if ((l = activatedLight.get(lightName)) != null) {
			LinkedList<Shadow> sl = shadows.get(l);
			if (sl != null) {
				sl.add(s);
				updateShadows(l);
			}
		}
	}

	public Collection<Light> getActivatedLight() {
		return activatedLight.values();
	}

	public int getShaderProgram() {
		return shaderProgram;
	}

	public void addLightTaker(LightTaker lt) {
		lightTakers.add(lt);
	}

	public void updateShadows(Light l){
		shadows.remove(l);
		LinkedList<Shadow> sl = new LinkedList<Shadow>();
		for(ShadowCaster sc : shadowCasters){
			sl.addAll(sc.computeShadow(l));
		}
		shadows.put(l,sl);
	}
	
	public void render() {
		
		glClear(GL_COLOR_BUFFER_BIT);
		for (Light l : activatedLight.values()) {
			
			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
			
			
			
			
			LinkedList<Shadow> lsc = shadows.get(l);
			if (lsc != null) {
				for (Shadow s : lsc) {
					Vector2f[] points = s.points;
					//glColor3f(0,0,0);
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
			
			
			//glUniform2f(glGetUniformLocation(shaderProgram, "cameraPosition"), cam.getX(), cam.getY());

			glUseProgram(shaderProgram);
			glUniform1f(glGetUniformLocation(shaderProgram, "light.radius"), l.getRadius());
			glUniform2f(glGetUniformLocation(shaderProgram, "light.position"), l.getX(), l.getY());
			glUniform3f(glGetUniformLocation(shaderProgram, "light.color"), l.getColor().x, l.getColor().y, l.getColor().z);
			
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			//glColor3f(0, 0, 0);
			
			for (LightTaker lt : lightTakers) {
				lt.draw();
			}
			
			glDisable(GL_BLEND);
			
			glUseProgram(0);
			glClear(GL_STENCIL_BUFFER_BIT);
			
			
			
		}
	}
}
