import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUI extends Application {
	Pane root = new Pane();
	Player player = new Player(50, 50, Color.DARKGREEN);

	
	@Override
	public void start(Stage primaryStage) {
		player.setTranslateX(250);
		player.setTranslateY(400);		
		root.getChildren().add(player);
		
		//Update
		AnimationTimer time = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				player.update(root);
//				System.out.println(player.getBullets().size());
				System.out.println(GroupOfMeteors.getMeteors().size());
				GroupOfMeteors.update(root, player);
				GroupOfMeteors.generate(root);
				
				root.getChildren().removeIf(n -> {
					Sprite s =(Sprite) n;
					return s.getDead();
				});
			}
		};
		time.start();
		//
		

		
		//Stage Show
		Scene scene = new Scene(root, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		
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
}
