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
    
	private static int mineLeft[] = new int[3];
	private static int mineRight[] = new int[3];
	private static int mineUp[] = new int[3];
	private static int mineDown[] = new int[3];
	
	private int inventory[] = new int[] {-1, -1, -1};
	private int size[];
	private int defaultSize[];
	
    // Character Specifics
    private int fps = 6;
    
    private GL2 myGL;
    
    // Strings
    private String shape = "rect";
    private String keyDown = null;
    private String blockLocation = "down";
	
	// Booleans
    private boolean attacking = false;
    private boolean isMining = false;
    private boolean ctrlDown = false;
    private boolean inventoryFull = false;
    
    // Counters
    private int walkCounter = 0;
    private int imgCounter = 0;
    private int mineCounter = 0;
    private int jumpCounter = 0;
    private int blocksRemoved = 0;
    private int textTimer = 0;
    private int gravityInc = 20;
    public Dummy dummy; // Used for player collision and mining collision
    public Dummy floorDummy; // Used for floor detection
    public Dummy myDummy; // Used for knowing when a block is on-top of us
    
	public Hero(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		// Walking textures
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
		
		// Mining textures
		mineLeft[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/ml1.tga", spriteSize);
		mineLeft[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/ml2.tga", spriteSize);
		mineLeft[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/ml3.tga", spriteSize);
		mineRight[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/mr1.tga", spriteSize);
		mineRight[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/mr2.tga", spriteSize);
		mineRight[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/mr3.tga", spriteSize);
		mineUp[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/mu1.tga", spriteSize);
		mineUp[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/mu2.tga", spriteSize);
		mineUp[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/mu3.tga", spriteSize);
		mineDown[0] = Main.glTexImageTGAFile(gl, "Sprites/Hero/md1.tga", spriteSize);
		mineDown[1] = Main.glTexImageTGAFile(gl, "Sprites/Hero/md2.tga", spriteSize);
		mineDown[2] = Main.glTexImageTGAFile(gl, "Sprites/Hero/md3.tga", spriteSize);
		
	    vsp = hsp = speed = Main.getBlockVSP();
		direction = "right";
	    width = 64;
	    height = 64;
	    keyDown = null;
	    isGrounded = false;
	    size = defaultSize = spriteSize;
	    	    
	    // Bunch of dummies
	 	dummy = new Dummy(0,0,spriteSize,gl);
	 	floorDummy = new Dummy(0,0,spriteSize,gl);
	 	myDummy = new Dummy(0,0,spriteSize,gl);
	 	
	 	currentImage = walkDown[2];
	}
	
	// Set hero's speed
	public void setSpeed(long bgSpeed){
		speed = vsp;
	}

	@Override
	public void update(GL2 gl) {

		//dummy.update(gl);
		//myDummy.update(gl);
		
		sink();
		checkCollision();
		checkCenter();
		checkMining();
		checkInventory();
		checkPlayerSpeech(gl);
	
		if(isGrounded == false)
			gravity();		
		else {
			gravityInc = 20;
			gravityTimer = 0;
		}
		
		if (shouldMove) {
			
			if(direction == "left")
				currentImage = walkLeft[2];
			
			if(direction == "right")
				currentImage = walkRight[2];
			
			if(direction == "up")
				currentImage = walkUp[2];
			
			if(direction == "down")
				currentImage = walkDown[2];
		}
	    
	    // Reset booleans
	    shouldMove = canMoveLeft = canMoveRight = false;
	    attacking = false;
	    keyDown = null;
	    isMining = false;
    	    	    
	    System.out.println("inventory: " + inventory[0] + ", " + inventory[1] + ", " + inventory[2]);
	    setImage(currentImage);

	    draw(gl);
	}
	
	public void checkPlayerSpeech(GL2 gl) {
		String text = "";
		int textSize = 0;
		
		if(inventoryFull) {
			size[0] = 8;
			size[1] = 13;
			text = "I CANT CARRY ANYMORE!";
			textSize = (text.length()*size[0])/2;
			textTimer++;
		}
		
		if(textTimer < 200)
			Main.drawText(gl, text, getX()-(textSize/2), getY()-32, Main.camera, size, true);
	}
	
	public boolean miningBlock() {
		return isMining;
	}
	
	public void giveInventory(int type) {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == -1) {
				inventory[i] = type;
				break;
			}
		}
	}
	
	public void checkInventory() {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == -1) {
				inventoryFull = false;
				textTimer = 0;
				break;
			}
			else
				inventoryFull = true;
		}
	}
	
	public boolean checkInventoryStatus() {
		return inventoryFull;
	}
		
	public void checkMining() {
		
		if(direction == "left" && isMining && !checkLeft()) {
		}
		
		if(direction == "right" && isMining && !checkRight()) {
		}
		
		if(direction == "up" && isMining && !checkAbove()) {
		}
		
		if(direction == "down" && isMining && !checkBelow()) {
		}
	}
	
	public void checkVSP() {
		vsp = Main.getBlockVSP();
	}
	
	public void sink() {
		if(Main.getGameTimer() % Main.getGameSpeed() == 0) 
			moveY(vsp);	
	}
	
	public void checkCollision() {			
		floorDummy.setWidth(60);
		floorDummy.setHeight(60);
		floorDummy.setX(getX()+2);
		floorDummy.setY(getY()+height-2);
		
		for(int i = 0; i < Main.blockArray.size(); i++) {		
			if(Main.Dummy_Collision(floorDummy, Main.blockArray.get(i))) {
				isGrounded = true;
				canJump = true;
				break;
			} else {
				isGrounded = false;
				canJump = false;
				jumpCounter = 0;
			}
		}
	}
	
	public String getBlockLocaction() {
		return blockLocation;
	}
	
	public void gravity() {
		gravityTimer++;
		
		if(gravityTimer % gravityInc == 0) {
			moveY(64);
			gravityTimer = 0;
			
			if(gravityInc > 6)
				gravityInc -= 4;
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
				blockLocation = "left";
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
				blockLocation = "right";
				return false;
			}
		return true;
	}
	
	public boolean checkAbove() {
		dummy.setWidth(32);
		dummy.setHeight(64);
		dummy.setX(getX()+16);
		dummy.setY(getY()-height);
		
		for(int i = 0; i < Main.blockArray.size(); i++)	
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				blockLocation = "up";
				return false;	
			}
		return true;
	}
	
	public boolean checkBelow() {
		dummy.setWidth(58);
		dummy.setHeight(58);
		dummy.setX(getX()+2);
		dummy.setY(getY()+height-2);
		
		for(int i = 0; i < Main.blockArray.size(); i++)	
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i))) {
				blockLocation = "down";
				return false;	
			}
		return true;
	}
	
	public void ctrlKeyDown(boolean isDown) {
		ctrlDown = isDown;
	}
		
	public void keyDown(String key) {
		keyDown = key;
		
		// Direction keys
		if((keyDown == "up" || keyDown == "down" || keyDown == "left" || keyDown == "right")) {
			direction = keyDown;
			shouldMove = true;
		}
		else
			shouldMove = false;
		
		if(ctrlDown) { // Used to look around, rather than move
			if(keyDown == "left")
				direction = "left";	
			else if(keyDown == "right")
				direction = "right";				
			else if(keyDown == "up") 
				direction = "up";	
			else
				direction = "down";
		} else { // Player to move instead
			if(keyDown == "up" && canJump == true && checkAbove()  && checkCenter()) {
				jumpCounter++;
					
				if(jumpCounter <= 1)
					if(getY() > 0)
						setY(getY()-64);				
			}
			
			if(keyDown == "left" && checkLeft()  && checkCenter())
				if (getX() > 0)
		    		moveX(-64);
				
			if(keyDown == "right" && checkRight()  && checkCenter()) 
				if(getX() < Main.worldWidth-getWidth())
					moveX(64);	
		}
		
		if(keyDown == "space") {
			Block block;
			for(int i = 0; i < inventory.length; i++) {
				if(inventory[i] != -1) {
					int rand = Main.getRandom(1000) * 11;
					
					if(direction == "left" && checkLeft() && getX() > 0) {
						if(inventory[i] == 0)
							block = new Block(getX()-width, getY()-4, defaultSize, 0, rand, myGL); 
						else
							block = new Block(getX()-width, getY()-4, defaultSize, 1, rand, myGL);
						
						Main.blockArray.add(block);
						inventory[i] = -1;
					}
					
					if(direction == "right" && checkRight() && getX() < Main.worldWidth-getWidth()) {
						if(inventory[i] == 0)
							block = new Block(getX()+width, getY()-4, defaultSize, 0, rand, myGL); 
						else
							block = new Block(getX()+width, getY()-4, defaultSize, 1, rand, myGL);
						
						Main.blockArray.add(block);
						inventory[i] = -1;
					}
	
					
				}
			}
		}	

		if(keyDown == "z") {
			isMining = true;
			
			// Play mining animation
			if (mineCounter >= (fps*mineRight.length)-2)
				mineCounter = -1;
			else
				mineCounter++;
			
			if(mineCounter % fps == 0) 
				getMiningImage(direction, mineCounter/fps);
		} else
			isMining = false;
		
		
	}
	
	public static void getMiningImage(String dir, int count) {
    	switch (dir) {
		case "left":
			currentImage = mineLeft[count];
			break;
		case "right":
			currentImage = mineRight[count];
			break;
		case "up":
			currentImage = mineUp[count];
			break;
		case "down":
			currentImage = mineDown[count];
			break;
    	}
    }

	@Override
	public String getShape() {
		// TODO Auto-generated method stub
		return null;
	}    
}
