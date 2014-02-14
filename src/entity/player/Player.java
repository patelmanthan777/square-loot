package entity.player;

import game.Game;
import item.Battery;
import item.Item;
import item.Energy;
import item.ItemManager;
import item.weapon.LaserRifle;
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
import rendering.TextureManager;
import sound.SoundManager;
import userInterface.MiniMap;
import userInterface.inventory.Inventory;
import userInterface.inventory.InventoryItemEnum;
import entity.LivingEntity;
import entity.npc.Npc;
import entity.npc.Shopkeeper;
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
	private int oxygenConsumptionPerSecond;
	
	private long apneaTimer = -1;
	private long apneaTimerMax = 10;
	private long damageApneaTimer = 1;
	private int apneaDamage = 1;
	
	private Item contactItem=null;
	private Npc contactNPC=null;

	
	protected Inventory inventory = null;
	
	public Player(Vector2f pos) {
		super(pos);
		oxygenConsumptionPerSecond = (int) ConfigManager.playerOxygenConsumption;
		this.gbtype = GameBodyType.PLAYER;
		this.setMaxHealth(20);
		this.setHealth(20);
		this.accFactor = ConfigManager.playerAcc;
		halfSize.set(0.4f, 0.4f);
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
	
	public void reinit(){		
		this.setHealth(getMaxHealth());
		inventory = new Inventory(5,this);
		initPhysics();
		this.energy = 0;
		pickUp(new LaserRifle(100,
				 0f,
				0f,
				100f,
				10,
				10));
		pickUp(new Battery(200,200));
		pickUp(new Battery(200,200));
		pickUp(new Battery(200,200));
		pickUp(new Battery(200,200));
		pickUp(new Battery(200,200));
	}
	
	@Override
	public void updatePosition(long delta, Map m){
		super.updatePosition(delta, m);
		((Weapon)(this.inventory.access(InventoryItemEnum.PWEAPON))).setSpeed(this.speed.x,this.speed.y);
		this.pressure = (int) m.getRoom(this.getX(), this.getY()).getPressure();
		
		if(pressure == 0){ 
			if(apneaTimer ==-1){
				apneaTimer = apneaTimerMax*Timer.unitInOneSecond + Timer.getTime();
			} else if(apneaTimer < Timer.getTime()){
				apneaTimer = damageApneaTimer*Timer.unitInOneSecond + Timer.getTime();
				damage(apneaDamage);
			}
		}else{
			apneaTimer = -1;
		}
		
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
		
		
		if(getHealth() == 0)
			Game.isAlive = false;
			
	}
	

	public void setLight(Light l) {
		light = l;
		Vector2f p = new Vector2f(position.x * ConfigManager.unitPixelSize,
				position.y * ConfigManager.unitPixelSize);
		l.setPosition(p);
	}

	public void setLaser(Laser l) {
		laser = l;
	}
	
	@Override
	public void draw() {
		float factor = 2f;
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor3f(1,1,1);
		
		/* BODY */
		float speed = this.getSpeed().length();
		float zoomx =  (float) (5f * Math.cos(Timer.getTime()/400f));
		float zoomy = speed != 0 ? (float) (5f * Math.cos(Timer.getTime()/20f)): 0;
		Image tile = bodySprites.getSprite(Math.min((int)((float)pressure/(float)OxygenRoom.maxPressure*5),4), 0);
		tile.setCenterOfRotation(halfSize.x*factor*ConfigManager.unitPixelSize+zoomx/2f, halfSize.y*factor*ConfigManager.unitPixelSize+zoomy/2f);
		tile.setRotation(-(this.getDegreAngle()+90));
		int fact = 40;
		float x = this.getX()*Map.blockPixelSize.x-halfSize.x*factor*ConfigManager.unitPixelSize + this.getDirection().x*fact-zoomx/2f;
		float y = this.getY()*Map.blockPixelSize.y-halfSize.y*factor*ConfigManager.unitPixelSize + this.getDirection().y*fact-zoomy/2f;
		tile.draw(x, y,halfSize.x*factor*2*ConfigManager.unitPixelSize+zoomx,halfSize.y*factor*2*ConfigManager.unitPixelSize+zoomy);
		
		
		x = this.getX()*Map.blockPixelSize.x-halfSize.x*factor*ConfigManager.unitPixelSize;
		y = this.getY()*Map.blockPixelSize.y-halfSize.y*factor*ConfigManager.unitPixelSize;
		
		/* HEAD */
		tile = headAnimation.getCurrentFrame();
		tile.setCenterOfRotation(halfSize.x*factor*ConfigManager.unitPixelSize, halfSize.y*factor*ConfigManager.unitPixelSize);
		tile.setRotation(-(this.getDegreAngle()+90));
		tile.draw(x, y, halfSize.x*factor*2*ConfigManager.unitPixelSize, halfSize.y*factor*2*ConfigManager.unitPixelSize);
		
		/* FEATHER */
		tile = featherAnimation.getCurrentFrame();
		tile.setCenterOfRotation(halfSize.x*factor*ConfigManager.unitPixelSize, halfSize.y*factor*ConfigManager.unitPixelSize);
		tile.setRotation(-(this.getDegreAngle()+90));
		tile.draw(x, y,halfSize.x*factor*2*ConfigManager.unitPixelSize,halfSize.y*factor*2*ConfigManager.unitPixelSize);
		glDisable(GL_BLEND);
	}

	@Override
	public void drawOnMiniMap() {
		float persoRatio = 0.8f;
		int posx = (int) (MiniMap.position.x + (getX() / Map.roomBlockSize.x)
				* MiniMap.roomSize.x - persoRatio * MiniMap.roomSize.x / 2);
		int posy = (int) (MiniMap.position.y + (getY() / Map.roomBlockSize.y)
				* MiniMap.roomSize.y - persoRatio * MiniMap.roomSize.y / 2);
		glColor3f(0, 1, 0);
		// draw quad
		glLoadIdentity();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		TextureManager.playerTexture().draw(posx,posy,persoRatio * MiniMap.roomSize.x,persoRatio * MiniMap.roomSize.y);
		glDisable(GL_BLEND);
	}

	@Override
	public void setDirection(float orix, float oriy) {
		if(orix != 0 && oriy != 0){
			super.setDirection(orix, oriy);
			if (laser != null) {
				laser.setDirection(orix, oriy);
			}
		}
	}

	
	
	@Override
	public void setPosition(Vector2f pos) {
		setPosition(pos.x, pos.y);
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
	
	@Override
	public void damage(int damage){
		super.damage(damage);
		SoundManager.playerPunched(this.getX(), this.getY());
	}
}
