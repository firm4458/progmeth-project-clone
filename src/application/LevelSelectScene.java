package application;

import java.util.ArrayList;
import java.util.function.Consumer;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.level.Space1;
import application.level.Space1Boss;
import application.level.Space2;
import application.level.Space2Boss;
import application.level.Space3;
import application.level.Space3Boss;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.AudioClip;
import logic.util.ResourceManager;

/*
 * This is a scene for selecting level
 */
public class LevelSelectScene extends GameScene {

	// determine whether or not previous scene is tutorial
	private boolean fromTutorial;
	
	/*
	 * text for alert when the previous scene is TutorialScene
	 */
	private static final String alertText = "Welcome to Space Master! \n\nThis is where you can select which level to play\n\n"
			+ "You can select any level, but we recommend playing easier levels first to gain some score and upgrade your health, damage, and heal"
			+ "\n\nEvery non boss levels are endless, you are free to quit the level any time you want";

	/*
	 * bgm player which will be shared across multiple instances of levelSelectScene
	 * it won't be stop until one of level button is pressed and scene is going to change
	 * changing to upgrade scene doesn't stop the music
	 */
	private static AudioClip bgmPlayer;

	/*
	 * create a LevelSelectScene
	 */
	public LevelSelectScene(String name) {
		super(name);
	}

	/*
	 * create LevelSelectScene with specified fromTutorial
	 */
	public LevelSelectScene(String name, boolean fromTutorial) {
		super(name);
		this.fromTutorial = fromTutorial;
	}

	// get instance of GameManager for easy access
	private static GameManager manager = GameManager.getInstance();

	/*
	 * construct new Scene based on sceneName
	 */
	private GameScene sceneFromString(String sceneName) {
		GameScene newScene;
		switch (sceneName) {
		case "space1":
			newScene = new Space1();
			break;
		case "space1.boss":
			newScene = new Space1Boss();
			break;
		case "space2":
			newScene = new Space2();
			break;
		case "space2.boss":
			newScene = new Space2Boss();
			break;
		case "space3":
			newScene = new Space3();
			break;
		case "space3.boss":
			newScene = new Space3Boss();
			break;
		default:
			newScene = null;
			System.err.println("cannot find scene:" + sceneName);
			break;
		}
		return newScene;
	}

	// arraylist of level select buttons
	private ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
	// name of levels
	private static final String[] SCENE_NAMES = { "space1", "space1.boss", "space2", "space2.boss", "space3", "space3.boss" };
	// number of level buttons per page
	private static final int BUTTON_PER_PAGE = 5;
	// current button page
	private int currentPage = 0;

	/*
	 * getter for currentPage
	 */
	private int getCurrentPage() {
		return currentPage;
	}

	/*
	 * setter for currentPage
	 * can't be accessed by other classes
	 */
	private void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/*
	 * update which buttons are visible based on currentPage
	 */
	private void updatePage() {
		int from = currentPage * BUTTON_PER_PAGE; // first visible button index
		int to = from + BUTTON_PER_PAGE - 1; // last visible button index
		for (int i = 0; i < buttons.size(); ++i) {
			/*
			 *  for each button, 
			 *  if the index is between from and to, set enable it
			 *  else diable it
			 */
			ImageButton button = buttons.get(i);
			if (from <= i && i <= to) {
				button.enable();
				button.getGameObject().setX(0);
				button.getGameObject().setY((i % BUTTON_PER_PAGE) * 100);
			} else {
				button.disable();
				button.getGameObject().setX(99999);
				button.getGameObject().setY(99999);
			}
		}
	}

	/*
	 * initialize the scene
	 */
	@Override
	public void init() {

		if (fromTutorial) {
			// previous scene is TutorialScene, show information alert
			Alert alert = new Alert(AlertType.INFORMATION);
			
			alert.setTitle("Welcome");
			alert.setHeaderText(null);
			alert.setContentText(alertText);
			
			// set minimum size for dialog pane
			alert.getDialogPane().setMinWidth(400);
			alert.getDialogPane().setMinHeight(400);
			
			alert.show();
		}

		// create necessary buttons
		createLevelButton();
		createPageChangeButton();
		createSaveButton();
		createUpgradeButton();

		// if there is no currently playing bgmPlayer
		if (bgmPlayer == null) {
			bgmPlayer = new AudioClip(ResourceManager.getSound("sound/levelSelect.mp3").getSource());
			bgmPlayer.play();
		}
	}
	
