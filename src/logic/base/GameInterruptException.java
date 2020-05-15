package logic.base;

public class GameInterruptException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Object data;

	public GameInterruptException(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

}
