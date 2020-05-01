package gui;

import application.GUI;
import application.GameManager;
import drawing.ImageSprite;
import drawing.TextSprite;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.IncompatibleScriptException;
import logic.util.InputUtil;
import logic.util.ResourceManager;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;

public class GameButton extends GameObject {
	Button button;
	String name;
	double normalHeight;
	double normalWidth;
	Animator animator;

	public GameButton(double X, double Y, String name, double width, double height, Image img, Image clickedImg) {
		super(X, Y);
		button = new Button();
		button.setFocusTraversable(false);
		button.setBackground(null);
		ImageSprite sprite = new ImageSprite(this, img);
		setSprite(sprite);
		AnimationState normalState = new AnimationState(img);
		AnimationState clickedState = new AnimationState(clickedImg);
		normalState.putTrigger("click", clickedState);
		clickedState.putTrigger("release", normalState);
		animator = new Animator(sprite, normalState);
		addScript(animator);
		normalWidth = width;
		normalHeight = height;
		this.name = name;
		InputUtil.buttonMap.put(name, false);
		GUI.root.getChildren().add(button);
		button.setPrefWidth(normalWidth);
		button.setMaxWidth(normalWidth);
		button.setMinWidth(normalWidth);
		button.setPrefHeight(normalHeight);
		button.setMaxHeight(normalHeight);
		button.setMinHeight(normalHeight);
		button.pressedProperty().addListener((observable, wasPressed, pressed) -> {
			InputUtil.buttonMap.put(getName(), pressed);
		});
		GUI.canvas.widthProperty().addListener(evt -> {
			double normalWidth = getNormalWidth();
			if (normalWidth == 0.0) {
				setNormalWidth(button.getWidth());
				normalWidth = getNormalWidth();
			}
			
			button.setPrefWidth(GUI.canvas.getWidth() / 600.0 * normalWidth);
			button.setMaxWidth(GUI.canvas.getWidth() / 600.0 * normalWidth);
			button.setMinWidth(GUI.canvas.getWidth() / 600.0 * normalWidth);
		});
		GUI.canvas.heightProperty().addListener(evt -> {
			double normalHeight = getNormalHeight();
			if (normalHeight == 0.0) {
				setNormalHeight(button.getHeight());
				normalHeight = getNormalHeight();
			}
			

			button.setPrefHeight(GUI.canvas.getHeight() / 600.0 * normalHeight);
			button.setMaxHeight(GUI.canvas.getHeight() / 600.0 * normalHeight);
			button.setMinHeight(GUI.canvas.getHeight() / 600.0 * normalHeight);
		});

		addScript(new Script() {

			GameObject parent;

			@Override
			public void update() throws GameInterruptException {
				button.setTranslateX(GUI.canvas.getWidth() / 600.0 * getX());
				button.setTranslateY(GUI.canvas.getHeight() / 600.0 * getY());
				animator.sendTrigger(InputUtil.buttonMap.get(getName()) ? "click" : "release");
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
		
		//GameObject gameObj = new GameObject(X+width/4, Y+20+height/4-3);
		GameObject gameObj = new GameObject(X+width/2, Y+height/2-3);
		TextSprite ts = new TextSprite(gameObj, name, 20,width);
		ts.setZ(99);
		gameObj.setSprite(ts);
		GameManager.getInstance().getCurrentScene().addGameObject(gameObj);
	}

	public double getNormalHeight() {
		return normalHeight;
	}

	public void setNormalHeight(double normalHeight) {
		this.normalHeight = normalHeight;
	}

	public double getNormalWidth() {
		return normalWidth;
	}

	public void setNormalWidth(double normalWidth) {
		this.normalWidth = normalWidth;
	}

	public String getName() {
		return name;
	}

	@Override
	public void destroy() {
		super.destroy();
		InputUtil.buttonMap.remove(name);
		GUI.root.getChildren().remove(button);
	}
}
