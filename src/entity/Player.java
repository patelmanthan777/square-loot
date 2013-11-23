package entity;

import light.Laser;
import light.Light;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import environment.Map;

public class Player extends Entity {

	private Vector2f halfSize = new Vector2f(10, 10);
	private Laser laser;
	private Light light;

	public Player(Vector2f pos) {
		super(pos);
		Vector3f col = new Vector3f(0, 0, 0);
		setColor(col);
		position.x = pos.x;
		position.y = pos.y;
		this.rotation.x = 1;
		this.rotation.y = 1;
	}

	@Override
	public boolean isInCollision(float x, float y, Map m) {
		if (m.testCollision(x - halfSize.x, y - halfSize.y)
				|| m.testCollision(x + halfSize.x, y - halfSize.y)
				|| m.testCollision(x - halfSize.x, y + halfSize.y)
				|| m.testCollision(x + halfSize.x, y + halfSize.y)) {
			return false;
		}
		return true;
	}

	public void setLight(Light l){
		light = l;
		l.setPosition(position);
	}
	
	public void setLaser(Laser l){
		laser = l;
		l.setOrientation(rotation);
		l.setPosition(position);
	}
	
	@Override
	public void draw() {

		// GL11.glColor3f(color.x,color.y,color.z);
		GL11.glColor3f(0, 0, 0);
		// draw quad
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(position.x + halfSize.x, position.y - halfSize.y);
		GL11.glVertex2f(position.x - halfSize.x, position.y - halfSize.y);
		GL11.glVertex2f(position.x + halfSize.x, position.y + halfSize.y);
		GL11.glVertex2f(position.x - halfSize.x, position.y + halfSize.y);
		GL11.glEnd();
	}
	
	@Override
	public void setOrientation(Vector2f ori){
		super.setOrientation(ori);
		if(laser != null){
			laser.setOrientation(ori);
		}
	}
	
	@Override
	public void setOrientation(float orix, float oriy){
		super.setOrientation(orix, oriy);
		if(laser != null){
			laser.setOrientation(orix,oriy);
		}
	}
	
	@Override
	public void setPosition(Vector2f pos){
		super.setPosition(pos);
		if(light != null){
			light.setPosition(pos);
		}
		if(laser != null){
			laser.setPosition(pos);
		}
	}
	
	@Override
	public void setPosition(float posx, float posy){
		super.setPosition(posx, posy);
		if(light != null){
			light.setPosition(posx,posy);
		}
		if(laser != null){
			laser.setPosition(posx,posy);
		}
	}

	public Light getLight(){
		return light;
	}
}
