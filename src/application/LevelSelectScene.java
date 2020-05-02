package application;

import drawing.Camera;
import drawing.Renderer;
import gui.GameButton;
import gui.util.ButtonScript;
import logic.base.GameInterruptException;
import logic.base.SceneChangeInterruptException;
import logic.util.ResourceManager;

public class LevelSelectScene extends GameScene {

	@Override
	public void init() {
		
		Camera camera = new Camera(Renderer.getInstance().getGc().getCanvas());
		addGameObject(camera);
		Renderer.getInstance().setCamera(camera);
		
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
		GameButton bossLevelButton = new GameButton(50, 200, "Space: BOSS", 500,100,
				ResourceManager.getImage("img/Button.png", 500, 100),
				ResourceManager.getImage("img/clickedButton.png", 500, 100));
		bossLevelButton.getSprite().setZ(99);
		bossLevelButton.addScript(new ButtonScript() {
			
			@Override
			public void whilePressed() throws GameInterruptException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRelease() throws GameInterruptException {
				throw new SceneChangeInterruptException(new BossScene());
				
			}
			
			@Override
			public void onPressed() throws GameInterruptException {
				// TODO Auto-generated method stub
				
			}
		});
		addGameObject(bossLevelButton);

	}

}
