package entity.projectile;

import org.lwjgl.util.vector.Vector2f;

public final class ProjectileManager {
	/** ProjectileFactory */
	static private ProjectileFactory bulletFactory = new ProjectileFactory(new Bullet());
	static private ProjectileFactory energyShotFactory = new ProjectileFactory(new EnergyShot());
	
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
	
	static public void reinit(){
		bulletFactory = new ProjectileFactory(new Bullet());
		energyShotFactory = new ProjectileFactory(new EnergyShot());
	}
	
	/**
	 * Create a Bullet
	 * @param pos the bullet position
	 * @param rot the bullet direction
	 */
	static public void createBullet(Vector2f pos, Vector2f rot, Vector2f initSpeed,float speedValue, float size, int damage){
		bulletFactory.createProjectile(pos, rot, initSpeed, speedValue,size,damage);
	}
	
	
	static public void createEnergyShot(Vector2f pos, Vector2f rot, Vector2f initSpeed,float speedValue, float size, int damage){
		energyShotFactory.createProjectile(pos, rot,  initSpeed,speedValue,size,damage);
	}
		
	/**
	 * Update all projectiles
	 * @param m the map
	 */
	static public void updateProjectiles() {
		bulletFactory.updateProjectiles();	
		energyShotFactory.updateProjectiles();
	}
	
	
	/**
	 * Draw all projectiles
	 */
	static public void drawProjectiles() {
			bulletFactory.drawProjectiles();
			energyShotFactory.drawProjectiles();
		}
	}
