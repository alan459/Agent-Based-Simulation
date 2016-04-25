

public class Bacterium extends Microbe 
{

	private double moveTime;
	private double divideTime;

	private double avgDivideTime;
	
	public Bacterium(Environment _world) 
	{
		super(_world);
		moveTime = id;
		divideTime = id + 15;
		     
		avgDivideTime = 0;
	} 

	public Bacterium(Environment _world, double _currentTime) 
	{
		super(_world);

		moveTime = _currentTime;
		scheduleNextMove(); 	// generate a future move time

		divideTime = _currentTime + 15;
		scheduleNextDivide(); 	// generate a future divide time
		
		avgDivideTime = 0;
	} 

	// Return the time of the next event
	public double getNextEventTime()
	{
		return(Math.min(moveTime,divideTime));
	}

	public void executeNextEvent()
	{
		if (moveTime < divideTime) {
			move();
		} else {
			divide();
		}
	}

	public void scheduleNextMove() 
	{
		moveTime += 30;
	}

	public void scheduleNextDivide() 
	{
		divideTime += 30;
	}

	public void move() 
	{
		int randomSpace = random.nextInt(9);

		int colOff;
		int rowOff;

		int currentSpace = randomSpace;
		do {

			colOff = (currentSpace % 3) - 1;
			rowOff = (currentSpace / 3) - 1;

			// Search for an adjacent space which does not contain another bacterium
			if (!world.containsBacterium(position[0] + rowOff, position[1] + colOff))
			{
				// move bacteria to empty space
				System.out.printf("Bacterium %d moving %d, %d\n", this.id, rowOff, colOff);
				
				position = world.moveMicrobe(this, rowOff, colOff);

				// If you have moved into a macrophage, get eaten
				Macrophage m = world.getMacrophage(position[0],position[1]);
				if (m != null)
				{
					System.out.printf("\tBacterium %d will be eaten by Macrophage %d\n", this.id, m.id);
					m.scheduleNextEat(moveTime);
				}
				break;
			}
			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != randomSpace);
		// if no empty spaces, don't move

		scheduleNextMove();
		System.out.printf("\tNext move: %f, Next Divide: %f\n", moveTime, divideTime);
		
	}

	/*
	*/
	public void divide()
	{
		System.out.printf("Bacterium %d dividing\n",this.id);

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
				Bacterium offspring = new Bacterium(world, divideTime);

				System.out.printf("\tcreated bacterium %d\n", offspring.id);

				// add bacteria to empty space
				world.addMicrobe(offspring, position[0] + rowOff, position[1] + colOff);

				break; // found place to move to so break out of loop
			}

			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != randomSpace);


		scheduleNextDivide();

		System.out.printf("\tNext move: %f, Next Divide: %f\n", moveTime, divideTime);
	}


}