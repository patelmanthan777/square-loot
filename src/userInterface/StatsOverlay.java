package userInterface;

import org.newdawn.slick.Color;

import event.Timer;
import static org.lwjgl.opengl.GL11.*;

public class StatsOverlay extends Overlay{
	
	
	public void draw(){
		
		glEnable(GL_BLEND); 
       	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();
		glLoadIdentity();
		
		OverlayManager.font.drawString(50, 50, Timer.getFPSDisplay(), Color.white);
		glPopMatrix();
		glDisable(GL_BLEND);
		
	}
}
