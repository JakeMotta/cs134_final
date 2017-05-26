package main;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;

import Camera.Camera;
import Camera.Window;
import background.Background;
import background.Lava;
import sprites.*;
import font.Font;
import sound.ClipPlayer;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.imageio.ImageIO;

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
    public static ClipPlayer clippy = new ClipPlayer();
    
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
    public static String collisionResult = "";
    public static int pressedRight, pressedLeft, pressedUp, pressedSpace;
    public static boolean isGameOver = true;
    public static int playerScoreLast = 0;
    public static int level = 1;
    
    public static void main(String[] args) {
          
		// Game initialization goes here.
    	
    	window = new Window();
    	GL2 gl = window.getGL();
    	    	
    	// The game loop
        long lastFrameNS;
        long curFrameNS = System.nanoTime();
        
        images = new Images(spriteSize,gl);
        background = new Background(spriteSize, gl);
        worldWidth = background.getWorldWidth();
        worldHeight = background.getWorldHeight();
        camera = new Camera(window.getWidth(),window.getHeight());
        hero = new Hero(512, (worldHeight-256)+blockVSP, spriteSize, gl);
        lava = new Lava(spriteSize, gl, worldHeight-160);
        font = new Font(spriteSize, gl);
        slime = new Slime(128, (worldHeight-256), spriteSize, gl);
        
        pressedRight = pressedLeft = pressedUp = pressedSpace = 0;
        lastFrameNS = curFrameNS; 
        curFrameNS = System.nanoTime();
        long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
        
        // Sounds
        Clip blockBreak = null;
        Clip music = null;
        
        try {
			blockBreak = clippy.loadClip("sounds/pop.wav");
			music = clippy.loadClip("sounds/main_music.wav");
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}
 
        // Physics runs at 100fps, or 10ms / physics frame
        int physicsDeltaMs = 10;
        int lastPhysicsFrameMs = 0;
        int nextBlock = 0, randomBlockX = 0, randomBlockType = 0, temp = 0, pow = 0;
        int lavaTimer = 0;
        int spawnRate = 100;
        
        while (!shouldExit) {

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
                    
                    try { // Reset the music
            			music = clippy.loadClip("sounds/main_music.wav");
            		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            			e.printStackTrace();
            		}
                    
                    music.start();
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
	        	    	
	        		    clippy.playClip(blockBreak);
	        	    }
	            }

	            slime.update(gl);
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

            	camera.setY(Main.worldHeight - 960);
                lava.setY(worldHeight-160);
                lavaVSP = 4;
                spawnRate = 100;
                level = 1;
                background.reset();

            	music.stop();
            	hero.reset();
            	playerScore = 0;
            	
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

            	nextBlock = 0;
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
	        	if(isGameOver)
	        		isGameOver = false;
	        }

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
            	
            	System.out.println("Spawn: " + spawnRate);
            }   
        }  
        
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
    	 if(blockArray.get(bA).getType() == 2) // Red block
		    	blockArray.get(bA).explode(bA, gl);
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
