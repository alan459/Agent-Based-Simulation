

public class Bacterium extends Microbe {
	private double moveTime;
	private double divideTime;

	private double avgDivideTime;
	
	public Bacterium(Environment _world) {
		super(_world);
		moveTime = id;
		divideTime = id + 15;
		
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

			if (!(world.getContents(position[0] + rowOff, position[1] + colOff) instanceof Bacterium))
			{
				// move bacteria to empty space
				System.out.printf("Bacterium %d moving %d, %d\n", this.id, rowOff, colOff);
				
				position = world.moveMicrobe(this, rowOff, colOff);
				break;
			}
			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != randomSpace);
		// if no empty spaces, don't move

		scheduleNextMove();
		System.out.printf("\tNext move: %f, Next Divide: %f\n", moveTime, divideTime);
		
	}

	public void divide()
	{
		System.out.printf("Bacterium %d dividing\n",this.id);

		int randomSpace = random.nextInt(9);

		int colOff;
		int rowOff;

		int currentSpace = randomSpace;
		do {

			colOff = (currentSpace % 3) - 1;
			rowOff = (currentSpace / 3) - 1;

			if (world.getContents(position[0] + rowOff, position[1] + colOff) == null)
			{
				Bacterium offspring = new Bacterium(world);
				System.out.printf("\tcreated bacterium %d\n", offspring.id);
				// add bacteria to empty space
				world.addMicrobe(offspring, position[0] + rowOff, position[1] + colOff);
				break; 
			}

			currentSpace = (currentSpace + 1) % 9;

		} while (currentSpace != randomSpace);


		scheduleNextDivide();
		System.out.printf("\tNext move: %f, Next Divide: %f\n", moveTime, divideTime);
	}


}