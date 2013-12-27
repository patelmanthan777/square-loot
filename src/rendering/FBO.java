package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL30.*;
import environment.Map;

public class FBO {
	private int frameBufferID;
	private int textureID;
	private int depthBufferID;
	private boolean isUpdated = false;
	private int textureBound = 0;
	private int texSave;
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
	
	public void bind(){
		texSave =  glGetInteger(GL_TEXTURE_BINDING_2D);
		frameBufferSave =  glGetInteger(GL_FRAMEBUFFER_BINDING);
		glBindTexture(GL_TEXTURE_2D, textureBound);
		
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
	}
	
	public void unbind(){
		glBindTexture(GL_TEXTURE_2D, texSave);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferSave);
	}
	
	public void setUpdated(boolean isUpdated){
		this.isUpdated = isUpdated;
	}
	
	public boolean isUpdated(){
		return isUpdated;
	}
	
	public void use(){
		glClearColor(0.0f, 0.0f, 0.0f, 1f);
		texSave =  glGetInteger(GL_TEXTURE_BINDING_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	public void unUse(){
		glBindTexture(GL_TEXTURE_2D, texSave);
	}
	
	public int getTextureID(){
		return textureID;
	}
}
