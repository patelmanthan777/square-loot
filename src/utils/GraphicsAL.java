package utils;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;


public class GraphicsAL{
	public static Vector2f[] fullTexPoints = new Vector2f[4];
	
	public static void init(){
		fullTexPoints[0] = new Vector2f(0,1);		
		fullTexPoints[1] = new Vector2f(1,1);		
		fullTexPoints[2] = new Vector2f(1,0);
		fullTexPoints[3] = new Vector2f(0,0);				
	}
	
	
	
	public static void drawQuad(float x, float y, float sizex, float sizey){
		glVertex2f(x, y + sizey);		
		glVertex2f(x + sizex, y + sizey);		
		glVertex2f(x + sizex, y);		
		glVertex2f(x, y);		
	}	
	
	public static void drawQuadTexture(Vector2f[] points,
									   Vector2f[] texPoints,
									   int textureId){
		
		
		glBindTexture(GL_TEXTURE_2D, textureId);
		glBegin(GL_QUADS);
		glTexCoord2f(texPoints[0].x, texPoints[0].y);
		glVertex2f(points[0].x, points[0].y);				
		glTexCoord2f(texPoints[1].x, texPoints[1].y);
		glVertex2f(points[1].x, points[1].y);
		glTexCoord2f(texPoints[2].x, texPoints[2].y);			
		glVertex2f(points[2].x, points[2].y);
		glTexCoord2f(texPoints[3].x, texPoints[3].y);		
		glVertex2f(points[3].x, points[3].y);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
				
	}
}