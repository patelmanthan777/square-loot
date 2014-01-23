package environment;

import environment.room.Room;

public class Sas {
	private Room r1;
	private Room r2;
	private Door d1;
	private Door d2;
	
	public Sas(Room room1, Room room2, Door door1, Door door2){
		this.r1 = room1;
		this.r2 = room2;
		this.d1 = door1;
		this.d2 = door2;
	}
	
	public boolean isOpened(){
		return d1.isOpened() && d2.isOpened();
	}
	
	public void open(){
		d1.open();
		d2.open();
	}
	
	public void close(){
		d1.close();
		d2.close();
	}
	
	public void toggle(){
		if(isOpened())
			close();
		else
			open();
	}
}
