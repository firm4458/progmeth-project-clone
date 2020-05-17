package logic.util;

import java.util.TreeMap;

import javafx.scene.input.KeyCode;

public class InputUtil {
	/**
	 * use to check what key is pressed
	 */
	private static final TreeMap<KeyCode, Boolean> keyMap = new TreeMap<KeyCode, Boolean>();
	static {
		reset();
	}

	public static boolean isKeyPressed(KeyCode code) {
		return keyMap.get(code);
	}

	public static void setKeyPressed(KeyCode code, boolean value) {
		keyMap.put(code, value);
	}

	public static void reset() {
		for (KeyCode code : KeyCode.values()) {
			keyMap.put(code, false);
		}
	}
}
