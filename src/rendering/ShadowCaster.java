package rendering;

import java.util.LinkedList;

import light.Light;
import light.Shadow;

public interface ShadowCaster {
	public LinkedList<Shadow> computeShadow(Light light);
	public LinkedList<Shadow> computeLaserShadow(Light light);
}