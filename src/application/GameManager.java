package application;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import drawing.Renderer;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.util.Pair;
import logic.base.GameInterruptException;
import logic.base.InvalidEventDataException;
import logic.util.DataManager;

/*
 * GameManager manages the flow of the game
 * this class utilizes singleton pattern
 * to access the instance use <code> getInstance() </code>
 */
public class GameManager {
	public static final double NATIVE_WIDTH = 600; // orginal width the game is designed in
	public static final double NATIVE_HEIGHT = 600; // original height the game is designed in

	private Stage stage;
	
	// current active game scene
	private GameScene currentScene; 
	
	// GameManager instance (singleton pattern)
	private static GameManager INSTANCE;
	
	// AnimationTimer used for  main gameloop
	private AnimationTimer timer; 
	
	// counters used for generating name for unnamed game objects
	private static long genCounter1 = 0; 
	private static long genCounter2 = 0;

	// boolean value determining whether the manager had been initialized via init() or not 
	private boolean initialized = false;
	
	// boolean value determining whether the manager is updating
	private boolean isUpdating;

	// class representing various game event
	public static class GameEvent {
		
		// This class is more like a struct, so all field are public, no need for encapsulation
		
		public GameScene source; // GameScene that fired the event 
		public GameEventType type; // type of event
		public Object data; // data which will be used to handle the event

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

	/*
	 	enumeration of all game event types
	 	SCENE_CHANGE 
	 		for changing scene
	 		data : the new GameScene 
	 	GAME_PAUSE
	 		for pausing scene, no data needed
	 	GAME_RESUME
	 		for resuming scene, no data needed
	 	WRITE_PERSISTENT_DATA, 
	 		writing a persistent data, which is a map from String to Object
	 		data : Pair<String,Object>
	 	SAVE_PERSISTENT_DATA, LOAD_PERSISTENT_DATA
	 		save and load persistent data, 
	 		data : a callback function that accepts a boolean value which will be true when successfully saving/loading
	*/
	public enum GameEventType {
		SCENE_CHANGE, GAME_PAUSE, GAME_RESUME, WRITE_PERSISTENT_DATA, SAVE_PERSISTENT_DATA, LOAD_PERSISTENT_DATA;
	};
	
	// prevents GameManager from being initialized by other classes
	private GameManager() {};

	/*
		handle an event
		InvalidEventDataException will be thrown when the data is not valid for the event type
	*/
	private void handleEvent(GameEvent evt) throws InvalidEventDataException {
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
			// data is of invalid type
			e.printStackTrace();
			throw new InvalidEventDataException(evt);
		}
	}

	static {
		INSTANCE = new GameManager(); // initialize the instance
	}

	/*
	 * a function for generating name for GameObjects without assigned name
	 */
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
	
	// event queue
	private Queue<GameEvent> events = new LinkedList<GameEvent>();

	/*
	 * signaling an event, this event is enqueued and will be handled at the end of the frame 
	 */
	public void signalEvent(GameEvent evt) {
		events.add(evt);
	}

	/*
	 * initialize game manager with initialScene as first scene
	 * setup animation time
	 */
	public void init(GameScene initialScene) {

		stage = Main.getStage();
		
		currentScene = initialScene;
		currentScene.init();
		
		initialized = true; 

		// Animation timer for main game loop
		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				try {
					GameScene currentScene = getCurrentScene();
					Renderer renderer = Renderer.getInstance();
					
					// updating the scene
					isUpdating = true;
					currentScene.update();
					isUpdating = false;
					
					// handle events in queue
					while (!events.isEmpty()) {
						GameEvent evt = events.poll(); // get first event in queue
						try {
							System.out.println(evt);
							if (evt.source == getCurrentScene()) {
								handleEvent(evt); // only handle event from current scene
							}
						} catch (InvalidEventDataException e) {
							// invalid event data type
							System.err.println(new StringBuilder("Invalid Event Data: ").append(evt));
						}
					}
					events.clear(); // clear the queue
					
					// render the scene
					renderer.render(currentScene);
					
				} catch (GameInterruptException e) {
					// handle game interrupts
					e.printStackTrace();
				}

			}
		};
		timer.start();
		
		stage.setScene(currentScene);
		stage.show();
	}


	/*
	 * set current scene
	 */
	private void setScene(GameScene scene) {
		
		if (!initialized) {
			// the instance is not initialized by init(GameScene)
			System.err.println("GameManger not initialized!!!");
			return;
		}
		
		timer.stop(); // Temporarily stop game loop
		
		
		currentScene.destroy(); //destroy current scene
		currentScene = scene; // set new scene 
		currentScene.init(); // initialize new scene
		
		stage.setScene(scene); // add scene to stage
		stage.show();
		
		timer.start(); // start game loop again
	}

	/*
	 * get the GameManager instance (singleton pattern)
	 */
	public static GameManager getInstance() {
		return INSTANCE;
	}
	
	/*
	 * getter for currentScene
	 */
	public GameScene getCurrentScene() {
		return currentScene;
	}
	
	/*
	 * getter for isUpdating
	 */
	public boolean isUpdating() {
		return isUpdating;
	}

}
