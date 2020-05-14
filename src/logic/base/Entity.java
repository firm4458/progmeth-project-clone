package logic.base;

public class Entity extends GameObject {
	private EntityStatus status;

	public Entity(double X, double Y, int health) {
		super(X, Y);
		status = new EntityStatus(health);
		addScript(status);
	}

	public Entity(double X, double Y, EntityStatus customStatus) {
		super(X, Y);
		status = customStatus;
		addScript(status);
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(EntityStatus status) {
		this.status = status;
	}

	public void onDeath() throws GameInterruptException {
		destroy();
	}

}
