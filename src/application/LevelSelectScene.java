package application;

import drawing.SimpleCamera;

import java.util.ArrayList;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.level.Space1;
import application.level.Space1Boss;
import application.level.Space2;
import application.level.Space2Boss;
import drawing.Renderer;
import gui.GameButton;
import gui.ImageButton;
import gui.util.ButtonScript;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import logic.base.GameInterruptException;
import logic.base.SceneChangeInterruptException;
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
			GameManager.getInstance().signalEvent(
					new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space1()));
		});
		ImageButton space1BossButton = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space1.boss"),
				ResourceManager.getImage("levelBanner.space1.boss.pressed"),
				ResourceManager.getImage("levelBanner.space1.boss.pressed")); 
		space1BossButton.getGameObject().translate(0, 100);
		space1BossButton.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(
					new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space1Boss()));
		});
		ImageButton space2Button = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space2"),
				ResourceManager.getImage("levelBanner.space2.pressed"),
				ResourceManager.getImage("levelBanner.space2.pressed")); 
		space2Button.getGameObject().translate(0, 200);
		space2Button.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(
					new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space2()));
		});
		ImageButton space2BossButton = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space2.boss"),
				ResourceManager.getImage("levelBanner.space2.boss.pressed"),
				ResourceManager.getImage("levelBanner.space2.boss.pressed")); 
		space2BossButton.getGameObject().translate(0, 300);
		space2BossButton.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(
					new GameEvent(scene, GameEventType.SCENE_CHANGE, new Space2Boss()));
		});
		
		ImageButton upgradeButton = new ImageButton(100,50,null,null,null);
		upgradeButton.getGameObject().translate(200,525);
		upgradeButton.setOnAction((evt)->{
			GameManager.getInstance().signalEvent(
					new GameEvent(scene, GameEventType.SCENE_CHANGE, new UpgradeScene("upgrade")));
		});
		root.getChildren().addAll(space1Button,space1BossButton,space2Button,space2BossButton,upgradeButton);
	}

}
