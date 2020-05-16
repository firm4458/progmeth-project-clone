package logic.util.scripts;

import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;

public class ShakerScript extends BasicScript<GameObject> {

	private double magnitude;
	private double speed;
	private double totalOffset;

	public ShakerScript(double magnitude, double speed) {
		this.magnitude = magnitude;
		this.speed = speed;
	}

	@Override
	public void update() throws GameInterruptException {
		if (totalOffset < magnitude) {
			parent.translate(speed, speed);
			totalOffset += speed;
		} else {
			parent.translate(-totalOffset, -totalOffset);
			parent.removeScript(this);
		}
	}

}
