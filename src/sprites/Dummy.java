package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Dummy extends Sprite {
	
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int vsp = Main.getBlockVSP();

	public Dummy(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);
		
		width = height = 64;
		vsp = Main.getBlockVSP();
		
		setImage(Main.images.dummyImg);
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public void checkVSP() {
		vsp = Main.getBlockVSP();
	}
	
	public void sink() {
		if(Main.getGameTimer() % Main.getGameSpeed() == 0) 
			moveY(vsp);	
	}
	
	public void update(GL2 gl) {
		setImage(Main.images.dummyImg);
		draw(gl);
	}
}
