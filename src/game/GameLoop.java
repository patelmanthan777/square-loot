package game;
import light.Laser;
import light.Light;
import light.LightManager;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import rendering.Background;
import rendering.Camera;
import userInterface.OverlayManager;
import entity.npc.LivingEntityManager;
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
		
		
		p = LivingEntityManager.createPlayer();
		controle = new Control(p);
		BlockFactory.initBlocks();
		
		map = new Map(new Vector2f(6,4), new Vector2f(16,12), new Vector2f(48,48));
		map.renderMapToFrameBuffers();
		background = new Background();
		p.setPosition(map.getSpawnPixelPosition());
		
		ProjectileManager.init();
			
		LivingEntityManager.init();
		
		LightManager.init();
		Light playerLight = LightManager.addPointLight("playerLight", new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 20,2*(int)ConfigManager.resolution.x,true);
		Laser playerLaser = LightManager.addLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), p.getDirection());
		
		p.setLight(playerLight);
		p.setLaser(playerLaser);
		
		LightManager.addShadowCaster(map);
		
		OverlayManager.init();
		OverlayManager.createStatsOverlay();
		OverlayManager.createMiniMap(map.getRooms(), p);
		OverlayManager.createPlayerStatsOverlay(p);
		
		isRunning = true;
	}

	/**
	 * Update the game state, namely entities, HUD and lights
	 * position
	 * @param elapsedTime represents the time passed since last update.
	 **/
	@Override
	public void update(long elapsedTime) {
		background.update(elapsedTime);
		map.update(elapsedTime);
		p.updatePostion(elapsedTime, map);
		ProjectileManager.updateProjectiles(map);
		cam.setPosition(p.getPosition());
		LightManager.setCamPosition(p.getPosition());
		map.setDrawPosition(p.getPosition());
		LivingEntityManager.update(elapsedTime);
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
		p.draw();
		//map.drawDoors();
		LivingEntityManager.render();
		ProjectileManager.drawProjectiles();
		OverlayManager.render();
		
		glPopMatrix();
	}
}
