package application;

import drawing.SimpleCamera;
import drawing.Renderer;
import gui.GameButton;
import gui.util.ButtonScript;
import javafx.scene.Parent;
import logic.base.GameInterruptException;
import logic.base.SceneChangeInterruptException;
import logic.util.ResourceManager;

public class LevelSelectScene extends GameScene {
	
	public LevelSelectScene(String name) {
		super(name);
	}

	@Override
	public void init() {
		
		SimpleCamera camera = new SimpleCamera(Renderer.getInstance().getGc().getCanvas());
		addGameObject(camera);
		Renderer.getInstance().setCamera(camera);
		
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
				throw new SceneChangeInterruptException(new BossScene("boss"));
				
			}
			
			@Override
			public void onPressed() throws GameInterruptException {
				// TODO Auto-generated method stub
				
			}
		});
		addGameObject(bossLevelButton);

	}

}
