package logic.player;

import application.GameManager;
import javafx.scene.input.KeyCode;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.util.InputUtil;

public class PlayerController implements Script {

	private Player parent;
	private double speed = 3;

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public void update() {
		double x = InputUtil.isKeyPressed(KeyCode.LEFT) ? -1 : 0;
		x += InputUtil.isKeyPressed(KeyCode.RIGHT) ? 1 : 0;
		x *= speed;
		double y = InputUtil.isKeyPressed(KeyCode.UP) ? -1 : 0;
		y += InputUtil.isKeyPressed(KeyCode.DOWN) ? 1 : 0;
		y *= speed;
		if (y == 0 && speed != 3) {
			y = -speed;
		}
		parent.translate(x, y);
		parent.setX(Math.max(0, parent.getX()));
		parent.setY(Math.max(0, parent.getY()));
		parent.setX(Math.min(GameManager.NATIVE_WIDTH - parent.getSprite().getWidth(), parent.getX()));
		parent.setY(Math.min(GameManager.NATIVE_HEIGHT - parent.getSprite().getHeight(), parent.getY()));
		if (x == 0) {
			parent.animator.sendTrigger("idle");
		} else if (x > 0) {
			parent.animator.sendTrigger("goRight");
		} else {
			parent.animator.sendTrigger("goLeft");
		}
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (Player) parent;
		} catch (ClassCastException e) {
			throw new IncompatibleScriptException(this.getClass().toString(), "Must be attached to Player");
		}
	}

	@Override
	public void onDestroy() {

	}

}
