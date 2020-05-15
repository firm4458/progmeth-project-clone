package logic.base;

public class IncompatibleScriptException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public IncompatibleScriptException(String scriptName, String message) {
		super();
		System.err.println("Incompatible Script!: " + scriptName + " -> " + message);
	}
}
