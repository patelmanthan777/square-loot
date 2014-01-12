package game;
import light.Laser;
import light.Light;
import light.LightManager;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import physics.PhysicsManager;
import configuration.ConfigManager;
import rendering.Camera;
import userInterface.OverlayManager;
import entity.EntityManager;
import entity.player.Player;
import entity.projectile.ProjectileManager;
import environment.Map;
import environment.blocks.BlockFactory;
import event.control.Control;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop extends Game{
	
	private static Player p;
	public static Map map ;
	public static Camera cam = new Camera(new Vector2f(0, 0));
	
	
	public static void main(String[] args) {
		GameLoop loop = new GameLoop();
		loop.start();
	}

	/**
	 * Initialize the state of the game entities, as well as the
	 * window manager and openGL.
	 */
	public void init() {
		
		
		p = EntityManager.createPlayer();
		controle = new Control(p);
		BlockFactory.initBlocks();
		map = new Map(new Vector2f(10,10), new Vector2f(32,32), new Vector2f(48,48));
		map.renderMapToFrameBuffers();	

		p.setPosition(map.getSpawnPixelPosition());
		
		ProjectileManager.init();
			
		EntityManager.init();
		LightManager.init();
		LightManager.initLightShaders();
		LightManager.initLaserShader();
		
		Light playerLight = LightManager.addPointLight("playerLight", new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 20,2*(int)ConfigManager.resolution.x,true);
		Laser playerLaser = LightManager.addLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), p.getDirection());
		
		p.setLight(playerLight);
		p.setLaser(playerLaser);
		LightManager.addShadowCaster(map);

		OverlayManager.init();
		OverlayManager.createStatsOverlay();
		OverlayManager.createMiniMap(map.getRooms(), p);
		OverlayManager.createPlayerStatsOverlay(p);

		PhysicsManager.init(map);
		isRunning = true;
	}

	/**
	 * Update the game state, namely entities, HUD and lights
	 * position
	 * @param elapsedTime represents the time passed since last update.z
	 **/
	@Override
	public void update(long elapsedTime) {
		/* Input */
		EntityManager.updateInput(elapsedTime);
		
		/* Physics */
		EntityManager.updatePhysics(elapsedTime);
		PhysicsManager.update(elapsedTime);
		
		/* Position*/
		EntityManager.updatePosition();
		
		ProjectileManager.updateProjectiles(map);
		
		/* Cam and light */
		cam.setPosition(p.getPosition());
		LightManager.setCamPosition(p.getPosition());
		map.setDrawPosition(p.getPosition());
	
	}
	
	/**
	 * Refresh the screen.
	 */
	@Override
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(0,0,0,0);
		glPushMatrix();
		cam.draw();
		map.renderMapToFrameBuffers();
		LightManager.render();
		p.draw();
		EntityManager.render();
		ProjectileManager.drawProjectiles();
		OverlayManager.render();
		glPopMatrix();
	}
}
