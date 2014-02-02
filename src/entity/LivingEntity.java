package entity;


import org.lwjgl.util.vector.Vector2f;
import physics.PhysicsDataStructure;
import configuration.ConfigManager;
import entity.projectile.Projectile;
import userInterface.inventory.Inventory;

public abstract class LivingEntity extends DynamicEntity {


	private static final int nbPoints = 4;
	protected Vector2f[] points = new Vector2f[nbPoints];
	private int health = 0;
	private int maxHealth = 100;
	private String healthFraction;
	protected Vector2f t = new Vector2f();
	protected Vector2f d = new Vector2f();
	protected Vector2f halfSize = new Vector2f(20, 20);
	
	protected Inventory inventory = null;
	
	public LivingEntity(Vector2f pos, int inventorySize) {
		super(pos);
		init();
		if (inventorySize > 0)
			inventory = new Inventory(inventorySize);
	}
	
	public LivingEntity(Vector2f pos,Vector2f dir, int inventorySize) {
		super(pos,dir);
		init();
		if (inventorySize > 0)
			inventory = new Inventory(inventorySize);
	}
	public LivingEntity(float posx, float posy, float dirx, float diry, int inventorySize) {
		super(posx,posy,dirx,diry);
		init();
		if (inventorySize > 0)
			inventory = new Inventory(inventorySize);
	}
	public LivingEntity(float posx, float posy, int inventorySize) {
		super(posx,posy);
		init();
		if (inventorySize > 0)
			inventory = new Inventory(inventorySize);
	}	
	
	public int getHealth(){
		return health;
	}

	private void init(){
		for(int i = 0 ; i < nbPoints; i++){
			points[i] = new Vector2f();
		}
	}
	
	/**
	 * Modify the health attribute by setting health to the value of the parameter.
	 * The attribute cannot be negative nor can it be greater than
	 * <b>maxHealth</b>. 
	 * 
	 * @param hp is the new value of <b>health</b>.
	 */
	public void setHealth(int health){
		if(health < 0){
			this.health = 0;
		}else if (health > maxHealth){
			this.health = maxHealth;
		}else{
			this.health = health;
		}
		updateHealthFraction();
	}
	
	/**
	 * Decreases the health attribute by an amount equal to <b>damage</b>
	 * @param damage is the number of health points to be subtracted
	 */
	public void damage(int damage){
		setHealth(this.health-damage);
	}
	
	/**
	 * Increases the health attribute by an amount equal to <b>heal</b>
	 * @param heal is the number of health points to be added
	 */
	public void heal(int heal){
		setHealth(this.health + heal);
	}

	private void updateHealthFraction(){
		healthFraction = "" + this.health + " / " + this.maxHealth;
	}
		
	public int getMaxHealth(){
		return maxHealth;
	}

	public void setMaxHealth(int health){
		this.maxHealth = health;
		if(maxHealth < 1){
			this.maxHealth = 1;
		}
		updateHealthFraction();
	}
	
	/**
	 * Increases the <b>maxHealth</b> attribute by the value of heal.
	 * 
	 * @param heal is the number of health points to be added to
	 * <b>maxHealth</b>
	 */
	public void increaseMaxHealth(int heal){
		setMaxHealth(this.maxHealth + heal);
	}
	
	public String getHealthFraction(){
		return healthFraction;
	}
	
	/**
	 * Compute the coordinates of the 4 points
	 * using the position the size and the orientation of the player
	 * 
	 * 0      1
	 * +------+
	 * |      |
	 * |      |
	 * +------+
	 * 3	  2
	 */
	
	protected void updatePoints(){
		d.x = getDirection().x;
		d.y = getDirection().y;
		t.x = getTangent().x;
		t.y = getTangent().y;
		d.scale(halfSize.y);
		t.scale(halfSize.x);
		points[0].x = this.position.x * ConfigManager.unitPixelSize - t.x - d.x;
		points[0].y = this.position.y * ConfigManager.unitPixelSize - t.y - d.y;
		points[1].x = this.position.x * ConfigManager.unitPixelSize  + t.x - d.x;
		points[1].y = this.position.y * ConfigManager.unitPixelSize  + t.y - d.y;
		points[2].x = this.position.x * ConfigManager.unitPixelSize  + t.x + d.x;
		points[2].y = this.position.y * ConfigManager.unitPixelSize  + t.y + d.y;
		points[3].x = this.position.x * ConfigManager.unitPixelSize  - t.x + d.x;
		points[3].y = this.position.y * ConfigManager.unitPixelSize  - t.y + d.y;
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{
		case BLOCK:
			break;
		case ENTITY:
			break;
		case PROJECTILE:
			Projectile p = (Projectile) a.getPhysicsObject();
			System.out.println(a);
			System.out.println(p.getDamage());
			damage(p.getDamage());
			break;
		default:
			break;
		}
	}

	
	public Inventory getInventory(){
		return inventory;
	}
}
