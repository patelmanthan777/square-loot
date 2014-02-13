package game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import rendering.TextureManager;
import userInterface.HUD;

import configuration.ConfigManager;


import event.Timer;
import event.control.Control;

public abstract class Game {
	
	private static final String WINDOW_TITLE = "SquareLoot";
	static public boolean isRunning; //false means that the game is closing
	protected Control controle;
	
	static public boolean isAlive= true;
	static public boolean newLevel= false;
	
	/**
	 * Enter the game loop, the function exit only when the variable isRunning
	 * is set to 'false', meaning that the game is shutting down.
	 */
	protected void start() {
		long elapsedTime = 0;
		try {
			
			firstInit();
			while (isRunning) {
						
				while (isAlive && isRunning && !newLevel) {
					Timer.tick();
					elapsedTime = Timer.getDelta();
					controle.update();								
					
					update(elapsedTime);
							
	
				
					render(); // render graphics
					
					
					Display.sync(ConfigManager.maxFps); // sync to fps
					Display.update(); // update the view/screen					
	
				}
			
				if(isRunning)
					reinit();
			}
			destroy();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
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
	
	protected void firstInit(){
		ConfigManager.init();
		createWindow();
		initGL();
		TextureManager.init();		
		HUD.init();

	}
	
	/**
	 * Specifically initialize openGL. It enables the different options and
	 * set the matrix modes. 
	 */
	public static void initGL() {
		
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
	public abstract void reinit();
	
	public abstract void update(long delta);
	
	public abstract void render();
	public abstract void destroy();
}
