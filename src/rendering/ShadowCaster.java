package rendering;

import java.util.LinkedList;
import light.Shadow;

public interface ShadowCaster {
	public LinkedList<Shadow> computeShadow();
}