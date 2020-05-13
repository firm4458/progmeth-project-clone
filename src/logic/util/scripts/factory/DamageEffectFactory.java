package logic.util.scripts.factory;

import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.scripts.DamageEffect;

public class DamageEffectFactory extends ScriptFactory {

	private long duration;
	private double brightness;

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public double getBrightness() {
		return brightness;
	}

	public void setBrightness(double brightness) {
		this.brightness = brightness;
	}

	@Override
	public Script createScript() {
		return new DamageEffect(duration, brightness);
	}

}
