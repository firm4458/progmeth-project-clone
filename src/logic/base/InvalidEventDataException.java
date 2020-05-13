package logic.base;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;

public class InvalidEventDataException extends Exception {
	public GameEvent evt;

	public InvalidEventDataException(GameEvent evt) {
		super();
		this.evt =evt;
	}
	
}
