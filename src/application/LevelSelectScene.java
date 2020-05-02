package application;

import gui.GameButton;
import gui.util.ButtonScript;
import logic.base.GameInterruptException;
import logic.base.SceneChangeInterruptException;
import logic.util.ResourceManager;

public class LevelSelectScene extends GameScene {

	@Override
	public void init() {
		GameButton normalLevelButton = new GameButton(50, 50, "Space", 500,100,
				ResourceManager.getImage("img/Button.png", 500, 100),
				ResourceManager.getImage("img/clickedButton.png", 500, 100));
		normalLevelButton.getSprite().setZ(99);
		normalLevelButton.addScript(new ButtonScript() {
			
			@Override
			public void whilePressed() throws GameInterruptException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRelease() throws GameInterruptException {
				throw new SceneChangeInterruptException(new NormalLevelScene());
				
			}
			
			@Override
			public void onPressed() throws GameInterruptException {
				// TODO Auto-generated method stub
				
			}
		});
		addGameObject(normalLevelButton);

	}

}
