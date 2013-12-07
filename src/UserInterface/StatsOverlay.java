package UserInterface;

import java.awt.Font;
import org.newdawn.slick.TrueTypeFont;
import event.Timer;
import static org.lwjgl.opengl.GL11.*;

public class StatsOverlay extends Overlay{
	
	private Font awtFont;
	private TrueTypeFont font;
	
	public StatsOverlay(){
		Font awtFont = new Font("Times New Roman", Font.BOLD, 18);
		font = new TrueTypeFont(awtFont, true);
	}
	
	public void draw(){                                    
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glLoadIdentity();
		font.drawString(50, 50, "FPS :" + Timer.getFPS());
	}
}
