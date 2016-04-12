public class Tester
{
    public static void main(String[] args) throws InterruptedException
    {
        // construct a simulation object w/ appropriate parameters and then run
        int numCells       = 40;
        int guiCellWidth   = 10;
        int numMacrophages = 3;
        int numBacteria    = 8;

        Simulation s = new Simulation(numCells, guiCellWidth,
                                      numMacrophages, numBacteria);
        double guiDelayInSecs = 0.25;
        s.run(guiDelayInSecs);
    }
}
