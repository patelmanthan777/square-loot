package rendering;

import light.Light;
import light.ShadowBuffer;

public interface ShadowCaster {

	public void computeShadow(Light light,ShadowBuffer shadows);
}