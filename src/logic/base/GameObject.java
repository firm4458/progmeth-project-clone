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
	protected final ArrayList<Script> scripts  = new ArrayList<Script>();
	protected double X;
	protected double Y;
	protected boolean isActive;
	protected boolean isDestroyed;
	protected Sprite sprite;
	protected String name;
	
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
			scripts.add(script);
		}catch(IncompatibleScriptException e) {
			e.printStackTrace();
		}
		
		return this;
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
	
	public void update() throws GameInterruptException {
			for(Script script : scripts) {
				if(isDestroyed) {
					break;
				}
				script.update();
			}
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
