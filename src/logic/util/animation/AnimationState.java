package logic.util.animation;
import java.util.Iterator;
import java.util.TreeMap;
import javafx.scene.image.Image;

public class AnimationState implements Iterator<Image> {
	private int index;
	private Image[] images;
	private TreeMap<String, AnimationState> stateMap;
	public String name;
	
	public AnimationState(String name,Image[] images, TreeMap<String, AnimationState> stateMap) {
		this.stateMap = stateMap;
		this.images = images;
		this.name = name;
		index = 0;
	}
	
	public AnimationState(Image image) {
		images = new Image[]{image};
		stateMap = new TreeMap<String, AnimationState>();
		index = 0;
	}
	
	public void putTrigger(String key, AnimationState value) {
		stateMap.put(key, value);
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}
	@Override
	public Image next() {
		index %= images.length;
		Image image = images[index++];
		return image;
	}
	
	public AnimationState updateState(String trigger) {
		AnimationState newState = this;
		if(stateMap.containsKey(trigger)) {
			index = 0;
			newState = stateMap.get(trigger);
		}else if(index == images.length && stateMap.containsKey("next")) {
			index = 0;
			newState = stateMap.get("next");
		}
		return newState;
	}
	
	public Image getImage() {
		return next();
	}
	
	public int getIndex() {
		return this.index;
	}
}
