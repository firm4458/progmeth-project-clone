package logic.util.animation;

import java.util.TreeMap;

import javafx.scene.image.Image;

public class AnimationState {
	protected int index;
	private Image[] images;
	private TreeMap<String, AnimationState> stateMap; // contain name of the state and animation state, in order to change it when receive the trigger
	public String name;
	protected boolean loop = true;
	protected Animator animator;

	protected Animator getAnimator() {
		return animator;
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	public AnimationState(String name, Image[] images, TreeMap<String, AnimationState> stateMap) {
		this.stateMap = stateMap;
		this.images = images;
		this.name = name;
		index = 0;
	}

	public AnimationState(Image image) {
		images = new Image[] { image };
		stateMap = new TreeMap<String, AnimationState>();
		index = 0;
	}

	public void putTrigger(String key, AnimationState value) {
		stateMap.put(key, value);
	}

	public void onEnterState() {
		index = 0;
	}

	public void onEnd() {
	}

	public AnimationState updateState(String[] triggers) {
		AnimationState newState = this;
		for (String trigger : triggers) {
			if (stateMap.containsKey(trigger)) {
				newState = stateMap.get(trigger);
				newState.onEnterState();
				return newState;
			}
		}
		if (index == images.length - 1) {
			index = loop ? 0 : -1;
			onEnd();
		} else if (index != -1) {
			index++;
		}
		return newState;
	}

	public Image getImage() {
		if (index == -1) {
			return images[images.length - 1];
		} else {
			return images[index];
		}
	}

	public int getIndex() {
		return this.index;
	}
}
