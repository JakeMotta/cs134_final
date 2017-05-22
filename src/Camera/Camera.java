package Camera;
import main.Main;
import sprites.Hero;

public class Camera {
	
	int x = 0;
	int y = 0;
	int w = 0;
	int h = 0;
	
	public Camera(int width, int height) {
		w = width;
		h = height;
		
		int asdf = Main.worldHeight - 960;
		//System.out.println(asdf);
		setY(Main.worldHeight - 960);
	}
	
	public void update(Hero hero) {
				
		if(hero.getY() <= getY() + Main.hero.getGoal())
			setY(getY()-128);
		if(hero.getY() > getY() + Main.hero.getGoal())
			setY(getY()+128);
				
		if(getY() > Main.worldHeight-getHeight()-1)
        	setY(Main.worldHeight-getHeight());
        if(getY() < 0)
        	setY(0);
        if(getX() > Main.worldWidth-getWidth()-1)
        	setX(Main.worldWidth-getWidth());
        if(getX() < 0)
        	setX(0);
	}

	public void setX(int a) { x = a; }
	public void setY(int b) { y = b; }
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return w; }
	public int getHeight() { return h; }
	
	public void moveX(long l) { x += l; }
	public void moveY(long l) { y += l; }

}
