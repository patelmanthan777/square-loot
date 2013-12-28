package configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.util.vector.Vector2f;

public class ConfigManager {
	private static final String separator = ":"; 
	
	
	public static boolean fullScreen = false;
	public static Vector2f resolution = new Vector2f(1280,720);
	public static int maxFps = 120;
	
	/**
	 * Initialize the configmanager class by reading from the "options.txt"
	 * file.
	 */
	public static void init(){
		String fichier = "options.txt";
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			while ((line=br.readLine())!=null){
				String[] lineSplit = line.split(separator);
				switch(lineSplit[0]){
					case ("resolution"):
						String [] resSplit = lineSplit[1].split("x");
						resolution.x = Float.parseFloat(resSplit[0]);
						resolution.y = Float.parseFloat(resSplit[1]);
						break;
					case ("fullscreen"):
						fullScreen = Boolean.parseBoolean(lineSplit[1]);
						break;
					case ("maxFps"):
						maxFps = Integer.parseInt(lineSplit[1]);
						break;
				}
			}
			br.close(); 
		}catch (Exception e){
			System.out.println(e.toString());
		}
	}
}
