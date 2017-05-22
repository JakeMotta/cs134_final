package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Block extends Sprite {

	public int currentImage;
	
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int vsp = Main.getBlockVSP();
	private boolean remove = false;
	private int onGroundTimer = 0;
	private int miningTimer = 0;
	private int type = 0;
	private int myID = 0;
	
	public Dummy floorDummy; // Used for floor detection

	public Block(int myX, int myY, int[] spriteSize, int num, int id, GL2 gl) {
		super(myX, myY, spriteSize, gl);
		
		width = height = 64;
		type = num;
		myID = id;
		
		floorDummy = new Dummy(0,0,spriteSize,gl);
		
		switch(type) {
			case 0: // Regular block
				currentImage = Main.images.getBlockImage(hp);
				break;
			case 1: // Dark block
				currentImage = Main.images.getDarkBlockImage(hp);
				break;
		}
		
	}
	
	public void update(GL2 gl) {
		if(hp <= 0) {
			isAlive = false;
		}
		
		if(shouldFall)
			fall();
		else {
			sink();
			onGroundTimer++;
		}
		
		checkVSP();
		checkMined();		
		 
		switch(type) {
			case 0: // Regular block
				//floorDummy.update(gl);
				setImage(Main.images.getBlockImage(hp));
				break;
			case 1: // Dark block
				//floorDummy.update(gl);
				setImage(Main.images.getDarkBlockImage(hp));
				break;
		}
		
		draw(gl);
	}
	
	public void checkMined() {
		if(Main.Dummy_Collision(Main.hero.dummy, this)) {
			if(Main.hero.miningBlock() && !Main.hero.checkInventoryStatus() && Main.hero.getDirection() == Main.hero.getBlockLocaction()) {
				
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
		if(getY() >= 896)
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
