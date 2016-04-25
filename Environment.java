import java.util.Random;
import java.util.ArrayList;

public class Environment {
	int numRows;
	int numCols;
	
	int numMacrophages;
	int numBacteria;
	Cell[][] landscape;
	
	Random random;

	Driver driver;
	
	public Environment(int _numRows,int _numCols,int _numMacrophages,int _numBacteria, Driver _driver) {

		driver = _driver;
		numRows        = _numRows;
		numCols        = _numCols;
		numMacrophages = _numMacrophages;
		numBacteria    = _numBacteria;
		
		random         = new Random();
		
		landscape      = new Cell[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				landscape[i][j] = new Cell();
			}
		}
		
	}
	
	public int[] moveMicrobe(Microbe agent, int rowOff, int colOff) {
		int currentRow = ( agent.getRow() + numRows ) % numRows;
		int currentCol = ( agent.getCol() + numCols ) % numCols;
		landscape[currentRow][currentCol].removeMicrobe(agent);
		
		int newRow = (currentRow + rowOff + numRows) % numRows;
		int newCol = (currentCol + colOff + numCols) % numCols;
		landscape[newRow][newCol].setMicrobe(agent);
		
		return new int[] {newRow, newCol};
	}
	
	// add a microbe to the environment in a random empty space
	public void addMicrobe(Microbe agent) {
		int row;
		int col;
		do {
			row = random.nextInt(numRows);
			col = random.nextInt(numCols);
		} while (!isEmpty((row+numRows) % numRows,(col+numCols) % numCols));
		
		landscape[(row+numRows) % numRows][(col+numCols) % numCols].setMicrobe(agent);
		agent.setRowCol((row+numRows) % numRows, (col+numCols) % numCols);
	}

	// add a microbe to the environment in a specified space
	// (as a result of a divide)
	public void addMicrobe(Microbe agent, int row, int col) {
		landscape[(row+numRows) % numRows][(col+numCols) % numCols].setMicrobe(agent);
		agent.setRowCol((row+numRows) % numRows,(col+numCols) % numCols);
		if (agent instanceof Bacterium) {
			numBacteria++;
			driver.addBacterium(agent);
		}
		else if (agent instanceof Macrophage)
			numMacrophages++;
		
	}

	public void removeBacterium(Bacterium agent)
	{
		landscape[(agent.getRow()+numRows) % numRows][(agent.getCol() + numCols) % numCols].removeMicrobe(agent);
		numBacteria--;
		driver.removeBacterium(agent);
	}


	public Bacterium getBacterium(int row, int col) 
	{
		return landscape[(row+numRows) % numRows][(col+numCols) % numCols].getBacterium();
	}

	public Macrophage getMacrophage(int row, int col) 
	{
		return landscape[(row+numRows) % numRows][(col+numCols) % numCols].getMacrophage();
	}

	public boolean containsBacterium(int row, int col)
	{
		Bacterium contents = landscape[(row+numRows) % numRows][(col+numCols) % numCols].getBacterium();
		return (contents != null);
	}

	public boolean containsMacrophage(int row, int col)
	{
		Macrophage contents = landscape[(row+numRows) % numRows][(col+numCols) % numCols].getMacrophage();
		return (contents != null);
	}

	public boolean isEmpty(int row, int col)
	{
		return ((!containsMacrophage(row,col)) && (!containsBacterium(row,col)));
	}

	
	// Inner class containing the contents of a landscape location.
	// May contain a bacterium, a macrophage, or both.
	private class Cell
	{
		Bacterium bacterium;
		Macrophage macrophage;
		double resource;
		double regrowthRate;
		double lastHarvestTime;

		public Cell() {
			bacterium = null;
			macrophage = null;
			resource = 0;
			lastHarvestTime = 0;
		}

		public Cell(Bacterium b) {
			bacterium = b;
			macrophage = null;
			resource = 0;
			lastHarvestTime = 0;
		}

		public Cell(Macrophage m) {
			bacterium = null;
			macrophage = m;
			resource = 0;
			lastHarvestTime = 0;
		}

		public Bacterium getBacterium() {
			return bacterium;
		}

		public Macrophage getMacrophage() {
			return macrophage;
		}

		public void setMicrobe(Microbe m) {
			if (m instanceof Bacterium) {
				bacterium = (Bacterium)m;
			} else {
				macrophage = (Macrophage)m;
			}
		}

		public void removeMicrobe(Microbe m) {
			if (m instanceof Bacterium) {
				bacterium = null;
			} else {
				macrophage = null;
			}
		}

		public void setBacterum(Bacterium b) {
			bacterium = b;
		}

		public void setMacrophage(Macrophage m) {
			macrophage = m;
		}
		
		public double harvestResource(double currentTime) {
			double r = resource + regrowthRate * (currentTime - lastHarvestTime);
			resource = 0;
			lastHarvestTime = currentTime;
			
			return r;
		}
	}
}