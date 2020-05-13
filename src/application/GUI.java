package application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import drawing.SimpleCamera;
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
import logic.base.IncompatibleScriptException;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
import logic.enemy.ExplosionAnimation;
import logic.enemy.GroupOfMeteors;
import logic.enemy.Meteor;
import logic.player.Player;
import logic.util.InputUtil;
import logic.util.ResourceManager;
import logic.util.scripts.ConstantSpeedMove;

public class GUI extends Application {
	public static Pane root = new AnchorPane();
	public static Canvas canvas;
	public static Stage stage;

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		canvas = new Canvas(GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Renderer.getInstance().setGc(gc);
		root.getChildren().add(canvas);
		GameManager.getInstance().init(new MenuScene("menu"));

	}

	public static void main(String[] args) {
		launch(args);
	}

	

	/*
	 * public List<Sprite> sprite(){ return root.getChildren().stream().map(n ->
	 * (Sprite)n).collect(Collectors.toList()); }
	 */
}
