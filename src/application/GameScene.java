package application;

import java.util.ArrayList;

import javafx.util.Pair;
import logic.base.Destroyable;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.util.GameObjectGroup;

public abstract class GameScene implements Destroyable {

	protected GameObjectGroup allObj;
	protected GameObjectGroup gui;
	protected ArrayList<Pair<GameObject, GameObjectGroup>> addBuffer;

	protected ArrayList<GameObjectGroup> groups;

	protected boolean isDestroyed=false;
	
	protected boolean isPause;

	private String name;

	public abstract void init();

	public GameScene() {
		allObj = new GameObjectGroup();
		gui = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
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

	public void update() throws GameInterruptException {
		if(!isPause) {
			resolveBuffer();
			for (GameObject gameObj : allObj) {
				gameObj.earlyUpdate();
			}
			resolveBuffer();
			for (GameObject gameObj : allObj) {
				gameObj.update();
			}
			resolveBuffer();
			for (GameObject gameObj : allObj) {
				gameObj.lateUpdate();
			}
		}
		resolveBuffer();
		for(GameObject gameObj: gui) {
			gameObj.update();
		}
		

		allObj.update();
		gui.update();
		for (GameObjectGroup group : groups) {
			group.update();
		}

		

	}
	
	private void resolveBuffer() {
		for (var p : addBuffer) {
			if(isDestroyed) {
				System.out.println("AAAA");
			}
			p.getValue().getChildren().add(p.getKey());
		}

		addBuffer.clear();
	}
	
	public void addGUIObject(GameObject gameObj) {
		addGameObject(gameObj, gui);
	}

	public void addGameObject(GameObject gameObj) {
		addBuffer.add(new Pair<GameObject, GameObjectGroup>(gameObj, allObj));
	}

	public void addGameObject(GameObject gameObj, GameObjectGroup group) {
		addBuffer.add(new Pair<GameObject, GameObjectGroup>(gameObj, group));
	}

	public GameObjectGroup createGroup() {
		GameObjectGroup group = new GameObjectGroup();
		groups.add(group);
		return group;
	}

	@Override
	public void destroy() {
		isDestroyed = true;
		for (GameObject gameObj : allObj.getChildren()) {
			gameObj.destroy();
		}
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
