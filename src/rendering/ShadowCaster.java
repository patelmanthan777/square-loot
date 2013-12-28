package rendering;

import light.Light;
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