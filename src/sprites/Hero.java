package sprites;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;

import background.Background;
import main.Main;

public class Hero extends Sprite implements Actor {

	public static int currentImage;
	
	private static int walkLeft[] = new int[3];
	private static int walkRight[] = new int[3];
	private static int walkUp[] = new int[3];
	private static int walkDown[] = new int[3];
    
    // Character Specifics
    private int fps = 6;
    
    // Strings
    private String shape = "rect";
    private String keyDown = null;
	
	// Booleans
    private boolean attacking = false;
    
    // Counters
    private int walkCounter = 0;
    private int idleCounter = 0;
    private int imgCounter = 0;
    private int punchCounter = 0;
    private int jumpCounter = 0;
    private int stack = 0;
    private int stackLeft = 0;
    private int stackRight = 0;
    private int blocksRemoved = 0;
    private int fallDiff = 0;
    Dummy dummy;
    
	public Hero(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		walkLeft[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wl1.tga", spriteSize);
		walkLeft[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wl2.tga", spriteSize);
		walkLeft[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wl3.tga", spriteSize);
		
		walkRight[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wr1.tga", spriteSize);
		walkRight[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wr2.tga", spriteSize);
		walkRight[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wr3.tga", spriteSize);
		
		walkUp[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wu1.tga", spriteSize);
		walkUp[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wu2.tga", spriteSize);
		walkUp[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wu3.tga", spriteSize);
		
		walkDown[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wd1.tga", spriteSize);
		walkDown[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wd2.tga", spriteSize);
		walkDown[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/wd3.tga", spriteSize);
		
	    vsp = hsp = speed = Main.getBlockVSP();
		direction = "right";
	    width = 64;
	    height = 64;
	    keyDown = null;
	    isGrounded = false;
	 	dummy = new Dummy(0,0,spriteSize,gl);
	 	
		setImage(walkDown[2]);
	}
	
	// Set hero's speed
	public void setSpeed(long bgSpeed){
		speed = vsp;
	}

	@Override
	public void update(GL2 gl) {

		dummy.update(gl);
		checkBlocks();
		sink();
		checkCollision();
		
	
		if(isGrounded == false)
			gravity();		
		
		if (shouldMove) {
			
			if(direction == "left")
				setImage(walkLeft[2]);
			
			if(direction == "right")
				setImage(walkRight[2]);
			
			if(direction == "up")
				setImage(walkUp[2]);
			
			if(direction == "down")
				setImage(walkDown[2]);
		
			/**
			// Play walking animation
		    if (walkCounter >= (fps*walkRight.length)-2)
		        walkCounter = -1;
		    else
		        walkCounter++;
		
		    if(walkCounter % fps == 0)
		    	getWalkingImage(direction, walkCounter/fps);
		} else {
			if(!attacking) {
				walkCounter = -1;
				
				// Reset attacks
				punchCounter = -1;
	
				// Play Idle animation
				if(idleCounter >= (fps*idleRight.length)-2)
					idleCounter = -1;
				else
					idleCounter++;
					 
				//if(idleCounter % fps == 0)
			    	//getIdleImage(direction, idleCounter/fps);
			}**/
		}
	        
	    //setImage(currentImage);
	    
	    // Reset booleans
	    shouldMove = canMoveLeft = canMoveRight = false;
	    attacking = false;
	    keyDown = null;

	    draw(gl);
	}
	
	public void checkVSP() {
		vsp = Main.getBlockVSP();
	}
	
	
	public void sink() {
		if(Main.getGameTimer() % Main.getGameSpeed() == 0) 
			moveY(vsp);	
	}
	
	public void checkBlocks() {
		stack = Main.myGrid.getPlayerGrid(getX()/64);
		
		if(getX() >= 64)
			stackLeft = Main.myGrid.getPlayerGrid((getX()/64)-1);
		
		if(getX() < 576)
			stackRight = Main.myGrid.getPlayerGrid((getX()/64)+1);
		
		blocksRemoved = Main.myGrid.blocksRemoved(getX()/64) * 64;
	}
	
	
	public void checkCollision() {	
		
		fallDiff = vsp * (Main.getGameTimer() / Main.getGameSpeed());

		// Player is on ground
		if(getY() >= Main.worldHeight - (stack * 64) - 64 - blocksRemoved) {
			isGrounded = true;
			canJump = true;
			gravityTimer = 0;
		}
		else { // Player not on ground
			isGrounded = false;
			canJump = false;
			jumpCounter = 0;
		}
		
		/**
		dummy.setX(getX());
		dummy.setY(getY()+5);
		
		for(int i = 0; i < Main.blockArray.size(); i++) {		
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				isGrounded = true;
				canJump = true;
				gravityTimer = 0;
			} else {
				isGrounded = false;
				canJump = false;
				jumpCounter = 0;
			}
			System.out.println("can jump: " + isGrounded);
		}**/
	}
	
	public void gravity() {
		gravityTimer++;
		
		if(gravityTimer % 25 == 0) {
			moveY(64);
			gravityTimer = 0;
		}
	}
	
	public boolean checkLeft() {
		dummy.setWidth(64);
		dummy.setHeight(64);
		dummy.setX(getX()-width+5);
		dummy.setY(getY()-5);
		
		for(int i = 0; i < Main.blockArray.size(); i++) {		
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				System.out.println("coliision left!");
				return false;
			}
		}
		return true;
	}
	
	public boolean checkRight() {
		dummy.setWidth(64);
		dummy.setHeight(64);
		dummy.setX(getX()+width-5);
		dummy.setY(getY()-5);
		
		for(int i = 0; i < Main.blockArray.size(); i++) {		
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				System.out.println("coliision right!");
				return false;
			}
		}
		return true;
	}
	
	public boolean checkAbove() {
		dummy.setWidth(32);
		dummy.setHeight(68);
		dummy.setX(getX()+16);
		dummy.setY(getY()-height);
		
		for(int i = 0; i < Main.blockArray.size(); i++) {		
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				System.out.println("coliision above!");
				return false;
			}
		}
		return true;
	}
		
	public void keyDown(String key) {
		keyDown = key;
		
		//System.out.println("shouldMove: " + shouldMove);

		// Direction keys
		if((keyDown == "up" || keyDown == "down" || keyDown == "left" || keyDown == "right")) {
			direction = keyDown;
			shouldMove = true;
		}
		else
			shouldMove = false;
	
		// If movement keys are pressed		
		if(keyDown == "up" && canJump == true && checkAbove()) {
			jumpCounter++;
			//System.out.println("jumpCounter: " + jumpCounter);
				
			if(jumpCounter <= 1)
				if(getY() > 0)
					setY(getY()-64);				
		}

		if(keyDown == "left" && checkLeft())
			if (getX() > 0)
	    		moveX(-64);
			
		if(keyDown == "right" && checkRight()) 
			if(getX() < Main.worldWidth-getWidth())
				moveX(64);	

		//if(keyDown == "down") 
		//	if(getY() < Main.worldHeight-getHeight())
		//		moveY(vsp);	
	}
	
	public static void getWalkingImage(String dir, int count) {
    	switch (dir) {
		case "left":
			currentImage = walkLeft[count];
			break;
		case "right":
			currentImage = walkRight[count];
			break;
    	}
    }

	@Override
	public String getShape() {
		// TODO Auto-generated method stub
		return null;
	}    
}
