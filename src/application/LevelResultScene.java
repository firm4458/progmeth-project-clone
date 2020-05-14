package application;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.text.Font;
import logic.TextObject;
import logic.util.DataManager;
import logic.util.ResourceManager;

public class LevelResultScene extends GameScene {
	private LevelResult result;
	private int score;
	
	public enum LevelResult {
		NONE,WIN,LOSE
	}

	public LevelResultScene(String name, LevelResult result, int score) {
		super(name);
		this.result = result;
		this.score = score;
	}

	@Override
	public void init() {
		String str="";
		switch(result) {
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
		int totalScore = (int)DataManager.getInstance().getPesistentData("totalScore");	
		TextObject resultText = new TextObject(300, 200, "RESULT"+str, new Font("ARCADECLASSIC",48), 600);
		TextObject scoreText = new TextObject(300,300,"SCORE : "+Integer.toString(score),new Font("ARCADECLASSIC",30),600);
		TextObject totalScoreText = new TextObject(300,350,"TOTAL SCORE : "+Integer.toString(totalScore),new Font("ARCADECLASSIC",30),600);
		addGameObject(resultText);
		addGameObject(scoreText);
		addGameObject(totalScoreText);
		ImageButton returnButton = new ImageButton(100, 50, ResourceManager.getImage("button.blueButton"), null, ResourceManager.getImage("button.blueButton.pressed"));
		GameScene scene = this;
		returnButton.setOnAction((evt)->{
			GameManager.getInstance().signalEvent(new GameEvent(scene,GameEventType.SCENE_CHANGE,new LevelSelectScene("select")));
		});
		Group root = (Group)getRoot();
		root.getChildren().add(returnButton);
	}

}
