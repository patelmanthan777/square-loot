package entity.projectile;

import org.lwjgl.util.vector.Vector2f;

public final class ProjectileManager {
	/** ProjectileFactory */
	static private ProjectileFactory bulletFactory = new ProjectileFactory(new Bullet());
	
	/**
	 * Private ProjectileManager Constructor
	 * This class must not be instantiated
	 */
	private ProjectileManager(){}
	
	
	/**
	 * Initialize the projectiles
	 */
	static public void init(){
		Bullet.initBulletShader();
	}
	
	
	/**
	 * Create a Bullet
	 * @param pos the bullet position
	 * @param rot the bullet direction
	 */
	static public void createBullet(Vector2f pos, Vector2f rot, float speedValue, float size, int damage){
		bulletFactory.createProjectile(pos, rot, speedValue,size,damage);
	}
	
		
	/**
	 * Update all projectiles
	 * @param m the map
	 */
	static public void updateProjectiles() {
		bulletFactory.updateProjectiles();	
	}
	
	
	/**
	 * Draw all projectiles
	 */
	static public void drawProjectiles() {
			bulletFactory.drawProjectiles();
		}
	}
