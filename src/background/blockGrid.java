package background;
import com.jogamp.opengl.GL2;

import main.Main;

public class blockGrid {
			
    private int grid[];
    private int history[];
    private int gridRemoved[];

	public blockGrid() {
		grid = new int[] {3,3,3,3,3,3,3,3,3,3};
		gridRemoved = new int[] {0,0,0,0,0,0,0,0,0,0};
		history = new int[2];
	}
	
	public void add(int column) {
		grid[column] += 1;

		//if(history[0] != )
		history[1] = history[0];
		history[0] = column;
		
	}
	
	public int[] getHistory() {
		return history;
	}
		
	public int getGrid(int column) {
		return grid[column];
	}
	
	public int remove(int column) {
		grid[column] -= 1;
		gridRemoved[column] += 1;
		return column;
	}
	
	public int blocksRemoved(int column) {
		return gridRemoved[column];
	}
	
}