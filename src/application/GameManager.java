package application;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import drawing.Renderer;
import javafx.animation.AnimationTimer;
import javafx.util.Pair;
import logic.base.GameInterruptException;
import logic.base.InvalidEventDataException;
import logic.util.DataManager;

public class GameManager {
	public static final double NATIVE_WIDTH = 600;
	public static final double NATIVE_HEIGHT = 600;

	private GameScene currentScene;
	private static GameManager gameManager;
	private AnimationTimer timer;
	private static long genCounter1 = 0;
	private static long genCounter2 = 0;

	private boolean initialized = false;

	private boolean isUpdating;

	public boolean isFreezing;

	public static class GameEvent {
		public GameScene source;
		public GameEventType type;
		public Object data;

		public GameEvent(GameScene source, GameEventType type, Object data) {
			super();
			this.source = source;
			this.type = type;
			this.data = data;
		}

		@Override
		public String toString() {
			return String.format("GameEvent:{%s,%s,%s}", source, type, data);
		}
	}

	public enum GameEventType {
		SCENE_CHANGE, GAME_PAUSE, GAME_RESUME, WRITE_PERSISTENT_DATA, SAVE_PERSISTENT_DATA, LOAD_PERSISTENT_DATA;
	};

	void handleEvent(GameEvent evt) throws InvalidEventDataException {
		try {
			switch (evt.type) {
			case GAME_PAUSE:
				getCurrentScene().pause();
				break;
			case GAME_RESUME:
				getCurrentScene().resume();
				break;
			case SCENE_CHANGE:
				GameScene newScene = (GameScene) evt.data;
				setScene(newScene);

				break;
			case WRITE_PERSISTENT_DATA:
				@SuppressWarnings("unchecked")
				Pair<String, Object> p = (Pair<String, Object>) evt.data;
				String key = p.getKey();
				Object value = p.getValue();
				DataManager.getInstance().writePersistentData(key, value);

				break;
			case SAVE_PERSISTENT_DATA:
				@SuppressWarnings("unchecked")
				Consumer<Boolean> saveCallback = (Consumer<Boolean>) evt.data;
				saveCallback.accept(DataManager.getInstance().saveData());
				break;
			case LOAD_PERSISTENT_DATA:
				@SuppressWarnings("unchecked")
				Consumer<Boolean> loadCallback = (Consumer<Boolean>) evt.data;
				loadCallback.accept(DataManager.getInstance().loadData());
				break;
			default:
				break;

			}
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new InvalidEventDataException(evt);
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

	public static final String getGeneratedName() {
		String name = "UNNAMED_OBJECT_" + Long.toString(genCounter2) + "_" + Long.toString(genCounter1);
		if (genCounter1 == Long.MAX_VALUE) {
			genCounter2++;
			genCounter1 = 0;
		} else {
			genCounter1++;
		}
		return name;
	}

	private Queue<GameEvent> events = new LinkedList<GameEvent>();

	public void signalEvent(GameEvent evt) {
		events.add(evt);
	}

	public void init(GameScene initialScene) {

		currentScene = initialScene;
		currentScene.init();
		initialized = true;

		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				try {
					GameScene currentScene = getCurrentScene();
					Renderer renderer = Renderer.getInstance();
					setUpdating(true);
					currentScene.update();
					setUpdating(false);
					while (!events.isEmpty()) {
						GameEvent evt = events.poll();
						try {
							System.out.println(evt);
							if (evt.source == getCurrentScene()) {
								handleEvent(evt); // only handle event from current scene
							}
						} catch (InvalidEventDataException e) {
							System.err.println(new StringBuilder("Invalid Event Data: ").append(evt));
						}
					}
					events.clear();
					renderer.render(currentScene);
				} catch (GameInterruptException e) {
					e.printStackTrace();
				}

			}
		};
		timer.start();
		Main.stage.setScene(currentScene);
		Main.stage.show();
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	public void setScene(GameScene scene) {
		if (!initialized) {
			System.err.println("GameManger not initialized!!!");
			return;
		}
		timer.stop();
		currentScene.destroy();
		currentScene = scene;
		currentScene.init();
		Main.stage.setScene(scene);
		Main.stage.show();
		timer.start();
	}

	public static GameManager getInstance() {
		return gameManager;
	}

	public GameScene getCurrentScene() {
		return currentScene;
	}

}
