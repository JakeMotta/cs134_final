package background;
import com.jogamp.opengl.GL2;

import main.Main;
import sprites.Sprite;

public class Lava {
	
	private static int lava;
	private static int lavaTop[] = new int[13];
	
	private int x;
    private int y;
    private int width;
    private int height;
    private int image;
    private int myY;
    
	private int startXTile = 0;
    private int startYTile = 0;
    private int endXTile = 0;
    private int endYTile = 0;
	
	private int[][] lavaGrid;

	public Lava(int[] size, GL2 gl, int setY) {
		
		lava = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava.tga", size);
		lavaTop[0] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top1.tga", size);
		lavaTop[1] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top2.tga", size);
		lavaTop[2] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top3.tga", size);
		lavaTop[3] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top4.tga", size);
		lavaTop[4] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top5.tga", size);
		lavaTop[5] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top6.tga", size);
		lavaTop[6] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top7.tga", size);
		lavaTop[7] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top8.tga", size);
		lavaTop[8] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top9.tga", size);
		lavaTop[9] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top10.tga", size);
		lavaTop[10] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top11.tga", size);
		lavaTop[11] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top12.tga", size);
		lavaTop[12] = Main.glTexImageTGAFile(gl, "Sprites/Lava/lava-top13.tga", size);

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
        				image = lava;
        				break;
        			case 1:
        				image = lavaTop[timer];
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
