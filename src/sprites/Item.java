package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Item extends Sprite {

	public int currentImage;
	
	private boolean shouldFall = true;
	
	private int vsp = Main.getBlockVSP();
	private boolean remove = false;
	private int onGroundTimer = 0;
	private int type = 0;
	private int myID = 0;
	public boolean isCollected = false;
	private int lifeSpan = 500;
	
	public Dummy floorDummy; // Used for floor detection

	public Item(int myX, int myY, int[] spriteSize, int num, int id, GL2 gl) {
		super(myX, myY, spriteSize, gl);
		
		width = height = 64;
		type = num;
		myID = id;
		
		floorDummy = new Dummy(0,0,spriteSize,gl);
		
		switch(type) {
			case 0: // Apple
				currentImage = Main.images.apple;
				break;
			case 1: // Block
				currentImage = Main.images.blockImg_icon;
				break;
			case 2: // Dark block
				currentImage = Main.images.blockImg2_icon;
				break;
		}		
	}
	
	public void update(GL2 gl) {
		if(isCollected)
			isAlive = false;

		if(shouldFall)
			fall();
		else {
			onGroundTimer++;
		}
		
		checkVSP();
		checkCollected();	
		checkLifeSpan();
		 
		switch(type) {
			case 0: // Apple
				setImage(Main.images.apple);
				break;
			case 1: // Block
				setImage(Main.images.blockImg_icon);
				break;
			case 2: // Dark block
				setImage(Main.images.blockImg2_icon);
				break;
		}
				
		draw(gl);
	}
	
	public void checkLifeSpan() {
		lifeSpan -= 1;
		
		if(lifeSpan <= 0)
			isAlive = false;
	}
	
	public int getLifeSpan() {
		return lifeSpan;
	}
	
	public void checkCollected() {
		if(Main.Dummy_Collision(Main.hero.myDummy, this)) {
			isCollected = true;
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
	
	public int getType() {
		return type;
	}
	
	public int getID() {
		return myID;
	}
	
	public void setShouldFall(boolean fall) {
		shouldFall = fall;
	}

	public void checkBelow() {
		// If block is at the bottom of the screen
		if(getY() >= Main.worldHeight-height)
			shouldFall = false;
				
			floorDummy.setWidth(58);
			floorDummy.setHeight(60);
			floorDummy.setX(getX()+4);
			floorDummy.setY(getY()+4);
			
			for(int i = 0; i < Main.blockArray.size(); i++) {		
				if(Main.Dummy_Collision(floorDummy, Main.blockArray.get(i))) {
					if(Main.blockArray.get(i).getID() != myID) {
						shouldFall = false;
						break;
					} else {
						if(type != 0)
							shouldFall = true;
					}
				}
			}

		if(getY() >= Main.worldHeight)
			remove = true;	
	}
	
	public boolean checkRemoval() {
		return remove;
	}
	
	public int getOnGroundTimer() {
		return onGroundTimer;
	}

}
