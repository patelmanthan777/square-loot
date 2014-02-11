package entity.player;

import item.Item;
import item.Energy;
import item.ItemManager;
import item.weapon.Weapon;
import light.Laser;
import light.Light;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import physics.GameBodyType;
import physics.PhysicsDataStructure;
import configuration.ConfigManager;
import rendering.MiniMapDrawable;
import userInterface.MiniMap;
import userInterface.inventory.Inventory;
import userInterface.inventory.InventoryItemEnum;
import entity.LivingEntity;
import entity.npc.Npc;
import entity.npc.Shopkeeper;
import entity.npc.Zombie;
import environment.Map;
import environment.room.OxygenRoom;
import event.Timer;



public class Player extends LivingEntity implements MiniMapDrawable {
	
	
	private int energy=0;	

	private Laser laser;
	private Light light;
	private SpriteSheet headSprites;
	private Animation headAnimation;
	private int headAnimationFrame = -1;
	private SpriteSheet bodySprites;
	private SpriteSheet featherSprites;
	private Animation featherAnimation;
	private int pressure=0;
	private int oxygenConsumptionPerSecond = 25;
	
	private long apneaTimer = -1;
	private long apneaTimerMax = 10;
	
	private Item contactItem=null;
	private Npc contactNPC=null;

	
	protected Inventory inventory = null;
	
	public Player(Vector2f pos) {
		super(pos);
		this.gbtype = GameBodyType.PLAYER;
		this.updatePoints();
		this.setMaxHealth(20);
		this.setHealth(20);
		try {
			headSprites = new SpriteSheet("assets/textures/animperso.png",256,256);
			headAnimation = new Animation(headSprites, 600);
			headAnimation.setPingPong(true);
			bodySprites = new SpriteSheet("assets/textures/playerBody.png",256,256);
			featherSprites = new SpriteSheet("assets/textures/feather.png",256,256);
			featherAnimation = new Animation(featherSprites, 50);
			featherAnimation.setPingPong(true);
		} catch (SlickException e) {
			e.printStackTrace();
		}
				
		inventory = new Inventory(5,this);		
	}
	
	@Override
	public void updatePosition(long delta, Map m){
		super.updatePosition(delta, m);
		((Weapon)(this.inventory.access(InventoryItemEnum.PWEAPON))).setSpeed(this.speed.x,this.speed.y);
		this.pressure = (int) m.getRoom(this.getX(), this.getY()).getPressure();
		
		if(pressure == 0 && apneaTimer !=-1)
			updateApnea();
		else if(pressure == 0)
			apneaTimer = apneaTimerMax*Timer.unitInOneSecond + Timer.getTime();
		else
			apneaTimer = -1;
		
		headAnimation.update(delta);
		featherAnimation.update(delta);
		if(this.getSpeed().length() == 0 && this.getDeltaAngle() == 0){
			featherAnimation.setLooping(false);
		}else{
			featherAnimation.setLooping(true);
			featherAnimation.start();
			if(this.getSpeed().length() != 0){
				featherAnimation.setSpeed(4f);
			}else{
				featherAnimation.setSpeed(1f);
			}
		}
		if(headAnimationFrame != headAnimation.getFrame()){
			headAnimationFrame = headAnimation.getFrame();
			headAnimation.setDuration(headAnimationFrame, (int) (Math.random()*1000));
		}
		
		/* Oxygen consumption */
		m.getRoom(this.getX(), this.getY()).consumeOxygen((float)(delta * oxygenConsumptionPerSecond)/(float)(Timer.unitInOneSecond));
		
	}
	
