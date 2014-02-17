package environment.room.template;

public class EmptyPatchTemplate extends PatchTemplate {

	@Override
	public void init() {
		for(int i=0; i<templateSize; i++) {
			for(int j=0; j<templateSize; j++) {
				patch[i][j] = new EmptyBlockTemplate();
			}
		}
	}

}
