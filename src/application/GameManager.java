package application;

import drawing.Renderer;
import javafx.animation.AnimationTimer;
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
	static {
		gameManager = new GameManager();
	}
	
	public static String getGeneratedName() {
		String name ="UNNAMED_OBJECT_"+Long.toString(genCounter2)+" "+Long.toString(genCounter1);
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
					getCurrentScene().update();
					Renderer.getInstance().render();
				} catch (SceneChangeInterruptException e) {
					GameManager.getInstance().setScene(e.getScene());
					return;
				} catch (GameInterruptException e) {
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
