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
import logic.base.Updatable;
import logic.util.InputUtil;
import logic.util.group.DuplicateGameObjectException;
import logic.util.group.GameObjectGroup;

public abstract class GameScene extends Scene implements Destroyable {

	protected GameObjectGroup allObj;
	protected ArrayList<Updatable> updatables;
	private static final int DEFAULT_GAMEOBJ_BUFFER_SIZE = 60;
	private static final int DEFAULT_UPDATABLE_BUFFER_SIZE = 60;

	protected ArrayList<GameObjectGroup> groups;

	protected boolean isFreezing = false;

	protected boolean isDestroyed = false;

	protected boolean isPause;

	private Canvas canvas;

	private GameObject[] gameObjBuffer;
	private Updatable[] updatableBuffer;

	protected SimpleCamera simpleCamera;

	public abstract void init();

	public Canvas getCanvas() {
		return canvas;
	}

	/*
	 * protected class ResizableCanvas extends Canvas {
	 * 
	 * public ResizableCanvas(double width, double height) { // Redraw canvas when
	 * size changes. super(width, height);
	 * widthProperty().bind(root.widthProperty());
	 * heightProperty().bind(root.heightProperty()); }
	 * 
	 * @Override public boolean isResizable() { return true; }
	 * 
	 * @Override public double prefWidth(double height) { return getWidth(); }
	 * 
	 * @Override public double prefHeight(double width) { return getHeight(); } }
	 */

	public GameObjectGroup getAllGameObject() {
		return allObj;
	}

	public boolean isFreezing() {
		return isFreezing;
	}

	public void setFreezing(boolean isFreezing) {
		this.isFreezing = isFreezing;
	}

	public void addGUIElement(Node e) {
		Group root = (Group) getRoot();
		root.getChildren().add(e);
	}

	public GameScene(String name) {
		super(new Group(), GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);
		Group root = (Group) getRoot();
		canvas = new Canvas(GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);
		simpleCamera = new SimpleCamera(canvas);
		root.getChildren().add(canvas);
		// Set Player move
		setOnKeyPressed(e -> {
			InputUtil.setKeyPressed(e.getCode(), true);
		});

		setOnKeyReleased(e -> {
			InputUtil.setKeyPressed(e.getCode(), false);
		});
		initializeGroups();
		addGameObject(simpleCamera);
	}

	public void addUpdatable(Updatable updatable) {
		updatables.add(updatable);
	}

	private void initializeGroups() {
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		updatables = new ArrayList<Updatable>();
		gameObjBuffer = new GameObject[DEFAULT_GAMEOBJ_BUFFER_SIZE];
		updatableBuffer = new Updatable[DEFAULT_UPDATABLE_BUFFER_SIZE];
	}

	public GameObject getGameObject(String name) {
		return allObj.getGameObject(name);
	}

	public void pause() {
		isPause = true;
	}

	public void flipPause() {
		isPause = !isPause;
	}

	public void resume() {
		isPause = false;
	}

	private boolean shouldUpdate(Destroyable obj) {
		return !obj.isDestroyed() && (!isFreezing || obj instanceof Dio);
	}

	private void copyIntoBuffer() {
		if (allObj.getChildren().size() >= gameObjBuffer.length) {
			gameObjBuffer = new GameObject[allObj.getChildren().size() + DEFAULT_GAMEOBJ_BUFFER_SIZE];
		}

		if (updatables.size() >= updatableBuffer.length) {
			updatableBuffer = new Updatable[updatables.size() + DEFAULT_UPDATABLE_BUFFER_SIZE];
		}

		// copy elements in arraylist into buffer
		// note that array is null terminated ( last element will be null signifying
		// that it is end of array)

		gameObjBuffer = allObj.getChildren().toArray(gameObjBuffer);
		updatableBuffer = updatables.toArray(updatableBuffer);
	}

	public void update() throws GameInterruptException {

		if (!isPause) {
			copyIntoBuffer();
			earlyUpdateObjects();
			copyIntoBuffer();
			updateObjects();
			copyIntoBuffer();
			lateUpdateObjects();
		}

		allObj.update();
		for (GameObjectGroup group : groups) {
			group.update();
		}

	}

	private void earlyUpdateObjects() throws GameInterruptException {
		int index = 0;
		while (gameObjBuffer[index] != null && !isDestroyed()) {
			if (shouldUpdate(gameObjBuffer[index])) {
				gameObjBuffer[index].earlyUpdate();
			}
			++index;
		}
		index = 0;
		while (updatableBuffer[index] != null && !isDestroyed()) {
			if (shouldUpdate(updatableBuffer[index])) {
				updatableBuffer[index].earlyUpdate();
			}
			++index;
		}
	}

	private void updateObjects() throws GameInterruptException {
		int index = 0;
		while (gameObjBuffer[index] != null && !isDestroyed()) {
			if (shouldUpdate(gameObjBuffer[index])) {
				gameObjBuffer[index].update();
			}
			++index;
		}
		index = 0;
		while (updatableBuffer[index] != null && !isDestroyed()) {
			if (shouldUpdate(updatableBuffer[index])) {
				updatableBuffer[index].update();
			}
			++index;
		}
	}

	private void lateUpdateObjects() throws GameInterruptException {
		int index = 0;
		while (gameObjBuffer[index] != null && !isDestroyed()) {
			if (shouldUpdate(gameObjBuffer[index])) {
				gameObjBuffer[index].lateUpdate();
			}
			++index;
		}
		index = 0;
		while (updatableBuffer[index] != null && !isDestroyed()) {
			if (shouldUpdate(updatableBuffer[index])) {
				updatableBuffer[index].lateUpdate();
			}
			++index;
		}
	}

	public void addGameObject(GameObject gameObj) {
		addGameObject(gameObj, allObj);
	}

	public void addGameObject(GameObject gameObj, GameObjectGroup group) {
		try {
			group.addGameObject(gameObj);
			gameObj.setScene(this);
		} catch (DuplicateGameObjectException e) {
			System.err.println("duplicate gameObj name!");
		}
	}

	public GameObjectGroup createGroup() {
		GameObjectGroup group = new GameObjectGroup();
		groups.add(group);
		return group;
	}

	@Override
	public void destroy() {
		isDestroyed = true;
		allObj.forEach(gameObj -> gameObj.destroy());
		updatables.forEach(updatable -> updatable.destroy());
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	public SimpleCamera getSimpleCamera() {
		return simpleCamera;
	}
}
