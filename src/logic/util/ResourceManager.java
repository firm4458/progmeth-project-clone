package logic.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import javafx.scene.image.Image;

public class ResourceManager {
	private ResourceManager() {}
	private static TreeMap<String,InputStream> map = new TreeMap<String,InputStream>();
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
	
}
