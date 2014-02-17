package environment.room.template;

public class CrossPatchTemplate extends PatchTemplate {
	static private int offset = 3;
	
	@Override
	public void init() {
		for(int i=0; i<templateSize; i++) {
			for(int j=0; j<templateSize; j++) {
				if((i == offset ||
				   i == templateSize - offset ||
				   j == templateSize - offset) &&
				   i >= offset &&
				   j >= offset &&
				   i <= templateSize - offset &&
				   j <= templateSize - offset)
				{
					patch[i][j] = new SolidBlockTemplate();
				}
				else
				{
					patch[i][j] = new EmptyBlockTemplate();
				}
			}
		}
	}

}
