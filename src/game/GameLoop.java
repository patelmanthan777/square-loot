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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.Camera;
import userInterface.OverlayManager;
import entity.player.Player;
import entity.projectile.Bullet;
import entity.projectile.ProjectileFactory;
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
	private static final int FPS = 0;
	private boolean isRunning;

	private static Weapon weapon = new LaserRifle(250);
	
	private Player p = new Player(new Vector2f(0, 0));
	private Map m = new Map(new Vector2f(15,15), new Vector2f(10,10), new Vector2f(40,40));
	private Vector2f mouse = new Vector2f();
	private float dwheel;
	private Camera cam = new Camera(new Vector2f(0, 0));
	
	private Keys keys = new Keys();
	private int displayed_x = WIDTH;
	private int displayed_y = HEIGHT;

	public static void main(String[] args) {
		GameLoop test = new GameLoop();
		test.start();
	}

	private void start() {
		int elapsedTime = 0;
		try {
			init();
			while (isRunning) {
				Timer.tick();
				elapsedTime = Timer.getDelta();
				getInput(); // read input
				render(elapsedTime); // render graphics

				Display.sync(FPS); // sync to fps
				Display.update(); // update the view/screen
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

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
		OverlayManager.createMiniMap(m.getRooms());
		
		Light playerLight = LightManager.addActivatedLight("playerLight", new Vector2f(200, 200), new Vector3f(1, 1, 0.8f), 10,2*WIDTH);
		Laser playerLaser = LightManager.addActivatedLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), p.getRotation());
		p.setLight(playerLight);
		p.setLaser(playerLaser);
		
		LightManager.addShadowCaster(m);
		LightManager.addLightTaker(m);
		
		isRunning = true;
	}

	private void initGL() {

		glEnable(GL_CULL_FACE);

		glMatrixMode(GL_PROJECTION); // change de matrice
		glLoadIdentity(); // la reinitialise
		glOrtho(0, displayed_x, displayed_y, 0, 1, -1);

		glMatrixMode(GL_MODELVIEW); // on passe en mode Model
		glEnable(GL_STENCIL_TEST);
		// glLoadIdentity(); // on reinitialise la matrice
		glClearColor(0, 0, 0, 0);
	}

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
	
	private void getInput() {
		keys.update();
		mouse.x = Mouse.getX(); // will return the X coordinate on the Display.
		mouse.y = Mouse.getY();
		dwheel = Mouse.getDWheel();
		
		
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
