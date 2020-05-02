package application;

import java.util.TreeMap;

import drawing.Camera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.base.Renderable;
import gui.GameButton;
import gui.util.ButtonScript;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
import logic.util.ConstantSpeedMove;
import logic.util.IncompatibleScriptException;
import logic.util.InputUtil;
import logic.util.ResourceManager;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;

public class MenuScene extends GameScene {
	private static AnimationState PlayerIcon;
	
	public MenuScene() {
		super();
	}
	
	@Override
	public void init() {
		this.isDestroyed = false;
		
		addGameObject(new Camera(Renderer.getInstance().getGc().getCanvas()));
		
		GameButton button = new GameButton((GameManager.NATIVE_WIDTH-100)/2, GameManager.NATIVE_HEIGHT*0.5, "Play", 100, 50,
				ResourceManager.getImage("img/Button.png", 100, 50),
				ResourceManager.getImage("img/clickedButton.png", 100, 48.88));
		button.addScript(new ButtonScript() {

			@Override
			public void onPressed() throws GameInterruptException {
				// executed on the first frame button is pressed
			}

			@Override
			public void whilePressed() throws GameInterruptException {
				// executed while button is still pressed
			}

			@Override
			public void onRelease() throws GameInterruptException {
				throw new SceneChangeInterruptException(new NormalLevelScene());
			}
			
		});
		addGameObject(button);
		
		//Background
		GameObject background = new GameObject(0, -420);
		Image img = new Image("img/parallax-space-backgound.png", GameManager.NATIVE_WIDTH, 0, true, true);
		Sprite bgSprite = new ImageSprite(background, img);
		bgSprite.setZ(-99);
		background.setSprite(bgSprite);
		background.addScript(new ConstantSpeedMove(0, 0.07));

		background.addScript(new Script() {

			private GameObject parent;
			private double a;

			@Override
			public void update() {
				parent.getSprite().getColorAdjust().setBrightness(0.1 * Math.sin(a));
				a += 0.02;
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}

			@Override
			public void onDestroy() {

			}

		});
		addGameObject(background);
		//////
		Image fullimg = new Image("img/ship.png",400,240,true,true);
		Image[] playerimg = {new WritableImage(fullimg.getPixelReader(),2*80,0,80,120), new WritableImage(fullimg.getPixelReader(),2*80,0,80,120),
				new WritableImage(fullimg.getPixelReader(),2*80,120,80,120), new WritableImage(fullimg.getPixelReader(),2*80,120,80,120)};
		PlayerIcon = new AnimationState("idle", playerimg, new TreeMap<String,AnimationState>());
		GameObject playerIcon = new GameObject((GameManager.NATIVE_WIDTH-80)/2, GameManager.NATIVE_HEIGHT/6);
		Sprite pyiconSprite = new ImageSprite(playerIcon, playerimg[0]);
		playerIcon.setSprite(pyiconSprite);
		playerIcon.addScript(new Animator((ImageSprite)pyiconSprite,PlayerIcon));
		addGameObject(playerIcon);
	}
}
