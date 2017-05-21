package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Block extends Sprite {
	
	private static int blockImg;
	private static int bd1;
	private static int bd2;
	private static int bd3;
	private static int bd4;
	private static int bd5;
	private static int bd6;
	private static int bd7;
	private static int bd8;
	private static int bd9;
	private static int bd10;
	
	public static int currentImage;
	
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int stack = 0;
	private int fallDiff = 0;
	private int vsp = Main.getBlockVSP();
	private boolean remove = false;
	private int blocksRemoved = 0;
	private int onGroundTimer = 0;
	private int miningTimer = 0;

	public Block(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		blockImg = Main.glTexImageTGAFile(gl, "Sprites/Blocks/block1.tga", spriteSize);
		bd1 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd1.tga", spriteSize);
		bd2 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd2.tga", spriteSize);
		bd3 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd3.tga", spriteSize);
		bd4 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd4.tga", spriteSize);
		bd5 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd5.tga", spriteSize);
		bd6 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd6.tga", spriteSize);
		bd7 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd7.tga", spriteSize);
		bd8 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd8.tga", spriteSize);
		bd9 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd9.tga", spriteSize);
		bd10 = Main.glTexImageTGAFile(gl, "Sprites/Blocks/bd10.tga", spriteSize);
		
		width = height = 64;
		stack = Main.myGrid.getGrid(getX()/64);
		blocksRemoved = Main.myGrid.blocksRemoved(getX()/64) * 64;
		
		currentImage = blockImg;
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
		checkMined();
		checkImage();
		
		System.out.println("HP: " + hp);
		
		
		setImage(currentImage);
		draw(gl);
	}
	
	public void checkMined() {
		if(Main.Dummy_Collision(Main.hero.dummy, this)) {
			if(Main.hero.miningBlock() && Main.hero.getDirection() == Main.hero.getBlockLocaction()) {
				
				miningTimer++;
				
				if(miningTimer % 10 == 0) {
					if(hp == 100)
						hp = 99;
					else if(hp == 99)
						hp -= 9;
					else
						hp -= 10;
					miningTimer = 0;
				}
			}
		} else {
			miningTimer = 0;
			hp = 100;
		}
	}
	
	public void checkImage() {
		switch (hp) {
		case 100:
			currentImage = blockImg;
			break;
		case 99:
			currentImage = bd1;
			break;
		case 90:
			currentImage = bd2;
			break;
		case 80:
			currentImage = bd3;
			break;
		case 70:
			currentImage = bd4;
			break;
		case 60:
			currentImage = bd5;
		case 50:
			currentImage = bd6;
			break;
		case 40:
			currentImage = bd7;
			break;
		case 30:
			currentImage = bd8;
			break;
		case 20:
			currentImage = bd9;
			break;
		case 10:
			currentImage = bd10;
			break;
    	}
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
