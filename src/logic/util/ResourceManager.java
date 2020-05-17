package logic.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.TreeMap;

import application.Main;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ResourceManager {
	private ResourceManager() {
	}

	private static TreeMap<String, InputStream> map = new TreeMap<String, InputStream>();
	private static TreeMap<String, Image> imageMap = new TreeMap<String, Image>();
	private static TreeMap<String, Media> soundMap = new TreeMap<String, Media>();
	private static FileChooser fileChooser = new FileChooser();

	/**
	 * load initial data
	 */
	static {

		fileChooser.getExtensionFilters().add(new ExtensionFilter("Game Save File (*.save)", "*.save"));
		loadImage("img/loadImage.csv");
		cropPlayerImage();
		Font.loadFont(ClassLoader.getSystemResourceAsStream("font/ARCADECLASSIC.TTF"), 20);
		Font.loadFont(ClassLoader.getSystemResourceAsStream("font/m5x7.ttf"), 20);
	}

	/**
	 * crop ship picture into frame-by-frame and add it
	 */
	private static void cropPlayerImage() {
		Image fullimg = getImage("ship");
		Image[] images = new Image[20];
		int index = 0;
		for (int i = 0; i < 5; ++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(), i * 80, 0, 80, 120);
		}
		for (int i = 0; i < 5; ++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(), i * 80, 120, 80, 120);
		}

		addImage("fullPlayerImg", fullimg);
		addImage("idle1", images[2]);
		addImage("idle2", images[7]);
		addImage("left1", images[0]);
		addImage("left2", images[5]);
		addImage("right1", images[4]);
		addImage("right2", images[9]);
	}

	public static void addImage(String imageName, Image img) {
		imageMap.put(imageName, img);
	}

	public static void addImage(String imageName, String path, double width, double height, boolean preserveRatio) {
		try {
			Image img = new Image(ClassLoader.getSystemResource(path).toString(), width, height, preserveRatio, true);
			imageMap.put(imageName, img);
		} catch (NullPointerException e) {
			System.out.println("problem loading: " + path);
		}
	}

	/**
	 * load every image from csv file into image map
	 * @param pathToCSV this is a file that contain name, path, width, height, and preserve ratio of the image
	 */
	private static void loadImage(String pathToCSV) {
		try {
			Scanner in = new Scanner(ClassLoader.getSystemResourceAsStream("img/loadImage.csv"));
			while (in.hasNext()) {
				String line = in.next();
				String[] data = line.split(",");
				String name = data[0];
				String path = data[1];
				double width = Double.parseDouble(data[2]);
				double height = Double.parseDouble(data[3]);
				boolean preserveRatio = Boolean.parseBoolean(data[4]);
				addImage(name, path, width, height, preserveRatio);
			}
		} catch (Exception e) {
			System.out.println("encountered an error while loading image csv");
			e.printStackTrace();
		}
	}

	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}

	@Deprecated
	public static Image getImage(String url, double width, double height) {
		if (!map.containsKey(url)) {
			map.put(url, ClassLoader.getSystemResourceAsStream(url));
			map.get(url).mark(10000000);
		}
		try {
			map.get(url).reset();
			map.get(url).mark(10000000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Image(map.get(url), width, height, false, true);
	}

	public static Media getSound(String url) {
		if (!soundMap.containsKey(url)) {
			Media sound = new Media(ClassLoader.getSystemResource(url).toString());
			soundMap.put(url, sound);
		}
		return soundMap.get(url);
	}

	public static File pickFile(String title) {
		fileChooser.setTitle(title);
		return fileChooser.showOpenDialog(Main.getStage());
	}

	public static File saveFile(String title) {
		fileChooser.setTitle(title);
		return fileChooser.showSaveDialog(Main.getStage());
	}

}
