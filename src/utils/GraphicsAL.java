package utils;

import static org.lwjgl.opengl.GL11.*;

public class GraphicsAL{
	public static void drawQuad(float x, float y, float sizex, float sizey){
		glVertex2f(x, y + sizey);		
		glVertex2f(x + sizex, y + sizey);		
		glVertex2f(x + sizex, y);		
		glVertex2f(x, y);		
	}	
}