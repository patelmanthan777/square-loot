package entity;

import org.lwjgl.util.vector.Vector2f;

public abstract class LivingEntity extends Entity {

	private int health = 0;
	private int maxHealth = 100;
	private String healthFraction;
	
	public LivingEntity(Vector2f pos) {
		super(pos);
	}
	
	public int getHealth(){
		return health;
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
		setHealth(-damage);
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
}
