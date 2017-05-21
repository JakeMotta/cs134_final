package background;
import com.jogamp.opengl.GL2;

import main.Main;
import sprites.Sprite;

public class Background {
	
	// background sprite
	
	private int bg1;
	private int alert;
	
    private int x;
    private int y;
    private int width;
    private int height;
    private int image;
    
    private int startXTile = 0;
    private int startYTile = 0;
    private int endXTile = 0;
    private int endYTile = 0;

    private int worldWidth = 0;
    private int worldHeight = 0;
    
    private int myBackground[][];

	public Background(int[] size, GL2 gl) {

		bg1 = Main.glTexImageTGAFile(gl, "Sprites/Background/bg1.tga", size);
		alert = Main.glTexImageTGAFile(gl, "Sprites/Background/ohno.tga", size);
		
		width = height = 64;
		
		x = Main.camera.getX();
		y = -1 * Main.camera.getY();
		worldWidth = (width*10);
		worldHeight = (height*15);
		
		myBackground = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		};
	}

	public int getWorldWidth() { return worldWidth; }
	public int getWorldHeight() { return worldHeight; }
	
	public void setX(int a) { x = a; }
	public void setY(int b) { y = b; }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public void addAlert(int column) {
		myBackground[1][column] = 1;
	}
	
	public void removeAlert(int column) {
		myBackground[1][column] = 0;
	}
	
	public void update(GL2 gl) {
		
		startXTile = (int) Math.abs(Math.floor((Main.camera.getX())/width));
		startYTile = (int) Math.abs(Math.floor((Main.camera.getY())/height));
		
		for (int i = startYTile; i < Math.min(startYTile+15, 17); i++) {
    		for(int j = 0; j < 10; j++) {

    			switch(myBackground[i][j]) {
        			case 0:
        				image = bg1;
        				break;
        			case 1:
        				image = alert;
        				break;
        			default:
        				System.out.println("tile " + myBackground[i][j] + " not printed");
        				break;
        		}
    			Main.DrawSprite(gl, image, (j * width), (i * height), width, height, Main.camera);
    		}
        }
	}
	
}