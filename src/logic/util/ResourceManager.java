package logic.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import application.GUI;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ResourceManager {
	private ResourceManager() {}
	private static TreeMap<String,InputStream> map = new TreeMap<String,InputStream>();
	private static TreeMap<String,Image> imageMap = new TreeMap<String, Image>();
	private static TreeMap<String,Media> soundMap = new TreeMap<String,Media>();
	private static FileChooser fileChooser = new FileChooser();
	
	static {
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Game Save File (*.save)","*.save"));
		
		addImage("bullet", "img/bullet1.png", 50, 50, true);
		addImage("meteor", "img/meteor.png", 29.2, 50, false);
		addImage("bossShip","img/boss.png",230*2, 300*2,false);
		addImage("bossBullet", "img/bossBullet.png",20,20,false);
		addImage("shield", "img/spr_shield.png",600,600,true);
		addImage("logo","img/Logo.png",400,124,true);
		addImage("normalLevelBanner","img/normalLevelBanner2.png",400,100,true);
		
		Image fullimg = new Image("img/ship.png",400,240,true,true);
		Image[] images = new Image[20];
		int index = 0;
		for(int i=0;i<5;++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(),i*80,0,80,120);
		}
		for(int i=0;i<5;++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(),i*80,120,80,120);
		}
		
		addImage("fullPlayerImg",fullimg);
		addImage("idle1",images[2]);
		addImage("idle2",images[7]);
		addImage("left1",images[0]);
		addImage("left2",images[5]);
		addImage("right1",images[4]);
		addImage("right2",images[9]);
	}
	
	public static void addImage(String imageName, Image img) {
		imageMap.put(imageName, img);
	}
	
	public static void addImage(String imageName, String path, double width, double height, boolean perserveRatio) {
		try {
			Image img = new Image(ClassLoader.getSystemResource(path).toString(),width,height,perserveRatio,true);
			imageMap.put(imageName, img);
		}catch(NullPointerException e) {
			System.out.println(path);
		}
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
	
	public static File pickFile(String title) {
		fileChooser.setTitle(title);
		return fileChooser.showOpenDialog(GUI.stage);
	}
	
	public static File saveFile(String title) {
		fileChooser.setTitle(title);
		return fileChooser.showSaveDialog(GUI.stage);
	}
}
