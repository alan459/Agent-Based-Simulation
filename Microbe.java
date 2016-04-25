import java.lang.Math;
import java.util.Random;
import java.util.*;
import java.lang.Double;

public abstract class Microbe implements AgentInterface, Comparable<Microbe>,
														 Comparator<Microbe> {

	protected static int numMicrobes = 0;

	int id;	

	Environment world;
	int[] position;
	int[] surroundings;
	Random random;

	int BACTERIUM_PRESENT = 1;
	int MACROPHAGE_PRESENT = 3;

	
	public Microbe(Environment _world) {
		world = _world;
		id = ++numMicrobes;
		
		random = new Random();
		
		position = new int[] {-1, -1};
		surroundings = new int[9];
	}
	
	public int getID() {
		return id;
	}

	public int[] getPosition()
	{
		return position;
	}
	
	public int getRow()
	{
		return position[0];
	}

	public int getCol()
	{
		return position[1];
	}

	public void setRowCol(int row, int col)
	{
		position[0] = row;
		position[1] = col;

	}

	public AgentType getType() 
	{
		if (this instanceof Macrophage) {
			return AgentType.MACROPHAGE;
		} else {
			return AgentType.BACTERIUM;
		}
	}
	
	public int compareTo(Microbe other) {
		double t = this.getNextEventTime() - other.getNextEventTime();
		if (t > 0) return 1;
		if (t == 0) return 0;
		return -1;
	}
	
	public int compare(Microbe one, Microbe two) {
		double t = one.getNextEventTime() - two.getNextEventTime();
		if (t > 0) return 1;
		if (t == 0) return 0;
		return -1;
	}
	
	// Return the time of the next event
	public abstract double getNextEventTime();

	public abstract void executeNextEvent();



}


