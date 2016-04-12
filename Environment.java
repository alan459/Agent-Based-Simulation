import java.util.Random;
import java.util.ArrayList;

public class Environment {
	int numRows;
	int numCols;
	
	int numMacrophages;
	int numBacteria;
	Microbe[][] landscape;
	
	Random random;

	Driver driver;
	
	public Environment(int _numRows,int _numCols,int _numMacrophages,int _numBacteria,Driver _driver) {

		driver = _driver;
		numRows        = _numRows;
		numCols        = _numCols;
		numMacrophages = _numMacrophages;
		numBacteria    = _numBacteria;
		
		random         = new Random();
		
		landscape      = new Microbe[numRows][numCols];
		
	}
	
	public int[] moveMicrobe(Microbe agent, int rowOff, int colOff) {
		int currentRow = agent.getRow();
		int currentCol = agent.getCol();
		landscape[(currentRow + numRows) % numRows][(currentCol + numCols) % numCols] = null;
		
		int newRow = (currentRow + rowOff + numRows) % numRows;
		int newCol = (currentCol + colOff + numCols) % numCols;
		landscape[newRow][newCol] = agent;
		
		return new int[] {newRow, newCol};
	}
	
	// add a microbe to the environment in a random empty space
	public void addMicrobe(Microbe agent) {
		int row;
		int col;
		do {
			row = random.nextInt(numRows);
			col = random.nextInt(numCols);
		} while (landscape[(row+numRows) % numRows][(col+numCols) % numCols] != null);
		
		landscape[(row+numRows) % numRows][(col+numCols) % numCols] = agent;
		agent.setRowCol((row+numRows) % numRows, (col+numCols) % numCols);
	}

	// add a microbe to the environment in a specified space
	// (as a result of a divide)
	public void addMicrobe(Microbe agent, int row, int col) {
		landscape[(row+numRows) % numRows][(col+numCols) % numCols] = agent;
		if (agent instanceof Bacterium)
			numBacteria++;
		else if (agent instanceof Macrophage)
			numMacrophages++;
		driver.addMicrobe(agent);
	}

	public Microbe getContents(int row, int col)
	{
		return landscape[(row+numRows) % numRows][(col+numCols) % numCols];
	}

	public void decrementBacteria()
	{
		numBacteria--;
		
	}
}