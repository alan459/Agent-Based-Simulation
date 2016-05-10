

public class Macrophage extends Microbe 
{
	private double moveTime;
	private double eatTime;
	private double divideTime;
	
	
	public Macrophage(Environment _world) 
	{
		super(_world);
		
		moveTime = 0;
		scheduleNextMove();
		
		divideTime = 0;
		scheduleNextDivide();
		
		eatTime = Double.MAX_VALUE;
	}
	
	public Macrophage(Environment _world, double _currentTime) 
	{
		super(_world);

		moveTime = _currentTime;
		scheduleNextMove(); 	// generate a future move time

		divideTime = _currentTime;
		scheduleNextDivide(); 	// generate a future divide time
		
		eatTime = Double.MAX_VALUE;
	} 

	public double getNextEventTime()
	{
		return Math.min(divideTime, Math.min(moveTime, eatTime));
	}

	public void executeNextEvent()
	{
		if (moveTime < eatTime && moveTime < divideTime) {
			move();
		} else if (eatTime < divideTime) {
			eat();
		} else {
			divide();
		}
	}

	public void scheduleNextMove()
	{
		moveTime += nextExp(Parameters.MACRO_INTER_MOVE);
	}
	
	
	public void scheduleNextEat(double currentTime)
	{
		eatTime = currentTime;
	}
	
	public void scheduleNextDivide()
	{
		divideTime += nextExp(Parameters.MACRO_INTER_DIVIDE);
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

			//System.out.printf("Microbe %d moving %d, %d\n", this.id, rowOff, colOff);
			position = world.moveMicrobe(this, rowOff, colOff);
		}

		scheduleNextMove();

	}

	public void eat()
	{
		Bacterium food = world.getBacterium(position[0],position[1]);
		if (food != null) {
			//System.out.printf("Macrophage %d eating Microbe %d\n", this.id, food.id);
					
			world.removeBacterium(food);
		}

		scheduleNextEat(Double.MAX_VALUE);

	}

	public void divide()
	{
		
		if(numBacteriumInSurroundings() < Parameters.MIN_BACT_TO_DIVIDE)
		{
			//System.out.printf("Macrophage %d skipped dividing not enough bacterium\n",this.id);
			scheduleNextDivide();
			return;
		}
		
		//System.out.printf("Macrophage %d dividing\n",this.id);
		
		int randomSpace = random.nextInt(9);

		int colOff;
		int rowOff;

		int currentSpace = randomSpace;
		do 
		{
			colOff = (currentSpace % 3) - 1;
			rowOff = (currentSpace / 3) - 1;

			if (world.isEmpty(position[0] + rowOff, position[1] + colOff))
			{
				Macrophage offspring = new Macrophage(world, divideTime);

				//System.out.printf("\tcreated macrophage %d\n", offspring.id);

				// add macrophage to empty space
				world.addMicrobe(offspring, position[0] + rowOff, position[1] + colOff);

				break; // found place to move to so break out of loop
			}

			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != randomSpace);


		scheduleNextDivide();

		//System.out.printf("\tNext move: %f, Next Divide: %f\n", moveTime, divideTime);
	}
	
	public int numBacteriumInSurroundings()
	{
		int numBacterium = 0;
		
		int colOff;
		int rowOff;

		int currentSpace = 0;
		do 
		{
			colOff = (currentSpace % 3) - 1;
			rowOff = (currentSpace / 3) - 1;

			if (world.containsBacterium(position[0] + rowOff, position[1] + colOff))
			{
				numBacterium++;
			}

			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != 0);
		
		return numBacterium;
	}

}

