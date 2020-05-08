package logic.base;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Comparator;

import application.GameManager;
import drawing.Sprite;
import javafx.scene.canvas.GraphicsContext;
import logic.util.IncompatibleScriptException;

public class GameObject implements Destroyable {
	public static final Comparator<GameObject> nameComparator = new Comparator<GameObject>() {
		@Override
		public int compare(GameObject o1, GameObject o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	private ArrayList<Script> bufferAdd = new ArrayList<Script>();
	private ArrayList<Script> bufferRemove = new ArrayList<Script>();
	protected final ArrayList<Script> scripts  = new ArrayList<Script>();
	protected double X;
	protected double Y;
	protected boolean isActive;
	protected boolean isDestroyed;
	protected Sprite sprite;
	protected String name;
	protected boolean isUpdating;
	
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
	
	public GameObject addScript(Script script) {
		try {
			script.setParent(this);
		}catch(IncompatibleScriptException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return this;
		}
		
		if(isUpdating) {
			bufferAdd.add(script);
		}else {
			scripts.add(script);
		}
		return this;
	}
	
	public GameObject removeScript(Script script) {
		if(isUpdating) {
			bufferRemove.add(script);
		}else {
			removeScriptNoConcurrency(script);
		}
		return this;
	}
	
	public void resolveBuffer() {
		for(Script script : bufferAdd) {
			addScript(script);
		}
		bufferAdd.clear();
		for(Script script : bufferRemove) {
			removeScriptNoConcurrency(script);
		}
		bufferRemove.clear();
	}
	
	private void removeScriptNoConcurrency(Script script) {
		script.onDestroy();
		scripts.remove(script);
	}
	
	public <T extends Script> T getScript(Class<T> type) throws ScriptNotFoundException{
		for(Script script : scripts) {
			if(type.isInstance(script)) {
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
		X+=translateX;
		Y+=translateY;
	}
	
	public final void earlyUpdate() throws GameInterruptException {
		resolveBuffer();
		isUpdating = true;
		for(Script script : scripts) {
			if(isDestroyed) {
				break;
			}
			script.earlyUpdate();
		}
		isUpdating = false;
	}
	
	public void update() throws GameInterruptException {
			resolveBuffer();
			isUpdating = true;
			for(Script script : scripts) {
				if(isDestroyed) {
					break;
				}
				script.update();
			}
			isUpdating = false;
	}
	
	public final void lateUpdate() throws GameInterruptException {
		resolveBuffer();
		isUpdating = true;
		for(Script script : scripts) {
			if(isDestroyed) {
				break;
			}
			script.lateUpdate();
		}
		isUpdating = false;
	}
	
	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	public void destroy() {
		isDestroyed = true;
		isActive = false;
		for(Script script : scripts) {
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
