package environment;

import rendering.ShadowCaster;
import light.Light;
import light.LightManager;
import light.ShadowBuffer;
import environment.blocks.Block;
import environment.blocks.BlockFactory;
import environment.blocks.DoorBlock;
import environment.room.Room;

public class Door implements ShadowCaster{
	private boolean opened = false;
	private Room r;
	private DoorBlock[] blocks = new DoorBlock[2];
	private static int cpt = 0;

	public Door(Room r, int wall) {
		this.r = r;
		int i;
		int j;
		Block btmp;
		if (wall == 0) {
			i = (int) (Map.roomBlockSize.x / 2 - 1);
			j = 1;
			btmp = r.getBlock(i, j);
			blocks[0] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
			i = (int) (Map.roomBlockSize.x / 2);
			j = 1;
			btmp = r.getBlock(i, j);
			blocks[1] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
		} else if (wall == 1) {
			i = (int) (Map.roomBlockSize.x - 2);
			j = (int) (Map.roomBlockSize.y / 2 - 1);
			btmp = r.getBlock(i, j);
			blocks[0] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
			i = (int) (Map.roomBlockSize.x - 2);
			j = (int) (Map.roomBlockSize.y / 2);
			btmp = r.getBlock(i, j);
			blocks[1] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
		} else if (wall == 2) {
			i = (int) (Map.roomBlockSize.x / 2 - 1);
			j = (int) (Map.roomBlockSize.y - 2);
			btmp = r.getBlock(i, j);
			blocks[0] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
			i = (int) (Map.roomBlockSize.x / 2);
			j = (int) (Map.roomBlockSize.y - 2);
			btmp = r.getBlock(i, j);
			blocks[1] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
		} else {
			i = 1;
			j = (int) (Map.roomBlockSize.y / 2 - 1);
			btmp = r.getBlock(i, j);
			blocks[0] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
			i = 1;
			j = (int) (Map.roomBlockSize.y / 2);
			btmp = r.getBlock(i, j);
			blocks[1] = (DoorBlock) BlockFactory.createDoorBlock(btmp,(int)(r.getX()/Map.blockPixelSize.x+i),(int)(r.getY()/Map.blockPixelSize.x+j));
			r.putBlock(blocks[0], i, j);
		}
	}

	public boolean isOpened() {
		return opened;
	}

	public void open() {
		if (opened == false) {
			opened = true;
			blocks[0].open();
			blocks[1].open();
			LightManager.updateAllShadows();
			r.setRenderUpdated(false);
		}
	}

	public void close() {
		if (opened == true) {
			opened = false;
			blocks[0].close();
			blocks[1].close();
			LightManager.updateAllShadows();
			r.setRenderUpdated(false);
		}
	}

	public void toggle() {
		if (opened)
			close();
		else
			open();
	}

	@Override
	public void computeShadow(Light light, ShadowBuffer[] shadows) {
		if(!opened){
			blocks[0].computeShadow(light, blocks[0].getI(), blocks[0].getJ(), shadows[1]);
			blocks[1].computeShadow(light, blocks[1].getI(), blocks[1].getJ(), shadows[1]);
		}
	}
}
