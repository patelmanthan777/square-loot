package environment;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import environment.blocks.Block;
import environment.blocks.BlockFactory;
import environment.room.Room;
import environment.room.SpawnRoom;
import environment.room.TestRoom;

public class MapGenerator {
	private static LinkedList<Room> rooms;
	private static LinkedList<Room> surroundedRooms;
	private static Room[][] roomsGrid;
	
	static Room[][] generate() {
		roomsGrid = new Room[(int)Map.mapRoomSize.x][(int)Map.mapRoomSize.y];
		rooms = new LinkedList<Room>();
		surroundedRooms = new LinkedList<Room>();
		createRooms();
		return roomsGrid;
	}

	private static void createRooms() {
		Map.spawnPixelPosition = new Vector2f(Map.mapPixelSize.x/2.0f+Map.roomPixelSize.x/2.0f, Map.mapPixelSize.y/2.0f+Map.roomPixelSize.y/2.0f);
		Map.spawnRoomPosition = new Vector2f(Map.mapRoomSize.x/2.0f, Map.mapRoomSize.y/2.0f);
		Room r = new SpawnRoom(Map.mapPixelSize.x/2.0f,Map.mapPixelSize.x/2.0f);
		roomsGrid[(int) (Map.mapRoomSize.x/ 2)][(int) (Map.mapRoomSize.y / 2)] = r;
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
				x = (int) (r.getX() / Map.roomPixelSize.x);
				y = (int) (r.getY() / Map.roomPixelSize.y);
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
						r2 = new TestRoom(x * Map.roomPixelSize.x, (y - 1) * Map.roomPixelSize.y);
						roomsGrid[x][y - 1] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (x != Map.mapRoomSize.x - 1 && canBeRoom(x + 1, y)) {
					if (rand2 == 0) {
						r2 = new TestRoom((x + 1) * Map.roomPixelSize.x, y * Map.roomPixelSize.y);
						roomsGrid[x + 1][y] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (y != Map.mapRoomSize.y - 1 && canBeRoom(x, y + 1)) {
					if (rand2 == 0) {
						r2 = new TestRoom(x * Map.roomPixelSize.x, (y + 1) * Map.roomPixelSize.y);
						roomsGrid[x][y + 1] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (x != 0 && roomsGrid[x - 1][y] == null
						&& canBeRoom(x - 1, y)) {
					if (rand2 == 0) {
						r2 = new TestRoom((x - 1) * Map.roomPixelSize.x,y * Map.roomPixelSize.y);
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

		for (int i = 0; i < Map.mapRoomSize.x; i++) {
			for (int j = 0; j < Map.mapRoomSize.y; j++) {

				if (roomsGrid[i][j] != null) {
					if (j > 0 && roomsGrid[i][j - 1] != null) {
						roomsGrid[i][j].createDoor(0);
					}
					if (i < Map.mapRoomSize.x - 1 && roomsGrid[i + 1][j] != null) {
						roomsGrid[i][j].createDoor(1);
					}
					if (j < Map.mapRoomSize.y - 1 && roomsGrid[i][j + 1] != null) {
						roomsGrid[i][j].createDoor(2);
					}
					if (i > 0 && roomsGrid[i - 1][j] != null) {
						roomsGrid[i][j].createDoor(3);
					}
				}
			}
		}
	}


	private static int surround(int i, int j) {
		int cpt = 0;

		if (j < 1 || roomsGrid[i][j - 1] != null) {
			cpt++;
		}
		if (i >= Map.mapRoomSize.x - 1 || roomsGrid[i + 1][j] != null) {
			cpt++;
		}
		if (j >= Map.mapRoomSize.y - 1 || roomsGrid[i][j + 1] != null) {
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
		bool &= (i + 1 >= Map.mapRoomSize.x || roomsGrid[i + 1][j] == null || surround(
				i + 1, j) < 3);
		bool &= (j + 1 >= Map.mapRoomSize.y || roomsGrid[i][j + 1] == null || surround(
				i, j + 1) < 3);
		bool &= (i - 1 < 0 || roomsGrid[i - 1][j] == null || surround(i - 1, j) < 3);
		return bool;
	}
}
