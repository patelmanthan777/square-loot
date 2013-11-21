package environment.blocks;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.LinkedList;

import light.Light;
import light.Shadow;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.ShadowCaster;

public class SolidBlock implements Block, ShadowCaster {
	private Vector3f color = new Vector3f(0,0,0);
	private int nb_points = 4;
	private Vector2f[] points = new Vector2f[4];
	private Vector2f halfBlockSize;

	protected SolidBlock(int i ,int j, Vector2f size){
		float x =  (i * size.x * 2 + size.x);
		float y =  (j * size.y * 2 + size.y);
		initBlock(x, y, size);
	}
	
	protected SolidBlock(float x, float y, Vector2f size){
		initBlock(x, y, size);
	}
	
	protected void initBlock(float positionx, float positiony, Vector2f size){
		float x = positionx;
		float y = positiony;
		this.halfBlockSize = size;
		points[0] = new Vector2f(x - halfBlockSize.x, y
				- halfBlockSize.y);
		points[1] = new Vector2f(x + halfBlockSize.x, y
				- halfBlockSize.y);
		points[2] = new Vector2f(x + halfBlockSize.x, y
				+ halfBlockSize.y);
		points[3] = new Vector2f(x - halfBlockSize.x, y
				+ halfBlockSize.y);
		
	}
	
	
	@Override
	public void drawAt(float posX, float posY, Vector2f halfBlockSize) {
		
		GL11.glColor3f(color.x,color.y,color.z);
		// draw quad
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glVertex2f(posX+halfBlockSize.x,posY-halfBlockSize.y);
		GL11.glVertex2f(posX-halfBlockSize.x,posY-halfBlockSize.y);
		GL11.glVertex2f(posX+halfBlockSize.x,posY+halfBlockSize.y);
		GL11.glVertex2f(posX-halfBlockSize.x,posY+halfBlockSize.y);
		GL11.glEnd();
	}

	@Override
	public boolean testCollision() {
		return true;
	}

	@Override
	public boolean castShadows() {
		return true;
	}

	@Override
	public LinkedList<Shadow> computeShadow(Light light) {
		LinkedList<Shadow>l = new LinkedList<Shadow>();
		
		for (int i = 0; i < nb_points; i++) {
			
			Vector2f currentVertex = points[i];
			Vector2f nextVertex = points[(i + 1) % 4];
			Vector2f edge = Vector2f.sub(nextVertex,currentVertex, null);
			Vector2f normal = new Vector2f(edge.getY(),-edge.getX());
			Vector2f lightToCurrent = Vector2f.sub(currentVertex, light.getPosition(), null);
			if (Vector2f.dot(normal, lightToCurrent) > 0) {
				Vector2f point1 = Vector2f.add(currentVertex,(Vector2f) Vector2f.sub(currentVertex,light.getPosition(), null).scale(80), null);
				Vector2f point2 = Vector2f.add(nextVertex,(Vector2f) Vector2f.sub(nextVertex,light.getPosition(), null).scale(80), null);
				l.add(new Shadow(currentVertex,nextVertex,point1,point2));
			}	
		}
		return l;
	}
}

	/*
	glBegin(GL_QUADS);
	{
		glVertex2f(currentVertex.getX(),
				currentVertex.getY());
		glVertex2f(point1.getX(), point1.getY());
		glVertex2f(point2.getX(), point2.getY());
		glVertex2f(nextVertex.getX(),
				nextVertex.getY());
	}
	glEnd();
	*/