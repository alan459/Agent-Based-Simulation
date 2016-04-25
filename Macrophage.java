

public class Macrophage extends Microbe 
{
	private double moveTime;
	private double eatTime;
	
	
	public Macrophage(Environment _world) 
	{
		super(_world);
		moveTime = id;
		eatTime = Double.MAX_VALUE;
	}

	public double getNextEventTime()
	{
		return Math.min(moveTime, eatTime);
	}

	public void executeNextEvent()
	{
		if (moveTime < eatTime)
			move();
		else
			eat();
	}

	public void scheduleNextMove()
	{
		moveTime += 30;
	}
	
	
	public void scheduleNextEat(double currentTime)
	{
		eatTime = currentTime;
	}

	public void move() 
	{
		int iteration = 0;

		int numBacterium = 0;
		int numMacrophages = 0;
		
		// iterate through the surroundings to count microbe types
		for (int rowOff = -1; rowOff <= 1; rowOff++) 
		{
			for (int colOff = -1; colOff <= 1; colOff++)
			{
				surroundings[iteration] = 0;

				// skip iteration of current position
				if (rowOff == 0 && colOff == 0) 
				{
					surroundings[iteration] = MACROPHAGE_PRESENT;
					iteration++;

					continue;
				}

				// surroundings[iteration] = world.getBacterium(position[0] + rowOff, position[1] + colOff); // get what's at current position


				// if the current spot has a macrophage increment macrophage count
				if (world.containsMacrophage(position[0] + rowOff, position[1] + colOff))
				{
					numMacrophages++;
					surroundings[iteration] = MACROPHAGE_PRESENT;
				}

				// if the current spot has a macrophage increment bacterium count
				else if (world.containsBacterium(position[0] + rowOff, position[1] + colOff))
				{
					numBacterium++;
					surroundings[iteration] = BACTERIUM_PRESENT;
				}

				
				iteration++;
			}

		}	// end count of microbe types


		// randomly choose which bacterium to move to and eat
		if (numBacterium > 0)
		{
			int nBacterium = random.nextInt(numBacterium); // randomly choose which bacteria to move to

			int bacteriaPosition;
			for (bacteriaPosition = 0; nBacterium >= 0; bacteriaPosition++) // find the nth bacterium to move to 
			{
				if (surroundings[bacteriaPosition] == BACTERIUM_PRESENT) 
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

			// Eat the bacterium we have found
			scheduleNextEat(moveTime);
			System.out.printf("Macrophage %d moving %d, %d\n", this.id, rowOff, colOff);
			position = world.moveMicrobe(this, rowOff, colOff);



		}

		// No bacteria present so randomly choose which empty cell to move into
		else if (numMacrophages < 8)
		{
			int numEmptySpaces = 8 - numMacrophages;

			// randomly choose which bacteria to move to
			int n = random.nextInt(numEmptySpaces); 

			int emptySpace;
			for (emptySpace = 0; n >= 0; emptySpace++) // find the nth bacterium to move to 
			{
				if (emptySpace == 4) continue;
				if (surroundings[emptySpace] == 0) 
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

	public void eat()
	{
		Bacterium food = world.getBacterium(position[0],position[1]);
		if (food != null) {
			System.out.printf("Macrophage %d eating Microbe %d\n", this.id, food.id);
					
			world.removeBacterium(food);
		}

		scheduleNextEat(Double.MAX_VALUE);

	}

	

}

