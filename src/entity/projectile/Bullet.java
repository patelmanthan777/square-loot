package entity.projectile;


import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;

import environment.Map;

public class Bullet extends Projectile {
	private static float speedValue = 1f;
	private static Vector2f size = new Vector2f(10,10);
	private static float radius = 2.0f;

	
	public Bullet(Vector2f pos, Vector2f rot) {
		super(pos,rot);
		Vector2f speed = new Vector2f();
		rot.normalise(speed);
		speed.scale(speedValue);
		this.setSpeed(speed);
	}

	@Override
	public void draw() {
		glUseProgram(ProjectileManager.bulletShaderProgram);
		glUniform2f(glGetUniformLocation(ProjectileManager.bulletShaderProgram, "bullet.position"),this.position.x,this.position.y);
		glUniform1f(glGetUniformLocation(ProjectileManager.bulletShaderProgram, "bullet.radius"),radius);
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
		
		
		/*
		// GL11.glColor3f(color.x,color.y,color.z);
		GL11.glColor3f(0, 0, 0);
		// draw quad
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(position.x + 5, position.y - 5);
		GL11.glVertex2f(position.x - 5, position.y - 5);
		GL11.glVertex2f(position.x + 5, position.y + 5);
		GL11.glVertex2f(position.x - 5, position.y + 5);
		GL11.glEnd();
		// TODO Auto-generated method stub
*/
	}

	@Override
	public boolean isInCollision(float x, float y, Map m) {
		return m.testCollision(x,y);
	}
	
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
