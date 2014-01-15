package environment.blocks;

import org.lwjgl.util.vector.Vector2f;

import environment.Map;
import light.Light;
import light.Shadow;
import light.ShadowBuffer;

/**
 * 
 * @author Mathgoat
 *
 */
public abstract class ShadowCasterBlock extends Block{
	
	/* ------ avoid dynamic allocation in each computeShadow call ! ---*/
	/**
	 * Normal vector to the square edges.
	 */
	private Vector2f normal = new Vector2f();
	/**
	 * Edge direction.
	 */
	private Vector2f edge = new Vector2f();
	/**
	 * Light direction.
	 */
	private Vector2f lightToCurrent = new Vector2f();
	/**
	 * 'Projection' of one the edge extremity in the light direction.
	 */
	private Vector2f point1 = new Vector2f();
	/**
	 * 'Projection' of the other edge extremity in the light direction.
	 */
	private Vector2f point2 = new Vector2f();
	/* ----------------------------------------------------------------*/
	
	public ShadowCasterBlock(){
		super();
		this.castShadows = true;
	}
	
	protected ShadowCasterBlock(float x, float y){
		super(x,y);
		this.castShadows = true;
	}
	
	/**
	 * Compute the shadow quadrilaterals generated by the relevant edges.
	 * 
	 * @param light is the light responsible for the shadows to be computed
	 * @param ix is the block horizontal index in reference to the map
	 * @param iy is the block vertical index in reference to the map
	 * @param neighbour: 4 booleans -> <b>true</b> where there is a neighbor
	 *      +---+
	 *      | 0 |
	 * 	+---+---+---+
	 *  | 3 |   | 1 |
	 *  +---+---+---+
	 *      | 2 |
	 *      +---+
	 * 
	 * @param shadowBuffer are the resulting shadows quadrilaterals
	 */
	public void computeShadow(Light light, int ix, int iy,boolean [] neighbour, ShadowBuffer shadowBuffer){
		float x =  (ix * Map.blockPixelSize.x);
		float y =  (iy * Map.blockPixelSize.y);
		int shadowInd = shadowBuffer.lastShadow+1;
		initBlock(x, y);

		for (int i = 0; i < nb_points; i++){
			Vector2f currentVertex = points[i];
			Vector2f nextVertex = points[(i + 1) % 4];
			Vector2f.sub(nextVertex,currentVertex, edge);
			normal.x = edge.getY();
			normal.y = -edge.getX();
			Vector2f.sub(currentVertex, light.getPosition(), lightToCurrent);
			Shadow[] shadows = (shadowBuffer.getShadows());
			if (Vector2f.dot(normal, lightToCurrent) > 0 ) {
				if((light instanceof Light && !neighbour[i]) ||
					this instanceof VoidBlock){
					Vector2f.sub(currentVertex,light.getPosition(), point1);
					point1.normalise(point1);
					point1.scale(10000);
					point1 = Vector2f.add(currentVertex, point1, point1);
					Vector2f.sub(nextVertex,light.getPosition(), point2);
					point2.normalise(point2);
					point2.scale(10000);
					point2 = Vector2f.add(nextVertex, point2, point2);
					shadows[shadowInd].points[0].x = currentVertex.x;
					shadows[shadowInd].points[0].y = currentVertex.y;
					shadows[shadowInd].points[1].x = nextVertex.x;
					shadows[shadowInd].points[1].y = nextVertex.y;
					shadows[shadowInd].points[2].x = point1.x;
					shadows[shadowInd].points[2].y = point1.y;
					shadows[shadowInd].points[3].x = point2.x;
					shadows[shadowInd].points[3].y = point2.y;
					shadowInd++;
				}
			}	
		}
		shadowBuffer.lastShadow = Math.max(0, shadowInd - 1);
	}

	/**
	 * Create the shadow aimed at hiding the laser once it collides
	 * with a shadow casting block.
	 * 
	 * @param l
	 * @param ix
	 * @param iy
	 * @param shadows
	 */
	public void laserShadow(Light l, int x, int y, ShadowBuffer shadows){

		int shadowInd = shadows.lastShadow+1;
		initBlock(x, y);				
		
		//LivingEntityManager.createZombie(x, y);
		
		for (int i = 0; i < nb_points; i++){
			Vector2f currentVertex = points[i];
			Vector2f nextVertex = points[(i + 1) % 4];
			Vector2f.sub(nextVertex,currentVertex, edge);
			normal.x = edge.getY();
			normal.y = -edge.getX();			
			Shadow[] shade = (shadows.getShadows());
			//if (Vector2f.dot(normal, l.getDirection()) < 0 ) {
				
				point1.x = l.getRotationX();
				point1.y = l.getRotationY();
				point1.normalise(point1);
				point1.scale(10000);
				point1 = Vector2f.add(currentVertex, point1, point1);
				
				point2.x = l.getRotationX();
				point2.y = l.getRotationY();
				point2.normalise(point2);
				point2.scale(10000);
				point2 = Vector2f.add(nextVertex, point2, point2);
				shade[shadowInd].points[0].x = currentVertex.x;
				shade[shadowInd].points[0].y = currentVertex.y;
				shade[shadowInd].points[1].x = nextVertex.x;
				shade[shadowInd].points[1].y = nextVertex.y;
				shade[shadowInd].points[2].x = point1.x;
				shade[shadowInd].points[2].y = point1.y;
				shade[shadowInd].points[3].x = point2.x;
				shade[shadowInd].points[3].y = point2.y;
				shadowInd++;				
			//}
		}
		shadows.lastShadow = shadowInd - 1;
	}
}
