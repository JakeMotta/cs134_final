package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Dummy extends Sprite {
	
	private static int dummyImg;
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int stack = 0;
	private int fallDiff = 0;
	private int vsp = Main.getBlockVSP();
	private boolean remove = false;
	private int blocksRemoved = 0;
	private int onGroundTimer = 0;

	public Dummy(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		width = height = 64;
		dummyImg = Main.glTexImageTGAFile(gl, "Sprites/Dummy/Dummy.tga", spriteSize);
		
		setImage(dummyImg);
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public void update(GL2 gl) {
		setImage(dummyImg);
		//draw(gl);
	}
}
