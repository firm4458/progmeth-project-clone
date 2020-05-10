package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.TreeMap;

import logic.util.ResourceManager;

import drawing.Renderer;
import javafx.animation.AnimationTimer;
import javafx.stage.FileChooser;
import logic.base.GameInterruptException;
import logic.base.SceneChangeInterruptException;

public class GameManager {
	public static final double NATIVE_WIDTH=600;
	public static final double NATIVE_HEIGHT=600;
	
	private GameScene currentScene;
	private static GameManager gameManager;
	private AnimationTimer timer;
	private static int score = 0;
	private static long genCounter1 = 0;
	private static long genCounter2 = 0;
	
	private static TreeMap<String,Object> persistentData = new TreeMap<String,Object>();;
	
	private boolean isUpdating;
	
	public boolean isFreezing;
	
	public Object getPersistentData(String key) {
		return persistentData.get(key);
	}
	
	public void setPersistentData(String key, Object value) {
		persistentData.put(key, value);
	}
	
	public void save() {
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			f = new FileOutputStream(ResourceManager.saveFile("Create a save file"));
			o = new ObjectOutputStream(f);
			o.writeObject(persistentData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				o.close();
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		System.out.println("load");
		FileInputStream fi = null;
		ObjectInputStream oi = null;
		try {
			fi = new FileInputStream(ResourceManager.pickFile("Choose a save file"));
			oi = new ObjectInputStream(fi);
			persistentData = (TreeMap<String,Object>)oi.readObject();
			score = (int) persistentData.get("score");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oi.close();
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch(Exception e) {
				//e.printStackTrace();
			}
		}
	}
	
	public boolean isUpdating() {
		return isUpdating;
	}

	public void setUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}

	static {
		gameManager = new GameManager();
	}
	
	public static String getGeneratedName() {
		String name ="UNNAMED_OBJECT_"+Long.toString(genCounter2)+"_"+Long.toString(genCounter1);
		if(genCounter1 == Long.MAX_VALUE) {
			genCounter2++;
			genCounter1=0;
		}else {
			genCounter1++;
		}
		return name;
	}

	public void init() {
		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				try {
					setUpdating(true);
					getCurrentScene().update();
					setUpdating(false);
					setPersistentData("score", score);
					Renderer.getInstance().render();
				} catch (SceneChangeInterruptException e) {
					GameManager.getInstance().setScene(e.getScene());
					return;
				}catch (GamePauseException e){
					getCurrentScene().pause();
				}catch (GameInterruptException e) {
					e.printStackTrace();
				} 

			}
		};
		timer.start();
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	public void setScene(GameScene scene) {
		if (currentScene != null) {
			currentScene.destroy();
		}
		Renderer.getInstance().reset();
		currentScene = scene;
		currentScene.init();
	}

	public static GameManager getInstance() {
		return gameManager;
	}

	public GameScene getCurrentScene() {
		return currentScene;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
