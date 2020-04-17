package logic.player;

import javafx.scene.input.KeyCode;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.InputUtil;

public class PlayerController implements Script {
	
	private GameObject parent;
	private final double speed=3;
	
	@Override
	public void update() {
		double x = InputUtil.isKeyPressed(KeyCode.LEFT)?-1:0;
		x += InputUtil.isKeyPressed(KeyCode.RIGHT)?1:0;
		x *= speed;
		double y = InputUtil.isKeyPressed(KeyCode.UP)?-1:0;
		y += InputUtil.isKeyPressed(KeyCode.DOWN)?1:0;
		y *= speed;
		parent.translate(x, y);
		if(x==0) {
			((Player)parent).animator.sendTrigger("idle");
		}
		else if(x>0) {
			((Player)parent).animator.sendTrigger("goRight");
		}
		else {
			((Player)parent).animator.sendTrigger("goLeft");
		}
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	@Override
	public void onDestroy() {
		
	}

}
