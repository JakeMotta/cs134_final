package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Slime extends Sprite {

	public int currentImage;
	
	private boolean shouldFall = true;
	
	private int hp = 100;
	private int vsp = Main.getBlockVSP();
	private boolean remove = false;
	private int onGroundTimer = 0;
	private int miningTimer = 0;
	private int type = 0;
	private int myID = 0;
	private int walkCounter = 0;
	private int fps = 6;
	private String direction = "right";
	
	public Dummy floorDummy; // Used for floor detection

	public Slime(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);
		
		width = height = 64;
		
		floorDummy = new Dummy(0,0,spriteSize,gl);
	
		currentImage = Main.images.getSlimeRightImage(0);
	}
	
	public void update(GL2 gl) {
		if(hp <= 0) {
			isAlive = false;
		}
		
		if(shouldFall)
			fall();
		else {
			onGroundTimer++;
			
			// Play walking animation
		    if (walkCounter >= (fps*Main.images.slimeRight.length)-2)
		        walkCounter = -1;
		    else
		        walkCounter++;
		
		    if(walkCounter % fps == 0) {
		    	if(direction == "right")
		    		Main.images.getSlimeRightImage(walkCounter/fps);
		    	else
		    		Main.images.getSlimeLeftImage(walkCounter/fps);
		    }
		}
		
		walkCounter++;
		checkBelow();

		setImage(currentImage);
		
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
