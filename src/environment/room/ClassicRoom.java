package environment.room;

import environment.Map;
import environment.room.template.CrossPatchTemplate;
import environment.room.template.EmptyPatchTemplate;
import environment.room.template.PatchTemplate;
import environment.room.template.SquarePatchTemplate;

public class ClassicRoom extends Room {

	public ClassicRoom(float posX, float posY) {
		super(posX, posY);
	}
	
	@Override
	public void construct() {
		super.construct();
		
		for(int i=0; i<Map.roomSize.x; i++) {
			for(int j=0; j<Map.roomSize.y; j++) {
				Double rand = Math.random();
				PatchTemplate template;
				if(rand < 0.2)
				{
					template = new SquarePatchTemplate();
				}
				else if(rand < 0.4)
				{
					template = new CrossPatchTemplate();
				}
				else
				{
					template = new EmptyPatchTemplate();
				}
				template.init();
				template.generatePatch(grid,
						2 + i*PatchTemplate.templateSize,
						2 + j*PatchTemplate.templateSize);
			}
		}
	}

	@Override
	public void destroy() {
		
	}

}
