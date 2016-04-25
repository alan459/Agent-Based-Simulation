import java.lang.Integer;
import java.util.*;

public class Driver extends SimulationManager {

	Environment world;
	int numMacrophages;
	int numBacteria;
	ArrayList<Microbe> agents;
	
	
	public Driver(int _numMacrophages, int _numBacteria, int _gridSize) 
    {
        // initialize GUI grid size and gui cell width
		super(_gridSize, 50);
		  
        // set the number of each type of microbe in the environment
		numMacrophages = _numMacrophages;
		numBacteria    = _numBacteria;
		
        // add a list to keep track of all our microbes in our environment
		agents = new ArrayList<Microbe>();
		
        // initialize our environement with the specified parameters and a pointer to the driver
		world = new Environment(_gridSize, _gridSize, numMacrophages, numBacteria, this);
		
		// add the specified number of bacterium to our world
		Bacterium nextBacterium; 
		for (int i = 0; i < numBacteria; i++) 
        {
			nextBacterium = new Bacterium(world);    // give our bacteria a pointer to the world
			agents.add(nextBacterium);               // add the bacteria to the list in our driver 
		}
		
        // add the specified number of macrophages to our world
		Macrophage nextMacrophage;
		for (int i = 0; i < numMacrophages; i++) 
        {
			nextMacrophage = new Macrophage(world);    // give our macrophage a pointer to the world
			agents.add(nextMacrophage);                // add the macrophage to the list in our driver
		}
		
		// add the microbes the our enviroment
		for (Microbe agent : agents) 
        {
			world.addMicrobe(agent);
		}
		
        // initialize timer
		time = 0;
		
		// Initialize GUI
		this.gui = new AgentGUI(this, _gridSize, 50);
	}
	

    /**************************************************************************
     * Accessor method that returns the number of macrophages still present.
     * @return an integer representing the number of macrophages present
     **************************************************************************/
    public int getNumMacrophages() {
    	return numMacrophages;
    }

    /**************************************************************************
     * Accessor method that returns the number of bacteria still present.
     * @return an integer representing the number of bacteria present
     **************************************************************************/
    public int getNumBacteria() {
    	return numBacteria;
    }

    /**************************************************************************
     * Accessor method that returns the current time of the simulation clocl.
     * @return a double representing the current time in simulated time
     **************************************************************************/
    public double getTime() {
    	return time;
    }

    /**************************************************************************
     * Method that constructs and returns a single list of all agents present.
     * This method is used by the gui drawing routines to update the gui based
     * on the number and positions of agents present.
     *
     * @return an ArrayList<AgentInterface> containing references to all agents
     **************************************************************************/
    public ArrayList<AgentInterface> getListOfAgents() {
    	ArrayList<AgentInterface> retval = new ArrayList<AgentInterface>();
    	for (int i = 0; i < agents.size(); i++) {
    		retval.add(agents.get(i));
    	}
    	//for (Microbe m : agents) {
		//	retval.add(m);    	
    	//}
    	return retval;
    }

    public void addBacterium(Microbe agent)
    {
        agents.add(agent);
        Collections.sort(agents);
        numBacteria++;
    }

    public void removeBacterium(Microbe agent)
    {
        if (agents.remove(agent)) {
        	numBacteria--;
        }
        //Collections.sort(agents);
    }
    
    public void addMacrophage(Microbe agent)
    {
    	agents.add(agent);
        Collections.sort(agents);
        numMacrophages++;
    }
    
    public void removeMacrophage(Microbe agent)
    {
    	if (agents.remove(agent)) {
        	numMacrophages--;
        }
    }

    /**************************************************************************
     * Method used to run the simulation.  This method should contain the
     * implementation of your next-event simulation engine (while loop handling
     * various event types).
     *
     * @param guiDelay  delay in seconds between redraws of the gui
     **************************************************************************/
    public void run(double guiDelay) throws InterruptedException {
    	while(true) {
    		Collections.sort(agents);
            time = (agents.get(0)).getNextEventTime();
            System.out.printf("t = %f\n", time);
    		(agents.get(0)).executeNextEvent();
    		gui.update(guiDelay);
    	
    	}
    }
    
    public static void main(String[] args) {

    	if (args.length < 3)
    	{
    		System.out.println("Usage: Java Driver numRows numMacrophages numBacteria");
            System.exit(1);
    	}
    	int numRowCols = Integer.parseInt(args[0]); // grid size
    	int numMacrophages = Integer.parseInt(args[1]); // numMacrophages
    	int numBacteria = Integer.parseInt(args[2]); // numBacteria
    	Driver d = new Driver(numMacrophages, numBacteria, numRowCols);
    	
    	try {
    		d.run(.1);
    	} catch (InterruptedException ex) {
    		
    	}
    }

}