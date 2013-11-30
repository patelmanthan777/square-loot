package entity.projectile;


import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import environment.Map;

public class Bullet extends Projectile {
	private static float speedValue = 2f;
	private static Vector2f size = new Vector2f(10,10);
	private static float radius = 10.0f;
	private static float length = 10.0f;
	
	/**
	 * Bullet class constructor 
	 * @param pos
	 * @param rot
	 */
	public Bullet(Vector2f pos, Vector2f rot) {
		super(pos,rot);
		Vector2f speed = new Vector2f();
		rot.normalise(speed);
		speed.scale(speedValue);
		this.setSpeed(speed);
		this.setColor(new Vector3f(1,0,1));
	}

	/**
	 * Draw the bullet
	 */
	@Override
	public void draw() {
		GL11.glColor3f(color.x, color.y, color.z);
		glUseProgram(ProjectileManager.bulletShaderProgram);
		glUniform2f(glGetUniformLocation(ProjectileManager.bulletShaderProgram, "bullet.position"),this.position.x,this.position.y);
		glUniform2f(glGetUniformLocation(ProjectileManager.bulletShaderProgram, "bullet.direction"),this.rotation.x,this.rotation.y);
		glUniform1f(glGetUniformLocation(ProjectileManager.bulletShaderProgram, "bullet.radius"),radius);
		glUniform1f(glGetUniformLocation(ProjectileManager.bulletShaderProgram, "bullet.length"),length);
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glBegin(GL_TRIANGLE_STRIP);
		glVertex2f(position.x + size.x, position.y - size.y);
		glVertex2f(position.x - size.x, position.y - size.y);
		glVertex2f(position.x + size.x, position.y + size.y);
		glVertex2f(position.x - size.x, position.y + size.y);
		glEnd();
		glDisable(GL_BLEND);
		glUseProgram(0);
	}
	

	/**
	 * Is the bullet in collision
	 */
	@Override
	public boolean isInCollision(float x, float y, Map m) {
		return m.testCollision(x,y);
	}
	
	
	/**
	 * Update the position of the bullet
	 */
	@Override
	public void updatePostion(float dt, Map m){
		float x_tmp = position.x + speed.x * dt;
		float y_tmp = position.y + speed.y * dt;

		if(!isInCollision(x_tmp, y_tmp, m)){
			setPosition(x_tmp, y_tmp);
		}
		else
		{
			toDestroy = true;
		}
	}
}
