package logic.util.scripts;

import java.util.TreeSet;
import logic.base.Dio;
import logic.base.GameObject;
import logic.base.Updatable;

public class DioShaker implements Dio, Updatable {
	private static TreeSet<String> set = new TreeSet<String>();
	private GameObject parent;
	private double magnitude;
	private double speed;
	private double totalOffset;
	private boolean isDestroyed;
	public DioShaker(GameObject parent, double magnitude, double speed) {
		if(!set.contains(parent.getName())) {
			this.parent = parent;
		}else {
			parent = null;
		}
		this.magnitude = magnitude;
		this.speed = speed;
	}
	@Override
	public void update() {
		if(parent==null) {
			destroy();
			return;
		}
		if(totalOffset < magnitude) {
			parent.translate(speed, speed);
			totalOffset+=speed;
		}else {
			parent.translate(-totalOffset, -totalOffset);
			destroy();
		}
	}
	@Override
	public void destroy() {
		isDestroyed = true;
	}
	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
