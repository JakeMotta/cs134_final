package sprites;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;

import background.Background;
import main.Main;

public class Hero extends Sprite implements Actor {

	public static int currentImage;
	
	public static int walkLeft[] = new int[3];
	public static int walkRight[] = new int[3];
	public static int walkUp[] = new int[3];
	public static int walkDown[] = new int[3];
    
    // Character Specifics
    private int fps = 6;
    
    // Strings
	String shape = "rect";
	String keyDown = null;
	
	// Booleans
    boolean attacking = false;
    
    // Counters
    int walkCounter = 0;
    int idleCounter = 0;
    int imgCounter = 0;
    public int punchCounter = 0;
    public int jumpCounter = 0;
    public int stack = 0;
    
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
		
	    vsp = hsp = speed = 3;
		direction = "right";
	    width = 64;
	    height = 64;
	    keyDown = null;
	    isGrounded = false;
	    //stack = Main.myGrid.getGrid(3);
	    //System.out.println(stack);
	    
		currentImage = walkDown[2];
	}
	
	// Set hero's speed
	public void setSpeed(long bgSpeed){
		speed = bgSpeed;
	}

	@Override
	public void update(GL2 gl) {
		
		stack = Main.myGrid.getGrid(getX()/64);
		checkCollision();
		
		
		if(isGrounded == false)
			gravity();		
		
		if (shouldMove) {
			// Play walking animation
		    if (walkCounter >= (fps*walkRight.length)-2)
		        walkCounter = -1;
		    else
		        walkCounter++;
		
		    if(walkCounter % fps == 0)
		    	getWalkingImage(direction, walkCounter/fps);
		} /**else {
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
			}
		}**/
	        
	    setImage(currentImage);
	    
	    // Reset booleans
	    shouldMove = false;
	    attacking = false;
	    keyDown = null;

	    draw(gl);
	}
	
	
	public void checkCollision() {	
		
		if(getY() >= Main.worldHeight - (stack * 64) - height)
			isGrounded = true;
		else
			isGrounded = false;
		
		//System.out.println("Grounded: " + isGrounded);
		//System.out.println(stack);
		
		/**
		for(int i = 0; i < Main.blockArray.size(); i++) {			
			if(Main.AABB_Collision(this, Main.blockArray.get(i))) {
				
				if(keyDown == "left") {
					canMoveLeft = false;
				//	System.out.println("Left collision");
				} else
					canMoveLeft = true;
				
				if(keyDown == "right") {
					canMoveRight = false;
				//	System.out.println("right collision");
				} else
					canMoveRight = true;
				
				//if(keyDown == "up") {
				//	System.out.println("up collision");
				//} else
				
				
				//if(keyDown == null)
					//System.out.println("standstill collision");
			} 
				
		
		}
		System.out.println("Grounded: " + isGrounded);
        /**
        	// Offset resolution
        	if(bottomOffsetTimer == 0)
        		bottomBG = getY()+bottomOffset;
        	bottomOffsetTimer++;
        	
        	// What happens when the hero is on the ground
        	if(getY() >= bottomBG) {
        		isGrounded = true;
        		gravityTimer = 0;
        		gravity = 1; // reset gravity back to 1
        		canJump = true;
        	}
        } else { // when hero is not on the ground
        	bottomOffsetTimer = 0;
        	isGrounded = false;
        	gravityTimer++;
        	canJump = false;
        }
        
        // Collidable tile to the right
        if(Main.background.levelCollision[sY()][sX()+1] != 0) {
        	canMoveRight = false;
        	System.out.println("collided right!");
        } else { 
        	canMoveRight = true;
        }
        
     // Collidable tile to the left
        if(Main.background.levelCollision[sY()][sX()] != 0) {
        	canMoveLeft = false;
        	System.out.println("collided left!");
        } else { 
        	canMoveLeft = true;
        }

        /**
        // Collidable tile to the left
        if(Main.background.levelCollision[sY()][sX()-1] != 0) {
        	
        	// Offset resolution
        	if(leftOffsetTimer == 0)
        		leftBG = getX()-leftOffset;
        	leftOffsetTimer++;
        	
        	// What happens the block is to the left
        	if(getX() <= leftBG) {
        		canMoveLeft = false;
        	}
        } else { 
        	canMoveLeft = true;
        	leftOffsetTimer = 0;
        }**/
        
	}
	
	public void gravity() {
		moveY(2);
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
			if(keyDown == "up" && canJump == true) {
				jumpCounter++;
				
				if(jumpCounter <= 1)
					if(getY() > 0)
						setY(getY()-64);				
			} 

			if(keyDown == "left")
				if (getX() > 0)
	    			moveX(-hsp);
			
			if(keyDown == "right") 
				if(getX() < Main.worldWidth-getWidth())
					moveX(hsp);	
			
			if(keyDown == "up") 
				if(getY() > 0)
					moveY(-vsp);	
			
			if(keyDown == "down") 
				if(getY() < Main.worldHeight-getHeight())
					moveY(vsp);	
	}
	
	public static void getWalkingImage(String dir, int count) {
    	switch (dir) {
		case "left":
			currentImage = walkLeft[count];
			break;
		case "right":
			currentImage = walkRight[count];
			break;
		case "up":
			currentImage = walkUp[count];
			break;
		case "down":
			currentImage = walkDown[count];
			break;
    	}
    }    
}
