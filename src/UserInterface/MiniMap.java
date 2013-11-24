package UserInterface;

import environment.room.Room;

public class Minimap extends Overlay{
	private Room[][] rooms;
	private int width;
	private int height;
	public Minimap(Room[][] rooms, int width, int height){
		this.rooms = rooms;
		this.width = width;
		this.height = height;
	}
	@Override
	public void draw() {
		for(int i = 0; i < width; i++){
			for(int j = 0; j < width; j++){
				if (rooms[i][j]!= null){
					
				}
			}
		}
	}
}
