package logic.player;

import javafx.scene.input.KeyCode;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.IncompatibleScriptException;
import logic.util.InputUtil;

public abstract class PlayerSkill implements Script {
	
	protected Player parent;
	protected long cooldown;
	protected long cooldownStart=-9999; 
	protected boolean usingSkill;
	protected KeyCode activateKey;
	
	public PlayerSkill(KeyCode activateKey,long cooldown) {
		this.activateKey = activateKey;
		this.cooldown = cooldown;
	}

	@Override
	public void update() throws GameInterruptException {
		
	}
	
	@Override
	public void earlyUpdate() {
		long timePassed = System.currentTimeMillis() - cooldownStart;
		if(!usingSkill && InputUtil.isKeyPressed(activateKey) && timePassed>cooldown) {
			usingSkill = true;
			startSkill();
		}
		
		if(usingSkill) {
			skillUpdate();
		}
	}
	
	protected void skillDone() {
		cooldownStart = System.currentTimeMillis();
		usingSkill = false;
	}
	
	protected abstract void startSkill();
	protected abstract void skillUpdate();

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (Player)parent;
		}catch (ClassCastException e) {
			throw new IncompatibleScriptException("PlayerSkill", "must be attached to player");
		}
	}

}
