package application;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.function.Consumer;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.ImageSprite;
import drawing.Sprite;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import logic.LoopBackground;
import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.ResourceManager;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;
import logic.util.scripts.ConstantSpeedMove;
import logic.util.scripts.factory.FlashingScriptFactory;

public class MenuScene extends GameScene {
	private static AnimationState PlayerIcon;

	public MenuScene(String name) {
		super(name);
	}

	@Override
	public void init() {
		Group root = (Group) getRoot();
		GameScene scene = this;

		ImageButton playButton = new ImageButton(200, 50, ResourceManager.getImage("button.blueButton"), null,
				ResourceManager.getImage("button.blueButton.pressed"));
		playButton.setOnMouseClicked((evt) -> {
			GameManager gameManager = GameManager.getInstance();
			ArrayList<Image> planetImgs = new ArrayList<Image>();
			planetImgs.add(ResourceManager.getImage("lavaPlanet"));
			planetImgs.add(ResourceManager.getImage("icePlanet"));
			planetImgs.add(ResourceManager.getImage("forestPlanet"));
			planetImgs.add(ResourceManager.getImage("oceanPlanet"));
			planetImgs.add(ResourceManager.getImage("planet1"));
			planetImgs.add(ResourceManager.getImage("planet2"));
			gameManager.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE,
					new TutorialScene("base", ResourceManager.getImage("background1"), planetImgs)));
		});
		playButton.getGameObject().setX(250);
		playButton.getGameObject().setY(280);
		playButton.createFollowText("New Game", 100, 25);

		ImageButton loadButton = new ImageButton(200, 50, ResourceManager.getImage("button.blueButton"), null,
				ResourceManager.getImage("button.blueButton.pressed"));
		loadButton.setOnAction((evt) -> {
			GameEvent event = new GameEvent(scene, GameEventType.LOAD_PERSISTENT_DATA, new Consumer<Boolean>() {
				@Override
				public void accept(Boolean success) {
					if (success) {
						GameManager.getInstance().signalEvent(
								new GameEvent(scene, GameEventType.SCENE_CHANGE, new LevelSelectScene("select")));
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Load failed");
						alert.setHeaderText(null);
						alert.setContentText("Cannot load save file");
						alert.show();
					}
				}
			});
			GameManager.getInstance().signalEvent(event);
		});
		loadButton.getGameObject().translate(250, 350);
		loadButton.createFollowText("Load", 100, 25);
		root.getChildren().addAll(playButton, loadButton);

		GameObject logo = new GameObject(160, 100);
		logo.setSprite(new ImageSprite(logo, ResourceManager.getImage("logo")));
		logo.getSprite().setZ(99);
		logo.addScript(new Script() {

			private GameObject parent;
			private double originY = 100;
			private double t = 0;
			private double f = 0.5;
			private double A = 10.0;

			@Override
			public void update() {
				double offset = A * Math.sin(2.0 * Math.PI * f * t); // simple harmonic oscillation
				parent.setY(originY + offset);
				t += 1 / 60.0;
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}

		});

		addGameObject(logo);

		ScriptFactory factory = new FlashingScriptFactory(0.02, 0.07);
		ArrayList<ScriptFactory> arr = new ArrayList<ScriptFactory>();
		arr.add(factory);
		LoopBackground.createLoopBackground(this, ResourceManager.getImage("background1"), 0.07, 3, arr);

		Image[] playerimg = { ResourceManager.getImage("idle1"), ResourceManager.getImage("idle1"),
				ResourceManager.getImage("idle2"), ResourceManager.getImage("idle2") };
		PlayerIcon = new AnimationState("idle", playerimg, new TreeMap<String, AnimationState>());
		GameObject playerIcon = new GameObject(50, 600);
		Sprite pyiconSprite = new ImageSprite(playerIcon, playerimg[0]);
		playerIcon.setSprite(pyiconSprite);
		playerIcon.addScript(new BasicScript<GameObject>() {
			@Override
			public void update() {
				if (parent.getY() < -200) {
					parent.setY(600);
				}
			}
		});
		playerIcon.addScript(new ConstantSpeedMove(0, -3));
		playerIcon.addScript(new Animator((ImageSprite) pyiconSprite, PlayerIcon));
		addGameObject(playerIcon);
	}
}
