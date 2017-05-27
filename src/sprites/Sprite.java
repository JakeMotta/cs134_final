package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Sprite {
	
	protected int x, y, width, height, image;
	protected String direction, shape;
	protected int speed, vsp, hsp, gravity;
	protected boolean isAlive, shouldMove;
	public boolean isGrounded;
	protected boolean canMoveRight;
	protected boolean canMoveLeft;
	protected boolean canJump;
	
	protected int gravityTimer = 0;
	public Sprite(int myX, int myY, int[] spriteSize, GL2 gl) {
		x = myX;
		y = myY;
		width = spriteSize[0];
		height = spriteSize[1];
		gravity = 64;
		isAlive = true;
		shouldMove = canMoveRight = canMoveLeft = canJump = false;
	}

	public void setX(int a) { x = a; }
	public void setY(int b) { y = b; }
	public void setImage(int i) { image = i; }
	
	public void setSpeed(int a) { speed = a; }
	public void setVsp(int a) { vsp = a; }
	public void setHsp(int b) { hsp = b; }
	public long getVsp() { return vsp; }
	public long getHsp() { return hsp; }
	public long getSpeed() { return speed; }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getImage() { return image; }
	
	public int sX() { return getX() / 64; }
    public int eX() { return getX() + getWidth() / 64; }
    public int sY() { return getY() / 64; }
    public int eY() { return getY() + getHeight() / 64; }
	
	public void moveX(long l) { x += l; }
	public void moveY(long l) { y += l; }
	
	public boolean isAlive() { return isAlive; } 
	
	public String getDirection() { return direction; }
	
	// Quick draw reference
	public void draw(GL2 gl) {
		Main.DrawSprite(gl, getImage(), x, y, width, height, Main.camera);
	}
}
