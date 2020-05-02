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
	private static TreeMap<String,Image> imageMap = new TreeMap<String, Image>();
	private static TreeMap<String,Media> soundMap = new TreeMap<String,Media>();
	
	static {
		addImage("bullet", "img/bullet1.png", 50, 50, true);
	}
	
	public static void addImage(String imageName, String path, double width, double height, boolean perserveRatio) {
		Image img = new Image(ClassLoader.getSystemResource(path).toString(),width,height,perserveRatio,true);
		imageMap.put(imageName, img);
	}
	
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}
	
	@Deprecated
	public static Image getImage(String url,double width, double height) {
		if(!map.containsKey(url)) {
			map.put(url, ClassLoader.getSystemResourceAsStream(url));
			map.get(url).mark(10000000);
		}
		try {
			map.get(url).reset();
			map.get(url).mark(10000000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Image(map.get(url),width,height,false,true);
	}
	public static Media getSound(String url) {
		if(!soundMap.containsKey(url)) {
			Media sound = new Media(ClassLoader.getSystemResource(url).toString());
			soundMap.put(url,sound);
		}
		return soundMap.get(url);
	}
}
