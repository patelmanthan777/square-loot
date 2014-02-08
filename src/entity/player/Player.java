package entity.player;

import item.Item;
import item.ItemManager;
import light.Laser;
import light.Light;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import rendering.MiniMapDrawable;
import rendering.TextureManager;
import userInterface.MiniMap;
import userInterface.inventory.InventoryItemEnum;
import utils.GraphicsAL;
import entity.LivingEntity;
import environment.Map;


public class Player extends LivingEntity implements MiniMapDrawable {
	
	
	private int energy=0;	

	private Laser laser;
	private Light light;
	
	public Player(Vector2f pos, int inventorySize) {
		super(pos, inventorySize);
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(10);
	}
	public void setLight(Light l) {
		light = l;
		Vector2f p = new Vector2f(position.x * ConfigManager.unitPixelSize,
				position.y * ConfigManager.unitPixelSize);
		l.setPosition(p);
	}

	public void setLaser(Laser l) {
		laser = l;
		l.setDirection(getDirection());
		Vector2f p = new Vector2f(position.x * ConfigManager.unitPixelSize,
				position.y * ConfigManager.unitPixelSize);
		l.setPosition(p);
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
		glVertex2f(posx + persoRatio * MiniMap.roomSize.x, posy + persoRatio
				* MiniMap.roomSize.y);
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
			light.setPosition(posx * ConfigManager.unitPixelSize,
					posy * ConfigManager.unitPixelSize);
		}
		if (laser != null) {
			laser.setPosition(posx * ConfigManager.unitPixelSize,
					posy * ConfigManager.unitPixelSize);
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

	/*Energy handling*/
	public void charge(int enrgy){
		energy += Math.abs(enrgy);		
	}
	
	public boolean decharge(int enrgy){
		boolean hasEnoughEnergy = this.energy >= enrgy;
		
		if(hasEnoughEnergy)
			energy -= Math.abs(enrgy);
		
		return hasEnoughEnergy;
	}
	
	public void dropBattery(){
		Item tmp = inventory.remove(InventoryItemEnum.BATTERY);
		if (tmp != null){
			dropItem(tmp, position.x, position.y);			
		}
	}
}
