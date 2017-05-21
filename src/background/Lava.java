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
	
	private int[][] lavaGrid;

	public Lava(int[] size, GL2 gl, int setY) {

		width = height = 128;
		myY = setY;
		
		lavaGrid = new int[][] {
			{1, 1, 1, 1, 1},	
			{0, 0, 0, 0, 0},	
			{0, 0, 0, 0, 0},	
		};
	}
	
	public void update(GL2 gl, int timer) {
		
		for (int i = 0; i < 3; i++) {
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
}
