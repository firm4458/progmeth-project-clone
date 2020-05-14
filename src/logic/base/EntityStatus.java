package logic.base;

public class EntityStatus implements Script {

	protected int health;
	protected int maxHealth;
	protected Entity parent;
	protected int damage;
	protected boolean isInvincible;

	public boolean isInvincible() {
		return isInvincible;
	}

	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}

	public EntityStatus(int health) {
		super();
		this.health = health;
		maxHealth = health;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	@Override
	public void lateUpdate() throws GameInterruptException {
		if (health <= 0) {
			parent.onDeath();
		}
	}

	@Override
	public void earlyUpdate() throws GameInterruptException {
		if (health <= 0) {
			parent.onDeath();
		}
	}

	public void takeDamage(int damage) {
		if (!isInvincible) {
			health -= damage;
			health = Math.max(health, 0); // health must not be negative
		}
	}

	public void heal(int heal) {
		health += heal;
		health = Math.min(health, maxHealth);
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
