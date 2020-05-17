package logic.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

public class DataManager {
	private DataManager() {
	};

	private TreeMap<String, Object> persistentData;
	private static final DataManager instance;
	static {
		instance = new DataManager();
		instance.persistentData = new TreeMap<String, Object>();
	}

	public static DataManager getInstance() {
		return instance;
	}

	public void writePersistentData(String key, Object value) {
		persistentData.put(key, value);
	}

	public Object getPesistentData(String key) {
		return persistentData.get(key);
	}

	public boolean contains(String key) {
		return persistentData.containsKey(key);
	}

	/**
	 * save persistent data in to .save file
	 * @return if success return true, otherwise return false
	 */
	public boolean saveData() {
		FileOutputStream f = null;
		ObjectOutputStream o = null;
		try {
			f = new FileOutputStream(ResourceManager.saveFile("Create a save file"));
			o = new ObjectOutputStream(f);
			o.writeObject(persistentData);
		} catch (Exception e) {
			System.out.println("Prolem saving file");
			return false;
		} finally {
			try {
				o.close();
				f.close();
			} catch (Exception e) {
				System.out.println("Prolem saving file");
				return false;
			}
		}
		return true;
	}

	/**
	 * load persistent data form .save file
	 * @return if success return true, otherwise return false
	 */
	@SuppressWarnings("unchecked")
	public boolean loadData() {
		FileInputStream fi = null;
		ObjectInputStream oi = null;
		try {
			fi = new FileInputStream(ResourceManager.pickFile("Choose a save file"));
			oi = new ObjectInputStream(fi);
			persistentData = (TreeMap<String, Object>) oi.readObject();
		} catch (Exception e) {
			System.err.println("invalid save file");
			e.printStackTrace();
			return false;
		} finally {
			try {
				oi.close();
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return true;
	}
}
