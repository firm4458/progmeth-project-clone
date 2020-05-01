package logic.util;

import java.io.File;
import java.io.InputStream;
import java.util.TreeMap;

import javafx.scene.image.Image;

public class ResourceManager {
	private ResourceManager() {}
	private static TreeMap<String,Image> map = new TreeMap<String,Image>();
	public static Image getImage(String url,double width, double height) {
		if(!map.containsKey(url)) {
			InputStream is = ClassLoader.getSystemResourceAsStream(url);
			map.put(url,new Image(is,width,height,false,true));
		}
		return map.get(url);
	}
	
}
