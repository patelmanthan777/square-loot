package userInterface;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;

import configuration.ConfigManager;
import entity.player.Player;

public class PlayerStatsOverlay extends Overlay{

	private Player player;
	private Vector2f size = new Vector2f(0.3f*ConfigManager.resolution.x,0.02f*ConfigManager.resolution.y);
	private Vector2f position;
	PlayerStatsOverlay(Player p){
		this.player = p;
		position = new Vector2f(0.5f*(ConfigManager.resolution.x - size.x),0.92f*ConfigManager.resolution.y);
	}
	
	@Override
	public void draw() {
		int health = player.getHealth();
		int maxHealth = player.getMaxHealth();
		String display = player.getHealthFraction();
		glDisable(GL_BLEND);
		glLoadIdentity();
		
		glBegin(GL_TRIANGLE_STRIP);
		glColor3f(1,0,0.1f);
		glVertex2f(position.x+((float)health/(float)maxHealth)*(size.x-1), position.y+1);
		glColor3f(1,0,0.1f);
		glVertex2f(position.x, position.y+1);
		glColor3f(1,0.1f,0.1f);
		glVertex2f(position.x+((float)health/(float)maxHealth)*(size.x-1), position.y+size.y);
		glColor3f(1,0.1f,0.1f);
		glVertex2f(position.x, position.y+size.y);
		glEnd();
		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor3f(1,1,1);
		glBegin(GL_LINE_STRIP);
		glVertex2f(position.x+size.x, position.y);
		glVertex2f(position.x, position.y);
		glVertex2f(position.x, position.y+size.y);
		glVertex2f(position.x+size.x, position.y+size.y);
		glVertex2f(position.x+size.x, position.y);
		glEnd();
		OverlayManager.font.drawString(position.x+size.x/2-15, position.y+0.1f*size.y, display);
		glDisable(GL_BLEND);
	}
}
