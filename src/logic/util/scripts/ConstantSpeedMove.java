package logic.util.scripts;

import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;

public class ConstantSpeedMove implements Script {

	private GameObject parent;
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

	}

}
