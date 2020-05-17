package application;

import java.util.ArrayList;

import drawing.SimpleCamera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import logic.base.Destroyable;
import logic.base.Dio;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.util.InputUtil;
import logic.util.group.DuplicateGameObjectException;
import logic.util.group.GameObjectGroup;

public abstract class GameScene extends Scene implements Destroyable {

	/*
	 * create a game scene
	 */
	public GameScene(String name) {
		super(new Group(), GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);

		this.name = name;
		
		// initialize scene's canvas and simple camera
		canvas = new Canvas(GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);
		simpleCamera = new SimpleCamera(canvas);

		// remove any lingering input
		InputUtil.reset();

		// detect key press
		setOnKeyPressed(e -> {
			InputUtil.setKeyPressed(e.getCode(), true);
		});
		setOnKeyReleased(e -> {
			InputUtil.setKeyPressed(e.getCode(), false);
		});

		initializeGroups();
		addGUIElement(canvas);
		addGameObject(simpleCamera);
	}

	private static final int DEFAULT_GAMEOBJ_BUFFER_SIZE = 100; // default size for buffer gameObjBuffer

	// buffer array used to store copied game objects in update loop
	// this is to prevent ConcurrentModificationException
	private GameObject[] gameObjBuffer;

	// GameObjectGroup holding all objects
	protected GameObjectGroup allObj;
	// ArrayList of all GameObjectGroup ( except allObj)
	protected ArrayList<GameObjectGroup> groups;

	// determine whether or not the time is freezed by TheWorld
	protected boolean isFreezing = false;

	// determine whether or not the scene is destroyed
	protected boolean isDestroyed = false;

	// determine whether or not the scene is paused
	protected boolean isPause;

	// canvas of this scene
	protected Canvas canvas;
	// simple camera of this scene
	protected SimpleCamera simpleCamera;
	
	// name of this scene
	protected String name;

	/*
	 * initialize the scene : adding game objects, adding scripts, etc.
	 * 
	 * note that there is no need for initializing protected fields(allObj, groups, etc.) 
	 * as that is done in the constructor
	 */
	public abstract void init();

	/*
	 * add a GUI element to scene's root
	 */
	public void addGUIElement(Node e) {
		Group root = (Group) getRoot();
		root.getChildren().add(e);
	}

	/*
	 * initialize array and groups
	 */
	private void initializeGroups() {
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		gameObjBuffer = new GameObject[DEFAULT_GAMEOBJ_BUFFER_SIZE];
	}

	public GameObject getGameObject(String name) {
		return allObj.getGameObject(name);
	}

	/*
	 * pause scene
	 */
	public void pause() {
		isPause = true;
	}

	/*
	 * resume scene
	 */
	public void resume() {
		isPause = false;
	}

	/*
	 * determine whether or not this gameObject should be updated
	 */
	private boolean shouldUpdate(GameObject obj) {
		return !obj.isDestroyed() && (!isFreezing || obj instanceof Dio);
	}

	/*
	 * copy elements in ArrayList into buffer this is done to prevent
	 * ConcurrentModificationException
	 */
	private void copyIntoBuffer() {
		if (allObj.getChildren().size() >= gameObjBuffer.length) {
			gameObjBuffer = new GameObject[allObj.getChildren().size() + DEFAULT_GAMEOBJ_BUFFER_SIZE];
		}

		/*
		 * copy elements in ArrayList into buffer note that array is null terminated 
		 * (last element will be null signifying that it is end of array)
		 */

		gameObjBuffer = allObj.getChildren().toArray(gameObjBuffer);
	}

	public final void update() throws GameInterruptException {

		// does not update if the game is paused
		if (!isPause) {

			// early update
			copyIntoBuffer();
			earlyUpdateObjects();

			// update
			copyIntoBuffer();
			updateObjects();

			// late update
			copyIntoBuffer();
			lateUpdateObjects();

		}

		// performing update on game groups ( by default, this remove any destroyed game
		// object from group)
		allObj.update();
		for (GameObjectGroup group : groups) {
			group.update();
		}

	}

	/*
	 * call earlyUpdate of every game objects
	 */
	private void earlyUpdateObjects() throws GameInterruptException {
		int index = 0;
		while (gameObjBuffer[index] != null && !isDestroyed()) { // iterate until null or scene is destroyed
			if (shouldUpdate(gameObjBuffer[index])) { // check if this game object should update
				gameObjBuffer[index].earlyUpdate();
			}
			++index;
		}
	}

	/*
	 * call update of every game objects
	 */
	private void updateObjects() throws GameInterruptException {
		int index = 0;
		while (gameObjBuffer[index] != null && !isDestroyed()) { // iterate until null or scene is destroyed
			if (shouldUpdate(gameObjBuffer[index])) { // check if this game object should update
				gameObjBuffer[index].update();
			}
			++index;
		}
	}

	/*
	 * call lateUpdate of every game objects
	 */
	private void lateUpdateObjects() throws GameInterruptException {
		int index = 0;
		while (gameObjBuffer[index] != null && !isDestroyed()) { // iterate until null or scene is destroyed
			if (shouldUpdate(gameObjBuffer[index])) { // check if this game object should update
				gameObjBuffer[index].lateUpdate();
			}
			++index;
		}
	}
	
	/*
	 * addGameObject to scene
	 */
	public void addGameObject(GameObject gameObj) {
		addGameObject(gameObj, allObj);
	}

	/*
	 * addGameObject to group
	 * note that you still need to call addGameObject to properly add game object to scene
	 */
	public void addGameObject(GameObject gameObj, GameObjectGroup group) {
		try {
			group.addGameObject(gameObj);
			gameObj.setScene(this);
		} catch (DuplicateGameObjectException e) {
			System.err.println("duplicate gameObj name!");
		}
	}

	/*
	 * create new group and add to groups
	 */
	public GameObjectGroup createGroup() {
		GameObjectGroup group = new GameObjectGroup();
		groups.add(group);
		return group;
	}

	/*
	 * destroy scene
	 */
	@Override
	public void destroy() {
		isDestroyed = true;
		allObj.forEach(gameObj -> gameObj.destroy()); // iterate through all game objects and destroy it
	}
	
	// getters and setters for necessary fields

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	public SimpleCamera getSimpleCamera() {
		return simpleCamera;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	public GameObjectGroup getAllGameObject() {
		return allObj;
	}

	public boolean isFreezing() {
		return isFreezing;
	}

	public void setFreezing(boolean isFreezing) {
		this.isFreezing = isFreezing;
	}

	public String getName() {
		return name;
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}
	
	
}
