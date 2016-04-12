import java.util.ArrayList;

/**
 * This abstact class must be extended by a class that implements the next-event
 * simulation engine for your agent-based simulation.  The primary purpose of
 * this abstract class is (a) to ensure that the appropriate object for drawing
 * to the gui is constructed and (b) to ensure consistency in method names used
 * by the gui.
 */

public abstract class SimulationManager
{
    protected double    time;  // the simulation time clock
    protected AgentGUI  gui;   // reference to gui for drawing

    /**************************************************************************
     * Constructor for an (abstract) SimulationManager.  Primarily this ensures
     * that the gui is set up for drawing, so that an extending class will have
     * a reference via the gui instance variable.
     * @param numCells       number of rows and columns in the environment
     * @param guiCellWidth   width of each cell drawn in the gui
     **************************************************************************/
    public SimulationManager(int numCells, int guiCellWidth)
    {
        this.gui = new AgentGUI(this, numCells, guiCellWidth);
    }

    // ************************************************************************
    // BELOW: abstract signatures for methods an extending class must implement
    // ************************************************************************


    /**************************************************************************
     * Accessor method that returns the number of macrophages still present.
     * @return an integer representing the number of macrophages present
     **************************************************************************/
    public abstract int getNumMacrophages();

    /**************************************************************************
     * Accessor method that returns the number of bacteria still present.
     * @return an integer representing the number of bacteria present
     **************************************************************************/
    public abstract int getNumBacteria();

    /**************************************************************************
     * Accessor method that returns the current time of the simulation clocl.
     * @return a double representing the current time in simulated time
     **************************************************************************/
    public abstract double getTime();

    /**************************************************************************
     * Method that constructs and returns a single list of all agents present.
     * This method is used by the gui drawing routines to update the gui based
     * on the number and positions of agents present.
     *
     * @return an ArrayList<AgentInterface> containing references to all agents
     **************************************************************************/
    public abstract ArrayList<AgentInterface> getListOfAgents();

    /**************************************************************************
     * Method used to run the simulation.  This method should contain the
     * implementation of your next-event simulation engine (while loop handling
     * various event types).
     *
     * @param guiDelay  delay in seconds between redraws of the gui
     **************************************************************************/
    public abstract void run(double guiDelay) throws InterruptedException;
}
