package boxit.arena;

import boxit.Edges;
import boxit.Edge;
import boxit.RmtBoard;
import java.util.Observer;
import java.util.Observable;
import java.rmi.RemoteException;

class Board  extends Observable implements RmtBoard{
    private Box box[];
    private int noOfColums, noOfRows, edgesLeft;
    private int[] score = new int[2];
    

    public int getEdgesLeft() throws RemoteException {
        return edgesLeft;
    }



    Board(int noOfColumns, int noOfRows) {
	setSize(noOfColumns, noOfRows);
    }
    // returns false if player must go again, true otherwise 
    public boolean acceptMove(Edge edge, int player) {
	if ( !selectEdge(edge.getCol(), edge.getRow(), edge.getEdge()) ) 
	    return false;   // illegal move, player has to go again
	// Now we know that an edge was added.
	setChanged();
	edgesLeft--;
	boolean ret = true; // Assume no box completed as default. 
	Box b = getBox(edge.getCol(), edge.getRow());
	if ( b.isComplete() ) {
	    b.setOwner(player);
	    score[player]++;
	    ret = false;
	}
	b = getNeighbour(edge.getCol(), edge.getRow(), edge.getEdge());
	if ( b != null && b.isComplete() ) {
            b.setOwner(player);
	    score[player]++;
            ret = false;
	}
	notifyObservers();
	return ret;
    }
    // remote methods needed by players and views
    public boolean isFull() throws RemoteException {
	return (edgesLeft == 0); 
    }
    public int[] getScore() throws RemoteException 
    {
	return score;
    }
    public int getOwner(int col, int row) throws RemoteException {
	return getBox(col, row).getOwner();
    }
    public int getSizeX() throws RemoteException { return noOfColums; }
    public int getSizeY() throws RemoteException { return noOfRows; }
    public boolean isSelected(int col, int row, int side)  throws RemoteException {
	return getBox(col, row).isPresent(side);
    }
    public String toString() {
	String ret = "";
	Box b;
	for (int row = 0; row < noOfRows; row++) {
	    for (int col = 0; col < noOfColums; col++) {
		b = getBox(col, row);
		ret += (b.isPresent(Edge.NORTH)) ? " -" : " .";
	    }
	    ret += "\n";
	    for (int col = 0; col < noOfColums; col++) {
		b = b = getBox(col, row);
		ret += (b.isPresent(Edge.WEST)) ? "|" : ".";
		ret += (b.isComplete()) ? b.getOwner() : " "; 
	    }
	    ret += (getBox(noOfColums - 1, row).isPresent(Edge.EAST)) ? "|" : ".";
	    ret += "\n";
	}
	for (int col = 0; col < noOfColums; col++) {
	    ret += (getBox(col, noOfRows - 1).isPresent(Edge.SOUTH)) ? " -" : " .";
	}
	ret += "\n";
	return ret;
    }
    // private helper methods
    private void setSize(int noOfColumns, int noOfRows) {
	this.noOfColums = noOfColumns;
	this.noOfRows = noOfRows;
	box = new Box[noOfColums * noOfRows];
	edgesLeft = noOfColums * (noOfRows + 1) + noOfRows * (noOfColums + 1);
	init();
    }
    private boolean selectEdge(int col, int row, int side) {
	if ( !getBox(col, row).selectEdge(side) ) {
	    return false;
	}
	Box b = getNeighbour(col, row, side);
	if ( b != null ) {
	    if ( ! (b.selectEdge(Edges.oppositeSide(side))) )
		System.err.println("There is a problem with overlapping edges");
	}
	return true;
    }
    private void init() {
	for (int i = 0; i < box.length; i++) 
	    box[i] = new Box();
    }
    private Box getBox(int x, int y) {
	return box[y * noOfColums + x];
    }
	     
    private boolean isLegal(int x, int y) {
	return (x >= 0 && x < noOfColums && y >= 0 && y < noOfRows);
    }
    private Box getNeighbour(int x, int y, int side) {
	int edge;
	if (side == Edge.NORTH) 
	    y -= 1;
	else if (side == Edge.SOUTH) 
	    y += 1;
	else if (side == Edge.EAST) 
	    x += 1;
	else if (side == Edge.WEST) 
	    x -= 1;
	if (!isLegal(x,y))
	    return null;
	return getBox(x, y);
    }
}
	    

