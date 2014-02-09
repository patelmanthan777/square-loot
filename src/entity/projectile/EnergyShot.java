package entity.projectile;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import light.Light;
import light.LightManager;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Shader;
import configuration.ConfigManager;

public class EnergyShot extends Projectile{
	private Light l;
	
	public EnergyShot(){
		super();
	}
	
	public EnergyShot(Vector2f pos, Vector2f rot, float speedValue, float size, int damage) {
		super(pos,rot,speedValue,size,damage);
		l = LightManager.addPointLight(null, new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 20,2*(int)ConfigManager.resolution.x,true);
	}
	
	@Override
	public void updatePostion() {
		super.updatePostion();
		l.setPosition(this.position);
	}
	
	@Override
	public void draw() {
	/*	glDisable(GL_TEXTURE_2D);
		GL11.glColor3f(1, 0, 1);
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
	*/	
	}

	@Override
	public Projectile Clone(Vector2f pos, Vector2f rot, float speedValue, float size, int damage) {
		// TODO Auto-generated method stub
		return new EnergyShot(pos, rot, speedValue, size, damage);
	}
	
}
