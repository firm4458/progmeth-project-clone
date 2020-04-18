package logic.base;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import drawing.Sprite;
import javafx.scene.canvas.GraphicsContext;
import logic.util.IncompatibleScriptException;

public class GameObject implements Destroyable {
	protected final ArrayList<Script> scripts  = new ArrayList<Script>();
	protected double X;
	protected double Y;
	protected boolean isActive;
	protected boolean isDestroyed;
	protected Sprite sprite;
	
	public GameObject(double X, double Y) {
		this.X = X;
		this.Y = Y;
		isDestroyed = false;
	}
	
	public GameObject(double X, double Y, Sprite sprite) {
		this.X = X;
		this.Y = Y;
		isDestroyed = false;
		this.sprite = sprite;
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
	
	public void update() {
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
