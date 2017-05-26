package sprites;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;

import background.Background;
import main.Main;

public class Hero extends Sprite implements Actor {

	public static int currentImage;
	
	private static int walkLeft;
	private static int walkRight;
	private static int walkUp;
	private static int walkDown;
    
	private static int mineLeft[] = new int[3];
	private static int mineRight[] = new int[3];
	private static int mineUp[] = new int[3];
	private static int mineDown[] = new int[3];
	
	private static int deathImg;
	
	private int inventory[] = new int[] {-1, -1, -1};
	private int size[];
	private int defaultSize[];
	
    // Character Specifics
    private int fps = 6;
    
    private GL2 myGL;
    
    // Strings
    private String keyDown = null;
    private String blockLocation = "down";
	
	// Booleans
    private boolean isMining = false;
    private boolean ctrlDown = false;
    private boolean inventoryFull = false;
    
    // Counters
    private int mineCounter = 0;
    private int jumpCounter = 0;
    private int textTimer = 0;
    private int gravityInc = 20;
    private int inventorySpace = 3;
    private int hp = 100;
    private int blockHitTimer = 0;
    private int level = 1;
    private int goal = 480;
    
    public Dummy dummy; // Used for player collision and mining collision
    public Dummy floorDummy; // Used for floor detection
    public Dummy myDummy; // Used for knowing when a block is on-top of us
    
	public Hero(int myX, int myY, int[] spriteSize, GL2 gl) {
		super(myX, myY, spriteSize, gl);

		// Walking textures
		walkLeft = Main.glTexImageTGAFile(gl, "Sprites/Hero/wl3.tga", spriteSize);
		walkRight = Main.glTexImageTGAFile(gl, "Sprites/Hero/wr3.tga", spriteSize);
		walkUp = Main.glTexImageTGAFile(gl, "Sprites/Hero/wu3.tga", spriteSize);
		walkDown = Main.glTexImageTGAFile(gl, "Sprites/Hero/wd3.tga", spriteSize);
		
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
		
		// Death image
		deathImg = Main.glTexImageTGAFile(gl, "Sprites/Hero/death.tga", spriteSize);
		
	    vsp = Main.getBlockVSP();
		direction = "down";
	    width = 64;
	    height = 64;
	    keyDown = null;
	    isGrounded = false;
	    size = defaultSize = spriteSize;
	    	    
	    // Bunch of dummies
	 	dummy = new Dummy(0,0,spriteSize,gl);
	 	floorDummy = new Dummy(0,0,spriteSize,gl);
	 	myDummy = new Dummy(0,0,spriteSize,gl);
	 	
	 	currentImage = walkDown;
	}
	
	public void reset() {
		// Strings
	    keyDown = null;
	    blockLocation = "down";
		
		// Booleans
	    isMining = false;
	    ctrlDown = false;
	    inventoryFull = false;
	    
	    // Counters
	    mineCounter = 0;
	    jumpCounter = 0;
	    textTimer = 0;
	    gravityInc = 20;
	    inventorySpace = 3;
	    hp = 100;
	    blockHitTimer = 0;
	    
	    isAlive = true;
		vsp = Main.getBlockVSP();
		direction = "down";
	    width = 64;
	    height = 64;
	    keyDown = null;
	    isGrounded = false;
	    
	    currentImage = walkDown;	    
	    setX(512);
	    setY(Main.worldHeight-256+vsp);
	    
	    // Set inventory to default 
	    for(int i = 0; i < inventory.length; i++)
	    	inventory[i] = -1;
	}

	@Override
	public void update(GL2 gl) {
		
		if(hp <= 0) {
			isAlive = false;
		}

		//dummy.update(gl);
		//myDummy.update(gl);
		
		checkVSP();
		checkMining();
		checkInventory();
		checkPlayerSpeech(gl);
		checkLava();
		
		blockHit();
	
		if(isGrounded == false)
			gravity();		
		else {
			gravityInc = 20;
			gravityTimer = 0;
		}
		
		if (shouldMove) {
			
			if(direction == "left")
				currentImage = walkLeft;
			
			if(direction == "right")
				currentImage = walkRight;
			
			if(direction == "up")
				currentImage = walkUp;
			
			if(direction == "down")
				currentImage = walkDown;
		}
	    
	    // Reset booleans
	    shouldMove = canMoveLeft = canMoveRight = false;
	    keyDown = null;
	    isMining = false;
    	    	    
	    setImage(currentImage);

	    draw(gl);
	}
	
	public int getGoal() {
		return goal;
	}

	public void setHP(int dmg) {
		hp -= dmg;
	}
	
	public int getHP() {
		return hp;
	}
	
	public void blockHit() {
		if(!checkCenter()) {
			blockHitTimer++;
			
			if(blockHitTimer % 50 == 0)
				hp -= 10;
		} else
			blockHitTimer = 0;
	}
	
	public void checkLava() {
		if(getY() > Main.lava.getY()-10) {
			currentImage = deathImg;
			lavaHit();
			hp -= 20;
		}
	}
	
	public void lavaHit() {
		for(int i = 0; i < 60; i++) {
			if(i % 20 == 0) {
				shouldMove = false;
				moveY(-64);
			}
		}
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
	
	public void giveItem(int type) {
		switch (type) {
			case 0: // Apple recieved
				if(hp + 20 <= 100)
					hp += 20;
				break;
			case 1: // Block recieved
				for(int i = 0; i < inventory.length; i++) {
					if(inventory[i] == -1) {
						inventory[i] = 0;
						inventorySpace--;
						break;
					}
				}
				break;
			case 2: // Dark block recieved
				for(int i = 0; i < inventory.length; i++) {
					if(inventory[i] == -1) {
						inventory[i] = 1;
						inventorySpace--;
						break;
					}
				}
				break;
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
	
	public int getInventorySpace() {
		return inventorySpace;
	}
		
	public void checkMining() {
		if(direction == "left" && isMining && !checkLeft()) {}
		
		if(direction == "right" && isMining && !checkRight()) {}
		
		if(direction == "up" && isMining && !checkAbove()) {}
		
		if(direction == "down" && isMining && !checkBelow()) {}
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
				blockLocation = "up";
				return false;	
			}
			// Block is on character
			if(Main.Dummy_Collision(dummy, Main.blockArray.get(i)) && !checkCenter()) {
				blockLocation = "up";
				return false;	
			}
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
		
		if(keyDown == "space" || keyDown == "down") {
			placeBlockBelow();
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
	
	public boolean playerIsGrounded() {
		return isGrounded;
	}
	
	public void placeBlockBelow() {
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
					inventorySpace++;
				}
				
				if(direction == "right" && checkRight() && getX() < Main.worldWidth-getWidth()) {
					if(inventory[i] == 0)
						block = new Block(getX()+width, getY()-4, defaultSize, 0, rand, myGL); 
					else
						block = new Block(getX()+width, getY()-4, defaultSize, 1, rand, myGL);
					
					Main.blockArray.add(block);
					inventory[i] = -1;
					inventorySpace++;
				}
				
				if(direction == "down" && checkBelow()) {
					if(inventory[i] == 0)
						block = new Block(getX(), getY()+height-4, defaultSize, 0, rand, myGL); 
					else
						block = new Block(getX(), getY()+height-4, defaultSize, 1, rand, myGL);
					
					Main.blockArray.add(block);
					inventory[i] = -1;
					inventorySpace++;
				}
			}
		}
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