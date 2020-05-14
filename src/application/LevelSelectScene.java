package application;

import java.util.function.Consumer;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.level.Space1;
import application.level.Space1Boss;
import application.level.Space2;
import application.level.Space2Boss;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import logic.util.ResourceManager;

public class LevelSelectScene extends GameScene {

	public LevelSelectScene(String name) {
		super(name);
	}

	@Override
	public void init() {
		Group root = (Group) getRoot();
		GameScene scene = this;
		ImageButton space1Button = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space1"),
				ResourceManager.getImage("levelBanner.space1.pressed"),
				ResourceManager.getImage("levelBanner.space1.pressed"));
		space1Button.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space1()));
		});
		ImageButton space1BossButton = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space1.boss"),
				ResourceManager.getImage("levelBanner.space1.boss.pressed"),
				ResourceManager.getImage("levelBanner.space1.boss.pressed"));
		space1BossButton.getGameObject().translate(0, 100);
		space1BossButton.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space1Boss()));
		});
		ImageButton space2Button = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space2"),
				ResourceManager.getImage("levelBanner.space2.pressed"),
				ResourceManager.getImage("levelBanner.space2.pressed"));
		space2Button.getGameObject().translate(0, 200);
		space2Button.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space2()));
		});
		ImageButton space2BossButton = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space2.boss"),
				ResourceManager.getImage("levelBanner.space2.boss.pressed"),
				ResourceManager.getImage("levelBanner.space2.boss.pressed"));
		space2BossButton.getGameObject().translate(0, 300);
		space2BossButton.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space2Boss()));
		});

		ImageButton upgradeButton = new ImageButton(100, 50, null, null, null);
		upgradeButton.getGameObject().translate(200, 525);
		upgradeButton.setOnAction((evt) -> {
			GameManager.getInstance()
					.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new UpgradeScene("upgrade")));
		});

		ImageButton saveButton = new ImageButton(100, 50, ResourceManager.getImage("button.blueButton"), null,
				ResourceManager.getImage("button.blueButton.pressed"));
		saveButton.setOnAction((evt) -> {
			GameEvent event = new GameEvent(scene, GameEventType.SAVE_PERSISTENT_DATA, new Consumer<Boolean>() {
				@Override
				public void accept(Boolean success) {
					System.out.println(success);
					if (!success) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Save failed");
						alert.setHeaderText(null);
						alert.setContentText("Cannot save");
						alert.show();
						System.out.println("GGG");
					}
				}
			});
			GameManager.getInstance().signalEvent(event);
		});
		saveButton.getGameObject().translate(350, 525);
		saveButton.createFollowText("Save", 50, 25);
		root.getChildren().addAll(space1Button, space1BossButton, space2Button, space2BossButton, upgradeButton,
				saveButton);
	}

}
