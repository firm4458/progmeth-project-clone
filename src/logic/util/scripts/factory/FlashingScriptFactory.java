package logic.util.scripts.factory;

import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.scripts.FlashingScript;

public class FlashingScriptFactory extends ScriptFactory {
	private double speed;
	private double amplitude;

	public FlashingScriptFactory(double speed, double amplitude) {
		super();
		this.speed = speed;
		this.amplitude = amplitude;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	@Override
	public Script createScript() {
		return new FlashingScript(speed, amplitude);
	}

}
