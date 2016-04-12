import java.util.ArrayList;

/**
 * This class implements the next-event simulation engine for your agent-based
 * simulation.  You may choose to define other helper classes (e.g., Event,
 * EventList, etc.), but the main while loop of your next-event engine should
 * appear here, in the run(...) method.
 */

public class Simulation extends SimulationManager
{
    // you may choose to have two separate lists, or only one list of all
    private ArrayList<Agent> macrophageList;
    private ArrayList<Agent> bacteriaList;

    /**************************************************************************
     * Constructs a Simulation object.  This should just perform initialization
     * and setup.  Later use the object to .run() the simulation.
     *
     * @param numCells       number of rows and columns in the environment
     * @param guiCellWidth   width of each cell drawn in the gui
     * @param numMacrophages number of initial macrophages in the environment
     * @param numBacteria    number of initial bacteria in the environment
     **************************************************************************/
    public Simulation(int numCells,       int guiCellWidth,
                      int numMacrophages, int numBacteria)
    {
        // call the SimulationManager constructor, which itself makes sure to 
        // construct and store an AgentGUI object for drawing
        super(numCells, guiCellWidth);

        time = 0;
        macrophageList = new ArrayList<Agent>();
        bacteriaList   = new ArrayList<Agent>();

        // as a simple example, construct the initial macrophages and
        // bacteria and add them "at random" (not really, here) to the
        // landscape
        int row = 0, col = 0;
        for (int i = 0; i < numMacrophages; i++)
        {
            Agent a = new Agent(Agent.AgentType.MACROPHAGE);
            a.setRowCol(row++, col++);
            macrophageList.add(a);
        }

        for (int i = 0; i < numBacteria; i++)
        {
            Agent a = new Agent(Agent.AgentType.BACTERIUM);
            a.setRowCol(row++, col++);
            bacteriaList.add(a);
        }

    }

    /**************************************************************************
     * Method used to run the simulation.  This method should contain the
     * implementation of your next-event simulation engine (while loop handling
     * various event types).
     *
     * @param guiDelay  delay in seconds between redraws of the gui
     **************************************************************************/
    public void run(double guiDelay) throws InterruptedException
    {  
        // create a simple "simulation" example that just moves all the agents
        // down and to the right...
        int gridSize = this.gui.getGridSize();
        for(int j = 0; j < 30; j++)
        {
            // change the positions of the agents within the agent objects...
            for (int i = 0; i < macrophageList.size(); i++)
            {
                Agent a = macrophageList.get(i);
                a.setRowCol((a.getRow() + 1) % gridSize, 
                            (a.getCol() + 1) % gridSize);
            }
            for (int i = 0; i < bacteriaList.size(); i++)
            {
                Agent a = bacteriaList.get(i);
                a.setRowCol((a.getRow() + 1) % gridSize, 
                            (a.getCol() + 1) % gridSize);
            }
        
            // and then update the gui
            gui.update(guiDelay); 
        }

        // for fun, let's remove one macrophage and two bacteria
        macrophageList.remove(macrophageList.size() / 2);
        bacteriaList.remove(bacteriaList.size() / 2);
        bacteriaList.remove(bacteriaList.size() / 2);

        // and then move the remaining straight up
        for(int j = 0; j < 40; j++)
        {
            for (int i = 0; i < macrophageList.size(); i++)
            {
                Agent a = macrophageList.get(i);
                int newRow = ((a.getRow() - 1) + gridSize) % gridSize;
                a.setRowCol(newRow, a.getCol());
            }
            for (int i = 0; i < bacteriaList.size(); i++)
            {
                Agent a = bacteriaList.get(i);
                int newRow = ((a.getRow() - 1) + gridSize) % gridSize;
                a.setRowCol(newRow, a.getCol());
            }
        
            // remember to update the gui
            gui.update(guiDelay); 
        }
    }

    /**************************************************************************
     * Accessor method that returns the number of macrophages still present.
     * @return an integer representing the number of macrophages present
     **************************************************************************/
    public int getNumMacrophages() { return(macrophageList.size()); }

    /**************************************************************************
     * Accessor method that returns the number of bacteria still present.
     * @return an integer representing the number of bacteria present
     **************************************************************************/
    public int getNumBacteria()    { return(bacteriaList.size()); }

    /**************************************************************************
     * Accessor method that returns the current time of the simulation clocl.
     * @return a double representing the current time in simulated time
     **************************************************************************/
    public double getTime()        { return(time); }

    /**************************************************************************
     * Method that constructs and returns a single list of all agents present.
     * This method is used by the gui drawing routines to update the gui based
     * on the number and positions of agents present.
     *
     * @return an ArrayList<AgentInterface> containing references to all macrophages and bacteria
     **************************************************************************/
    public ArrayList<AgentInterface> getListOfAgents()
    {
        // your implementation may differ depending on one or two lists...
        ArrayList<AgentInterface> returnList = new ArrayList<AgentInterface>();
        for (int i = 0; i < macrophageList.size(); i++) returnList.add( macrophageList.get(i) );
        for (int i = 0; i < bacteriaList.size(); i++)   returnList.add( bacteriaList.get(i) );
        return(returnList);
    }
}
