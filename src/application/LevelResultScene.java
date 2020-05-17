package application;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.text.Font;
import logic.TextObject;
import logic.util.DataManager;
import logic.util.ResourceManager;

/*
 * show the result of playing a level
 */
public class LevelResultScene extends GameScene {
	
	private LevelResult result; // result of the level (WIN,LOSE,NONE)
	private int score; // score gained from the level

	/*
	 * enumeration of all result of a level
	 */
	public enum LevelResult {
		NONE, WIN, LOSE
	}

	/*
	 * creates a new result scene with specified result and score
	 */
	public LevelResultScene(String name, LevelResult result, int score) {
		super(name);
		this.result = result;
		this.score = score;
	}
	
	/*
	 * initialize the scene
	 */
	@Override
	public void init() {
		
		String str = "";
		
		// convert result to string
		switch (result) {
		case NONE:
			break;
		case LOSE:
			str = " : LOSE";
			break;
		case WIN:
			str = " : WIN";
			break;
		default:
			break;
		}
		
		int totalScore = (int) DataManager.getInstance().getPesistentData("totalScore");
		
		// text object showing word RESULT and the level result
		TextObject resultText = new TextObject(300, 150, "RESULT" + str, new Font("ARCADECLASSIC", 48), 600);
		// text object showing word Score and score gained from the level
		TextObject scoreText = new TextObject(300, 200, "SCORE : " + Integer.toString(score),
				new Font("ARCADECLASSIC", 30), 600);
		// text object showing the current total score
		TextObject totalScoreText = new TextObject(300, 250, "TOTAL SCORE : " + Integer.toString(totalScore),
				new Font("ARCADECLASSIC", 30), 600);
		// add text objects to scene
		addGameObject(resultText);
		addGameObject(scoreText);
		addGameObject(totalScoreText);
		
		// create a button for returning to level select scene
		ImageButton returnButton = new ImageButton(150, 50, ResourceManager.getImage("button.blueButton"), null,
				ResourceManager.getImage("button.blueButton.pressed"));
		returnButton.getGameObject().translate(225, 300);
		returnButton.createFollowText("Go Back", 75, 23);
		
		GameScene scene = this; // this is for the lamda
		returnButton.setOnAction((evt) -> {
			GameManager.getInstance()
					.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new LevelSelectScene("select")));
		});
		
		// add return button to scene
		Group root = (Group) getRoot();
		root.getChildren().add(returnButton);
	}

}
