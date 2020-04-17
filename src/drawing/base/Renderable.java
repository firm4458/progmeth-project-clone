package drawing.base;
import drawing.Camera;
import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
	// draw this into gc using camera
	public void draw(GraphicsContext gc, Camera camera);
	// if a Renderable is invisible, it will not be rendered in render()
	public boolean isVisible(); 
	// if a Renderable is destroyed, it will be removed in clearInactiveRenderable()
	public boolean isDestroyed(); 
	// used for ordering renderable
	public int getZ();
}
