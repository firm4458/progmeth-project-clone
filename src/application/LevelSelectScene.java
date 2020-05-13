package application;

import drawing.SimpleCamera;

import java.util.ArrayList;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
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
	
	private GameScene space1;
	private GameScene space1Boss;

	public LevelSelectScene(String name) {
		super(name);
		Image background = ResourceManager.getImage("background1");
		ArrayList<Image> planetImgs = new ArrayList<Image>();
		planetImgs.add(ResourceManager.getImage("planet.dark"));
		planetImgs.add(ResourceManager.getImage("planet.ring"));
		space1 = new NormalLevelScene("space1",background, planetImgs, ResourceManager.getSound("sound/normal.mp3"));
		space1Boss = new BossScene("boss", background, planetImgs, ResourceManager.getSound("sound/boss.mp3"));
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
					new GameEvent(scene, GameEventType.SCENE_CHANGE, space1));
		});
		ImageButton space1BossButton = new ImageButton(600, 100, ResourceManager.getImage("levelBanner.space1.boss"),
				ResourceManager.getImage("levelBanner.space1.boss.pressed"),
				ResourceManager.getImage("levelBanner.space1.boss.pressed")); 
		space1BossButton.getGameObject().translate(0, 100);
		space1BossButton.setOnAction((evt) -> {
			GameManager.getInstance().signalEvent(
					new GameEvent(scene, GameEventType.SCENE_CHANGE, space1Boss));
		});
		root.getChildren().addAll(space1Button,space1BossButton);
	}

}
