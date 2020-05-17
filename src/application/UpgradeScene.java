package application;

import java.util.TreeMap;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.ImageSprite;
import drawing.TextSprite;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.util.Pair;
import logic.TextObject;
import logic.base.BasicScript;
import logic.base.GameObject;
import logic.util.DataManager;
import logic.util.ResourceManager;

/*
 * scene for upgrading things
 */
public class UpgradeScene extends GameScene {

	/*
	 * invoking super class' constructor
	 */
	public UpgradeScene(String name) {
		super(name);
	}

	// int value signifying maximum upgrade
	private static final int MAX_UPGRADE_POINT = -1;
	
	// upgrade price data
	private static TreeMap<String, int[]> upgradePrice = new TreeMap<String, int[]>();
	
	// upgrades name
	private static final String[] upgrades = new String[] { "health", "damage", "healthItem" };
	
	// generate price data from formula
	static {
		int[] arr;
		upgradePrice.put("health", new int[101]);
		arr = upgradePrice.get("health");
		for (int i = 1; i <= 100; ++i) {
			arr[i - 1] = 1000 + i * i * 50;
		}
		arr[100] = MAX_UPGRADE_POINT;
		upgradePrice.put("damage", new int[101]);
		arr = upgradePrice.get("damage");
		for (int i = 1; i <= 100; ++i) {
			arr[i - 1] = 1000 + i * i * 100;
		}
		arr[100] = MAX_UPGRADE_POINT;
		upgradePrice.put("healthItem", new int[101]);
		arr = upgradePrice.get("healthItem");
		for (int i = 1; i <= 100; ++i) {
			arr[i - 1] = 1000 + i * i * 30;
		}
		arr[100] = MAX_UPGRADE_POINT;
	}

	/*
	 * calculate current health
	 */
	public static int calculateHealth() {
		Object data = DataManager.getInstance().getPesistentData("upgrade.health");
		int upgrade = 0;
		if (data != null) {
			upgrade = (int) data;
		}
		return 10 + upgrade * 10;
	}

	/*
	 *  calculate current damage
	 */
	public static int calculateDamage() {
		Object data = DataManager.getInstance().getPesistentData("upgrade.damage");
		int upgrade = 0;
		if (data != null) {
			upgrade = (int) data;
		}
		return 10 + upgrade * 10;
	}

	/*
	 * 
	 */
	public static int calculateHealthItem() {
		Object data = DataManager.getInstance().getPesistentData("upgrade.healthItem");
		int upgrade = 0;
		if (data != null) {
			upgrade = (int) data;
		}
		return 1 + upgrade * 3;
	}

	/*
	 * initialize the scene with three buttons, picture, and information text for each upgrade
	 */
	@Override
	public void init() {
		Group root = (Group) getRoot();
		GameScene scene = this;

		createUpgradeButtons();
		
		ImageButton levelSelect = new ImageButton(600, 100, ResourceManager.getImage("button.red"), null, null);
		levelSelect.setOnAction((evt) -> {
			GameManager.getInstance()
					.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new LevelSelectScene("select")));
		});
		levelSelect.createFollowText("Go Back", 300, 50);
		levelSelect.getGameObject().translate(0, 500);
		root.getChildren().add(levelSelect);
		
		TextObject totalScoreText = new TextObject(300, 400, "", new Font("m5x7", 36), 500);
		totalScoreText.addScript(new BasicScript<TextObject>() {
			@Override
			public void update() {
				Object data = DataManager.getInstance().getPesistentData("totalScore");
				int totalScore = 0;
				if (data != null) {
					totalScore = (int) data;
				}
				parent.setText("Total Score: " + Integer.toString(totalScore));
			}
		});
		addGameObject(totalScoreText);
		
	}

	/*
	 * create all upgrade buttons
	 */
	private void createUpgradeButtons() {
		for (int i = 0; i < upgrades.length; ++i) {
			createUpgradeButton(i);
			createInfoText(i);
			createUpgradeIcon(i);
		}	
	}
	
	/*
	 * create i th upgrade button
	 * when the button is pressed, get the current upgrade level and total score from persistent data
	 * check if upgrade price of that level is lower than total score
	 * show alert when cannot upgrade
	 */
	private void createUpgradeButton(int i) {
		Group root = (Group) getRoot();
		GameScene scene = this;
		ImageButton button = new ImageButton(100, 100, null, null, null);
		String currentUpgrade = upgrades[i];
		button.setOnAction((evt) -> {
			
			int upgradeLevel = getCurrentUpgradeLevel(currentUpgrade);
			int totalScore = getTotalScore();
			int price = upgradePrice.get(currentUpgrade)[upgradeLevel];
			
			if (price == MAX_UPGRADE_POINT) {
				alert("Maximum level reached");
				return;
			}
			if (totalScore < price) {
				alert("Not enough score");
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
		button.createFollowText("upgrade", 50, 50);
		root.getChildren().add(button);
	}
	
	/*
	 * create i th upgrade icon
	 */
	private void createUpgradeIcon(int i) {
		String currentUpgrade = upgrades[i];
		GameObject icon = new GameObject(0, 100 * i);
		icon.setSprite(new ImageSprite(icon, ResourceManager.getImage("upgrade." + currentUpgrade)));
		GameObject banner = new GameObject(100, 100 * i);
		banner.setSprite(new ImageSprite(banner, ResourceManager.getImage("button.red")));
		banner.getSprite().setZ(-1);
		System.out.println(banner.getSprite().getWidth());
		addGameObject(icon);
		addGameObject(banner);
	}
	
	/*
	 * create i th upgrade info text with script to update text
	 */
	private void createInfoText(int i){
		String currentUpgrade = upgrades[i];
		TextObject info = new TextObject(180, i * 100 + 40, "", new Font("m5x7", 30), 500);
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
	}
	
	/*
	 * get total score from persistent data
	 */
	private int getTotalScore() {
		Object data = DataManager.getInstance().getPesistentData("totalScore");
		int totalScore = 0;
		if (data != null) {
			totalScore = (int) data;
		}
		return totalScore;
	}
	
	/*
	 * get current upgrade level from persistent data
	 */
	private int getCurrentUpgradeLevel(String upgrade) {
		Object data = DataManager.getInstance().getPesistentData("upgrade." + upgrade);
		int upgradeLevel = 0;
		if (data != null) {
			upgradeLevel = (int) data;
		}
		return upgradeLevel;
	}

	/*
	 * create alert when upgrade is not successful
	 */
	private void alert(String reason) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notice");
		alert.setContentText("Cannot Upgrade: " + reason);
		alert.setHeaderText(null);
		alert.show();
	}

}
