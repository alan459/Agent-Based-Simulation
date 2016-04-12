

public class Macrophage extends Microbe {
	private double nextEventTime;
	
	public Macrophage(Environment _world) {
		super(_world);
		nextEventTime = id;
	}

	public double getNextEventTime()
	{
		return nextEventTime;
	}

	public void executeNextEvent()
	{
		move();
	}

	public void scheduleNextMove()
	{
		nextEventTime += 30;
	}

	public void move() 
	{
		int iteration = 0;

		int numBacterium = 0;
		int numMacrophages = 0;

		for (int rowOff = -1; rowOff <= 1; rowOff++) // iterate through the surroundings to count microbe types
		{
			for (int colOff = -1; colOff <= 1; colOff++)
			{
				if (rowOff == 0 && colOff == 0) // skip iteration of current position
				{
					surroundings[iteration] = this;
					iteration++;
					continue;
				}
					
				surroundings[iteration] = world.getContents(position[0] + rowOff, position[1] + colOff); // get what's at current position

				if (surroundings[iteration] instanceof Bacterium)
				{
					numBacterium++;
				}
				else if (surroundings[iteration] instanceof Macrophage)
				{
					numMacrophages++;
				}

				iteration++;
			}

		}	// end count of microbe types


		if (numBacterium > 0)
		{
			int nBacterium = random.nextInt(numBacterium); // randomly choose which bacteria to move to

			int bacteriaPosition;
			for (bacteriaPosition = 0; nBacterium >= 0; bacteriaPosition++) // find the nth bacterium to move to 
			{
				if (surroundings[bacteriaPosition] instanceof Bacterium) 
				{
					nBacterium--;
				}

			}

			bacteriaPosition--;

			int rowOff = 0;
			int colOff = 0;

			// adjust the row offset
			if (bacteriaPosition < 3) 
				rowOff--;

			else if (bacteriaPosition > 5)
				rowOff++;

			// adjust the column offset
			if (bacteriaPosition % 3 == 0)
					colOff--;
			else if (bacteriaPosition % 3 == 2)
					colOff++;

			//eat(surroundings[bacteriaPosition]); // assuming eat happens immediately
			world.decrementBacteria();
			System.out.printf("Macrophage %d moving %d, %d\n", this.id, rowOff, colOff);
			position = world.moveMicrobe(this, rowOff, colOff);



		}

		else if (numMacrophages < 8)
		{
			int numEmptySpaces = 8 - numMacrophages;

			int n = random.nextInt(numEmptySpaces); // randomly choose which bacteria to move to

			int emptySpace;
			for (emptySpace = 0; n >= 0; emptySpace++) // find the nth bacterium to move to 
			{
				if (emptySpace == 4) continue;
				if (surroundings[emptySpace] == null) 
				{
					n--;
				}

			}

			emptySpace--;

			int rowOff = 0;
			int colOff = 0;

			// adjust the row offset
			if (emptySpace < 3) 
				rowOff--;

			else if (emptySpace > 5)
				rowOff++;

			// adjust the column offset
			if (emptySpace % 3 == 0)
					colOff--;
			else if (emptySpace % 3 == 2)
					colOff++;

			System.out.printf("Microbe %d moving %d, %d\n", this.id, rowOff, colOff);
			position = world.moveMicrobe(this, rowOff, colOff);	
		}

		scheduleNextMove();

	}
}