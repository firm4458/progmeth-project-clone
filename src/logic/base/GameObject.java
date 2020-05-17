package logic.base;

import java.util.ArrayList;
import java.util.Comparator;
import application.GameManager;
import application.GameScene;
import drawing.Sprite;


/**
 * GameObject represents everything that is in the game 
 * GameObject does not have any behavior by itself,
 * but can be added by using Script
 * 
 * @see Script
 */
public class GameObject implements Destroyable {
	protected static final int DEFAULT_SCRIPT_BUFFER_SIZE = 20;
	public static final Comparator<GameObject> nameComparator = new Comparator<GameObject>() {
		@Override
		public int compare(GameObject o1, GameObject o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	protected final ArrayList<Script> scripts = new ArrayList<Script>();
	protected double X;
	protected double Y;
	protected boolean isActive;
	protected boolean isDestroyed;
	protected Sprite sprite;
	protected String name;
	protected boolean isUpdating;
	protected GameScene scene = null;

	public GameScene getScene() {
		return scene;
	}

	public void setScene(GameScene scene) {
		if (this.scene == null) {
			this.scene = scene;
		}
	}

	public String getName() {
		return name;
	}

	public GameObject(double X, double Y) {
		this.X = X;
		this.Y = Y;
		name = GameManager.getGeneratedName();
		isDestroyed = false;

	}

	public GameObject(double X, double Y, String name) {
		this.X = X;
		this.Y = Y;
		this.name = name;
		isDestroyed = false;

	}

	public ArrayList<Script> getScripts() {
		return scripts;
	}

	public GameObject addScript(Script script) {
		try {
			script.setParent(this);
			scripts.add(script);
			script.onAttach();
		} catch (IncompatibleScriptException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return this;
		}
		return this;
	}

	public GameObject removeScript(Script script) {
		script.onDestroy();
		scripts.remove(script);
		return this;
	}

	public <T extends Script> T getScript(Class<T> type) throws ScriptNotFoundException {
		for (Script script : scripts) {
			if (type.isInstance(script)) {
				return type.cast(script);
			}
		}
		throw new ScriptNotFoundException();
	}

	public double getX() {
		return X;
	}

	public double getY() {
		return Y;
	}

	public void translate(double translateX, double translateY) {
		X += translateX;
		Y += translateY;
	}

	public final void earlyUpdate() throws GameInterruptException {
		ArrayList<Script> scripts = new ArrayList<Script>(this.scripts);
		for (Script script : scripts) {
			if (!isDestroyed()) {
				script.earlyUpdate();
			}
		}
	}

	public final void update() throws GameInterruptException {
		ArrayList<Script> scripts = new ArrayList<Script>(this.scripts);
		for (Script script : scripts) {
			if (!isDestroyed()) {
				script.update();
			}
		}
	}

	public final void lateUpdate() throws GameInterruptException {
		ArrayList<Script> scripts = new ArrayList<Script>(this.scripts);
		for (Script script : scripts) {
			if (!isDestroyed()) {
				script.lateUpdate();
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public void destroy() {
		isDestroyed = true;
		isActive = false;
		ArrayList<Script> scripts = new ArrayList<Script>(this.scripts);
		for (Script script : scripts) {
			script.onDestroy();
		}
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setX(double x) {
		X = x;
	}

	public void setY(double y) {
		Y = y;
	}

}
