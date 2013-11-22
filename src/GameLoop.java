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
import entity.Player;
import environment.Map;
import event.Timer;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop {
	private static final int WIDTH = 1600, HEIGHT = 900;
	private static final DisplayMode DISPLAY_MODE = new DisplayMode(WIDTH,
			HEIGHT);
	private static final String WINDOW_TITLE = "SquareLoot";
	private static final int FPS = 60;
	private boolean isRunning;
	private LightManager lm;

	private Player p = new Player(new Vector2f(0, 0));
	private Map m = new Map(500, 500);
	private Vector2f mouse = new Vector2f();
	private Camera cam = new Camera(new Vector2f(0, 0));

	public static void main(String[] args) {
		GameLoop test = new GameLoop();
		test.start();
	}

	private void start() {
		try {
			init();
			while (isRunning) {
				long elapsedTime = Timer.getDelta();
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
		p.setPosition(m.getSpawnPosition());
		lm = new LightManager(cam);
		lm.initLightShaders();
		lm.initLaserShader();

		for (int i = 0; i < 10; i++) {
			lm.addActivatedLight(
					"" + i,
					new Vector2f((int) (Math.random() * 1200), (int) (Math
							.random() * 1200)),
					new Vector3f((float) Math.random(), (float) Math.random(),
							(float) Math.random()), 10);
		}
		
		
		Light playerLight = lm.addActivatedLight("playerLight", new Vector2f(200, 200), new Vector3f(1,
				1, 1), 20);
		Laser playerLaser = lm.addActivatedLaser("playerLaser", new Vector2f(200,200), new Vector3f(1,0,0), p.getRotation());
		p.setLight(playerLight);
		p.setLaser(playerLaser);
		
		lm.addShadowCaster(m);
		lm.addLightTaker(m);
		
		isRunning = true;
		Timer.initTimer();
	}

	private void initGL() {

		glEnable(GL_CULL_FACE);

		glMatrixMode(GL_PROJECTION); // change de matrice
		glLoadIdentity(); // la reinitialise
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);

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
		mouse.x = Mouse.getX(); // will return the X coordinate on the Display.
		mouse.y = Mouse.getY();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
				|| Display.isCloseRequested()) {
			isRunning = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			p.translate(0, -1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			p.translate(-1, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			p.translate(0, 1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			p.translate(1, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
		}
	}

	private void render(long elapsedTime) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// glMatrixMode(GL_MODELVIEW);

		p.updatePostion(elapsedTime, m); // a sortir de la boucle de rendu ?
		p.setOrientation(-(mouse.x-WIDTH/2.0f),mouse.y-HEIGHT/2.0f);
		cam.setPosition(p.getPosition());
		m.setDrawPosition(p.getPosition());

		GL11.glPushMatrix();
		cam.draw();
		//lm.setLightPosition("player", p.getPosition());
		
		lm.render();

		p.draw();
		GL11.glPopMatrix();
	}
}
