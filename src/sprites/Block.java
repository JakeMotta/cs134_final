package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Block extends Sprite {
	
	private static int blockImg;
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int stack = 0;
	private int initTimer = 0;
	private double fallDiff = 0;
	private int fallSpeed = 50;

	public Block(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		width = height = 64;
		blockImg = Main.glTexImageTGAFile(gl, "Sprites/Blocks/block1.tga", spriteSize);
		stack = Main.myGrid.getGrid(getX()/64);
		initTimer = Main.getGameTimer();
		
		setImage(blockImg);
	}
	
	public void update(GL2 gl) {
		
		if(hp <= 0)
			isAlive = false;
		
		checkBelow();
		
		if(shouldFall)
			fall();
		
		if(!shouldFall)
			sink();
			
		setImage(blockImg);
		draw(gl);
	}
	
	public void sink() {
		if(Main.gameTimer % fallSpeed == 0)
			moveY(1);
	}
	
	public void fall() {
		moveY(3);
	}
	
	public void checkBelow() {
		// If block is at the bottom of the screen
		if(getY() >= 896)
			shouldFall = false;
		
		fallDiff = Math.floor(Main.getGameTimer() / fallSpeed);
		
		// If a block is at the top of another block in its column
		if(getY() >= Main.worldHeight - (stack * 64) - 64 + fallDiff)
			shouldFall = false;		
	}
	
	public void setHP(int dmg) {
		hp -= dmg;
	}
	
	public int getHP() {
		return hp;
	}

}
