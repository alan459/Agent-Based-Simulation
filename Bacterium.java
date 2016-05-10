

public class Bacterium extends Microbe 
{

	private double moveTime;
	private double divideTime;
	private double dieTime;

	private double prevTime;

	private double consumptionRate;
	private double resource;
	
	public Bacterium(Environment _world) 
	{
		super(_world);
		moveTime = 0;
		scheduleNextMove();
		divideTime = 0;
		scheduleNextDivide();
		prevTime = 0;

		resource = random.nextGaussian() * Parameters.INIT_RESOURCE_SD + Parameters.INIT_RESOURCE_MEAN;
		dieTime = Double.MAX_VALUE;
		consumptionRate = random.nextGaussian() * Parameters.CONSUMPTION_RATE_SD + Parameters.CONSUMPTION_RATE_MEAN;
		    
	} 

	public Bacterium(Environment _world, double _currentTime, double _resource, double _consumptionRate) 
	{
		super(_world);

		moveTime = _currentTime;
		scheduleNextMove(); 	// generate a future move time

		divideTime = _currentTime;
		scheduleNextDivide(); 	// generate a future divide time
		prevTime = _currentTime;

		resource = _resource;
		dieTime = Double.MAX_VALUE;
		consumptionRate = _consumptionRate;
		
	} 

	// Return the time of the next event
	public double getNextEventTime()
	{
		return(Math.min(dieTime, Math.min(moveTime,divideTime)));
	}

	public void executeNextEvent()
	{
		prevTime = getNextEventTime();

		if (moveTime < divideTime && moveTime < dieTime) {
			move();
		} else if (divideTime < dieTime) {
			divide();
		} else {
			die();
		}
	}

	public void scheduleNextMove() 
	{
		moveTime += nextExp(Parameters.BACT_INTER_MOVE);
	}

	public void scheduleNextDivide() 
	{
		divideTime += nextExp(Parameters.BACT_INTER_DIVIDE);
	}

	public void scheduleDeath()
	{
		double netResourceLossRate = consumptionRate - world.getRegrowth(position[0], position[1]);
		if (netResourceLossRate > 0) {
			dieTime = resource / netResourceLossRate + getNextEventTime();
			//System.out.printf("Die time %f\n", dieTime);
		} else {
			dieTime = Double.MAX_VALUE;
		}
	}

	public void die()
	{
		// System.out.printf("=============Bacterium %d Died============\n", this.id);
		world.removeBacterium(this);
	}

	public void move() 
	{
		resource += world.harvest(moveTime, position[0], position[1], true);

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
				//System.out.printf("Bacterium %d moving %d, %d\n", this.id, rowOff, colOff);
				
				position = world.moveMicrobe(this, rowOff, colOff);

				// If you have moved into a macrophage, get eaten
				Macrophage m = world.getMacrophage(position[0],position[1]);
				if (m != null)
				{
					//System.out.printf("\tBacterium %d will be eaten by Macrophage %d\n", this.id, m.id);
					m.scheduleNextEat(moveTime);
				}
				else {
					// Only harvest if there is no macrophage in the space
					resource += world.harvest(moveTime, position[0], position[1], false);

					// consume some of your resource
					resource -= (getNextEventTime() - prevTime) * consumptionRate;

					// if you consumed all your resources, die
					if (resource < 0)
					{
						die();

						// get out of method if you die
						return;
					}

					// if you are still alive, schedule a next death time based on current resources and consumuption rate
					scheduleDeath();
				}

				break;
			}
			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != randomSpace);
		// if no empty spaces, don't move

		scheduleNextMove();
		//System.out.printf("\tNext move: %f, Next Divide: %f, Die: %f\n", moveTime, divideTime, dieTime);
		//System.out.printf("\tResource: %f\n", resource);
		
	}

	/*
	*/
	public void divide()
	{
		//System.out.printf("Bacterium %d dividing\n",this.id);

		resource = resource / 2;

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
				Bacterium offspring = new Bacterium(world, divideTime, resource, consumptionRate);

				//System.out.printf("\tcreated bacterium %d\n", offspring.id);

				// add bacteria to empty space
				world.addMicrobe(offspring, position[0] + rowOff, position[1] + colOff);

				break; // found place to move to so break out of loop
			}

			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != randomSpace);


		scheduleDeath();

		scheduleNextDivide();

		//System.out.printf("\tNext move: %f, Next Divide: %f, Die: %f\n", moveTime, divideTime, dieTime);
		//System.out.printf("\tResource: %f\n", resource);
	}


}