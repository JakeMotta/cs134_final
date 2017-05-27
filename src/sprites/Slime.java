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
	private int hsp = 2;
	private int hSight = 250;
	private boolean heroFound = false;
	private boolean isGrounded = false;
	private boolean playMovement = false;
	private int gravityInc = 5;
	
	 public Dummy dummy; // Used for player collision and mining collision
	 public Dummy floorDummy; // Used for floor detection
	 public Dummy myDummy; // Used for knowing when a block is on-top of us

	public Slime(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);
		
		width = height = 64;
		
		// Bunch of dummies
		dummy = new Dummy(0,0,spriteSize,gl);
		floorDummy = new Dummy(0,0,spriteSize,gl);
		myDummy = new Dummy(0,0,spriteSize,gl);
	
		currentImage = Main.images.getSlimeWalk(direction, 0);
	}
	
	public void update(GL2 gl) {
		if(hp <= 0) {
			isAlive = false;
		}
		
		floorDummy.update(gl);
		myDummy.update(gl);
		
		checkCenter();
		checkCollision();
		
		if(!isGrounded)
			fall();
		else {
			onGroundTimer++;
			
			checkHero();

			if(heroFound && playMovement) { // Hero within target distance
				// Play walking animation
			    if (walkCounter >= (fps*Main.images.slimeRight.length)-2)
			        walkCounter = -1;
			    else
			        walkCounter++;
			
			    if(walkCounter % fps == 0) {
			    	currentImage = Main.images.getSlimeWalk(direction, walkCounter/fps);
			    }
			}
		}
		//System.out.println("isGrounded: " + isGrounded);
		setImage(currentImage);
		draw(gl);
	}
	
	public void checkHero() {
		// Check if hero is within detection range
		if((getX() + hSight) >= Main.hero.getX() && (getX() - hSight) <= Main.hero.getX()) {
			heroFound = true;
			
			if(getX() == Main.hero.getX()) { // Slime is at the same location
				playMovement = false;
				currentImage = Main.images.getSlimeWalk(direction, 0);
			} else {
				playMovement = true;
				
				if(Main.hero.getX() < getX()) { // Hero left of slime
					direction = "left";
					moveX(-hsp);
				} else { // Hero right of slime
					direction = "right";
					moveX(hsp);
				}
			}
		} else { // Hero not detected
			heroFound = false;
			currentImage = Main.images.getSlimeWalk(direction, 0);
		}
	}
	
	public void gravity() {
		gravityTimer++;
		
		if(gravityTimer % 5 == 0) {
			
			if(gravityInc > -5)
				gravityInc -= 1;
			
			moveY(gravityInc);
			gravityTimer = 0;
		}
	}
	
	public void checkCollision() {			
		floorDummy.setWidth(58);
		floorDummy.setHeight(60);
		floorDummy.setX(getX()+4);
		floorDummy.setY(getY()+4);
		
		for(int i = 0; i < Main.blockArray.size(); i++) {		
			if(Main.Dummy_Collision(floorDummy, Main.blockArray.get(i))) {
				isGrounded = true;
				canJump = true;
				break;
			} else {
				isGrounded = false;
				canJump = false;
			}
		}
	}
	
	public boolean checkCenter() {
		myDummy.setWidth(32);
		myDummy.setHeight(32);
		myDummy.setX(getX()+16);
		myDummy.setY(getY()+16);
		
		for(int i = 0; i < Main.blockArray.size(); i++)
			if(Main.Dummy_Collision(myDummy, Main.blockArray.get(i)))
				return false;
		return true;
	}
	
	public boolean checkLeft() {
		dummy.setWidth(58);
		dummy.setHeight(58);
		dummy.setX(getX()-width+4);
		dummy.setY(getY()+2);
		
		for(int i = 0; i < Main.blockArray.size(); i++)
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				return false;
			}
		return true;
	}
	
	public boolean checkRight() {
		dummy.setWidth(58);
		dummy.setHeight(58);
		dummy.setX(getX()+width+4);
		dummy.setY(getY()+2);
		
		for(int i = 0; i < Main.blockArray.size(); i++)	
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				return false;
			}
		return true;
	}
	
	public boolean checkAbove() {
		
		if(checkCenter()) { // No block on character
			dummy.setWidth(32);
			dummy.setHeight(62);
			dummy.setX(getX()+16);
			dummy.setY(getY()-62);
		} else { // Block is on character
			dummy.setWidth(32);
			dummy.setHeight(32);
			dummy.setX(getX()+16);
			dummy.setY(getY()+16);
		}
		
		for(int i = 0; i < Main.blockArray.size(); i++)	{
			// No block on character
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i)) && checkCenter()) {
				return false;	
			}
			// Block is on character
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i)) && !checkCenter()) {
				return false;	
			}
		}
		return true;
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
