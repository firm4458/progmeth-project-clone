package logic.base;

import application.GameManager.GameEvent;

public class InvalidEventDataException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public GameEvent evt;

	public InvalidEventDataException(GameEvent evt) {
		super();
		this.evt = evt;
	}

}
