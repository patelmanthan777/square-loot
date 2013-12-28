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
	
	private void updateHealthFraction(){
		healthFraction = "" + this.health + " / " + this.maxHealth;
	}
	
	public void addHealth(int health){
		setHealth(this.health + health);
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
	
	public void increaseMaxHealth(int heal){
		setMaxHealth(this.maxHealth += heal);
	}
	
	public String getHealthFraction(){
		return healthFraction;
	}
}
