package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL30.*;
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
	
	/**
	 * texture used for the rendering
	 */
	private int textureBound = 0;
	/**
	 * previous texture state
	 */
	private int texSave;
	/**
	 * previous frame buffer state
	 */
	private int frameBufferSave;
	
	public FBO(){
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
	}
	
	/**
	 * Set OpenGL to the appropriate frame buffer
	 */
	public void bind(){
		//texSave =  glGetInteger(GL_TEXTURE_BINDING_2D);
		frameBufferSave =  glGetInteger(GL_FRAMEBUFFER_BINDING);
		//glBindTexture(GL_TEXTURE_2D, textureBound);
		
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
	}
	
	/**
	 * Set OpenGL back to its previous frame buffer state
	 */
	public void unbind(){
		//glBindTexture(GL_TEXTURE_2D, texSave);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferSave);
	}
	
	public void setUpdated(boolean isUpdated){
		this.isUpdated = isUpdated;
	}
	
	public boolean isUpdated(){
		return isUpdated;
	}
	
	/**
	 * Trigger the use of the computed texture
	 */
	public void use(){
		glClearColor(1.0f, 1.0f, 1.0f, 1f);
		texSave =  glGetInteger(GL_TEXTURE_BINDING_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	/**
	 * Set the texture back to its previous state.
	 */
	public void unUse(){
		glBindTexture(GL_TEXTURE_2D, texSave);
	}
	
	public int getTextureID(){
		return textureID;
	}
}
