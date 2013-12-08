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
	
	public void damage(int damage){
		this.health -= damage;
		if(health < 0){
			this.health = 0;
		}else if (health > maxHealth){
			this.health = maxHealth;
		}
	}
	
	public void heal(int heal){
		this.health += heal;
		if(health < 0){
			this.health = 0;
		}else if (health > maxHealth){
			this.health = maxHealth;
		}
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
	
	public void increaseMaxHealth(int heal){
		this.maxHealth += heal;
		if(maxHealth < 1){
			this.maxHealth = 1;
		}
	}
}
