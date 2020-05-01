package application;

import java.util.TreeMap;

import drawing.Camera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.base.Renderable;
import gui.GameButton;
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
		GameButton button = new GameButton(250, 300, "Click to start");
		button.addScript(new Script() {
			
			GameButton parent;

			@Override
			public void update() throws GameInterruptException {
				if(InputUtil.buttonMap.get(parent.getName())) {
					throw new SceneChangeInterruptException(GUI.sampleScene);
				}
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				try {
					this.parent = (GameButton)parent;
				}catch(ClassCastException e) {
					throw new IncompatibleScriptException("button", "GG");
				}
			}

			@Override
			public void onDestroy() {
				
			}
			
		});
		addGameObject(button);
		
		//Background
		GameObject background = new GameObject(0, -420);
		Image img = new Image("img/parallax-space-backgound.png", 600, 1020, true, true);
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
		GameObject playerIcon = new GameObject(250, 100);
		Sprite pyiconSprite = new ImageSprite(playerIcon, playerimg[0]);
		playerIcon.setSprite(pyiconSprite);
		playerIcon.addScript(new Animator((ImageSprite)pyiconSprite,PlayerIcon));
		addGameObject(playerIcon);
	}
}
