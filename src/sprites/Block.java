package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Block extends Sprite {
	
	private static int blockImg;
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int stack = 0;
	private int fallDiff = 0;
	private int vsp = Main.getBlockVSP();
	private boolean remove = false;
	private int blocksRemoved = 0;
	private int onGroundTimer = 0;

	public Block(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		width = height = 64;
		blockImg = Main.glTexImageTGAFile(gl, "Sprites/Blocks/block1.tga", spriteSize);
		stack = Main.myGrid.getGrid(getX()/64);
		blocksRemoved = Main.myGrid.blocksRemoved(getX()/64) * 64;
		
		setImage(blockImg);
	}
	
	public void update(GL2 gl) {
		
		if(hp <= 0)
			isAlive = false;
		
		if(shouldFall)
			fall();
		else {
			sink();
			onGroundTimer++;
		}
		
		checkBelow();
		checkVSP();
		
		setImage(blockImg);
		draw(gl);
	}
	
	public void checkVSP() {
		vsp = Main.getBlockVSP();
	}
	
	public void sink() {
		if(Main.getGameTimer() % Main.getGameSpeed() == 0) 
			moveY(vsp);	
	}
	
	public void fall() {
		moveY(vsp);
	}

	public void checkBelow() {
		// If block is at the bottom of the screen
		if(getY() >= 896)
			shouldFall = false;
				
		fallDiff = vsp * (Main.getGameTimer() / Main.getGameSpeed());

		if(getY() >= Main.worldHeight - (stack * 64) - 64 + fallDiff - blocksRemoved)
			shouldFall = false;	
		
		if(getY() >= 960)
			remove = true;
	}
	
	public boolean checkRemoval() {
		return remove;
	}
	
	public void setHP(int dmg) {
		hp -= dmg;
	}
	
	public int getHP() {
		return hp;
	}
	
	public int getOnGroundTimer() {
		return onGroundTimer;
	}

}
