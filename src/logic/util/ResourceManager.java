package logic.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ResourceManager {
	private ResourceManager() {}
	private static TreeMap<String,InputStream> map = new TreeMap<String,InputStream>();
	private static TreeMap<String,MediaPlayer> soundMap = new TreeMap<String,MediaPlayer>();
	public static Image getImage(String url,double width, double height) {
		if(!map.containsKey(url)) {
			map.put(url, ClassLoader.getSystemResourceAsStream(url));
			map.get(url).mark(0);
		}
		try {
			map.get(url).reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Image(map.get(url),width,height,false,true);
	}
	public static void playSound(String url) {
		if(!soundMap.containsKey(url)) {
			Media sound = new Media(ClassLoader.getSystemResource("sound/theme.mp3").toString());
			soundMap.put(url,new MediaPlayer(sound));
		}
		soundMap.get(url).play();
	}
}
