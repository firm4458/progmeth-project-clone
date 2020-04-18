package application;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import drawing.Camera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.PlanetSpawner;
import logic.base.GameObject;
import logic.base.Script;
import logic.enemy.ExplosionAnimation;
import logic.enemy.GroupOfMeteors;
import logic.enemy.Meteor;
import logic.player.Player;
import logic.util.ConstantSpeedMove;
import logic.util.IncompatibleScriptException;
import logic.util.InputUtil;

public class GUI extends Application {
	Pane root = new Pane();
	public static GroupOfMeteors groupOfMeteors;
	public static GameSceneManager sampleScene;

	@Override
	public void start(Stage primaryStage) {

		Canvas canvas = new Canvas(600, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		//Canvas bgCanvas = new Canvas(600,600);
		//GraphicsContext bgGc = bgCanvas.getGraphicsContext2D();

		//root.getChildren().add(bgCanvas);
		root.getChildren().add(canvas);
		
		

		
		Camera camera = new Camera(canvas.getWidth(),canvas.getHeight());
		
		Renderer.getInstance().setCamera(camera);
		
		sampleScene = new GameSceneManager();
		
		groupOfMeteors = new GroupOfMeteors();
		
		GameObject planetSpawn = new GameObject(0, 0);
		planetSpawn.addScript(new PlanetSpawner());
		sampleScene.addGameObject(planetSpawn);
		
		GameObject background = new GameObject(0,-420);
		Image img = new Image("img/parallax-space-backgound.png",600,0,true,true);
		Sprite bgSprite = new ImageSprite(background,img);
		bgSprite.setZ(-99);
		System.out.println(img.getWidth());
		System.out.println(img.getHeight());
		background.setSprite(bgSprite);
		background.addScript(new ConstantSpeedMove(0,0.07));
		
		background.addScript(new Script() {
			
			private GameObject parent;
			private double a; 
			@Override
			public void update() {
				parent.getSprite().getColorAdjust().setBrightness(0.1*Math.sin(a));
				a+=0.02;
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
		sampleScene.addGameObject(background);
		
		Player player = new Player(250, 400);
		
		sampleScene.addGameObject(player);
		sampleScene.addGameObject(groupOfMeteors);
		sampleScene.addGameObject(camera);
		
		
		// Stage Show
		Scene scene = new Scene(root, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Set Player move
		scene.setOnKeyPressed(e -> {
			InputUtil.setKeyPressed(e.getCode(), true);
		});

		scene.setOnKeyReleased(e -> {
			InputUtil.setKeyPressed(e.getCode(), false);
		});

		// Update
		AnimationTimer time = new AnimationTimer() {

			@Override
			public void handle(long now) {
				sampleScene.update();
				Renderer.getInstance().render(gc);
			}
		};
		time.start();

	}

	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * public List<Sprite> sprite(){ return root.getChildren().stream().map(n ->
	 * (Sprite)n).collect(Collectors.toList()); }
	 */
}
