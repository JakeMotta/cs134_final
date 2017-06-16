package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Splashscreen {
	
	protected int gravityTimer = 0;

	public Splashscreen(int[] spriteSize, GL2 gl) {
		
	}
	
	public void update(GL2 gl) {
		draw(gl);
	}

	// Quick draw reference
	public void draw(GL2 gl) {
		Main.DrawSprite(gl, Main.images.splashScreen, Main.camera.getX(), Main.camera.getY(), Main.camera.getWidth(), Main.camera.getHeight(), Main.camera);
	}
}
