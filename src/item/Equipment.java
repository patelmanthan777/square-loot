package item;

import org.lwjgl.util.vector.Vector2f;

import entity.LivingEntity;
import event.Timer;

public abstract class Equipment extends Item{
	protected long cooldown;
	/**
	 * Last shot timestamp, allow the handling of the
	 * fire cooldown.
	 */
	protected long lastAction;
	
	
	
	public Equipment(float x, float y, ItemListEnum s, long cd){
		super(x,y,s);
		this.cooldown = cd;
		this.lastAction = 0;
	}

	
	/**
	 * Update the lastShot
	 */
	protected void updateLastAction(){
		lastAction = Timer.getTime();
	}
	
	/**
	 * Test whether the weapon is ready to fire
	 * @return <b>true</b> if the weapon is ready to fire, <b>false</b> otherwise.
	 */
	protected boolean readyToFire(){
		long currentTime = Timer.getTime();
		return cooldown + lastAction < currentTime;
	}
	
	protected abstract void specificAction(Vector2f pos, Vector2f target, LivingEntity doer);
	
	public boolean action(Vector2f pos, Vector2f target, LivingEntity doer){
		if(readyToFire()){
			
			specificAction(pos, target, doer);
			
			updateLastAction();
			return true;
		}
		
		return false;
	}
}