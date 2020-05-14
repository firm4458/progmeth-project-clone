package logic.util;

import java.util.TreeMap;

import javafx.scene.input.KeyCode;

public class InputUtil {
	private static final TreeMap<KeyCode, Boolean> keyMap = new TreeMap<KeyCode, Boolean>();
	public static final TreeMap<String, Boolean> buttonMap = new TreeMap<String, Boolean>();
	static {
		for (KeyCode code : KeyCode.values()) {
			keyMap.put(code, false);
		}
	}

	public static boolean isKeyPressed(KeyCode code) {
		return keyMap.get(code);
	}

	public static void setKeyPressed(KeyCode code, boolean value) {
		keyMap.put(code, value);
	}
}
