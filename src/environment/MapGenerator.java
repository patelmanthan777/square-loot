package environment;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import environment.blocks.Block;
import environment.blocks.BlockFactory;

public class MapGenerator {
	private static Block[][] grid;
	private static int maxRoomSize = 10;
	private static int nbRoomsAligned;
	private static Vector2f spawn;
	private static int size;
	private static int blockSize;

	private static LinkedList<Room> rooms;
	private static LinkedList<Room> surroundedRooms;
	private static Room[][] roomsGrid;

	static Block[][] generate(int size,int blockSize) {
		MapGenerator.size = size;
		MapGenerator.blockSize = blockSize;
		grid = new Block[size][size];
		maxRoomSize = size / 10;
		nbRoomsAligned = size / maxRoomSize;
		roomsGrid = new Room[nbRoomsAligned][nbRoomsAligned];
		rooms = new LinkedList<Room>();
		surroundedRooms = new LinkedList<Room>();
		createRooms();
		pasteRooms();
		return grid;
	}

	private static void createRooms() {
		spawn = new Vector2f((MapGenerator.size + maxRoomSize) / 2,
				(MapGenerator.size + maxRoomSize) / 2);
		Room r = new SpawnRoom(maxRoomSize, maxRoomSize, (nbRoomsAligned / 2)
				* maxRoomSize, (nbRoomsAligned / 2) * maxRoomSize,blockSize);
		roomsGrid[nbRoomsAligned / 2][nbRoomsAligned / 2] = r;
		rooms.add(r);
		boolean stop = false;
		while (rooms.size() > 0 && !stop) {
			
			int surround;
			int rand = (int) (Math.random() * rooms.size());
			int x;
			int y;
			int cptMax = rooms.size();
			int cpt = 0;
			do {
				cpt = 0;
				r = rooms.get(rand);
				x = r.getX() / maxRoomSize;
				y = r.getY() / maxRoomSize;
				surround = surround(x, y);
				if(surround>2){
					surroundedRooms.add(rooms.remove(rand));
					rand = (int)(Math.random() * rooms.size());
				}
				cpt++;
			} while (surround > 2 && rooms.size() > 0);
			Room r2;
			
			if (surround < 3 && rooms.size()>0) {
				int rand2 = (int) (Math.random() * (4 - surround));
				if (y != 0 && canBeRoom(x, y - 1)) {
					if (rand2 == 0) {
						r2 = new TestRoom(maxRoomSize, maxRoomSize, x
								* maxRoomSize, (y - 1) * maxRoomSize, blockSize);
						roomsGrid[x][y - 1] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (x != nbRoomsAligned - 1 && canBeRoom(x + 1, y)) {
					if (rand2 == 0) {
						r2 = new TestRoom(maxRoomSize, maxRoomSize, (x + 1)
								* maxRoomSize, y * maxRoomSize, blockSize);
						roomsGrid[x + 1][y] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (y != nbRoomsAligned - 1 && canBeRoom(x, y + 1)) {
					if (rand2 == 0) {
						r2 = new TestRoom(maxRoomSize, maxRoomSize, x
								* maxRoomSize, (y + 1) * maxRoomSize, blockSize);
						roomsGrid[x][y + 1] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (x != 0 && roomsGrid[x - 1][y] == null
						&& canBeRoom(x - 1, y)) {
					if (rand2 == 0) {
						r2 = new TestRoom(maxRoomSize, maxRoomSize, (x - 1)
								* maxRoomSize, y * maxRoomSize, blockSize);
						roomsGrid[x - 1][y] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if(rand2 > 0){
					surroundedRooms.add(rooms.remove(rand));
				}
			}else{
				stop = true;
			}
		}

		for (int i = 0; i < nbRoomsAligned; i++) {
			for (int j = 0; j < nbRoomsAligned; j++) {

				if (roomsGrid[i][j] != null) {
					if (j > 0 && roomsGrid[i][j - 1] != null) {
						roomsGrid[i][j].createDoor(0);
					}
					if (i < nbRoomsAligned - 1 && roomsGrid[i + 1][j] != null) {
						roomsGrid[i][j].createDoor(1);
					}
					if (j < nbRoomsAligned - 1 && roomsGrid[i][j + 1] != null) {
						roomsGrid[i][j].createDoor(2);
					}
					if (i > 0 && roomsGrid[i - 1][j] != null) {
						roomsGrid[i][j].createDoor(3);
					}
				}
			}
		}
	}

	private static void pasteRooms() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				grid[i][j] = BlockFactory.createVoidBlock();
			}
		}
		for (Room r : surroundedRooms) {
			r.place(grid);
		}
	}

	public static Vector2f getSpawn() {
		return spawn;
	}

	private static int surround(int i, int j) {
		int cpt = 0;

		if (j < 1 || roomsGrid[i][j - 1] != null) {
			cpt++;
		}
		if (i >= nbRoomsAligned - 1 || roomsGrid[i + 1][j] != null) {
			cpt++;
		}
		if (j >= nbRoomsAligned - 1 || roomsGrid[i][j + 1] != null) {
			cpt++;
		}
		if (i < 1 || roomsGrid[i - 1][j] != null) {
			cpt++;
		}
		return cpt;
	}

	private static boolean canBeRoom(int i, int j) {
		boolean bool = true;
		bool &= (roomsGrid[i][j] == null);
		bool &= (surround(i, j) < 3);
		bool &= (j - 1 < 0 || roomsGrid[i][j - 1] == null || surround(i, j - 1) < 3);
		bool &= (i + 1 >= nbRoomsAligned || roomsGrid[i + 1][j] == null || surround(
				i + 1, j) < 3);
		bool &= (j + 1 >= nbRoomsAligned || roomsGrid[i][j + 1] == null || surround(
				i, j + 1) < 3);
		bool &= (i - 1 < 0 || roomsGrid[i - 1][j] == null || surround(i - 1, j) < 3);
		return bool;
	}
}
