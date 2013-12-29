package light;

public class ShadowBuffer {
	/**
	 * Index of the last meaningful Shadow stored in <b>shadows</b>,
	 * similar to the current number of computed shadows minus 1.
	 */
	public int lastShadow = 0;
	private static int size = 10000;
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
