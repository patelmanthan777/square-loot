package environment.room.template;

public class SquarePatchTemplate extends PatchTemplate {
	static private int sizeSquare = 2;
	
	@Override
	public void init() {
		for(int i=0; i<templateSize; i++) {
			for(int j=0; j<templateSize; j++) {
				if(i >= (templateSize/2-sizeSquare) &&
				   i < (templateSize/2+sizeSquare) &&
				   j >= (templateSize/2-sizeSquare) &&
				   j < (templateSize/2+sizeSquare))
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
