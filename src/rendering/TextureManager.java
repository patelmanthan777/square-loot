package rendering;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TextureManager {

	private static enum textureEnum {
		BACKGROUND, PLAYER, ZOMBIE, LASERRIFLE, BATTERY, KEY, ENERGY,SHOPKEEPER
	}

	private static Image[] images = new Image[textureEnum.values().length];

	public static void init() {
		try {
			images[textureEnum.BACKGROUND.ordinal()] = new Image("assets/textures/background.png");
			images[textureEnum.PLAYER.ordinal()] = new Image("assets/textures/player.png");
			images[textureEnum.ZOMBIE.ordinal()] = new Image("assets/textures/zombie.png");
			images[textureEnum.LASERRIFLE.ordinal()] = new Image("assets/textures/laserRifle.png");
			images[textureEnum.BATTERY.ordinal()] = new Image("assets/textures/battery.png");
			images[textureEnum.ENERGY.ordinal()] = new Image("assets/textures/energy.png");
			images[textureEnum.KEY.ordinal()] = new Image("assets/textures/key.png");
			images[textureEnum.SHOPKEEPER.ordinal()] = new Image("assets/textures/shopkeeper.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Image playerTexture() {
		return images[textureEnum.PLAYER.ordinal()];
	}

	public static Image keyTexture() {
		return images[textureEnum.KEY.ordinal()];
	}

	public static Image backgroundTexture() {
		return images[textureEnum.BACKGROUND.ordinal()];
	}

	public static Image zombieTexture() {
		return images[textureEnum.ZOMBIE.ordinal()];
	}

	public static Image laserRifleTexture() {
		return images[textureEnum.LASERRIFLE.ordinal()];
	}

	public static Image batteryTexture() {
		return images[textureEnum.BATTERY.ordinal()];
	}

	public static Image energyTexture() {
		return images[textureEnum.ENERGY.ordinal()];
	}
	public static Image shopKeeperTexture() {
		return images[textureEnum.SHOPKEEPER.ordinal()];
	}
}