	/*
	 * setup button for going to UpgradeScene
	 */
	private void createUpgradeButton() {
		GameScene scene = this;
		ImageButton upgradeButton = new ImageButton(100, 50, null, null, null);
		upgradeButton.getGameObject().translate(195, 525);
		upgradeButton.setOnAction((evt) -> {
			GameManager.getInstance()
					.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new UpgradeScene("upgrade")));
		});
		upgradeButton.createFollowText("Upgrade", 50, 23);

		Group root = (Group)getRoot();
		root.getChildren().add(upgradeButton);
	}
	
	/*
	 * setup button for saving
	 */
	private void createSaveButton() {
		GameScene scene = this;
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
					}
				}
			});
			GameManager.getInstance().signalEvent(event);
		});
		saveButton.getGameObject().translate(305, 525);
		saveButton.createFollowText("Save", 50, 23);
		
		Group root = (Group)getRoot();
		root.getChildren().add(saveButton);
	}
	
	/*
	 * create button for changing page
	 */
	private void createPageChangeButton() {
		ImageButton nextPage = new ImageButton(50, 50, ResourceManager.getImage("button.resume"), null, null);
		ImageButton prevPage = new ImageButton(50, 50, ResourceManager.getImage("button.back"), null, null);
		prevPage.setInactive();
		if (buttons.size() < BUTTON_PER_PAGE) {
			nextPage.setInactive();
		}
		nextPage.setOnAction((evt) -> {
			int page = getCurrentPage();
			int totalPage = (buttons.size() - 1) / BUTTON_PER_PAGE;
			if (page >= totalPage) {
				nextPage.setInactive();
				return;
			}
			++page;
			setCurrentPage(page);
			if (page == totalPage) {
				nextPage.setInactive();
			}
			prevPage.setActive();
			updatePage();
		});
		prevPage.setOnAction((evt) -> {
			int page = getCurrentPage();
			if (page == 0) {
				prevPage.setInactive();
				return;
			}
			--page;
			setCurrentPage(page);
			if (page == 0) {
				prevPage.setInactive();
			}
			nextPage.setActive();
			updatePage();
		});
		prevPage.getGameObject().translate(125, 525);
		nextPage.getGameObject().translate(430, 525);
		
		Group root = (Group)getRoot();
		root.getChildren().addAll(prevPage, nextPage);
	}
	
	/*
	 * create buttons for each level
	 */
	private void createLevelButton() {
		for (int i = 0; i < SCENE_NAMES.length; ++i) {
			
			String name = SCENE_NAMES[i];
			ImageButton button = new ImageButton(600, 100, ResourceManager.getImage("levelBanner." + name),
					ResourceManager.getImage("levelBanner." + name + ".pressed"),
					ResourceManager.getImage("levelBanner." + name + ".pressed"));
			
			// create scene change GameEvent based on name
			GameEvent event = new GameEvent(this, GameEventType.SCENE_CHANGE, sceneFromString(name));
			
			button.setOnAction(evt -> {
				/*
				 *  when the button is pressed, signal the scene change event
				 *  and stop the bgm player
				 */
				manager.signalEvent(event);
				bgmPlayer.stop();
				bgmPlayer = null;
			});
			if (i < BUTTON_PER_PAGE) {
				// if the button is on the first page, enable it
				button.enable();
				// move it to (0,i*100)
				button.getGameObject().setX(0);
				button.getGameObject().setY(i * 100);
			} else {
				// disable it
				button.disable();
				// move it far away
				button.getGameObject().setX(99999); 
				button.getGameObject().setY(99999);
			}
			// add button to scene
			buttons.add(button);
			Group root = (Group)getRoot();
			root.getChildren().add(button);
		}
	}

}
