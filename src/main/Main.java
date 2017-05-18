package main;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;

import Camera.Camera;
import Camera.Window;
import background.Background;
import sprites.*;

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
    private static int grass_top_left_corner;
    
    public static int worldWidth;
    public static int worldHeight;
               
    public static void main(String[] args) {
          
		// Game initialization goes here.
    	
    	window = new Window();
    	GL2 gl = window.getGL();
    	
    	grass_top_left_corner = Main.glTexImageTGAFile(gl, "Sprites/Map/0.tga", spriteSize);
    	
    	// The game loop
        long lastFrameNS;
        long curFrameNS = System.nanoTime();

        camera = new Camera(window.getWidth(),window.getHeight());
        
        hero = new Hero(950, 2050, spriteSize, gl);
        ArrayList<Fire> fireArray = new ArrayList<Fire>();
        
        background = new Background(spriteSize, gl);
        worldWidth = background.getWorldWidth();
        worldHeight = background.getWorldHeight();
         
        // Physics runs at 100fps, or 10ms / physics frame
        int physicsDeltaMs = 10;
        int lastPhysicsFrameMs = 0;
        
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
	        } else {
	        	hero.jumpCounter = 0;
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
	        
	        if (window.kbState[KeyEvent.VK_Z]) {
        		hero.keyDown("z");
        		  				
        		if(hero.punchCounter == 6 || hero.punchCounter == 24) {
        			Fire fire;
        			
        			if(hero.getDirection() == "left") {
    			   		fire = new Fire(hero.getX()-20, hero.getY()+15, spriteSize, gl);
    			   		fire.setDirection("left");
        			}
        			else {
        				fire = new Fire(hero.getX()+60, hero.getY()+15, spriteSize, gl);
        				fire.setDirection("right");
        			}
        			
        			fireArray.add(fire);
        		}
	        }
	         
            window.gl.glClearColor(0, 0, 0, 1);
            window.gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
            
            camera.update(hero);
            background.update(gl);
            
            
             
            // Physics update
            do {
 	           // 1. Physics movement
 	           // 2. Physics collision detection
 	           // 3. Physics collision resolution
            	
            	// Draw any fire sprites created in-game
            	for(int fA = 0; fA < fireArray.size(); fA++) {
            		if(AABB_Collision(fireArray.get(fA), block)) {
            			fireArray.get(fA).collision = true;
            			block.setHP(fireArray.get(fA).damage);
            		}
                
            	    if(fireArray.get(fA).isAlive)
            		    fireArray.get(fA).update(gl);
            	    else {
            		    System.out.println("Remove me daddy");
            		    fireArray.remove(fA);
            	    }
            	    
                }   
         	              
                hero.update(gl);

                lastPhysicsFrameMs += physicsDeltaMs;
            } while (lastPhysicsFrameMs + physicsDeltaMs < deltaTimeMS );

            //System.out.println("cur: " + window.kbState + ", last: " + window.kbPrevState);
        }
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
