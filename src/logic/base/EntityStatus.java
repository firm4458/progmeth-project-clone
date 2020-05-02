package logic.base;

import logic.util.IncompatibleScriptException;

public class EntityStatus implements Script {

	private int health;
	private Entity parent;
	
	public EntityStatus(int health) {
		super();
		this.health = health;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public void lateUpdate() throws GameInterruptException {
		if(health<=0) {
			parent.onDeath();
		}
	}
	
	public void takeDamage(int damage) {
		health -= damage;
	}
	
	@Override
	public void update() throws GameInterruptException {
		
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (Entity) parent;
		} catch (ClassCastException e) {
			throw new IncompatibleScriptException("EntityStatus", "only for entity");
		}
	}

}
