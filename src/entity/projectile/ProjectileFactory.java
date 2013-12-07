package entity.projectile;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

public class ProjectileFactory<P extends Projectile> {

	private P projectile;
	private LinkedList <Projectile> projectileList;
	
	public ProjectileFactory(P projectile, LinkedList <Projectile> list)
	{
		this.projectile = projectile;
		this.projectileList = list;
	}
	
	/**
	 * Create a projectile at the given position with the given direction
	 * @param pos Initial position of the projectile
	 * @param rot Initial direction of the projectile
	 */
	public void createProjectile(Vector2f pos, Vector2f rot){
		Projectile project = projectile.Clone(pos, rot);
		projectileList.add(project);
	}
}
