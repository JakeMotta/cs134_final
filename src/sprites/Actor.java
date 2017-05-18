package sprites;
import com.jogamp.opengl.GL2;

public interface Actor {
		
	void update(GL2 gl);
	
	String getShape();
		
}
