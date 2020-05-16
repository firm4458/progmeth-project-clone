package logic.util.scripts;

import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;

public class ConstantSpeedMove extends BasicScript<GameObject> {

	private double speedX;
	private double speedY;

	public ConstantSpeedMove(double speedX, double speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

	@Override
	public void update() {
		parent.translate(speedX, speedY);
	}

}
