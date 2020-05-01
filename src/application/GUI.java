package application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import drawing.Camera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import gui.GameButton;
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
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
import logic.enemy.ExplosionAnimation;
import logic.enemy.GroupOfMeteors;
import logic.enemy.Meteor;
import logic.player.Player;
import logic.util.ConstantSpeedMove;
import logic.util.IncompatibleScriptException;
import logic.util.InputUtil;
import logic.util.ResourceManager;

public class GUI extends Application {
	public static Pane root = new AnchorPane();
	public static GameScene sampleScene;
	public static GameScene menuScene;
	public static Canvas canvas;

	@Override
	public void start(Stage primaryStage) {
		canvas = new ResizableCanvas(GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Renderer.getInstance().setGc(gc);
		root.getChildren().add(canvas);
		
		sampleScene = new NormalLevelScene();
		
		menuScene = new MenuScene();
		
		// Stage Show
		Scene scene = new Scene(root, GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Set Player move
		scene.setOnKeyPressed(e -> {
			InputUtil.setKeyPressed(e.getCode(), true);
		});

		scene.setOnKeyReleased(e -> {
			InputUtil.setKeyPressed(e.getCode(), false);
		});

		GameManager.getInstance().setScene(menuScene);
		GameManager.getInstance().init();

	}

	public static void main(String[] args) {
		launch(args);
	}

	class ResizableCanvas extends Canvas {

		public ResizableCanvas(double width, double height) {
			// Redraw canvas when size changes.
			super(width, height);
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
