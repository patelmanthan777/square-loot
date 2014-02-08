package game;

import item.Battery;
import item.ItemManager;
import item.weapon.LaserRifle;
import light.Laser;
import light.Light;
import light.LightManager;
import utils.GraphicsAL;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import physics.PhysicsManager;
import configuration.ConfigManager;
import rendering.Background;
import rendering.Camera;
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
	public void init() {
		PhysicsManager.init();
		
		p = EntityManager.createPlayer();
		controle = new Control(p);
		BlockFactory.initBlocks();

		GraphicsAL.init();
		map = new Map(new Vector2f(10,10), new Vector2f(32,32), new Vector2f(ConfigManager.unitPixelSize,ConfigManager.unitPixelSize));
		map.initPhysics();
		map.renderMapToFrameBuffers();	

		p.setPosition(map.getSpawnPosition());

		background = new Background();
		
		ProjectileManager.init();

		EntityManager.init();
	
		LightManager.init();
		Light playerLight = LightManager.addPointLight("playerLight", new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 20,2*(int)ConfigManager.resolution.x,true);
		Laser playerLaser = LightManager.addLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), p.getDirection());
		
		p.setLight(playerLight);

		p.setLaser(playerLaser);		
		p.pickUp(new LaserRifle(250,200,200,0.05f,10,50));
		p.pickUp(new Battery(200,200));
		
		LightManager.addShadowCaster(map);
		
		ItemManager.init();
		
		OverlayManager.init();
		OverlayManager.createStatsOverlay();
		OverlayManager.createMiniMap(map.getRooms(), p);
		OverlayManager.createPlayerStatsOverlay(p);

		OverlayManager.createPlayerInventory(p);
		
		Timer.start();
		
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
		EntityManager.updatePosition(elapsedTime, map);
		
		ProjectileManager.updateProjectiles();
		ItemManager.update();
		background.update(elapsedTime);
		
		
		
		
		/* Cam and light */
		Vector2f pos = new Vector2f(p.getPosition().x * ConfigManager.unitPixelSize,
				p.getPosition().y * ConfigManager.unitPixelSize);
		cam.setPosition(pos);
		LightManager.setCamPosition(pos);
		map.setDrawPosition(pos);
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
		glPopMatrix();

	}
}
