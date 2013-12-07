package entity.projectile;

import static org.lwjgl.opengl.GL20.glCreateProgram;

import java.util.Iterator;
import java.util.LinkedList;

import rendering.Shader;
import environment.Map;


public class ProjectileManager {
	private LinkedList <Projectile> projectileList = new LinkedList<Projectile>();
	
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
	
	
	public ProjectileFactory<Bullet> createBulletFactory(){
		return new ProjectileFactory<Bullet>(new Bullet(), projectileList);
	}
	
		
	/**
	 * Update all projectiles of the projectileManager
	 * @param dt the elapsed time since the last update
	 * @param m the map
	 */
	public void updateProjectiles(float dt, Map m) {
		Iterator<Projectile> ite = projectileList.iterator();
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
		for(Projectile project : projectileList){
			project.draw();
		}
	}

}
