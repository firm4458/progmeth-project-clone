package logic.util.scripts;

import javafx.scene.effect.ColorAdjust;
import logic.base.BasicScript;
import logic.base.GameObject;

public class FlashingScript extends BasicScript<GameObject> {

	private double speed;
	private double amplitude;
	private double a;
	private ColorAdjust effect;

	public FlashingScript(double speed, double amplitude) {
		super();
		this.speed = speed;
		this.amplitude = amplitude;
		a = 0;
	}

	@Override
	public void onAttach() {
		effect = new ColorAdjust();
		parent.getSprite().addEffect("Flashing", effect);
	}

	@Override
	public void update() {
		effect.setBrightness(amplitude * Math.sin(a));
		a += speed;
	}

	@Override
	public void onDestroy() {
		parent.getSprite().removeEffect("Flashing");
	}
}
