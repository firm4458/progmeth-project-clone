package logic.util;

import drawing.Sprite;
import javafx.scene.effect.ColorAdjust;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;

public class DamageEffect implements Script {
	
	private GameObject parent;
	private final ColorAdjust colorAdjust = new ColorAdjust();
	private long start;
	private long duration;
	
	public DamageEffect(long duration,GameObject parent) {
		colorAdjust.setBrightness(0.3);
		parent.getSprite().addEffect(parent.getName(),colorAdjust);
		try {
			setParent(parent);
		} catch (IncompatibleScriptException e) {
			e.printStackTrace();
		}
		this.duration = duration;
		start = System.currentTimeMillis();
	}

	@Override
	public void update() throws GameInterruptException {
		long now = System.currentTimeMillis();
		if(now-start>duration) {
			parent.removeScript(this);
		}
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		this.parent = parent;
	}
	
	@Override
	public void onDestroy() {
		parent.getSprite().removeEffect(parent.getName());
	}
	

}
