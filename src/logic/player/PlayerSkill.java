package logic.player;

import javafx.scene.input.KeyCode;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.util.InputUtil;

public abstract class PlayerSkill implements Script {

	protected Player parent;
	protected long timeCount; //use to count the time after skill is done
	protected long timeDuration;//use to count the time when start using the skill
	protected long cooldown;
	protected long cooldownStart = -9999;
	protected boolean usingSkill;
	protected KeyCode activateKey;
	protected boolean isActive = false;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public PlayerSkill(KeyCode activateKey, long cooldown) {
		this.activateKey = activateKey;
		this.cooldown = cooldown;
	}

	@Override
	public void update() throws GameInterruptException {

	}

	@Override
	public void earlyUpdate() {
		if (!isActive) {
			return;
		}
		long timePassed = System.currentTimeMillis() - cooldownStart;
		timeCount = timePassed;
		if (!usingSkill && InputUtil.isKeyPressed(activateKey) && timePassed > cooldown) {
			usingSkill = true;
			startSkill();
		}

		if (usingSkill) {
			skillUpdate();
		}
	}

	protected void skillDone() {
		cooldownStart = System.currentTimeMillis();
		usingSkill = false;
	}

	protected abstract void startSkill();

	protected abstract void skillUpdate();

	// public abstract void get

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (Player) parent;
		} catch (ClassCastException e) {
			throw new IncompatibleScriptException("PlayerSkill", "must be attached to player");
		}
	}

	public long getCooldown() {
		if (timeCount > cooldown) {
			return 0;
		}
		return (cooldown - timeCount) / 60;
	}

	public boolean getUsingSkill() {
		return usingSkill;
	}

	public long getTimeDuration() {
		if (timeDuration < 0) {
			return 0;
		}
		return timeDuration / 60;
	}

}
