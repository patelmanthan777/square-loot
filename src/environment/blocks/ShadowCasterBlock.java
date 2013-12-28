package environment.blocks;

import org.lwjgl.util.vector.Vector2f;

import environment.Map;
import light.Laser;
import light.Light;
import light.Shadow;

/**
 * 
 * @author Mathgoat
 *
 */
public abstract class ShadowCasterBlock extends Block{
	
	protected int nbShadowsMax = 4;
	public int nbShadows = 0;
	private Shadow shadows[] = new Shadow[nbShadowsMax];
	
	/* ------ avoid dynamic allocation in each computeShadow call ! ---*/
	private Vector2f normal = new Vector2f();  
	private Vector2f edge = new Vector2f();
	private Vector2f lightToCurrent = new Vector2f();
	private Vector2f point1 = new Vector2f();
	private Vector2f point2 = new Vector2f();
	/* ----------------------------------------------------------------*/
	
	public ShadowCasterBlock(){
		super();
		for(int i = 0 ; i < nbShadowsMax ; i++){
			shadows[i] = new Shadow();
		}
	}
	
	protected ShadowCasterBlock(float x, float y){
		super(x,y);
		for(int i = 0 ; i < nbShadowsMax ; i++){
			shadows[i] = new Shadow();
		}
	}
	
	/**
	 * 
	 * @param light
	 * @param ix
	 * @param iy
	 * @param halfBlockSize
	 * @param neighbour : 4 booleans -> true were there is a neighbour
	 *      +---+
	 *      | 1 |
	 * 	+---+---+---+
	 *  | 4 |   | 2 |
	 *  +---+---+---+
	 *      | 3 |
	 *      +---+
	 *  
	 * @return
	 */
	public Shadow[] computeShadow(Light light, int ix, int iy,boolean [] neighbour){
		float x =  (ix * Map.blockPixelSize.x);
		float y =  (iy * Map.blockPixelSize.y);
		int shadowInd = 0;
		nbShadows = 0;
		initBlock(x, y);
		if(light instanceof Laser){
			shadows[shadowInd].points[0].x = points[1].x;
			shadows[shadowInd].points[0].y = points[1].y;
			shadows[shadowInd].points[1].x = points[0].x;
			shadows[shadowInd].points[1].y = points[0].y;
			shadows[shadowInd].points[2].x = points[2].x;
			shadows[shadowInd].points[2].y = points[2].y;
			shadows[shadowInd].points[3].x = points[3].x;
			shadows[shadowInd].points[3].y = points[3].y;
			shadows[shadowInd].exists = true;
			shadowInd++;
		}
		for (int i = 0; i < nb_points; i++){
			Vector2f currentVertex = points[i];
			Vector2f nextVertex = points[(i + 1) % 4];
			Vector2f.sub(nextVertex,currentVertex, edge);
			normal.x = edge.getY();
			normal.y = -edge.getX();
			Vector2f.sub(currentVertex, light.getPosition(), lightToCurrent);
			
			if (Vector2f.dot(normal, lightToCurrent) > 0 ) {
				if((light instanceof Light && !neighbour[i]) || light instanceof Laser){
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
					shadows[shadowInd].exists = true;
					shadowInd++;
				}
			}	
		}
		for (int i = shadowInd; i < nbShadowsMax; i++){
			shadows[i].exists = false;
		}
		nbShadows = shadowInd;
		return shadows;
	}

	
	@Override
	public boolean castShadows() {
		return true;
	}
	public Shadow[] getShadow() {
		return shadows;
	}
}
