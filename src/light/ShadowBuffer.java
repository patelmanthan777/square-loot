package light;

public class ShadowBuffer {
	public int lastShadow = 0;
	private static int size = 2000;
	private Shadow[] shadows = new Shadow[size];
	public ShadowBuffer() {
		for(int i = 0 ; i < size ; i++){
			shadows[i] = new Shadow();
		}
	}
	public Shadow get(int i){
		if(i<size && i >= 0){
			return shadows[i];
		}else{
			return null;
		}
	}
	
	public Shadow[] getShadows(){
		return shadows;
	}
}
