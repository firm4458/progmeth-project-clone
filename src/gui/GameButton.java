package gui;

import application.GUI;
import javafx.scene.control.Button;
import logic.base.GameObject;
import logic.util.InputUtil;

public class GameButton extends GameObject {
	Button button;
	String name;
	double normalHeight;
	double normalWidth;

	public GameButton(double X, double Y, String text) {
		super(X, Y);
		button = new Button(text);
		button.setFocusTraversable(false);
		name = text;
		InputUtil.buttonMap.put(name, false);
		GUI.root.getChildren().add(button);
		normalWidth = button.getWidth();
		normalHeight = button.getHeight();
		button.pressedProperty().addListener((observable, wasPressed, pressed) -> {
			InputUtil.buttonMap.put(getName(), pressed);
		});
		GUI.canvas.widthProperty().addListener(evt->{
			double normalWidth = getNormalWidth();
			if(normalWidth==0.0) {
				setNormalWidth(button.getWidth());
				normalWidth = getNormalWidth();
			}
			System.out.println(GUI.canvas.getWidth()/600.0 * normalWidth);
			button.setPrefWidth(GUI.canvas.getWidth()/600.0 * normalWidth);
			button.setMaxWidth(GUI.canvas.getWidth()/600.0 * normalWidth);
			button.setMinWidth(GUI.canvas.getWidth()/600.0 * normalWidth);
		});
		GUI.canvas.heightProperty().addListener(evt->{
			double normalHeight = getNormalHeight();
			if(normalHeight==0.0) {
				setNormalHeight(button.getHeight());
				normalHeight = getNormalHeight();
			}
			button.setPrefHeight(GUI.canvas.getHeight()/600.0 * normalHeight);
			button.setMaxHeight(GUI.canvas.getHeight()/600.0 * normalHeight);
			button.setMinHeight(GUI.canvas.getHeight()/600.0 * normalHeight);
		});
		button.setTranslateX(X);
		button.setTranslateY(Y);
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
