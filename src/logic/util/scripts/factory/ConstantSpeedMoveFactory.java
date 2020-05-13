package logic.util.scripts.factory;

import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.scripts.ConstantSpeedMove;

public class ConstantSpeedMoveFactory extends ScriptFactory {

	private double speedX;
	private double speedY;

	public double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

	public ConstantSpeedMoveFactory(double speedX, double speedY) {
		super();
		this.speedX = speedX;
		this.speedY = speedY;
	}

	@Override
	public Script createScript() {
		return new ConstantSpeedMove(speedX, speedY);
	}

}
