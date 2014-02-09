package entity.projectile;

import static org.lwjgl.opengl.GL11.*;
import light.Light;
import light.LightManager;

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
	private Light l;

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
	}
	
	/**
	 * Bullet class constructor 
	 * @param pos
	 * @param rot
	 */
	public Bullet(Vector2f pos, Vector2f rot, float speedValue, float size, int damage) {
		super(pos,rot,speedValue,size,damage);
		color = new Vector3f(1,1,0.8f);
		l = LightManager.addPointLight(this.toString(), new Vector2f(200, 200), color, 20,2*(int)ConfigManager.resolution.x,true);
	
	}

	@Override
	public void updatePostion() {
		super.updatePostion();
		l.setPosition(this.position.x* ConfigManager.unitPixelSize,this.position.y* ConfigManager.unitPixelSize);
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
	public Projectile Clone(Vector2f pos, Vector2f rot, float speedValue, float size, int damage) {		
		return new Bullet(pos, rot, speedValue, size, damage);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		l.deactivate();
	}
	
	@Override
	public void reset(Vector2f pos, Vector2f rot,  float speedValue, float size, int damage)
	{
		super.reset(pos, rot, speedValue, size, damage);
		l.activate();
		l.setPosition(pos.x*ConfigManager.unitPixelSize, pos.y*ConfigManager.unitPixelSize);
	}
}
