package game;


import item.ItemManager;
import light.LightManager;
import utils.GraphicsAL;

import org.lwjgl.util.vector.Vector2f;


import physics.PhysicsManager;
import configuration.ConfigManager;
import rendering.Background;
import rendering.Camera;
import sound.SoundManager;
import userInterface.HUD;
import userInterface.OverlayManager;
import entity.EntityManager;
import entity.player.Player;
import entity.projectile.ProjectileManager;
import environment.Map;
import environment.blocks.BlockFactory;
import event.Timer;
import event.control.Control;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop extends Game{
	
	private static Player p;
	public static Map map ;
	public static Camera cam = new Camera(new Vector2f(0, 0));
	private static Background background;
	
	public static void main(String[] args) {
		
		GameLoop loop = new GameLoop();
		loop.start();
	}

	/**
	 * Initialize the state of the game entities, as well as the
	 * window manager and openGL.
	 */

	public void firstInit(){
		super.firstInit();
		
		PhysicsManager.init();		
			
		BlockFactory.initBlocks();
		EntityManager.init();
		GraphicsAL.init();

		map = new Map(15, new Vector2f(10,10), new Vector2f(20,16), new Vector2f(ConfigManager.unitPixelSize,ConfigManager.unitPixelSize));
		map.initPhysics();
		map.renderMapToFrameBuffers();	

		background = new Background();
		
		ProjectileManager.init();
		LightManager.init();

	
		p = EntityManager.createPlayer();
		p.setPosition(map.getSpawnPosition());
		controle = new Control(p);
		
		
		HUD.registerPlayer(p);
		
		LightManager.addShadowCaster(map);
		
		ItemManager.init();
		
		OverlayManager.init();
		OverlayManager.createStatsOverlay();
		OverlayManager.createMiniMap(map.getRooms(), p);
		OverlayManager.createPlayerStatsOverlay(p);
		
		
		SoundManager.init();
		
		Timer.start();
		
		isRunning = true;

	}
	
	/**
	 * Regenerate a map and reinitialize what needs to be
	 */
	public void reinit() {
		LightManager.reinit();
		ProjectileManager.reinit();
		PhysicsManager.reinit();		
		EntityManager.reinitNPCS();
		
		map.destroy();
		map = new Map(15, new Vector2f(6,6), new Vector2f(20,16), new Vector2f(ConfigManager.unitPixelSize,ConfigManager.unitPixelSize));
		map.initPhysics();
		map.renderMapToFrameBuffers();
		
		if(!isAlive)
			p.reinit();						
		
		p.setPosition(map.getSpawnPosition());
		controle = new Control(p);
		
		HUD.registerPlayer(p);
		
		OverlayManager.reinit();
		OverlayManager.createStatsOverlay();
		OverlayManager.createMiniMap(map.getRooms(), p);
		OverlayManager.createPlayerStatsOverlay(p);
		
		Timer.start();
		
		isAlive = true;
		newLevel = false;
	}

	/**
	 * Update the game state, namely entities, HUD and lights
	 * position
	 * @param elapsedTime represents the time passed since last update.z
	 **/
	@Override

	public void update(long elapsedTime) {		
		isRunning &= !HUD.update();

		/* Input */
		EntityManager.updateInput(elapsedTime);
		
		/* Physics */
		EntityManager.updatePhysics(elapsedTime);
		ItemManager.updatePhysics(elapsedTime);
		PhysicsManager.update(elapsedTime);
		
		/* Position*/
		EntityManager.updatePosition(elapsedTime, map);
		
		ProjectileManager.updateProjectiles();
		ItemManager.update(elapsedTime, map);
		background.update(elapsedTime);
		
		/* Cam and light */
		
		cam.setPosition(p.getPosition());
		LightManager.setCamPosition(p.getPosition());
		map.setDrawPosition(p.getPosition());
		map.update(elapsedTime);
	}
	
	/**
	 * Refresh the screen.
	 */
	@Override
	public void render() {
        
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(0,0,0,0);
		glPushMatrix();
		glLoadIdentity();
		cam.draw();
		background.draw();
		map.renderMapToFrameBuffers();
		LightManager.render();
		ItemManager.render();

		EntityManager.render();


		ProjectileManager.drawProjectiles();
		OverlayManager.render();		
		HUD.render();
		        
		glPopMatrix();

	}

	@Override
	public void destroy() {
		SoundManager.destroy();
		
	}
}
