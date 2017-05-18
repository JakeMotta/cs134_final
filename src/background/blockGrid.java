package background;
import com.jogamp.opengl.GL2;

import main.Main;

public class blockGrid {
			
    private int grid[];

	public blockGrid() {
		grid = new int[] {0,0,0,0,0,0,0,0,0,0};
	}
	
	public void add(int column) {
		grid[column] += 1;
	}
		
	public int getGrid(int column) {
		return grid[column];
	}
	
}