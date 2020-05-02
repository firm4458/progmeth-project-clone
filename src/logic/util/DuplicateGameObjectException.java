package logic.util;

public class DuplicateGameObjectException extends Exception {
	private String duplicatedName;
	public DuplicateGameObjectException(String duplicatedName) {
		super();
		this.duplicatedName = duplicatedName;
	}
	public String getDuplicatedName() {
		return duplicatedName;
	}
}
