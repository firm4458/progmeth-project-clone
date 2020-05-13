package gui;

import application.GUI;
import application.GameManager;
import drawing.ImageSprite;
import drawing.TextSprite;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
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
		if(clickedImg == null) {
			clickedImg = img;
		}
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
		double Sscale = GUI.canvas.getWidth() / GameManager.NATIVE_WIDTH;
		button.setPrefWidth(normalWidth*Sscale);
		button.setMaxWidth(normalWidth*Sscale);
		button.setMinWidth(normalWidth*Sscale);
		Sscale = GUI.canvas.getHeight() / GameManager.NATIVE_HEIGHT;
		button.setPrefHeight(normalHeight*Sscale);
		button.setMaxHeight(normalHeight*Sscale);
		button.setMinHeight(normalHeight*Sscale);
		button.pressedProperty().addListener((observable, wasPressed, pressed) -> {
			InputUtil.buttonMap.put(getName(), pressed);
		});
		GUI.canvas.widthProperty().addListener(evt -> {
			double normalWidth = getNormalWidth();
			if (normalWidth == 0.0) {
				setNormalWidth(button.getWidth());
				normalWidth = getNormalWidth();
			}
			
			double scale = GUI.canvas.getWidth() / GameManager.NATIVE_WIDTH;
			button.setPrefWidth(scale * normalWidth);
			button.setMaxWidth(scale * normalWidth);
			button.setMinWidth(scale * normalWidth);
		});
		GUI.canvas.heightProperty().addListener(evt -> {
			double normalHeight = getNormalHeight();
			if (normalHeight == 0.0) {
				setNormalHeight(button.getHeight());
				normalHeight = getNormalHeight();
			}
			
			double scale = GUI.canvas.getHeight() / GameManager.NATIVE_HEIGHT;
			button.setPrefHeight(scale * normalHeight);
			button.setMaxHeight(scale * normalHeight);
			button.setMinHeight(scale * normalHeight);
		});

		addScript(new Script() {

			GameObject parent;

			@Override
			public void update() throws GameInterruptException {
				button.setTranslateX(GUI.canvas.getWidth() / GameManager.NATIVE_WIDTH * getX());
				button.setTranslateY(GUI.canvas.getHeight() / GameManager.NATIVE_HEIGHT * getY());
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
		Font.loadFont( ClassLoader.getSystemResourceAsStream("font/ARCADECLASSIC.TTF"), 48);
		TextSprite ts = new TextSprite(gameObj, name,new Font("ArcadeClassic",30),width);
		ts.setZ(99);
		gameObj.setSprite(ts);
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
		System.out.println(name);
	}
}
