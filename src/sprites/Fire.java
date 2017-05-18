package sprites;

import com.jogamp.opengl.GL2;

import main.Main;

public class Fire extends Sprite implements Actor {
	
	public static int fire_left;
    public static int fire_right;
    public int lifeTimer = 40;
    public int damage = 25;
    
    public boolean isAlive = false;
    public boolean collision = false;

	public Fire(int myX, int myY, int[] size, GL2 gl) {
		super(myX, myY, size, gl);
		
		fire_left =  Main.glTexImageTGAFile(gl, "Sprites/Fire/fire_left.tga", size);
		fire_right =  Main.glTexImageTGAFile(gl, "Sprites/Fire/fire_right.tga", size);
		
		shape = "rect";
		isAlive = true;
		
		speed = 8;
	}
	
	public void setDirection(String dir) {
		direction = dir;
		
		if(direction == "right")
			setImage(fire_right);
		else
			setImage(fire_left);
	}

	@Override
	public void update(GL2 gl) {
		
		lifeTimer--;
		
		//checkCollision();
		
		// Delete object if fire hits and object
		if(collision == true)
			lifeTimer = 0;
		
		if(lifeTimer <= 0)
			isAlive = false;
		
		if(isAlive) {
			if(direction == "right") 
				moveX(speed);
			else
				moveX(-speed);
			
			draw(gl);
		}
	}
	
	/**
	public void checkCollision() {
		if(Main.AABB_Collision(this, Main.block)) {
			collision = true;
			Main.block.setHP(damage);
		}
	}
**/
	@Override
	public String getShape() {
		// TODO Auto-generated method stub
		return null;
	}

}
