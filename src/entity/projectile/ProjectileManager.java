package entity.projectile;

import static org.lwjgl.opengl.GL20.glCreateProgram;

import java.util.Iterator;
import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import rendering.Shader;
import environment.Map;


public class ProjectileManager {
	private LinkedList <Projectile> projectiles = new LinkedList<Projectile>();
	
	static protected int bulletShaderProgram;
	
	/**
	 * Initialize the projectiles
	 */
	public void init(){
		this.initBulletShader();
	}
	
	
	/**
	 * Initialize the bullet shader
	 */
	private void initBulletShader(){
		bulletShaderProgram = glCreateProgram();
		Shader s = new Shader("bullet");
		s.loadCode();
		s.compile();
		s.link(bulletShaderProgram);
	}
	
	
	
	/**
	 * Create a bullet at the given position with the given direction
	 * @param pos Initial position of the bullet
	 * @param rot Initial direction of the bullet
	 */
	public void createBullet(Vector2f pos, Vector2f rot){
		Bullet project = new Bullet(pos,rot);
		projectiles.add(project);
	}
	
	
	/**
	 * Update all projectiles of the projectileManager
	 * @param dt the elapsed time since the last update
	 * @param m the map
	 */
	public void updateProjectiles(float dt, Map m) {
		Iterator<Projectile> ite = projectiles.iterator();
		while(ite.hasNext()){
			Projectile project = ite.next();
			project.updatePostion(dt,m);
			if(project.mustBeDestroy())
				ite.remove();
		}
		
	}
	
	
	/**
	 * Draw all projectiles
	 */
	public void drawProjectiles() {
		for(Projectile project : projectiles){
			project.draw();
		}
	}

}
