package event;

import org.lwjgl.Sys;

public class Timer {
	static public long unitInOneSecond = 1000;
	static private long lastFrame = (Sys.getTime() * unitInOneSecond) / Sys.getTimerResolution();
	static private long currentFrame = (Sys.getTime() * unitInOneSecond) / Sys.getTimerResolution();
	static private long delta = 0;
	static private long runningTime = 0;
	static private int FPS = 0;
	static private int FPScpt = 0;
	static private String fpsDisplay = new String();
	static private String chronoDisplay = new String();
	static private long chrono = 0;
	static private boolean running = false;
	
	static public long getTime() {
        return currentFrame ;
    }
	
	static public long getDelta() {
        return delta;
    }
	
	static public void tick(){
		lastFrame = currentFrame;
		currentFrame = (Sys.getTime() * unitInOneSecond) / Sys.getTimerResolution();
        delta = currentFrame - lastFrame;
        
        runningTime += delta;
        if(running){
        	chrono += delta;
        	int hours = (int) (chrono/(3600 * unitInOneSecond));
        	int hoursInUnit = (int) (hours * 3600 * unitInOneSecond);
        	int minutes = (int) ((chrono - hoursInUnit)/(60*unitInOneSecond));
        	int minutesInUnit = (int) (minutes * 60 * unitInOneSecond);
        	int seconds = (int) ((chrono - hoursInUnit - minutesInUnit )/unitInOneSecond);
        	
        	chronoDisplay = "Time : " +  hours + ":" + ((minutes<10)?"0"+minutes:minutes) +  ":" + ((seconds<10)?"0"+seconds:seconds);
        }
        
        FPScpt++;
        if(currentFrame/500 != lastFrame/500){
        	FPS = FPScpt*2;
        	FPScpt = 0;
        	fpsDisplay = "FPS : " + FPS;
        }
	}
	
	static public int getFPS(){
		return FPS;
	}
	
	static public String getFPSDisplay(){
		return fpsDisplay;
	}
	
	static public long getRunningTime(){
		return runningTime;
	}
	
	static public void start(){
		running = true;
	}
	
	static public void pause(){
		running = false;
	}
	
	static public long stop(){
		running = false;
		long tmp = chrono;
		chrono = 0;
		return tmp;
	}
	
	static public String getChrono(){
		return chronoDisplay;
	}
}
