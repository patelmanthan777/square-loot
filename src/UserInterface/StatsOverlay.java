package userInterface;

import java.awt.Font;
import org.newdawn.slick.TrueTypeFont;
import event.Timer;
import static org.lwjgl.opengl.GL11.*;

public class StatsOverlay extends Overlay{
	
	
	private TrueTypeFont font;
	
	public StatsOverlay(){
		font = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 18), true);
	}
	
	public void draw(){                                    
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glLoadIdentity();
		font.drawString(50, 50, "FPS :" + Timer.getFPS());
	}
}
