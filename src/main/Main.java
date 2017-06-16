package main;

import com.jogamp.opengl.*;

import Camera.Camera;
import Camera.Window;
import background.Background;
import background.Lava;
import sprites.*;
import font.Font;
import sound.ClipPlayer;

import com.jogamp.newt.event.KeyEvent;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main {
	
    // Set this to true to make the game loop exit.
    private static boolean shouldExit;
    
    // Size of the sprite.
    public static int[] spriteSize = new int[2];
    
    public static Window window;
    public static Camera camera;
    public static Hero hero;
    public static Background background;
    public static Lava lava;
    public static Font font;
    public static Images images;
    public static Block block;
    public static Item item;
    public static Slime slime;
    public static Dummy boomDummy;
    public static ClipPlayer clippy = new ClipPlayer();
    public static Splashscreen splashscreen;
    
    public static int worldWidth;
    public static int worldHeight;
    public static int gameTimer = 0;
    public static int gameSpeed = 100;
    
    public static int blockVSP = 4;
    public static int lavaVSP = 4;
    public static int playerScore = 0;
    public static String intToString = "";
    public static ArrayList<Block> blockArray = new ArrayList<Block>();
    public static ArrayList<Item> itemArray = new ArrayList<Item>();
    public static ArrayList<Slime> slimeArray = new ArrayList<Slime>();
    public static String collisionResult = "";
    public static int pressedRight, pressedLeft, pressedUp, pressedSpace, volumeUp, volumeDown, noVolume;
    public static boolean isMuted = false;
    public static boolean isGameOver = true;
    public static int playerScoreLast = 0;
    public static int level = 1;
    
    public static float volumeLevel = 0, lastVolume = 0;
    public static FloatControl gainControl; 
    
    // Sounds
    public static Clip blockBreak = null;
    public static Clip music = null;
    public static Clip startMusic = null;
    public static Clip explosionSound = null;
    public static Clip fireballSound = null;
    public static Clip blockThrow = null;
    public static Clip hit1 = null;
    public static Clip hit2 = null;
    public static Clip hit3 = null;
    public static Clip hit4 = null;
    public static Clip eatSound = null;
    
    
    public static void main(String[] args) {
          
		// Game initialization goes here.
    	
    	window = new Window();
    	GL2 gl = window.getGL();
    	    	
    	// The game loop
        long lastFrameNS;
        long curFrameNS = System.nanoTime();
        
        splashscreen = new Splashscreen(spriteSize, gl);
        images = new Images(spriteSize,gl);
        background = new Background(spriteSize, gl);
        worldWidth = background.getWorldWidth();
        worldHeight = background.getWorldHeight();
        camera = new Camera(window.getWidth(),window.getHeight());
        hero = new Hero(512, (worldHeight-256)+blockVSP, spriteSize, gl);
        lava = new Lava(spriteSize, gl, worldHeight-160);
        font = new Font(spriteSize, gl);
        boomDummy = new Dummy(0,0, spriteSize, gl);
        boomDummy.setWidth(80);
        boomDummy.setHeight(80);
        
        slimeArray.add(slime = new Slime(320, (worldHeight-512), spriteSize, gl));
        
        pressedRight = pressedLeft = pressedUp = pressedSpace = volumeUp = volumeDown = noVolume = 0;
        lastFrameNS = curFrameNS; 
        curFrameNS = System.nanoTime();
        long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
        
        try {
			blockBreak = clippy.loadClip("sounds/pop.wav");
			music = clippy.loadClip("sounds/main_music.wav");
			startMusic = clippy.loadClip("sounds/startMusic.wav");
			explosionSound = clippy.loadClip("sounds/explosion.wav");
			fireballSound = clippy.loadClip("sounds/fireball.wav");
			blockThrow = clippy.loadClip("sounds/throw.wav");
			hit1 = clippy.loadClip("sounds/hit1.wav");
			hit2 = clippy.loadClip("sounds/hit2.wav");
			hit3 = clippy.loadClip("sounds/hit3.wav");
			hit4 = clippy.loadClip("sounds/hit4.wav");
			eatSound = clippy.loadClip("sounds/eat.wav");
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}

        // Physics runs at 100fps, or 10ms / physics frame
        int physicsDeltaMs = 10;
        int lastPhysicsFrameMs = 0;
        int nextBlock = 0, randomBlockX = 0, randomBlockType = 0, temp = 0;
        int lavaTimer = 0;
        int spawnRate = 100;
        int gameLoop = 0;
        
        volumeLevel = -15.0f;
        changeVolume(volumeLevel);
        
        while (!shouldExit) {
        	
        	System.out.println("Gameloop: " + gameLoop);

            System.arraycopy(window.kbState, 0, window.kbPrevState, 0, window.kbState.length);

            // Actually, this runs the entire OS message pump.
            window.myWindow.display();
            if (!window.myWindow.isVisible()) {
                shouldExit = true;
                break;
            }
            
            if(!hero.isAlive())
            	isGameOver = true;
                      
            window.gl.glClearColor(0, 0, 0, 1);
            window.gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            
            // ---------------------- UPDATES -----------------------
            
            if(!isGameOver) {
            	
            	// Run once per game
            	if(nextBlock == 1) {
            		// Starting blocks
                    for(int i = 1; i <= 3; i++) {
                    	for(int j = 0; j < 10; j++) {
            		    	block = new Block(j*64, worldHeight-(64*i), spriteSize, 0, (i+j)*7, gl);
            		    	blockArray.add(block);    	
                    	}
                    }
                    
                    startMusic.stop();
                    music.loop(Clip.LOOP_CONTINUOUSLY);
            	}
            	            	
	            camera.update(hero);
	            background.update(gl);
	            
	            
	            // Add new block 
	            if(nextBlock % spawnRate == 0) {
	            	
	            	// Get random X position for block
	            	randomBlockX = getRandom(10) * 64;
	            	randomBlockType = getRandom(100);
	            	int blockID = (nextBlock/10);
	
	            	// Add background alert
	            	background.addAlert(randomBlockX/64);
	            	            	
	            	if(randomBlockType < 60) // Regular block
	            		block = new Block(randomBlockX, camera.getY()-128, spriteSize, 0, blockID, gl);  
	            	else if(randomBlockType >= 60 && randomBlockType < 90)  // Dark block
	            		block = new Block(randomBlockX, camera.getY()-128, spriteSize, 1, blockID, gl); 
	            	else // Red block
	            		block = new Block(randomBlockX, camera.getY()-128, spriteSize, 2, blockID, gl); 
	            	
	            	blockArray.add(block); 
		            
		            temp = nextBlock + 25;
	            }
	            
	            if(nextBlock >= temp) {
	            	// Remove background alert
		            background.removeAlert(randomBlockX/64);
	            }
	            
	            // Update items
	            for(int iA = 0; iA < itemArray.size(); iA++) {
	        	    if(itemArray.get(iA).isAlive()) { 
	        	    	if(!itemArray.get(iA).checkRemoval()) // Item is alive
	        	    		itemArray.get(iA).update(gl);
	        	    	else // Item is below screen and needs to be removed
	        	    		itemArray.remove(iA);
	        	    }
	        	    else { // Item is dead (from player)
	        	    	if(itemArray.get(iA).getLifeSpan() > 0) // The player picked the item up
	        	    		hero.giveItem(itemArray.get(iA).getType()); // Add to player's inventory
	        	    	itemArray.remove(iA);
	        	    }
	            }
	            
	            // Update blocks
	            for(int bA = 0; bA < blockArray.size(); bA++) {
	        	    if(blockArray.get(bA).isAlive()) { 
	        	    	if(!blockArray.get(bA).checkRemoval()) // Block is alive
	        	    		blockArray.get(bA).update(gl);
	        	    	else // Block is below screen and needs to be removed
	        	    		blockArray.remove(bA);
	        	    }
	        	    else { // Block is dead (from player)
	        	    	if(blockArray.get(bA).getType() != 2) 
	        	    		dropItem(bA, gl);   	
	        	    	else
	        	    		explosion(bA, gl);
	        	    }
	            }
	            
	        	// Update slimes
	            for(int sA = 0; sA < slimeArray.size(); sA++) {
	        	    if(slimeArray.get(sA).isAlive())
	        	    		slimeArray.get(sA).update(gl);
	        	    else { // Block is dead (from player)
	        	    	slimeArray.remove(sA);
	        	    	
	        		    //clippy.playClip(blockBreak);
	        	    }
	            }

	            hero.update(gl);
	            lava.update(gl, lavaTimer);

	     	    intToString = String.valueOf(playerScoreLast);
	            drawText(gl, "SCORE:" + intToString, 10, camera.getY()+25, camera, spriteSize, false);
	            drawText(gl, "CAN CARRY:" + hero.getInventorySpace(), 10, camera.getY()+100, camera, spriteSize, false);
	            drawText(gl, "HEALTH:" + hero.getHP(), 10, camera.getY()+75, camera, spriteSize, false);       

	            for(int i = 0; i < 14; i++) {
	            	intToString = String.valueOf((camera.getY()/64) - ((worldHeight/64)-i-1));
	            	drawText(gl, intToString, 575, (camera.getY()+67) + (i*64), camera, spriteSize, false);
	            }
            } else { 

            	if(gameLoop >= 1) { // GAME OVER
            		camera.setY(Main.worldHeight - 960);
                    lava.setY(worldHeight-160);
                    lavaVSP = 4;
                    spawnRate = 100;
                    level = 1;
                    background.reset();

                	music.stop();
                	hero.reset();
                	playerScore = 0;
                	nextBlock = 0;  
                	
                	for(int i = 0; i < blockArray.size(); i++)
                		blockArray.remove(i);
                	
                	for(int i = 0; i < itemArray.size(); i++)
                		itemArray.remove(i);
                	
                	int[] mySize = new int[2];
                	mySize[0] = 56;
                	mySize[1] = 76;
                	drawText(gl, "GAME OVER", 68, camera.getY()+300, camera, mySize, true);
                	
                	drawText(gl, "PRESS ENTER TO RETRY", 200, camera.getY()+400, camera, mySize, false);
                	
                	intToString = String.valueOf(playerScoreLast);
                	mySize[0] = 28;
                	mySize[1] = 38;
                	
                	if(playerScoreLast < 10)
                		drawText(gl, "SCORE:" + intToString, 222, camera.getY()+500, camera, mySize, true);
                	else if(playerScoreLast > 0 && playerScoreLast < 100)
                		drawText(gl, "SCORE:" + intToString, 208, camera.getY()+500, camera, mySize, true);
                	else
                		drawText(gl, "SCORE:" + intToString, 194, camera.getY()+500, camera, mySize, true);     	
            	} else { // GAME START
            		splashscreen.update(gl);
            		
            		camera.setY(Main.worldHeight - 960);
                    lava.setY(worldHeight-160);
                    lavaVSP = 4;
                    spawnRate = 100;
                    level = 1;
                    background.reset();

                	music.stop();
                	hero.reset();
                	playerScore = 0;
                	nextBlock = 0;
                	
                	for(int i = 0; i < blockArray.size(); i++)
                		blockArray.remove(i);
                	
                	for(int i = 0; i < itemArray.size(); i++)
                		itemArray.remove(i);
                	
                	int[] mySize = new int[2];
                	mySize[0] = 56;
                	mySize[1] = 76;
                	
                	drawText(gl, "PRESS ENTER TO RETRY", 200, camera.getY()+750, camera, mySize, false);
                	startMusic.loop(Clip.LOOP_CONTINUOUSLY);
            	}
            }
            	         
            // // ---------------------- PHYSICS UPDATE -----------------------
            do {
            	
            	hero.checkCollision();
        		hero.checkCenter();
          
        		for(int iA = 0; iA < itemArray.size(); iA++)
        			itemArray.get(iA).checkBelow();      	 
                
        		for(int bA = 0; bA < blockArray.size(); bA++)
        			blockArray.get(bA).checkBelow();    

                lastPhysicsFrameMs += physicsDeltaMs;
            } while (lastPhysicsFrameMs + physicsDeltaMs < deltaTimeMS );
                    
            // ---------------------- GAME UPDATES -----------------------
            if (window.kbState[KeyEvent.VK_ESCAPE]) {
                shouldExit = true;
            }

            
            if (window.kbState[KeyEvent.VK_UP]) {   
            	pressedUp++;
            	
            	if(pressedUp == 1)
            		hero.keyDown("up");
	        } else
	        	pressedUp = 0;
			
	        if (window.kbState[KeyEvent.VK_DOWN]) {
	        	hero.keyDown("down");
	        }     
	
	        if (window.kbState[KeyEvent.VK_LEFT]) {
	        	pressedLeft++;
	        	
	        	if(pressedLeft == 1)
	        		hero.keyDown("left");
	        } else
	        	pressedLeft = 0;
	        
	        if (window.kbState[KeyEvent.VK_RIGHT]) {
	        	pressedRight++;
	        	
	        	if(pressedRight == 1)
	        		hero.keyDown("right");
	        } else
	        	pressedRight = 0;
	        
	        if (window.kbState[KeyEvent.VK_Z]) {
	        	hero.keyDown("z");
	        }
	        
	        if (window.kbState[KeyEvent.VK_SPACE]) {
	        	pressedSpace++;
	        	
	        	if(pressedSpace == 1)
	        		hero.keyDown("space");
	        } else
	        	pressedSpace = 0;
	        
	        if (window.kbState[KeyEvent.VK_CONTROL]) {
	        	hero.ctrlKeyDown(true);
	        } else
	        	hero.ctrlKeyDown(false);
	        
	        if (window.kbState[KeyEvent.VK_ENTER]) {
	        	
	        	gameLoop++;
	        	
	        	if(isGameOver)
	        		isGameOver = false;
	        }
	        
	        // Add volume
	        if (window.kbState[KeyEvent.VK_1]) {
	        	volumeUp++;	
	        	
	        	if(volumeLevel < -6.0 && volumeUp == 1 && !isMuted) {
	        		volumeLevel += 5.0;
	        		changeVolume(volumeLevel);
	        	}
	        } else
	        	volumeUp = 0;
	        
	        // Subtract volume
	        if (window.kbState[KeyEvent.VK_2]) {
	        	volumeDown++;
	        	
	        	if(volumeLevel > -74.0 && volumeDown == 1 && !isMuted) {
	        		volumeLevel -= 5.0;
	        		changeVolume(volumeLevel);
	        	}
	        } else
	        	volumeDown = 0;
	        
	        // Mute volume
	        if (window.kbState[KeyEvent.VK_3]) {
	        	noVolume++;
	        	
	        	if(noVolume == 1)
	        		if(!isMuted) {
	        			isMuted = true;
	        			lastVolume = volumeLevel;
	        			volumeLevel = gainControl.getMinimum();
	        			changeVolume(volumeLevel);
	        		}
	        		else {
	        			isMuted = false;
	        			volumeLevel = lastVolume;
	        			changeVolume(volumeLevel);
	        		}  	
	        } else
	        	noVolume = 0;
	        
            nextBlock++;
            gameTimer = nextBlock;
                        
            // Animation speed for the lava
            if(nextBlock % 5 == 0)
	            if(lavaTimer < 12)
	            	lavaTimer++;
	            else
	            	lavaTimer = 0;      
            
            if(playerScore > playerScoreLast)
            	playerScoreLast = playerScore;
            
            playerScore = (-1) * ((hero.getY()/ 64) - 155);
            
            if(gameTimer % 1000 == 0) {
            	lavaVSP += 1;
            	level += 1;
            	
            	if(spawnRate > 50)
            		spawnRate -= level*2;
            }   
        }  
    }
    
    // Change volume levels
    public static void changeVolume(float volume) { 	
        gainControl = (FloatControl) blockBreak.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) startMusic.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) explosionSound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) fireballSound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) blockThrow.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) hit1.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) hit2.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) hit3.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) hit4.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
        gainControl = (FloatControl) eatSound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }
    
    public static void drawText(GL2 gl, String text, int x, int y, Camera cam, int[] textSize, boolean newSize){
    	
    	ArrayList<FontSprite> Text;
    	
    	if(!newSize) {
    		textSize[0] = 12;
    		textSize[1] = 17;
    	} 
    	
    	Text = new ArrayList<>(Font.getTextures(text, x, y, textSize, gl));
    	
        for (int i = 0; i < Text.size(); i++){
            DrawSprite(gl, Text.get(i).getImage(), Text.get(i).getX(), Text.get(i).getY(), Text.get(i).getWidth(), Text.get(i).getHeight(), cam);
        }
    }
    
    public static void explosion(int bA, GL2 gl) {
		boomDummy.setX(blockArray.get(bA).getX()-8);
		boomDummy.setY(blockArray.get(bA).getY()+8);
		
		for(int i = 0; i < blockArray.size(); i++)
	    	if(Dummy_Collision(boomDummy, blockArray.get(i)))
	    		blockArray.get(i).setAlive(false);
		
		if(Dummy_Collision(boomDummy, hero)) {
    		hero.damage(20);
    	}
		
		clippy.playClip(explosionSound);
		blockArray.remove(bA);
    }

    public static void dropItem(int bA, GL2 gl) {
    	if(blockArray.get(bA).getID() % 7 == 0) { // Add random item drop
	    	item = new Item(blockArray.get(bA).getX(), blockArray.get(bA).getY(), spriteSize, 0, blockArray.get(bA).getID(), gl);
	    	itemArray.add(item);
	    } else {
		    if(blockArray.get(bA).getType() == 0) { // Regular block
		    	item = new Item(blockArray.get(bA).getX(), blockArray.get(bA).getY(), spriteSize, 1, blockArray.get(bA).getID(), gl);
		    	itemArray.add(item);
		    }
		    if(blockArray.get(bA).getType() == 1) { // Dark block
		    	item = new Item(blockArray.get(bA).getX(), blockArray.get(bA).getY(), spriteSize, 2, blockArray.get(bA).getID(), gl);
		    	itemArray.add(item);
		    }		   
	    }
    	
    	clippy.playClip(blockBreak);
    	blockArray.remove(bA);
    }
    
    public static int getBlockVSP() {
    	return blockVSP;
    }
    
    public static int getLavaVSP() {
		return lavaVSP;
	}
    
    public static int getGameTimer() {
    	return gameTimer;
    }
    
    public static int getGameSpeed() {
    	return gameSpeed;
    }
    
    public static int getRandom(int max) {
    	int randomNum = (int)(Math.random() * max);
		return randomNum;
    }
        
    public static boolean[] getKBState() {
		return window.kbState;
    }
    
    public static boolean[] getKBPrevState() {
		return window.kbPrevState;
    }
    
    public static boolean AABB_BG(Sprite a, int y, int x)
    {
	     // box1 to the right
    	 if (a.sX() == x) {
	     return false;
	     }
    	 // box1 to the left
	     if (a.eX() == x) {
	     return false;
	     }
	     // box1 below
	     if (a.sY() == y) {
	     return false;
	     }
	     // box1 above
	     if (a.eY() == y) {
	     return false;
	     }
	     return true;
    }
    
    static boolean AABBIntersect(Camera camera2, Sprite box2)
    {
	     // box1 to the right
	     if (camera2.getX() > box2.getX() + box2.getWidth()) {
	     return false;
	     }
	     // box1 to the left
	     if (camera2.getX() + camera2.getWidth() < box2.getX()) {
	     return false;
	     }
	     // box1 below
	     if (camera2.getY() > box2.getY() + box2.getHeight()) {
	     return false;
	     }
	     // box1 above
	     if (camera2.getY() + camera2.getHeight() < box2.getY()) {
	     return false;
	     }
	     return true;
    }
    
    public static boolean AABB_Collision(Sprite a, Sprite b) {
	     // box1 to the right
	     if (a.getX() > b.getX() + b.getWidth()) {
	     return false;
	     }
	     // box1 to the left
	     if (a.getX() + a.getWidth() < b.getX()) {
	     return false;
	     }
	     // box1 below
	     if (a.getY() > b.getY() + b.getHeight()) {
	     return false;
	     }
	     // box1 above
	     if (a.getY() + a.getHeight() < b.getY()) {
	     return false;
	     }
	     
	     return true;
    }
    
    public static boolean Dummy_Collision(Dummy a, Sprite b) {
	     // box1 to the right
	     if (a.getX() > b.getX() + b.getWidth()) {
	     return false;
	     }
	     // box1 to the left
	     if (a.getX() + a.getWidth() < b.getX()) {
	     return false;
	     }
	     // box1 below
	     if (a.getY() > b.getY() + b.getHeight()) {
	     return false;
	     }
	     // box1 above
	     if (a.getY() + a.getHeight() < b.getY()) {
	     return false;
	     }
	     
	     return true;
   }
    
    public static void DrawSprite(GL2 gl, int tex, int x, int y, int w, int h, Camera camera) {
        glDrawSprite(gl, tex, x - camera.getX(), y - camera.getY(), w, h);
    }

    public static void glDrawSprite(GL2 gl, int tex, int x, int y, int w, int h) {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glColor3ub((byte)-1, (byte)-1, (byte)-1);
            gl.glTexCoord2f(0, 1);
            gl.glVertex2i(x, y);
            gl.glTexCoord2f(1, 1);
            gl.glVertex2i(x + w, y);
            gl.glTexCoord2f(1, 0);
            gl.glVertex2i(x + w, y + h);
            gl.glTexCoord2f(0, 0);
            gl.glVertex2i(x, y + h);
        }
        gl.glEnd();
    }
    
    public static int glTexImageTGAFile(GL2 gl, String filename, int[] out_size) {
        final int BPP = 4;

        DataInputStream file = null;
        try {
            // Open the file.
            file = new DataInputStream(new FileInputStream(filename));
        } catch (FileNotFoundException ex) {
            System.err.format("File: %s -- Could not open for reading.", filename);
            return 0;
        }

        try {
            // Skip first two bytes of data we don't need.
            file.skipBytes(2);

            // Read in the image type.  For our purposes the image type
            // should be either a 2 or a 3.
            int imageTypeCode = file.readByte();
            if (imageTypeCode != 2 && imageTypeCode != 3) {
                file.close();
                System.err.format("File: %s -- Unsupported TGA type: %d", filename, imageTypeCode);
                return 0;
            }

            // Skip 9 bytes of data we don't need.
            file.skipBytes(9);

            int imageWidth = Short.reverseBytes(file.readShort());
            int imageHeight = Short.reverseBytes(file.readShort());
            int bitCount = file.readByte();
            file.skipBytes(1);

            // Allocate space for the image data and read it in.
            byte[] bytes = new byte[imageWidth * imageHeight * BPP];

            // Read in data.
            if (bitCount == 32) {
                for (int it = 0; it < imageWidth * imageHeight; ++it) {
                    bytes[it * BPP + 0] = file.readByte();
                    bytes[it * BPP + 1] = file.readByte();
                    bytes[it * BPP + 2] = file.readByte();
                    bytes[it * BPP + 3] = file.readByte();
                }
            } else {
                for (int it = 0; it < imageWidth * imageHeight; ++it) {
                    bytes[it * BPP + 0] = file.readByte();
                    bytes[it * BPP + 1] = file.readByte();
                    bytes[it * BPP + 2] = file.readByte();
                    bytes[it * BPP + 3] = -1;
                }
            }

            file.close();

            // Load into OpenGL
            int[] texArray = new int[1];
            gl.glGenTextures(1, texArray, 0);
            int tex = texArray[0];
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
            gl.glTexImage2D(
                    GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0,
                    GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

            out_size[0] = imageWidth;
            out_size[1] = imageHeight;
            return tex;
        }
        catch (IOException ex) {
            System.err.format("File: %s -- Unexpected end of file.", filename);
            return 0;
        }
    }
}
