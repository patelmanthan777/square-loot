package entity.projectile;

import org.lwjgl.util.vector.Vector2f;
import environment.Map;


public final class ProjectileManager {
	/** ProjectileFactory */
	static private ProjectileFactory<Bullet> bulletFactory = new ProjectileFactory<Bullet>(new Bullet());
	
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
	
	
	
	static public void createBullet(Vector2f pos, Vector2f rot){
		bulletFactory.createProjectile(pos, rot);
	}
	
		
	/**
	 * Update all projectiles of the projectileManager
	 * @param dt the elapsed time since the last update
	 * @param m the map
	 */
	static public void updateProjectiles(Map m) {
		bulletFactory.updateProjectiles(m);	
	}
	
	
	/**
	 * Draw all projectiles
	 */
	static public void drawProjectiles() {
			bulletFactory.drawProjectiles();
		}
	}
