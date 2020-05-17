package logic.enemy;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.base.GameObjectFactory;
import logic.base.ScriptFactory;

public class EnemyFactory extends GameObjectFactory<Enemy> {

	protected double X;
	protected double Y;
	protected int health;
	protected int damage;
	protected int point;
	protected ArrayList<ScriptFactory> scriptFactories = new ArrayList<ScriptFactory>();
	protected BiConsumer<ArrayList<GameObject>, Enemy> onHitPlayerFunc = null;
	protected Consumer<Enemy> onDeathFunc = null;
	protected Image image;

	/**
	 * use to create enemy
	 */
	@Override
	public Enemy createGameObject() {
		Enemy enemy = new Enemy(X, Y, health, damage, image);
		scriptFactories.forEach(factory -> enemy.addScript(factory.createScript()));
		if (onHitPlayerFunc != null) {
			enemy.setOnHitPlayerFunc(onHitPlayerFunc);
		}
		if (onDeathFunc != null) {
			enemy.setOnDeathFunc(onDeathFunc);
		}
		enemy.setPoint(point);
		return enemy;
	}

	public BiConsumer<ArrayList<GameObject>, Enemy> getOnHitPlayerFunc() {
		return onHitPlayerFunc;
	}

	public void setOnHitPlayerFunc(BiConsumer<ArrayList<GameObject>, Enemy> onHitPlayerFunc) {
		this.onHitPlayerFunc = onHitPlayerFunc;
	}

	public Consumer<Enemy> getOnDeathFunc() {
		return onDeathFunc;
	}

	public void setOnDeathFunc(Consumer<Enemy> onDeathFunc) {
		this.onDeathFunc = onDeathFunc;
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public ArrayList<ScriptFactory> getScriptFactories() {
		return scriptFactories;
	}

	public void setScriptFactories(ArrayList<ScriptFactory> scriptFactories) {
		this.scriptFactories = scriptFactories;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}
