package light;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Camera;
import rendering.Shader;

public class LightManager {

	private static HashMap<String, Light> activatedLight = new HashMap<String, Light>();
	private static HashMap<String, Light> desactivatedLight = new HashMap<String, Light>();

	int shaderProgram;
	
	private Camera cam;
	
	public LightManager(Camera cam){
		this.cam = cam;
	}
	
	public void addActivatedLight(String name, Vector2f p, Vector3f color,
			float radius) {
		Light l = new Light(p, color, radius);
		activatedLight.put(name, l);
		updateRendering();
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
		updateRendering();
	}

	public void deleteLight(String name) {
		if (activatedLight.remove(name) == null) {
			desactivatedLight.remove(name);
		}else{
			updateRendering();
		}
		
	}

	public void initLightShaders() {
		shaderProgram = GL20.glCreateProgram();
		Shader spec = new Shader("light");
		spec.loadCode();
		spec.compile();
		spec.link(shaderProgram);
		/* on defini notre program actif */
		GL20.glUseProgram(shaderProgram);
	}

	public void updateRendering() {
		int i = 0;
		/* on recupere les ID */
		for (Light l : activatedLight.values()) {
			
			int lpos = GL20.glGetUniformLocation(shaderProgram,
					"light["+i+"].position");
			int lcol = GL20.glGetUniformLocation(shaderProgram, "light["+i+"].color");
			int lrad = GL20.glGetUniformLocation(shaderProgram, "light["+i+"].radius");
			
			GL20.glUniform2f(lpos, l.getX(), l.getY());
			GL20.glUniform3f(lcol, l.getColor().x, l.getColor().y,
					l.getColor().z);
			GL20.glUniform1f(lrad, l.getRadius());	
			i++;
		}
		int lnbl = GL20.glGetUniformLocation(shaderProgram, "nbLights");
		GL20.glUniform1i(lnbl, activatedLight.size());
		int lcampos = GL20.glGetUniformLocation(shaderProgram, "cameraPosition");
		GL20.glUniform2f(lcampos, cam.getX(),cam.getY());
	}
}
