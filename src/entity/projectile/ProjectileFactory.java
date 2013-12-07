package entity.projectile;

import java.util.Iterator;
import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import environment.Map;
import event.Timer;


public class ProjectileFactory<P extends Projectile> {
	/** Alive projectile list */ 
	private LinkedList <Projectile> projectileList;
	
	private P modelProjectile;
	
	/**
	 * ProjectileFactory class constructor
	 * @param projectile the porjectile to duplicate
	 */
	public ProjectileFactory(P projectile)
	{
		this.modelProjectile = projectile;
		this.projectileList = new LinkedList<Projectile>();
	}
	
	/**
	 * Create a projectile at the given position with the given direction
	 * @param pos Initial position of the projectile
	 * @param rot Initial direction of the projectile
	 */
	public void createProjectile(Vector2f pos, Vector2f rot){
		Projectile project = modelProjectile.Clone(pos, rot);
		projectileList.add(project);
	}
	
	public void destroyProjectile(P projec)
	{
		/** TODO add a linked list to sore destroy projectiles */
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
			if(project.mustBeDestroy())
			{
				ite.remove();				
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
