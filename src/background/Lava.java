package background;
import com.jogamp.opengl.GL2;

import main.Main;
import sprites.Sprite;

public class Lava {
	
	private int x;
    private int y;
    private int width;
    private int height;
    private int image;
    private int myY;
    private int vsp;
    
    private int startXTile = 0;
    private int startYTile = 0;
    private int endXTile = 0;
    private int endYTile = 0;
	
	private int[][] lavaGrid;

	public Lava(int[] size, GL2 gl, int setY) {

		width = height = 128;
		myY = setY;
		vsp = Main.getLavaVSP();
		
		lavaGrid = new int[][] {
			{1, 1, 1, 1, 1},	
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0},
		};
	}
	
	public int getY() {
		return myY;
	}
	
	public void setY(int incY) {
		myY = incY;
	}
	
	public void update(GL2 gl, int timer) {
		
		checkVSP();
		rise();
		
		startYTile = (int) Math.abs(Math.floor((Main.camera.getY())/height));
				
		for (int i = 0; i < 15; i++) {
    		for(int j = 0; j < 5; j++) {

    			switch(lavaGrid[i][j]) {
        			case 0:
        				image = Main.images.getLavaImage();
        				break;
        			case 1:
        				image = Main.images.getLavaTopImage(timer);
        				break;
        			default:
        				System.out.println("tile " + lavaGrid[i][j] + " not printed");
        				break;
        		}
    			Main.DrawSprite(gl, image, (j * width), myY + (i * height), width, height, Main.camera);
    		}
        }
	}
	
	public void checkVSP() {
		vsp = Main.getLavaVSP();
	}
	
	public void rise() {
		if(Main.getGameTimer() % Main.getGameSpeed() == 0) 
			myY -= vsp;	
	}
}
