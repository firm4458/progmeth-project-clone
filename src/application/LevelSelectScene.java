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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.AudioClip;
import logic.util.ResourceManager;

public class LevelSelectScene extends GameScene {

	private boolean fromTutorial;
	
	private static AudioClip bgmPlayer;

	public LevelSelectScene(String name) {
		super(name);
	}

	public LevelSelectScene(String name, boolean fromTutorial) {
		super(name);
		this.fromTutorial = fromTutorial;
	}

	private static GameManager manager = GameManager.getInstance();

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

	private ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
	private static String[] sceneNames = { "space1", "space1.boss", "space2", "space2.boss", "space3", "space3.boss" };
	private static final int BUTTON_PER_PAGE = 5;
	private int currentPage = 0;

	private int getCurrentPage() {
		return currentPage;
	}

	private void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	private void updatePage() {
		int from = currentPage * BUTTON_PER_PAGE;
		int to = from + BUTTON_PER_PAGE - 1;
		for (int i = 0; i < buttons.size(); ++i) {
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

	@Override
	public void init() {
		Group root = (Group) getRoot();

		if (fromTutorial) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Welcome");
			alert.setHeaderText(null);
			alert.setContentText("Welcome to Space Master!\nThis is where you can select which level to play\n"
					+ "You can select any level, but we recommend playing easier levels first to gain some score and upgrade your health, damage, and heal"
					+ "\nEvery non boss levels are endless, you are free to quit the level any time you want");
			alert.show();
		}

		for (int i = 0; i < sceneNames.length; ++i) {
			String name = sceneNames[i];
			ImageButton button = new ImageButton(600, 100, ResourceManager.getImage("levelBanner." + name),
					ResourceManager.getImage("levelBanner." + name + ".pressed"),
					ResourceManager.getImage("levelBanner." + name + ".pressed"));
			GameEvent event = new GameEvent(this, GameEventType.SCENE_CHANGE, sceneFromString(name));
			button.setOnAction(evt -> {manager.signalEvent(event); bgmPlayer.stop(); bgmPlayer=null;});
			if (i < BUTTON_PER_PAGE) {
				button.enable();
				button.getGameObject().setX(0);
				button.getGameObject().setY(i * 100);
			} else {
				button.disable();
				button.getGameObject().setX(99999);
				button.getGameObject().setY(99999);
			}
			buttons.add(button);
			root.getChildren().add(button);
		}

		GameScene scene = this;

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

		ImageButton upgradeButton = new ImageButton(100, 50, null, null, null);
		upgradeButton.getGameObject().translate(195, 525);
		upgradeButton.setOnAction((evt) -> {
			GameManager.getInstance()
					.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new UpgradeScene("upgrade")));
		});
		upgradeButton.createFollowText("Upgrade", 50, 23);

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
		saveButton.getGameObject().translate(305, 525);
		saveButton.createFollowText("Save", 50, 23);
		root.getChildren().addAll(upgradeButton, nextPage, prevPage, saveButton);
		if(bgmPlayer==null) {
			bgmPlayer = new AudioClip(ResourceManager.getSound("sound/levelSelect.mp3").getSource());
			bgmPlayer.play();
		}
	}

}
