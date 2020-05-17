package logic.util.group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import drawing.Sprite;
import drawing.base.Renderable;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptNotFoundException;

public class GameObjectGroup implements Iterable<GameObject> {
	private final TreeSet<GameObject> children;
	private int framesBeforeUpdate;
	private int counter;

	public GameObjectGroup() {
		children = new TreeSet<GameObject>(GameObject.nameComparator);
		this.framesBeforeUpdate = 0;
		counter = 0;
	}
	
	/**
	 * if counter is equal to 0 set it back and update it
	 */
	public final void update() {
		if ((counter--) == 0) {
			counter = framesBeforeUpdate;
			updateAction();
		}
	}

	public void destroyAll() {
		for (GameObject gameObj : children) {
			gameObj.destroy();
		}
	}

	public boolean addGameObject(GameObject gameObj) throws DuplicateGameObjectException {
		boolean success = children.add(gameObj);
		if (!success) {
			throw new DuplicateGameObjectException(gameObj.getName());
		}
		return success;
	}

	public GameObject getGameObject(String name) {
		GameObject result = null;
		for (GameObject gameObj : children) {
			if (gameObj.getName().equals(name)) {
				result = gameObj;
			}
		}
		return result;
	}
	
	public <T extends Script> ArrayList<T> getScripts(Class<T> class1) {
		ArrayList<T> arr = new ArrayList<T>();
		for (GameObject gameObj : children) {
			try {
				arr.add(gameObj.getScript(class1));
			} catch (ScriptNotFoundException ex) {
				// Script not found in gameObj;
			}
		}
		return arr;
	}

	public ArrayList<Script> getScripts() {
		ArrayList<Script> arr = new ArrayList<Script>();
		for (GameObject gameObj : children) {
			arr.addAll(gameObj.getScripts());
		}
		return arr;
	}

	public ArrayList<Renderable> getRenderables() {
		ArrayList<Renderable> arr = new ArrayList<Renderable>();
		for (GameObject gameObj : children) {
			Sprite sprite = gameObj.getSprite();
			if (sprite != null) {
				arr.add(sprite);
			}
		}
		return arr;
	}

	public int size() {
		return children.size();
	}

	private void updateAction() {
		children.removeIf(gameObject -> gameObject.isDestroyed());
	}

	public TreeSet<GameObject> getChildren() {
		return children;
	}

	public void setFramesBeforeUpdate(int framesBeforeUpdate) {
		this.framesBeforeUpdate = framesBeforeUpdate;
	}

	@Override
	public Iterator<GameObject> iterator() {
		return children.iterator();
	}
}
