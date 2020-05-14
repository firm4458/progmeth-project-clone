package application;

import java.util.TreeMap;

import com.sun.javafx.css.CalculatedValue;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.level.Space2Boss;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.util.Pair;
import logic.util.DataManager;

public class UpgradeScene extends GameScene {

	public UpgradeScene(String name) {
		super(name);
	}

	private static final int MAX_UPGRADE_POINT = -1;
	private static TreeMap<String, int[]> upgradePrice = new TreeMap<String, int[]>();
	static {
		upgradePrice.put("health", new int[] { 10, 20, 40, 60, 80, 160, 320, 640, MAX_UPGRADE_POINT });
		upgradePrice.put("damage", new int[] { 10, 20, 40, 60, 80, 160, 320, 640, MAX_UPGRADE_POINT });
		upgradePrice.put("healthItem", new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, MAX_UPGRADE_POINT });
	}

	public static int calculateHealth() {
		Object data = DataManager.getInstance().getPesistentData("upgrade.health");
		int upgrade = 0;
		if (data != null) {
			upgrade = (int) data;
		}
		return 10 + upgrade * 10;
	}

	public static int calculateDamage() {
		Object data = DataManager.getInstance().getPesistentData("upgrade.damage");
		int upgrade = 0;
		if (data != null) {
			upgrade = (int) data;
		}
		return 10 + upgrade * 10;
	}

	public static int calculateHealthItem() {
		Object data = DataManager.getInstance().getPesistentData("upgrade.healthItem");
		int upgrade = 0;
		if (data != null) {
			upgrade = (int) data;
		}
		return 1 + upgrade * 3;
	}

	@Override
	public void init() {
		String[] upgrades = new String[] {"health","damage","healthItem"};
		Group root = (Group) getRoot();
		GameScene scene = this;
		for(int i=0;i<upgrades.length;++i) {
			ImageButton button = new ImageButton(600,100,null,null,null);
			String currentUpgrade = upgrades[i];
			button.setOnAction((evt) -> {
				Object data = DataManager.getInstance().getPesistentData("upgrade."+currentUpgrade);
				int upgradeLevel = 0;
				if (data != null) {
					upgradeLevel = (int) data;
				}
				data = DataManager.getInstance().getPesistentData("totalScore");
				int totalScore = 0;
				if (data != null) {
					totalScore = (int) data;
				}
				
				int price = upgradePrice.get(currentUpgrade)[upgradeLevel];

				if (price == MAX_UPGRADE_POINT || totalScore < price) {
					return;
				}

				totalScore -= price;
				upgradeLevel++;

				GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
						new Pair<String, Object>("totalScore", totalScore)));
				GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
						new Pair<String, Object>("upgrade."+currentUpgrade, upgradeLevel)));
			});
			button.getGameObject().translate(0, i*100);
			button.createFollowText(upgrades[i], 300, 50);
			root.getChildren().add(button);
		}

		ImageButton levelSelect = new ImageButton(600, 100, null, null, null);
		levelSelect.setOnAction((evt) -> {
			GameManager.getInstance()
					.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new LevelSelectScene("select")));
		});
		levelSelect.getGameObject().translate(0, 500);
		root.getChildren().add(levelSelect);
	}

}
