package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import environment.Map;

public class FBO {
	/**
	 * FBO on which to perform the rendering task 
	 */
	private int frameBufferID;
	/**
	 * texture on which to perform the rendering task
	 */
	private int textureID;
	/**
	 * depth buffer on which to perform the rendering task
	 */
	private int depthBufferID;
	private boolean isUpdated = false;

	
	private int width;
	private int height;

	public FBO(int width, int height){

		frameBufferID = glGenFramebuffers();
		textureID = glGenTextures();
		depthBufferID = glGenRenderbuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, (int) Map.mapPixelSize.x,
				(int) Map.mapPixelSize.y, 0, GL_RGBA, GL_INT,
				(java.nio.ByteBuffer) null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
				GL_TEXTURE_2D, textureID, 0);

		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_STENCIL,
				(int) Map.mapPixelSize.x, (int) Map.mapPixelSize.y);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
				GL_RENDERBUFFER, depthBufferID);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_STENCIL_ATTACHMENT,
				GL_RENDERBUFFER, depthBufferID);
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Set OpenGL to the appropriate frame buffer
	 */

	public void bind(){
		glBindTexture(GL_TEXTURE_2D, 0);
		glViewport(0, 0, width, height);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glPushAttrib(GL11.GL_VIEWPORT_BIT);
		glViewport(0, 0, width, height);
		glPushMatrix();
		glLoadIdentity();
		glClearColor(0.0f, 0.0f, 0.0f, 1f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
	}
	
	/**
	 * Set OpenGL back to its previous frame buffer state
	 */
	public void unbind(){

		glPopMatrix();
		glPopAttrib();
		
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0,
				1, -1);
		glMatrixMode(GL_MODELVIEW);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
	}
	
	public void use(){
		glClearColor(1f, 1f, 1f, 1f);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	public void unUse(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	

	public void setUpdated(boolean isUpdated){
		this.isUpdated = isUpdated;
	}
	
	public boolean isUpdated(){
		return isUpdated;

	}
	
	public int getTextureID(){
		return textureID;
	}
}
