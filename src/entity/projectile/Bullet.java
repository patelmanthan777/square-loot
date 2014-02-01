package entity.projectile;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import rendering.Shader;

public class Bullet extends Projectile {

/**
 * Save the shader program associated with the bullets. -1 means
 * not initialized. 
 */
	static private Shader bulletShaderProgram = null;

	/**
	 * Initialize the bullet shader
	 */
	static public void initBulletShader(){
		if(bulletShaderProgram == null)
		{
			bulletShaderProgram = new Shader("bullet");
		}
	}
	
	/**
	 * Bullet class constructor 
	 * @param pos
	 * @param rot
	 */
	public Bullet() {
		super();
		color = new Vector3f(1,0,1);
	}
	
	/**
	 * Bullet class constructor 
	 * @param pos
	 * @param rot
	 */
	public Bullet(Vector2f pos, Vector2f rot, float speedValue, float size) {
		super(pos,rot,speedValue,size);
		color = new Vector3f(1,0,1);
	}
	
	@Override
	public void reset(Vector2f pos, Vector2f rot, float speedValue, float size)
	{
		super.reset(pos,rot,speedValue,size);

	}

	/**
	 * Draw the bullet.
	 */
	@Override
	public void draw() {
		glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(color.x, color.y, color.z);
		Bullet.bulletShaderProgram.use();
		bulletShaderProgram.setUniform2f("bullet.position",this.position.x * ConfigManager.unitPixelSize,this.position.y * ConfigManager.unitPixelSize);
		bulletShaderProgram.setUniform2f("bullet.direction",getDirection().x,getDirection().y);
		bulletShaderProgram.setUniform1f("bullet.radius",size);
		bulletShaderProgram.setUniform1f("bullet.length",size);
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glBegin(GL_TRIANGLE_STRIP);
		glVertex2f((position.x + size) * ConfigManager.unitPixelSize, (position.y - size) * ConfigManager.unitPixelSize);
		glVertex2f((position.x - size) * ConfigManager.unitPixelSize, (position.y - size) * ConfigManager.unitPixelSize);
		glVertex2f((position.x + size) * ConfigManager.unitPixelSize, (position.y + size) * ConfigManager.unitPixelSize);
		glVertex2f((position.x - size) * ConfigManager.unitPixelSize, (position.y + size) * ConfigManager.unitPixelSize);
		glEnd();
		glDisable(GL_BLEND);
		Shader.unuse();
		glEnable(GL_TEXTURE_2D);
	}

	@Override
	public Projectile Clone(Vector2f pos, Vector2f rot, float speedValue, float size) {		
		return new Bullet(pos, rot, speedValue, size);
	}
}
