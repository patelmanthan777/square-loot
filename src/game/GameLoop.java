package game;
import item.weapon.LaserRifle;
import item.weapon.Weapon;
import light.Laser;
import light.Light;
import light.LightManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Camera;
import userInterface.OverlayManager;
import entity.player.Player;
import entity.projectile.ProjectileManager;
import environment.Map;
import event.KeyState;
import event.Keys;
import event.Timer;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop {
	public static final int WIDTH = 1600, HEIGHT = 900;
	private static final DisplayMode DISPLAY_MODE = new DisplayMode(WIDTH,
			HEIGHT);
	private static final String WINDOW_TITLE = "SquareLoot";
	private static final int FPS = 0; //max number of frame per second (0 means nolimit)
	private boolean isRunning; //false means that the game is closing

	private static Weapon weapon = new LaserRifle(250);
	
	private Player p = new Player(new Vector2f(0, 0));
	private Map m = new Map(new Vector2f(10,8), new Vector2f(16,12), new Vector2f(40,40));
	private Vector2f mouse = new Vector2f();
	private Camera cam = new Camera(new Vector2f(0, 0));
	
	private Keys keys = new Keys();
	
	public static void main(String[] args) {
		GameLoop loop = new GameLoop();
		loop.start();
	}

	/**
	 * Enter the game loop, the function exit only when the variable isRunning
	 * is set to 'false', meaning that the game is shutting down.
	 */
	private void start() {
		int elapsedTime = 0;
		try {
			init();
			while (isRunning) {
				Timer.tick();
				elapsedTime = Timer.getDelta();
				getInput(); 
				render(elapsedTime); // render graphics

				Display.sync(FPS); // sync to fps
				Display.update(); 
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
		createWindow();
		initGL();
		m.generate();

		p.setPosition(m.getSpawnPixelPosition());
		ProjectileManager.init();


		LightManager.initLightShaders();
		LightManager.initLaserShader();
		LightManager.setScreenHeight(HEIGHT);
		LightManager.setScreenWidth(WIDTH);
		OverlayManager.createStatsOverlay();
		OverlayManager.createMiniMap(m.getRooms(),p);
		OverlayManager.createPlayerStatsOverlay(p);
		
		Light playerLight = LightManager.addActivatedLight("playerLight", new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 10,2*WIDTH);
		Laser playerLaser = LightManager.addActivatedLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), p.getRotation());
		p.setLight(playerLight);
		p.setLaser(playerLaser);
		
		LightManager.addShadowCaster(m);
		LightManager.addLightTaker(m);
		
		isRunning = true;
	}

	/**
	 * Specifically initialize openGL. It enables the different options and
	 * set the matrix modes. 
	 */
	private void initGL() {

		glEnable(GL_CULL_FACE);

		glMatrixMode(GL_PROJECTION); // PROJECTION from 3D to Camera plane
		glLoadIdentity(); 
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);

		glMatrixMode(GL_MODELVIEW); // MODELVIEW manages the 3D scene
		glEnable(GL_STENCIL_TEST); 
		glClearColor(0, 0, 0, 0);
	}

	/**
	 * Initialization the game window. 
	 */
	private void createWindow() {
		try {
			Display.setDisplayMode(DISPLAY_MODE);
			Display.setTitle(WINDOW_TITLE);
			Display.create(new PixelFormat(0, 16, 1));
			Mouse.setGrabbed(false);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Interpret the inputs and modify the game entities accordingly.
	 */
	private void getInput() {
		keys.update();
		mouse.x = Mouse.getX(); 
		mouse.y = Mouse.getY();
		
		
		if(Mouse.isButtonDown(0)){
			
			weapon.Fire(new Vector2f(p.getPosition()), new Vector2f(mouse.x-Display.getWidth()/2.0f,Display.getHeight()/2.0f - mouse.y));
		}
		
		if (keys.getState(Keyboard.KEY_ESCAPE) == KeyState.PRESSED|| Display.isCloseRequested()) {
			isRunning = false;
		}
		if (keys.getState(Keyboard.KEY_Z) == KeyState.HELD || keys.getState(Keyboard.KEY_Z) == KeyState.PRESSED) {
			p.translate(0, -1);
		}
		if (keys.getState(Keyboard.KEY_Q) == KeyState.HELD || keys.getState(Keyboard.KEY_Q) == KeyState.PRESSED) {
			p.translate(-1, 0);
		}
		if (keys.getState(Keyboard.KEY_S) == KeyState.HELD || keys.getState(Keyboard.KEY_S) == KeyState.PRESSED) {
			p.translate(0, 1);
		}
		if (keys.getState(Keyboard.KEY_D) == KeyState.HELD || keys.getState(Keyboard.KEY_D) == KeyState.PRESSED) {
			p.translate(1, 0);
		}
		if (keys.getState(Keyboard.KEY_E) == KeyState.PRESSED) {
			if(p.getLight().isActive())
				p.getLight().desactivate();
			else
				p.getLight().activate();
		}
	}

	/**
	 * Update the game state, namely entities, HUD and lights
	 * position and refresh the screen accordingly. 
	 * @param elapsedTime represents the time passed since last update.
	 */
	private void render(long elapsedTime) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		p.updatePostion(elapsedTime, m);
		ProjectileManager.updateProjectiles(m);
		p.setOrientation(-(mouse.x-WIDTH/2.0f),mouse.y-HEIGHT/2.0f);
		cam.setPosition(p.getPosition());
		LightManager.setCamPosition(p.getPosition());
		m.setDrawPosition(p.getPosition());

		glPushMatrix();
		cam.draw();
		LightManager.render();
		
		p.draw();
		ProjectileManager.drawProjectiles();
		
		OverlayManager.render();
		glPopMatrix();
	}
}
