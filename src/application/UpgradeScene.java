package application;

import java.util.TreeMap;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.TextSprite;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.util.Pair;
import logic.TextObject;
import logic.base.BasicScript;
import logic.util.DataManager;

public class UpgradeScene extends GameScene {

	public UpgradeScene(String name) {
		super(name);
	}

	private static final int MAX_UPGRADE_POINT = -1;
	private static TreeMap<String, int[]> upgradePrice = new TreeMap<String, int[]>();
	static {
		int[] arr;
		upgradePrice.put("health", new int[101]);
		arr = upgradePrice.get("health");
		for(int i=1;i<=100;++i) {
			arr[i-1]=1000+i*i*100;
		}
		arr[100] = MAX_UPGRADE_POINT;
		upgradePrice.put("damage", new int[101]);
		arr = upgradePrice.get("damage");
		for(int i=1;i<=100;++i) {
			arr[i-1]=1000+i*i*100;
		}
		arr[100] = MAX_UPGRADE_POINT;
		upgradePrice.put("healthItem", new int[101]);
		arr = upgradePrice.get("healthItem");
		for(int i=1;i<=100;++i) {
			arr[i-1]=1000+i*i*100;
		}
		arr[100] = MAX_UPGRADE_POINT;
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
		String[] upgrades = new String[] { "health", "damage", "healthItem" };
		Group root = (Group) getRoot();
		GameScene scene = this;
		for (int i = 0; i < upgrades.length; ++i) {
			ImageButton button = new ImageButton(100, 100, null, null, null);
			String currentUpgrade = upgrades[i];
			button.setOnAction((evt) -> {
				Object data = DataManager.getInstance().getPesistentData("upgrade." + currentUpgrade);
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

				if (price == MAX_UPGRADE_POINT) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Notice");
					alert.setContentText("Cannot Upgrade: Maximum level reached");
					alert.setHeaderText(null);
					alert.show();
					return;
				}
				if (totalScore < price) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Notice");
					alert.setContentText("Cannot Upgrade: Not enough score");
					alert.setHeaderText(null);
					alert.show();
					return;
				}

				totalScore -= price;
				upgradeLevel++;

				GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
						new Pair<String, Object>("totalScore", totalScore)));
				GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
						new Pair<String, Object>("upgrade." + currentUpgrade, upgradeLevel)));
			});
			button.getGameObject().translate(500, i * 100);
			button.createFollowText(upgrades[i], 300, 50);
			TextObject info = new TextObject(0, i * 100 + 20, "", new Font("ARCADECLASSIC", 18), 500);
			TextSprite sprite = (TextSprite) info.getSprite();
			sprite.setCenterAligned(false);
			info.addScript(new BasicScript<TextObject>() {
				@Override
				public void update() {
					Object data = DataManager.getInstance().getPesistentData("upgrade." + currentUpgrade);
					int upgradeLevel = 0;
					if (data != null) {
						upgradeLevel = (int) data;
					}
					int price = upgradePrice.get(currentUpgrade)[upgradeLevel];
					String priceString = price == MAX_UPGRADE_POINT ? "\nMaximum level reached"
							: "\nupgrade price : " + Integer.toString(price);
					int value = 0;
					String name = "";
					if (currentUpgrade.equals("health")) {
						name = "Health";
						value = calculateHealth();
					} else if (currentUpgrade.equals("damage")) {
						name = "Damage";
						value = calculateDamage();
					} else if (currentUpgrade.equals("healthItem")) {
						name = "Heal";
						value = calculateHealthItem();
					}
					((TextSprite) parent.getSprite())
							.setText("Current " + name + " : " + Integer.toString(value) + priceString);
				}
			});
			addGameObject(info);
			root.getChildren().add(button);
		}
		ImageButton levelSelect = new ImageButton(600, 100, null, null, null);
		levelSelect.setOnAction((evt) -> {
			GameManager.getInstance()
					.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new LevelSelectScene("select")));
		});
		TextObject totalScoreText = new TextObject(100, 300, "", new Font("ARCADECLASSIC",18), 500);
		totalScoreText.addScript(new BasicScript<TextObject>() {
			@Override
			public void update() {
				Object data = DataManager.getInstance().getPesistentData("totalScore");
				int totalScore = 0;
				if (data != null) {
					totalScore = (int) data;
				}
				parent.setText("Total Score: "+Integer.toString(totalScore));
			}
		});
		addGameObject(totalScoreText);
		levelSelect.getGameObject().translate(0, 500);
		root.getChildren().add(levelSelect);
	}

}
