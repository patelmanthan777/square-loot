package entity.player;

import item.Item;
import item.ItemManager;
import light.Laser;
import light.Light;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.MiniMapDrawable;
import rendering.TextureManager;
import userInterface.MiniMap;
import userInterface.inventory.InventoryItemEnum;
import utils.GraphicsAL;
import entity.LivingEntity;
import environment.Map;

public class Player extends LivingEntity implements MiniMapDrawable {
	
	
	
	private Laser laser;
	private Light light;
	
	
	public Player(Vector2f pos, int inventorySize) {
		super(pos, inventorySize);
		Vector3f col = new Vector3f(0, 0, 0);
		setColor(col);
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(10);
	}

	
	
	@Override
	public boolean isInCollision(float x, float y, Map m) {
		if (m.testCollision(x - halfSize.x, y - halfSize.y)
			|| m.testCollision(x + halfSize.x, y - halfSize.y)
			|| m.testCollision(x - halfSize.x, y + halfSize.y)
			|| m.testCollision(x + halfSize.x, y + halfSize.y)) {
			return true;
		}
		return false;
	}

	public void setLight(Light l) {
		light = l;
		l.setPosition(position);
	}

	public void setLaser(Laser l) {
		laser = l;
		l.setDirection(getDirection());
		l.setPosition(position);
	}

	@Override
	public void draw() {
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		GraphicsAL.drawQuadTexture(points,
				   				   GraphicsAL.fullTexPoints,
				   				   TextureManager.playerTexture().getTextureID());	
		glDisable(GL_BLEND);
	}

	@Override
	public void drawOnMiniMap() {
		float persoRatio = 0.5f;
		int posx = (int) (MiniMap.position.x + (getX() / Map.roomPixelSize.x)
				* MiniMap.roomSize.x - persoRatio * MiniMap.roomSize.x / 2);
		int posy = (int) (MiniMap.position.y + (getY() / Map.roomPixelSize.y)
				* MiniMap.roomSize.y - persoRatio * MiniMap.roomSize.y / 2);
		glColor3f(0, 1, 0);
		// draw quad
		glLoadIdentity();
		glBegin(GL_TRIANGLE_STRIP);
		glVertex2f(posx + persoRatio * MiniMap.roomSize.x, posy);
		glVertex2f(posx, posy);
		glVertex2f(posx + persoRatio * MiniMap.roomSize.x, posy
				+ persoRatio * MiniMap.roomSize.y);
		glVertex2f(posx, posy + persoRatio * MiniMap.roomSize.y);
		glEnd();
	}


	@Override
	public void setDirection(float orix, float oriy) {
		if(orix != 0 && oriy != 0){
			super.setDirection(orix, oriy);
			if (laser != null) {
				laser.setDirection(orix, oriy);
			}
			updatePoints();
		}
	}

	@Override
	public void setPosition(float posx, float posy) {
		super.setPosition(posx, posy);
		if (light != null) {
			light.setPosition(posx, posy);
		}
		if (laser != null) {
			laser.setPosition(posx, posy);
		}
		updatePoints();
	}

	public Light getLight() {
		return light;
	}
	
	private void dropItem(Item i, float x, float y){
		i.setPosition(x, y);
		ItemManager.add(i);
	}
	
	public void pickUp(Item i){
		Item tmp = inventory.add(i);
		if(tmp != null)
			dropItem(tmp, position.x, position.y);
	}

	
	public void primaryWeapon(float directionX, float directionY){
		inventory.equippedItemAction(InventoryItemEnum.PWEAPON, position.x, position.y,
									  							directionX, directionY);
	}

	public void dropBattery(){
		Item tmp = inventory.remove(InventoryItemEnum.BATTERY);
		if (tmp != null){
			dropItem(tmp, position.x, position.y);			
		}
	}
}
