import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUI extends Application {
	Pane root = new Pane();
	Player player = new Player(50, 50, Color.DARKGREEN);
	BackGround background = new BackGround(600, 600);
	static int score = 0;
	
	@Override
	public void start(Stage primaryStage) {

		Canvas canvas = new Canvas(600,600);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		root.getChildren().add(canvas);
		root.getChildren().add(background);
		
		Player player = new Player(50, 50, Color.DARKGREEN);
		
		//Update
		AnimationTimer time = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				background.update();
				gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
				
				player.update(root);
				System.out.println(GroupOfMeteors.getMeteors().size());
				GroupOfMeteors.update(root, player);
				GroupOfMeteors.generate(root);
				
				player.getBullets().removeIf(bullet -> bullet.getDead());
				GroupOfMeteors.getMeteors().removeIf(meteor -> meteor.getDead());
				
				player.draw(gc);
				
				for(Bullet bullet: player.getBullets()) {
					bullet.draw(gc);
				}
				
				for(Meteor meteor: GroupOfMeteors.getMeteors()) {
					meteor.draw(gc);
				}
				System.out.println(score);

			}
		};
		time.start();
		background.init();
		//
		

		
		//Stage Show
		Scene scene = new Scene(root, 600, 600);
		canvas.toFront();
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		
		player.setTranslateX(250);
		player.setTranslateY(400);		
		
		//Set Player move
		scene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.LEFT) {
				player.setMoveLeft(true);
			}
			if(e.getCode() == KeyCode.RIGHT) {
				player.setMoveRight(true);
			}
			if(e.getCode() == KeyCode.UP) {
				player.setMoveUp(true);
			}
			if(e.getCode() == KeyCode.DOWN) {
				player.setMoveDown(true);
			}
			if(e.getCode() == KeyCode.SPACE) {
				player.shoot(root);
				
			}
		}); 
		
		scene.setOnKeyReleased(e -> {
			if(e.getCode() == KeyCode.LEFT) {
				player.setMoveLeft(false);
			}
			if(e.getCode() == KeyCode.RIGHT) {
				player.setMoveRight(false);
			}
			if(e.getCode() == KeyCode.UP) {
				player.setMoveUp(false);
			}
			if(e.getCode() == KeyCode.DOWN) {
				player.setMoveDown(false);
			}
		}); 
		
		/////////////////////////////////////////

	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public List<Sprite> sprite(){
		return root.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
	}
	
	public static void addScore(int i) {
		score += i;
	}
}
