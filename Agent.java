public class Agent implements AgentInterface
{
    private int row;
    private int col;
    private AgentType type;

    public Agent(AgentType which)
    {
        row = col = -1;
        type = which;
    }

    public int getRow() { return(row); }
    public int getCol() { return(col); }
    
    public AgentType getType() { return(type); }

    public void setRowCol(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
}
