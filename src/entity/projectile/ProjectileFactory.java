package entity.projectile;

import java.util.Iterator;
import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import environment.Map;
import event.Timer;


public class ProjectileFactory{
	/** Alive projectile list */ 
	private LinkedList <Projectile> projectileList;
	
	/** Dead projectile list */
	private LinkedList <Projectile> deadList;
	
	private Projectile modelProjectile;
	
	/**
	 * ProjectileFactory class constructor
	 * @param projectile the projectile to duplicate
	 */
	public ProjectileFactory(Projectile projectile)
	{
		this.modelProjectile = projectile;
		this.projectileList = new LinkedList<Projectile>();
		this.deadList = new LinkedList<Projectile>();
	}
	
	/**
	 * Create a projectile at the given position with the given direction
	 * @param pos Initial position of the projectile
	 * @param rot Initial direction of the projectile
	 */
	public void createProjectile(Vector2f pos, Vector2f rot){
		if(deadList.isEmpty())
		{
			Projectile project = modelProjectile.Clone(pos, rot);
			projectileList.add(project);
		}
		else
		{
			Projectile project = deadList.poll();
			project.reset(pos, rot);
			projectileList.add(project);
		}
	}
	
	public void destroyProjectile(Projectile project)
	{
		deadList.add(project);
	}
	
	/**
	 * Update all projectiles of the projectileFactory
	 * @param m the map
	 */
	public void updateProjectiles(Map m) {
		long dt = Timer.getDelta();
		Iterator<Projectile> ite = projectileList.iterator();
		while(ite.hasNext()){
			Projectile project = ite.next();
			if(project.shouldBeDestroyed())
			{
				ite.remove();
				destroyProjectile(project);
			}
			else
			{
				project.updatePostion(dt,m);
			}
			
		}
	}
	
	/**
	 * Draw all projectiles of the factory
	 */
	public void drawProjectiles() {
		for(Projectile project : projectileList){
			project.draw();
		}
	}
}
