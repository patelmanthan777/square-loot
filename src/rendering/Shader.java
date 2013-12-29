package rendering;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
	private String name;
	private StringBuilder vertexShaderSource = new StringBuilder();
	private StringBuilder fragmentShaderSource = new StringBuilder();

	int vertexShader;
	int fragmentShader;

	public Shader(String name) {
		this.name = name;
		vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	}

	public void loadCode() {
		loadCodeVertex();
		loadCodeFragment();
	}

	private void loadCodeVertex() {
		String fileName = "./assets/shaders/" + name + ".vert";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				vertexShaderSource.append(line).append('\n');
			}
			reader.close();
		} catch (IOException e) {
			String current;
			try {
				current = new java.io.File( "." ).getCanonicalPath();
				 System.out.println("Current dir:"+current);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.err.println("Vertex shader file : " + fileName
					+ " does not exist.");
			System.exit(1);
		}
		System.out.println(vertexShaderSource);
	}

	private void loadCodeFragment() {
		String fileName = "./assets/shaders/" + name + ".frag";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				fragmentShaderSource.append(line).append('\n');
			}
			reader.close();
		} catch (IOException e) {
			
			
	       
			System.err.println("Fragment shader file : " + fileName
					+ " does not exist.");
			System.exit(1);
		}
		System.out.println(fragmentShaderSource);
	}

	public void compile() {
		GL20.glShaderSource(vertexShader, vertexShaderSource);
		GL20.glCompileShader(vertexShader);
		if (GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Vertex shader : " + name
					+ " was not compiled correctly");
			String log = GL20.glGetShaderInfoLog(vertexShader,
					GL20.GL_INFO_LOG_LENGTH);
			System.out.println(log);
		}
		GL20.glShaderSource(fragmentShader, fragmentShaderSource);
		GL20.glCompileShader(fragmentShader);
		if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Fragment shader : " + name
					+ " was not compiled correctly");
			GL20.glGetShaderInfoLog(fragmentShader, GL20.GL_INFO_LOG_LENGTH);
			String log = GL20.glGetShaderInfoLog(fragmentShader,
					GL20.GL_INFO_LOG_LENGTH);
			System.out.println(log);
		}
	}
	
	/**
	 * Link the compiled shaders to be used in the next rendering
	 * tasks.
	 *  
	 * @param program is the program which is run on the graphic card
	 */
	public void link(int program){
		GL20.glAttachShader(program, vertexShader);
		GL20.glAttachShader(program, fragmentShader);
		GL20.glLinkProgram(program);
		GL20.glValidateProgram(program);
	}
}