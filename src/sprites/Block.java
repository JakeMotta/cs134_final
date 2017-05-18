package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Block extends Sprite {
	
	private static int blockImg;
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int stack = 0;

	public Block(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		width = height = 64;
		blockImg = Main.glTexImageTGAFile(gl, "Sprites/Blocks/block1.tga", spriteSize);
		
		setImage(blockImg);
		System.out.println(getX()/64);
		System.out.println("----------------------");
	}
	
	public void update(GL2 gl) {
		
		if(hp <= 0)
			isAlive = false;
		
		stack = Main.myGrid.getGrid(getX()/64);
		checkBelow();
		
		if(shouldFall)
			fall();
		
		System.out.println("Shoudfall:" + shouldFall);
			
		setImage(blockImg);
		draw(gl);
	}
	
	public void fall() {
		moveY(3);
	}
	
	public void checkBelow() {
		// If block is at the bottom of the screen
		if(getY() >= 896) {
			shouldFall = false;
		}
		
		if(getY() >= stack * 64)
			shouldFall = false;
		
		//System.out.println("grid[" + getX()/64 + "]:" + Main.myGrid.getGrid(getX()/64));
		
	}
	
	public void setHP(int dmg) {
		hp -= dmg;
	}
	
	public int getHP() {
		return hp;
	}

}
