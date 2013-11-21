package event;

import org.lwjgl.Sys;

public class Timer {
	static private long lastFrame;
	
	static public void initTimer() {
		lastFrame = getTime();
	}
	
	static public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
	
	static public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
        return delta;
    }
}
