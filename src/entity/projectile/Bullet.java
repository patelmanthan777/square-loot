package entity.projectile;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Shader;
import environment.Map;

public class Bullet extends Projectile {

/**
 * Save the shader program associated with the bullets. -1 means
 * not initialized. 
 */
	static private Shader bulletShaderProgram = null;
	
	private float speedValue = 2f;
	private Vector2f size = new Vector2f(10,10);

	/**
	 * Initialize the bullet shader
	 */
	static public void initBulletShader(){
		if(bulletShaderProgram == null)
		{
			bulletShaderProgram = new Shader("bullet");
		}
	}
	
	public Bullet() {
		super();
	}
	
	/**
	 * Bullet class constructor 
	 * @param pos
	 * @param rot
	 */
	public Bullet(Vector2f pos, Vector2f rot, float speedValue, Vector2f size) {
		super(pos,rot);
		Vector2f speed = new Vector2f();
		rot.normalise(speed);
		speed.scale(speedValue);
		this.speedValue = speedValue;
		this.size = new Vector2f(size);
		this.setSpeed(speed);
		this.setColor(new Vector3f(1,0,1));
	}
	
	@Override
	public void reset(Vector2f pos, Vector2f rot)
	{
		super.reset(pos,rot);
		Vector2f speed = new Vector2f();
		rot.normalise(speed);
		speed.scale(speedValue);
		this.setSpeed(speed);
	}

	/**
	 * Draw the bullet.
	 */
	@Override
	public void draw() {
		glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(color.x, color.y, color.z);
		Bullet.bulletShaderProgram.use();
		bulletShaderProgram.setUniform2f("bullet.position",this.position.x,this.position.y);
		bulletShaderProgram.setUniform2f("bullet.direction",this.direction.x,this.direction.y);
		bulletShaderProgram.setUniform1f("bullet.radius",size.x);
		bulletShaderProgram.setUniform1f("bullet.length",size.y);
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glBegin(GL_TRIANGLE_STRIP);
		glVertex2f(position.x + size.x, position.y - size.y);
		glVertex2f(position.x - size.x, position.y - size.y);
		glVertex2f(position.x + size.x, position.y + size.y);
		glVertex2f(position.x - size.x, position.y + size.y);
		glEnd();
		glDisable(GL_BLEND);
		Shader.unuse();
		glEnable(GL_TEXTURE_2D);
	}
	

	/**
	 * Test whether the bullet is in collision.
	 */
	@Override
	public boolean isInCollision(float x, float y, Map m) {
		return m.testCollision(x,y);
	}
	
	
	/**
	 * Update the position of the bullet.
	 */
	@Override
	public void updatePostion(long dt, Map m){
		float x_tmp = position.x + speed.x * dt;
		float y_tmp = position.y + speed.y * dt;

		if(!isInCollision(x_tmp, y_tmp, m)){
			setPosition(x_tmp, y_tmp);
		}
		else
		{
			destroyed = true;
		}
	}

	@Override
	public Projectile Clone(Vector2f pos, Vector2f rot) {		
		return new Bullet(pos, rot, this.speedValue, this.size);
	}
}
