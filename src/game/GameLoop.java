package game;
import light.Laser;
import light.Light;
import light.LightManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import configuration.ConfigManager;
import rendering.Camera;
import rendering.TextureManager;
import userInterface.OverlayManager;
import entity.npc.LivingEntityManager;
import entity.player.Player;
import entity.projectile.ProjectileManager;
import environment.Map;
import environment.blocks.BlockFactory;
import event.Timer;
import event.control.Control;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop {
	private static final String WINDOW_TITLE = "SquareLoot";
	static public boolean isRunning; //false means that the game is closing
	

	private static Player p;// = new Player(new Vector2f(0, 0));
	public static Map map ;


	public static Camera cam = new Camera(new Vector2f(0, 0));
	
	private Control controle;

	public static void main(String[] args) {
		GameLoop loop = new GameLoop();
		loop.start();
	}

	/**
	 * Enter the game loop, the function exit only when the variable isRunning
	 * is set to 'false', meaning that the game is shutting down.
	 */
	private void start() {
		long elapsedTime = 0;
		try {
			init();
			while (isRunning) {
				Timer.tick();
				elapsedTime = Timer.getDelta();
				controle.update();
				render(elapsedTime); // render graphics

				Display.sync(ConfigManager.maxFps); // sync to fps
				Display.update(); // update the view/screen

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Initialize the state of the game entities, as well as the
	 * window manager and openGL.
	 */
	private void init() {
		ConfigManager.init();
		createWindow();
		initGL();
		TextureManager.init();
		
		p = LivingEntityManager.createPlayer();
		controle = new Control(p);
		BlockFactory.initBlocks();

		map = new Map(new Vector2f(10,10), new Vector2f(12,12), new Vector2f(40,40));
		map.renderMapToFrameBuffers();
		p.setPosition(map.getSpawnPixelPosition());
		ProjectileManager.init();
		LivingEntityManager.init();
		LightManager.init();
		LightManager.initLightShaders();
		LightManager.initLaserShader();
		LightManager.setScreenHeight((int)ConfigManager.resolution.y);
		LightManager.setScreenWidth((int)ConfigManager.resolution.x);
		OverlayManager.init();
		OverlayManager.createStatsOverlay();
		OverlayManager.createMiniMap(map.getRooms(), p);
		OverlayManager.createPlayerStatsOverlay(p);
		Light playerLight = LightManager.addLight("playerLight", new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 20,2*(int)ConfigManager.resolution.x,true);
		Laser playerLaser = LightManager.addActivatedLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), p.getRotation());
		p.setLight(playerLight);
		p.setLaser(playerLaser);
		LightManager.addShadowCaster(map);
		
		
		map.renderMapToFrameBuffers();
		LightManager.render();
		map.renderMapToFrameBuffers();
		LightManager.render();
		map.renderMapToFrameBuffers();
		LightManager.render();
		
		
		isRunning = true;
	}

	/**
	 * Specifically initialize openGL. It enables the different options and
	 * set the matrix modes. 
	 */
	public static void initGL() {
		
		glEnable(GL_CULL_FACE);
		glEnable(GL_STENCIL_TEST);
		glEnable(GL_TEXTURE_2D);

		glEnable(GL_BLEND);


		glMatrixMode(GL_PROJECTION); // PROJECTION from 3D to Camera plane
		glLoadIdentity(); 
		glOrtho(0, ConfigManager.resolution.x, ConfigManager.resolution.y, 0, 1, -1);

		glMatrixMode(GL_MODELVIEW); // MODELVIEW manages the 3D scene

		glClearColor(0, 0, 0, 0);
		glEnable(GL_TEXTURE_2D);
	}

	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen) {

	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
		(Display.isFullscreen() == fullscreen)) {
		    return;
	    }

	    try {
	        DisplayMode targetDisplayMode = null;
			
		if (fullscreen) {
		    DisplayMode[] modes = Display.getAvailableDisplayModes();
		    int freq = 0;
					
		    for (int i=0;i<modes.length;i++) {
		        DisplayMode current = modes[i];
						
			if ((current.getWidth() == width) && (current.getHeight() == height)) {
			    if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
			        if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
				    targetDisplayMode = current;
				    freq = targetDisplayMode.getFrequency();
	                        }
	                    }

			    // if we've found a match for bpp and frequence against the 
			    // original display mode then it's probably best to go for this one
			    // since it's most likely compatible with the monitor
			    if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }

	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }

	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
				
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}
	
        /**
         * Initialization the game window.
         */
	private void createWindow() {
		setDisplayMode((int)ConfigManager.resolution.x, (int)ConfigManager.resolution.y, ConfigManager.fullScreen);
		try {
			Display.setTitle(WINDOW_TITLE);
			Display.create(new PixelFormat(0, 16, 1));
			Mouse.setGrabbed(false);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the game state, namely entities, HUD and lights
	 * position and refresh the screen accordingly. 
	 * @param elapsedTime represents the time passed since last update.
	 */
	private void render(long elapsedTime) {
		
		/*** UPDATE ***/
		
		p.updatePostion(elapsedTime, map);
		ProjectileManager.updateProjectiles(map);
		cam.setPosition(p.getPosition());
		LightManager.setCamPosition(p.getPosition());
		map.setDrawPosition(p.getPosition());
		LivingEntityManager.update();
		
		/*** RENDER ***/
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(0,0,0,0);
		glPushMatrix();

		
		cam.draw();
		map.renderMapToFrameBuffers();
		LightManager.render();
		
		p.draw();
		LivingEntityManager.render();
		ProjectileManager.drawProjectiles();
		OverlayManager.render();
		glPopMatrix();
	}
}
