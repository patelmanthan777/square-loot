package userInterface;

import event.Timer;
import static org.lwjgl.opengl.GL11.*;

public class StatsOverlay extends Overlay{
	
	public void draw(){                                    
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glLoadIdentity();
		OverlayManager.font.drawString(50, 50, "FPS :" + Timer.getFPS());
	}
}
