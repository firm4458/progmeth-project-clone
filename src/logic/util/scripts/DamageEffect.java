package logic.util.scripts;

import javafx.scene.effect.ColorAdjust;
import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;

public class DamageEffect extends BasicScript<GameObject> {

	private final ColorAdjust colorAdjust = new ColorAdjust();
	private long start;
	private long duration;

	public DamageEffect(long duration, GameObject parent) {
		colorAdjust.setBrightness(0.3);
		this.duration = duration;
		start = System.currentTimeMillis();
	}

	public DamageEffect(long duration, double brightness) {
		colorAdjust.setBrightness(brightness);
		this.duration = duration;
		start = System.currentTimeMillis();
	}

	@Override
	public void onAttach() {
		parent.getSprite().addEffect("damageEffect", colorAdjust);
	}

	@Override
	public void update() throws GameInterruptException {
		long now = System.currentTimeMillis();
		if (now - start > duration) {
			parent.removeScript(this);
		}
	}

	@Override
	public void onDestroy() {
		parent.getSprite().removeEffect("damageEffect");
	}

}
