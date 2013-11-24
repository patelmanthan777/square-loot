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
	
	public void init(){
		this.initBulletShader();
	}
	
	private void initBulletShader(){
		bulletShaderProgram = glCreateProgram();
		Shader s = new Shader("bullet");
		s.loadCode();
		s.compile();
		s.link(bulletShaderProgram);
	}
	
	public void createBullet(Vector2f pos, Vector2f rot){
		Bullet project = new Bullet(pos,rot);
		projectiles.add(project);
	}
	
	public void updateProjectiles(float dt, Map m) {
		Iterator<Projectile> ite = projectiles.iterator();
		while(ite.hasNext()){
			Projectile project = ite.next();
			project.updatePostion(dt,m);
			if(project.mustBeDestroy())
				ite.remove();
		}
		
	}
	
	public void drawProjectiles() {
		for(Projectile project : projectiles){
			project.draw();
		}
	}

}
