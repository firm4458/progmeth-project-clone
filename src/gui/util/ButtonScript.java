package gui.util;

import gui.GameButton;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.util.InputUtil;

public abstract class ButtonScript implements Script {

	private boolean isPressed = false;
	private GameButton parent;
	
	public abstract void onPressed() throws GameInterruptException;
	public abstract void whilePressed() throws GameInterruptException;
	public abstract void onRelease() throws GameInterruptException;
	
	@Override
	public final void update() throws GameInterruptException {
		boolean pressState = InputUtil.buttonMap.get(parent.getName());
		if(pressState) {
			if(!isPressed) {
				onPressed();
				isPressed=true;
			}
			whilePressed();
		}else if(!pressState && isPressed){
			onRelease();
			isPressed=false;
		}
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (GameButton)parent;
		}catch(ClassCastException e) {
			throw new IncompatibleScriptException("button", "GG");
		}

	}

	@Override
	public void onDestroy() {

	}

}