	private void updateApnea(){
		if(apneaTimer < Timer.getTime())
			damage(1);				
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
		
		/* BODY */
		float speed = this.getSpeed().length();
		float zoomx =  (float) (5f * Math.cos(Timer.getTime()/400f));
		float zoomy = speed != 0 ? (float) (5f * Math.cos(Timer.getTime()/20f)): 0;
		Image tile = bodySprites.getSprite(Math.min((int)((float)pressure/(float)OxygenRoom.maxPressure*5),4), 0);
		tile.setCenterOfRotation(halfSize.x+zoomx/2f, halfSize.y+zoomy/2f);
		tile.setRotation(-(this.getDegreAngle()+90));
		int fact = 40;
		
		tile.draw(this.getX()*Map.blockPixelSize.x-halfSize.x + this.getDirection().x*fact-zoomx/2f, this.getY()*Map.blockPixelSize.y-halfSize.y + this.getDirection().y*fact-zoomy/2f,halfSize.x*2+zoomx,halfSize.y*2+zoomy);
		
		/* HEAD */
		tile = headAnimation.getCurrentFrame();
		tile.setCenterOfRotation(halfSize.x, halfSize.y);
		tile.setRotation(-(this.getDegreAngle()+90));
		tile.draw(this.getX()*Map.blockPixelSize.x-halfSize.x, this.getY()*Map.blockPixelSize.y-halfSize.y,halfSize.x*2,halfSize.y*2);
		
		/* FEATHER */
		tile = featherAnimation.getCurrentFrame();
		tile.setCenterOfRotation(halfSize.x, halfSize.y);
		tile.setRotation(-(this.getDegreAngle()+90));
		tile.draw(this.getX()*Map.blockPixelSize.x-halfSize.x, this.getY()*Map.blockPixelSize.y-halfSize.y,halfSize.x*2,halfSize.y*2);
		glDisable(GL_BLEND);
	}

	@Override
	public void drawOnMiniMap() {
		float persoRatio = 0.5f;
		int posx = (int) (MiniMap.position.x + (getX() / Map.roomBlockSize.x)
				* MiniMap.roomSize.x - persoRatio * MiniMap.roomSize.x / 2);
		int posy = (int) (MiniMap.position.y + (getY() / Map.roomBlockSize.y)
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
		ItemManager.add(i);
		i.setPosition(x, y);
	}
	
	public void pickUpOrBuy(){
		if(contactItem != null && pickUp(contactItem))
			contactItem.destroyed = true;
		else if	(contactNPC != null && contactNPC instanceof Shopkeeper){
			Item tmp = ((Shopkeeper) contactNPC).buy(this);
				if (tmp != null)
					pickUp(tmp);
		}
	}

	public boolean pickUp(Item i){
		Item tmp = inventory.add(i);
		if(tmp != null)
			dropItem(tmp, position.x, position.y);
		
		return tmp != i;
	}
	
	public void primaryWeapon(float directionX, float directionY){
		float x = (float) (position.x + (0.4f * Math.sqrt(2)) * directionX /Math.sqrt(directionX*directionX + directionY*directionY));
		float y = (float) (position.y + (0.4f * Math.sqrt(2)) * directionY /Math.sqrt(directionX*directionX + directionY*directionY));
		inventory.equippedItemAction(InventoryItemEnum.PWEAPON, x, y,
									  							directionX, directionY);
	}
	

	/*Energy handling*/
	public void charge(int enrgy){
		energy += Math.abs(enrgy);		
	}
	
	public boolean discharge(int enrgy){
		boolean hasEnoughEnergy = this.energy >= enrgy;
		
		if(hasEnoughEnergy)
			energy -= Math.abs(enrgy);
		
		return hasEnoughEnergy;
	}
	
	public void drop(){
		Item tmp;
		
		if (inventory.isCarryingKey())
			tmp = inventory.remove(InventoryItemEnum.KEY);
		else{
			tmp = inventory.remove(InventoryItemEnum.BATTERY);			
		}
		
		if (tmp != null){
			dropItem(tmp, position.x, position.y);			
		}
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		super.ContactHandler(a);
		switch(a.getType())
		{		
		case BATTERY:
		case ITEM:
			contactItem = (Item) a.getPhysicsObject();
			break;
		case ENERGY:
			charge(((Energy) a.getPhysicsObject()).getCharge());			
			break;
		case SHOPKEEPER:
			contactNPC = (Npc) a.getPhysicsObject();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void EndContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
			case SHOPKEEPER:
				if(contactNPC == a.getPhysicsObject())
					contactNPC = null;
				break;
			case BATTERY:
			case ITEM:
				if(contactItem == a.getPhysicsObject())
					contactItem = null;
		}
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public int getBatteriesNb(){
		return inventory.getBatteriesNb();
	}
	
	public int getPressure(){
		return pressure;
	}
	
	public void shootEnergy(){
		Vector2f p = new Vector2f(this.position.x+this.direction.x,this.position.y+this.direction.y);
		
		inventory.energyShot(p, this.direction);
	}
}
