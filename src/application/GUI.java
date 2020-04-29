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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
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
	Pane root = new AnchorPane();
	public static GroupOfMeteors groupOfMeteors;
	public static GameSceneManager sampleScene;
	public static GameSceneManager newScene;

	@Override
	public void start(Stage primaryStage) {

		ResizableCanvas canvas = new ResizableCanvas(600, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Renderer.getInstance().setGc(gc);

		//Canvas bgCanvas = new Canvas(600,600);
		//GraphicsContext bgGc = bgCanvas.getGraphicsContext2D();

		//root.getChildren().add(bgCanvas);
		Button button = new Button("Hello");
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("hello");
			}
			
		});
		button.setFocusTraversable(false);
		root.getChildren().add(canvas);
		root.getChildren().add(button);
		AnchorPane.setBottomAnchor(button, 50.0);
		AnchorPane.setLeftAnchor(button, 50.0);
		AnchorPane.setTopAnchor(canvas, 0.0);
		AnchorPane.setBottomAnchor(canvas, 0.0);
		AnchorPane.setLeftAnchor(canvas, 0.0);
		AnchorPane.setRightAnchor(canvas, 0.0);
		Camera camera = new Camera(canvas);
		
		Renderer.getInstance().setCamera(camera);
		
		sampleScene = new GameSceneManager();
		
		newScene = new GameSceneManager();
		newScene.addGameObject(new Player(250,400));
		newScene.addGameObject(camera);
		
		groupOfMeteors = new GroupOfMeteors();
		
		GameObject planetSpawn = new GameObject(0, 0);
		planetSpawn.addScript(new PlanetSpawner());
		sampleScene.addGameObject(planetSpawn);
		
		GameObject background = new GameObject(0,-420);
		Image img = new Image("img/parallax-space-backgound.png",600,1020,true,true);
		Sprite bgSprite = new ImageSprite(background,img) {
			/*@Override
			public void draw(GraphicsContext gc, Camera camera) {
				double absoluteX = parent.getX()+relativeX-camera.getX();
				double absoluteY = parent.getY()+relativeY-camera.getY();
				gc.setEffect(colorAdjust);
				gc.drawImage(getImage(), absoluteX, absoluteY,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
				gc.restore();
			}*/
		};
		bgSprite.setZ(-99);
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

		GameManager.init(sampleScene);

	}

	public static void main(String[] args) {
		launch(args);
	}
	
	class ResizableCanvas extends Canvas {

        public ResizableCanvas(double width,double height) {
            // Redraw canvas when size changes.
        	super(width,height);
        	widthProperty().bind(root.widthProperty());
        	heightProperty().bind(root.heightProperty());
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
        }

        private void draw() {
            double width = getWidth();
            double height = getHeight();

            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);
        }

        @Override
        public boolean isResizable() {
            return true;
        }

        @Override
        public double prefWidth(double height) {
            return getWidth();
        }

        @Override
        public double prefHeight(double width) {
            return getHeight();
        }
    }

	/*
	 * public List<Sprite> sprite(){ return root.getChildren().stream().map(n ->
	 * (Sprite)n).collect(Collectors.toList()); }
	 */
}
