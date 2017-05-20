package main;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;

import Camera.Camera;
import Camera.Window;
import background.Background;
import background.Lava;
import background.blockGrid;
import sprites.*;
import font.Font;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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
    public static blockGrid myGrid;
    public static Lava lava;
    public static Font font;
    
    public static int worldWidth;
    public static int worldHeight;
    public static int gameTimer = 0;
    public static int gameSpeed = 100;
    public static int blockVSP = 4;
               
    public static void main(String[] args) {
          
		// Game initialization goes here.
    	
    	window = new Window();
    	GL2 gl = window.getGL();
    	    	
    	// The game loop
        long lastFrameNS;
        long curFrameNS = System.nanoTime();
        
        camera = new Camera(window.getWidth(),window.getHeight());
        
        hero = new Hero(100, 100, spriteSize, gl);
        ArrayList<Block> blockArray = new ArrayList<Block>();
        
        background = new Background(spriteSize, gl);
        worldWidth = background.getWorldWidth();
        worldHeight = background.getWorldHeight();
        myGrid = new blockGrid();
        lava = new Lava(spriteSize, gl, 832);
        font = new Font(spriteSize, gl);
         
        // Physics runs at 100fps, or 10ms / physics frame
        int physicsDeltaMs = 10;
        int lastPhysicsFrameMs = 0;
        int nextBlock = 0, randomBlockX = 0, temp = 0, pow = 0;
        int lavaTimer = 0;
        //textSize[0] = spriteSize[0];
        //textSize[1] = spriteSize[1];
        
        // Starting blocks
        for(int i = 1; i <= 3; i++) {
        	for(int j = 0; j < 10; j++) {
		        Block block;
		    	block = new Block(j*64, worldHeight-(64*i+blockVSP), spriteSize, gl);
		    	blockArray.add(block);    	
        	}
        }
        
        while (!shouldExit) {
        	
            System.arraycopy(window.kbState, 0, window.kbPrevState, 0, window.kbState.length);
            lastFrameNS = curFrameNS; 
            curFrameNS = System.nanoTime();
            long deltaTimeMS = (curFrameNS - lastFrameNS) / 1000000;
            
            long bgSpeed = (1 * deltaTimeMS)/4;
            hero.setSpeed(bgSpeed);

            // Actually, this runs the entire OS message pump.
            window.myWindow.display();
            if (!window.myWindow.isVisible()) {
                shouldExit = true;
                break;
            }
                         
            // Game logic goes here.
            if (window.kbState[KeyEvent.VK_ESCAPE]) {
                shouldExit = true;
            }

            if (window.kbState[KeyEvent.VK_UP]) {   
            	hero.keyDown("up");
	        }
	
	        if (window.kbState[KeyEvent.VK_DOWN]) {
	        	hero.keyDown("down");
	        }
	
	        if (window.kbState[KeyEvent.VK_LEFT]) {
	        	hero.keyDown("left");
	        }
	        
	        if (window.kbState[KeyEvent.VK_RIGHT]) {
	        	hero.keyDown("right");
	        }
	         
            window.gl.glClearColor(0, 0, 0, 1);
            window.gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            
            camera.update(hero);
            background.update(gl);
                
            // Add new block 
            if(nextBlock % 100 == 0) {
            	
            	int[] blockHistory = myGrid.getHistory();

            	// Get random X position for block
            	randomBlockX = getRandom(10) * 64;
            	
            	// If two blocks in a row
            	if(blockHistory[0] == randomBlockX)
	            	while(randomBlockX == blockHistory[0])
	            		randomBlockX = getRandom(10) * 64;
            	
            	
            	// Add background alert
            	background.addAlert(randomBlockX/64);

            	Block block;
            	block = new Block(randomBlockX, -128, spriteSize, gl); 
	            blockArray.add(block);    	
	            
	            temp = nextBlock + 25;
	            
	            // Update grid with column block is in
	            myGrid.add(randomBlockX/64);      
            }
            
            if(nextBlock >= temp) {
            	// Remove background alert
	            background.removeAlert(randomBlockX/64);
            }
            
            for(int bA = 0; bA < blockArray.size(); bA++) {
            	
        	    if(blockArray.get(bA).isAlive())
        	    	blockArray.get(bA).update(gl);
        	    else {
        		    //System.out.println("Remove me daddy");
        		    blockArray.remove(bA);
        	    }
        	    
            }
            
            hero.update(gl);
            lava.update(gl, lavaTimer);
             
            /**
            // Physics update
            do {
 	           // 1. Physics movement
 	           // 2. Physics collision detection
 	           // 3. Physics collision resolution
      
         	   //hero.update(gl);

                lastPhysicsFrameMs += physicsDeltaMs;
            } while (lastPhysicsFrameMs + physicsDeltaMs < deltaTimeMS );
            **/

            nextBlock++;
            gameTimer = nextBlock;
            
            drawText(gl, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10, 100, camera, spriteSize);
            
            // Animation speed for the lava
            if(nextBlock % 5 == 0)
	            if(lavaTimer < 12)
	            	lavaTimer++;
	            else
	            	lavaTimer = 0;      
         
            if(gameTimer % 1000 == 0) {
            	pow += 1;
            	//blockVSP = (int) Math.pow(2, pow);
            }
            
            //System.out.println("diff:     " + Main.getGameTimer() / Main.getGameSpeed());
            //System.out.println("falldiff: " + blockVSP * (Main.getGameTimer() / Main.getGameSpeed()));
        }  
    }
    
    public static void drawText(GL2 gl, String text, int x, int y, Camera cam, int[] textSize){
        ArrayList<FontSprite> Text = new ArrayList<>(Font.getTextures(text, x, y, textSize, gl));
        for (int i = 0; i < Text.size(); i++){
            DrawSprite(gl, Text.get(i).getImage(), Text.get(i).getX(), Text.get(i).getY(), Text.get(i).getWidth(), Text.get(i).getHeight(), cam);
        }
    }
    
    public static int getBlockVSP() {
    	return blockVSP;
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
   
    public static boolean AABB_Collision(Sprite a, Sprite b)
    {
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
