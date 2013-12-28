package rendering;

import java.util.LinkedList;

import light.Light;
import light.Shadow;
import light.ShadowBuffer;

public interface ShadowCaster {
	/**
	 * 
	 * @param light light casting shadows
	 * @param shadows buffer to fill with shadows
	 * @return
	 */
	public void computeShadow(Light light,ShadowBuffer shadows);
}