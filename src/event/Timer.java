package event;

import org.lwjgl.Sys;

public class Timer {
	static private long lastFrame = (Sys.getTime() * 1000) / Sys.getTimerResolution();
	static private long currentFrame = (Sys.getTime() * 1000) / Sys.getTimerResolution();
	static private int delta = 0;
	static private long runningTime = 0;
	static private int FPS = 0;
	static private int FPScpt = 0;
	static private String fpsDisplay = new String();
	
	static public long getTime() {
        return currentFrame ;
    }
	
	static public int getDelta() {
        return delta;
    }
	
	static public void tick(){
		lastFrame = currentFrame;
		currentFrame = (Sys.getTime() * 1000) / Sys.getTimerResolution();
        delta = (int) (currentFrame - lastFrame);
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
