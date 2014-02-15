package environment;

import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

import environment.room.EndRoom;
import environment.room.EnergyRoom;
import environment.room.KeyRoom;
import environment.room.Market;
import environment.room.RandomBlockRoom;
import environment.room.Room;
import environment.room.SpawnRoom;

public class MapGenerator {
	private static LinkedList<Room> rooms;
	/**
	 * Represents the list of rooms that cannot admit more neighbouring rooms.
	 */
	private static LinkedList<Room> surroundedRooms;
	private static Room[][] roomsGrid;
	
	private static int requiredEndRoom;
	private static int requiredMarket;
	private static int requiredKeyRoom;
	
	/**
	 * Generate the rooms maze.
	 * @return The class attribute roomsGrid initialized to form a room maze.
	 */
	static void generate(Map map, int n) {
		roomsGrid = map.getRooms();
		rooms = new LinkedList<Room>();
		surroundedRooms = new LinkedList<Room>();
		createRooms(n);
	}

	
	/**
	 * Iteratively decide which cells of roomsGrid are going to be rooms.
	 * For a cell to be a room it must not be surrounded by already existing
	 * rooms. The generation ends when no cell satisfies this requirement.
	 */
	private static void createRooms(int n) {
		requiredEndRoom = 1;
		requiredMarket = 2;
		requiredKeyRoom = 1;
		int ispawn = (int)(Math.random() * Map.mapRoomSize.x);
		int jspawn = (int)(Math.random() * Map.mapRoomSize.y);
		Map.spawnPixelPosition = new Vector2f(((float)ispawn+0.5f)*Map.roomPixelSize.x, ((float)jspawn+0.5f)*Map.roomPixelSize.y);
		Map.spawnRoomPosition = new Vector2f(ispawn, jspawn);
		Room r = new SpawnRoom((float)ispawn*Map.roomPixelSize.x,(float)jspawn*Map.roomPixelSize.y);
		r.construct();
		roomsGrid[(int)ispawn][(int)jspawn] = r;
		rooms.add(r);
		boolean stop = false;
		while (rooms.size() >0 && !stop && (rooms.size() + surroundedRooms.size()) <= n) {
			int surround;
			int rand = (int) (Math.random() * rooms.size());
			int x;
			int y;
			do {
				r = rooms.get(rand);
				x = (int) (r.getX() / Map.roomPixelSize.x);
				y = (int) (r.getY() / Map.roomPixelSize.y);
				surround = surround(x, y);
				if(surround>2){
					surroundedRooms.add(rooms.remove(rand));
					rand = (int)(Math.random() * rooms.size());
				}
			} while (surround > 2 && rooms.size() > 0);
			Room r2;
			
			if (surround < 3 && rooms.size()>0) {
				int rand2 = (int) (Math.random() * (4 - surround));
				if (y != 0 && canBeRoom(x, y - 1)) {
					if (rand2 == 0) {
						r2 = randRoom(x * Map.roomPixelSize.x, (y - 1) * Map.roomPixelSize.y, rooms.size(), n);
						roomsGrid[x][y - 1] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (x != Map.mapRoomSize.x - 1 && canBeRoom(x + 1, y)) {
					if (rand2 == 0) {
						r2 =randRoom((x + 1) * Map.roomPixelSize.x, y * Map.roomPixelSize.y, rooms.size(), n);
						roomsGrid[x + 1][y] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (y != Map.mapRoomSize.y - 1 && canBeRoom(x, y + 1)) {
					if (rand2 == 0) {
						r2 = randRoom(x * Map.roomPixelSize.x, (y + 1) * Map.roomPixelSize.y, rooms.size(), n);
						roomsGrid[x][y + 1] = r2;
						rooms.add(r2);
					} else {
						rand2--;
					}
				}
				if (x != 0 && roomsGrid[x - 1][y] == null
						&& canBeRoom(x - 1, y)) {
					if (rand2 == 0) {
						r2 = randRoom((x - 1) * Map.roomPixelSize.x,y * Map.roomPixelSize.y, rooms.size(), n);
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
						Door door1 = roomsGrid[i][j].createDoor(0);
						Door door2 = roomsGrid[i][j-1].createDoor(2);
						Sas sas = new Sas(roomsGrid[i][j], roomsGrid[i][j-1], door1, door2);
						roomsGrid[i][j].setSas(sas,0);
						roomsGrid[i][j-1].setSas(sas,2);
					}
					if (i > 0 && roomsGrid[i - 1][j] != null) {
						Door door1 = roomsGrid[i][j].createDoor(3);
						Door door2 = roomsGrid[i-1][j].createDoor(1);
						Sas sas = new Sas(roomsGrid[i][j], roomsGrid[i-1][j], door1, door2);
						roomsGrid[i][j].setSas(sas,3);
						roomsGrid[i-1][j].setSas(sas,1);
					}
				}
			}
		}
	}

	/**
	 * Compute the number of cells surrounding the one referenced by the function
	 * parameters.
	 * 
	 * @param i is the line index
	 * @param j is the column index
	 * @return An integer representing the number of cells surrounding the one
	 * located at (<b>i</b>, <b>j</b>). 
	 */
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

	/**
	 * Test whether the cell located at (<b>i</b>, <b>j</b>) satisfies the
	 * requirements to be a room.
	 * 
	 * @param i is the line index
	 * @param j is the column index
	 * @return <b>true</b> if the cell can be a room, <b>false</b> otherwise.
	 */
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
	
	private static Room randRoom(float x, float y, int nbRoom, int n){
		Room room;

		Double rand = Math.random();
		Double factor = ((double)nbRoom)/n;
		Double thresholdEndRoom = factor * requiredEndRoom;
		Double thresholdMarket = factor * requiredMarket + thresholdEndRoom;
		Double thresholdKeyRoom = factor * requiredKeyRoom + thresholdMarket;
		if(rand < thresholdEndRoom)
		{
			room = new EndRoom(x,y);
			room.construct();
			requiredEndRoom--;
			return room;
		}
		else if(rand < thresholdMarket)
		{
			room = new Market(x,y);
			room.construct();
			requiredMarket--;
			return room;
		}
		else if(rand < thresholdKeyRoom)
		{
			room = new KeyRoom(x,y);
			room.construct();
			requiredKeyRoom--;
			return room;
		}

		rand = Math.random();
		if(rand > 0.4){
			room = new EnergyRoom(x,y);
		}
		else{
			room = new RandomBlockRoom(x,y);
		}

		room.construct();
		return room;
	}
}
