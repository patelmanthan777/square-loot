package environment.blocks;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import light.Light;
import light.Shadow;

/**
 * 
 * @author Mathgoat
 *
 */
public interface ShadowCasterBlock {
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
	public LinkedList<Shadow> computeShadow(Light light, int ix, int iy, Vector2f halfBlockSize,boolean [] neighbour);
	public LinkedList<Shadow> computeLaserShadow(Light light, int ix, int iy, Vector2f halfBlockSize,boolean [] neighbour);
}
