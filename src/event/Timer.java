package event;

import org.lwjgl.Sys;

public class Timer {
	static private long unitInOneSecond = 1000;
	static private long lastFrame = (Sys.getTime() * unitInOneSecond) / Sys.getTimerResolution();
	static private long currentFrame = (Sys.getTime() * unitInOneSecond) / Sys.getTimerResolution();
	static private long delta = 0;
	static private long runningTime = 0;
	static private int FPS = 0;
	static private int FPScpt = 0;
	static private String fpsDisplay = new String();
	
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
        runningTime+=delta;
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
}
