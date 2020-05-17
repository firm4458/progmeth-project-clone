package logic.enemy;

import java.util.ArrayList;

import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.enemy.AttackScriptFactory.AttackScript;

public class AttackController implements Script {

	private AttackScript currentScript;
	private Enemy parent;
	private AttackPickStrategy pickStrategy;
	private final ArrayList<AttackScriptFactory> scripts = new ArrayList<AttackScriptFactory>();
	private int pickCount;
	private AttackScriptFactory prevPick;

	public AttackController(AttackPickStrategy pickStrategy) {
		super();
		this.pickStrategy = pickStrategy;
	}

	public int getPickCount() {
		return pickCount;
	}

	public ArrayList<AttackScriptFactory> getScripts() {
		return scripts;
	}

	@Override
	public void update() throws GameInterruptException {
	}

	public AttackScriptFactory getPrevPick() {
		return prevPick;
	}

	public void setPrevPick(AttackScriptFactory prevPick) {
		this.prevPick = prevPick;
	}

	/**
	 * use pick strategy to pick next script
	 */
	@Override
	public void earlyUpdate() throws GameInterruptException {
		if (currentScript == null || currentScript.isDone()) {
			if (currentScript != null) {
				parent.removeScript(currentScript);
			}
			AttackScriptFactory fact = pickStrategy.pick(scripts, this);
			pickCount++;
			prevPick = fact;
			System.out.println(fact);
			currentScript = fact.createScript();
			parent.addScript(currentScript);
		}
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (Enemy) parent;
		} catch (ClassCastException e) {
			throw new IncompatibleScriptException("AttackScript", "must be attached to Enemy");
		}
	}

}
