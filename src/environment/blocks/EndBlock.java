package environment.blocks;

import game.Game;
import physics.PhysicsDataStructure;

public class EndBlock extends PhysicalBlock {
	public EndBlock(){
		super();
		layer = 0;
	}
	
	@Override
	public void ContactHandler(PhysicsDataStructure a) {
		switch(a.getType())
		{		
		case PLAYER:
			Game.newLevel = true;
			break;
		}
	}
}
