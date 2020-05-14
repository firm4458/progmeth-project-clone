package logic.base;

public class IncompatibleScriptException extends Exception {
	public IncompatibleScriptException(String scriptName, String message) {
		super();
		System.err.println("Incompatible Script!: " + scriptName + " -> " + message);
	}
}
