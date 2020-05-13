package application;

import java.util.ArrayList;
import java.util.TreeMap;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.SimpleCamera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.base.Renderable;
import gui.GameButton;
import gui.ImageButton;
import gui.util.ButtonScript;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.LoopBackground;
import logic.TextObject;
import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.base.Updatable;
import logic.util.DataManager;
import logic.util.InputUtil;
import logic.util.ResourceManager;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;
import logic.util.scripts.AutoRemove;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;

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
					new TutorialScene("base",ResourceManager.getImage("background2"),planetImgs)));
		});
		playButton.getGameObject().setX(250);
		playButton.getGameObject().setY(280);
		playButton.createFollowText("New Game", 100, 25);
		root.getChildren().addAll(playButton);
		
		DataManager dataManager = DataManager.getInstance();
		int score = 0;
		if(dataManager.contains("totalScore")) {
			score = (int) dataManager.getPesistentData("totalScore");
		}
		TextObject totalScoreText = new TextObject(50, 50, Integer.toString(score), new Font(20), 500);
		addGameObject(totalScoreText);

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
		
		ScriptFactory factory = new ScriptFactory() {
			@Override
			public Script createScript() {
				return new BasicScript<GameObject>() {
					private double a;
					@Override
					public void update() {
						parent.getSprite().getColorAdjust().setBrightness(0.1 * Math.sin(a));
						a += 0.02;
					}

				};
			}
		};
		ArrayList<ScriptFactory> arr = new ArrayList<ScriptFactory>();
		arr.add(factory);
		LoopBackground.createLoopBackground(this, ResourceManager.getImage("background1"), 0.07, 3, arr);
		//////
		Image fullimg = new Image("img/ship.png", 400, 240, true, true);
		Image[] playerimg = { new WritableImage(fullimg.getPixelReader(), 2 * 80, 0, 80, 120),
				new WritableImage(fullimg.getPixelReader(), 2 * 80, 0, 80, 120),
				new WritableImage(fullimg.getPixelReader(), 2 * 80, 120, 80, 120),
				new WritableImage(fullimg.getPixelReader(), 2 * 80, 120, 80, 120) };
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
