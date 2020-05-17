package application;

import java.util.TreeMap;
import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import logic.TextObject;
import logic.base.BasicScript;
import logic.base.Dio;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.InputUtil;
import logic.util.ResourceManager;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;
import logic.util.scripts.AutoDestroy;

/*
 * GameObject for providing tutorial
 */
public class Tutorial extends GameObject implements Dio {

	// script for tutorial of the world
	private Script TheWorldTutorial;
	// script for tutorial of dash
	private Script dashTutorial;
	// script for movement Tutorial
	private Script movementTutorial;

	/*
	 * create tutorial script for the world, dash and, movement
	 */
	public Tutorial() {
		super(0, 0);

		createTheWorldTutorialScript();
		createDashTutorialScript();
		createMovementTutorialScript();

	}
	
	/*
	 * create tutorial script for the world
	 * show picture of button X and instruction text
	 * when key X is pressed, remove button and instruction text
	 * then, create a new instruct text which destroy it self in 5 seconds
	 * on destroy, signal scene change event to LevelSelectScene with fromTutorial equals true
	 */
	private void createTheWorldTutorialScript() {
		TheWorldTutorial = new BasicScript<GameObject>() {
			private TextObject text;
			private GameObject button;

			@Override
			public void onAttach() {
				button = new GameObject(0, 0);
				ImageSprite sprite = new ImageSprite(button, ResourceManager.getImage("button.keys.X"));
				sprite.setScale(0.2);
				sprite.setZ(99);
				button.setSprite(sprite);
				Image[] frames = new Image[20];
				for (int j = 0; j < 10; ++j) {
					frames[j] = ResourceManager.getImage("button.keys.X");
				}
				for (int j = 10; j < 20; ++j) {
					frames[j] = ResourceManager.getImage("button.keys.X.pressed");
				}
				button.addScript(new Animator(sprite,
						new AnimationState("idle", frames, new TreeMap<String, AnimationState>())));
				button.translate(270, 270);
				parent.getScene().addGameObject(button);

				text = new TextObject(300, 350, "Press X to use The World", new Font("m5x7", 30), 600);
				text.getSprite().setZ(99);
				parent.getScene().addGameObject(text);
			}

			@Override
			public void update() {
				if (InputUtil.isKeyPressed(KeyCode.X)) {
					parent.removeScript(this);
					TextObject info = new TextObject(300, 200,
							"While the time is stopped:\n\nItems can be picked up\n\nEnemies can be damaged,\nbut it won't die until skill has ended",
							new Font("m5x7", 30), 600);

					info.getSprite().setZ(99);
					info.addScript(new AutoDestroy(5000) {
						@Override
						public void onDestroy() {
							super.onDestroy();
							GameManager.getInstance().signalEvent(new GameEvent(parent.getScene(),
									GameEventType.SCENE_CHANGE, new LevelSelectScene("select", true)));
						}
					});
					parent.getScene().addGameObject(info);
				}
			}

			@Override
			public void onDestroy() {
				button.destroy();
				text.destroy();
			}
		};
	}
	
	/*
	 * create tutorial script for the world
	 * show picture of button Z and instruction text
	 * when key X is pressed, remove button, instruction text, and remove dashTutorialScript from this game object
	 * then, attach theWorldTutorial to this game object
	 */
	private void createDashTutorialScript() {
		dashTutorial = new BasicScript<GameObject>() {
			private TextObject text;
			private GameObject button;

			@Override
			public void onAttach() {
				button = new GameObject(0, 0);
				ImageSprite sprite = new ImageSprite(button, ResourceManager.getImage("button.keys.Z"));
				sprite.setScale(0.2);
				sprite.setZ(99);
				button.setSprite(sprite);
				Image[] frames = new Image[20];
				for (int j = 0; j < 10; ++j) {
					frames[j] = ResourceManager.getImage("button.keys.Z");
				}
				for (int j = 10; j < 20; ++j) {
					frames[j] = ResourceManager.getImage("button.keys.Z.pressed");
				}
				button.addScript(new Animator(sprite,
						new AnimationState("idle", frames, new TreeMap<String, AnimationState>())));
				button.translate(270, 270);
				parent.getScene().addGameObject(button);

				text = new TextObject(300, 350, "Press Z to dash", new Font("m5x7", 30), 600);
				text.getSprite().setZ(99);
				parent.getScene().addGameObject(text);
			}

			@Override
			public void update() {
				if (InputUtil.isKeyPressed(KeyCode.Z)) {
					parent.removeScript(this);
					parent.addScript(TheWorldTutorial);
				}
			}

			@Override
			public void onDestroy() {
				button.destroy();
				text.destroy();
			}
		};
	}
	
	/*
	 * create tutorial script for the movement
	 * show picture of arrow buttons and instruction text
	 * when any arrow keys is pressed, remove button, instruction text, and remove movementTutorialScript from this game object
	 * then, attach dashTutorial to this game object
	 */
	private void createMovementTutorialScript() {
		movementTutorial = new BasicScript<GameObject>() {
			private GameObject[] buttons;
			private TextObject text;

			@Override
			public void onAttach() {
				String[] path = new String[] { "button.keys.UP", "button.keys.DOWN", "button.keys.LEFT",
						"button.keys.RIGHT" };
				buttons = new GameObject[4];
				for (int i = 0; i < 4; ++i) {
					buttons[i] = new GameObject(0, 0);
					ImageSprite sprite = new ImageSprite(buttons[i], ResourceManager.getImage(path[i]));
					sprite.setScale(0.2);
					sprite.setZ(99);
					buttons[i].setSprite(sprite);
					Image[] frames = new Image[20];
					for (int j = 0; j < 10; ++j) {
						frames[j] = ResourceManager.getImage(path[i]);
					}
					for (int j = 10; j < 20; ++j) {
						frames[j] = ResourceManager.getImage(path[i] + ".pressed");
					}
					buttons[i].addScript(new Animator(sprite,
							new AnimationState("idle", frames, new TreeMap<String, AnimationState>())));
					System.out.println(parent.getScene());
					parent.getScene().addGameObject(buttons[i]);
				}
				buttons[0].translate(270, 200);
				buttons[1].translate(270, 270);
				buttons[2].translate(200, 270);
				buttons[3].translate(340, 270);

				text = new TextObject(300, 350, "Press Arrow Keys to move", new Font("m5x7", 30), 600);
				text.getSprite().setZ(99);
				parent.getScene().addGameObject(text);
			}

			@Override
			public void update() {
				if (InputUtil.isKeyPressed(KeyCode.UP) || InputUtil.isKeyPressed(KeyCode.DOWN)
						|| InputUtil.isKeyPressed(KeyCode.LEFT) || InputUtil.isKeyPressed(KeyCode.RIGHT)) {
					parent.removeScript(this);
					parent.addScript(dashTutorial);
				}
			}

			@Override
			public void onDestroy() {
				for (GameObject button : buttons) {
					button.destroy();
				}
				text.destroy();
			}
		};
	}

	/*
	 * start the tutorial by adding movementTutorial to this game object
	 */
	public void startTutorial() {
		addScript(movementTutorial);
	}

}
