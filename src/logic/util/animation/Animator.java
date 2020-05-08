package logic.util.animation;
import java.util.ArrayList;

import drawing.ImageSprite;
import drawing.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.base.Script;

public class Animator implements Script {

	protected AnimationState currentState;
	private ArrayList<String> receivedTrigger;
	private GameObject parent;
	private final ImageSprite sprite;

	public Animator(ImageSprite sprite, AnimationState initialState) {
		this.sprite = sprite;
		currentState = initialState;
		receivedTrigger = new ArrayList<String>();
	}

	public Animator(ImageSprite sprite, Image image) {
		this.sprite = sprite;
		currentState = new AnimationState(image);
		receivedTrigger = new ArrayList<String>();

	}

	public void sendTrigger(String trigger) {
		receivedTrigger.add(trigger);
	}

	@Override
	public void update() {
		currentState = currentState.updateState(receivedTrigger.toArray(new String[0]));
		receivedTrigger.clear();
		sprite.setImage(currentState.getImage());
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	@Override
	public void onDestroy() {
		
	}

}
