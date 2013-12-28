package entity;

import org.lwjgl.util.vector.Vector2f;

public abstract class LivingEntity extends Entity {

	private int health = 0;
	private int maxHealth = 100;
	
	public LivingEntity(Vector2f pos) {
		super(pos);
	}
	
	public int getHealth(){
		return health;
	}

	public void setHealth(int health){
		if(health < 0){
			this.health = 0;
		}else if (health > maxHealth){
			this.health = maxHealth;
		}else{
			this.health = health;
		}
	}
	
	/**
	 * Modify the health attribute by adding the value of the parameter.
	 * The attribute cannot be negative nor can it be greater than
	 * <b>maxHealth</b>. 
	 * 
	 * @param hp is the relative number of hp to be offsetted.
	 */
	private void modifyHealth(int hp){
		this.health += hp;
		if(health < 0){
			this.health = 0;
		}else if (health > maxHealth){
			this.health = maxHealth;
		}
	}
	
	/**
	 * Decreases the health attribute by an amount equal to <b>damage</b>
	 * @param damage is the number of health points to be subtracted
	 */
	public void damage(int damage){
		modifyHealth(-damage);
	}
	
	/**
	 * Increases the health attribute by an amount equal to <b>heal</b>
	 * @param heal is the number of health points to be added
	 */
	public void heal(int heal){
		modifyHealth(heal);
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}

	public void setMaxHealth(int health){
		this.maxHealth = health;
		if(maxHealth < 1){
			this.maxHealth = 1;
		}
	}
	
	/**
	 * Increases the <b>maxHealth</b> attribute by the value of heal.
	 * 
	 * @param heal is the number of health points to be added to
	 * <b>maxHealth</b>
	 */
	public void increaseMaxHealth(int heal){
		this.maxHealth += heal;
		if(maxHealth < 1){
			this.maxHealth = 1;
		}
	}
}
