package boxit.client.cat;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import static boxit.Edge.*;
import boxit.Edge;
/** 
 * Traverses rows from left to right from the top,
 * picking the same side and applies {@link #nextSide}
 * before going back to the top left again.
 */
public class LazyCat extends Cat implements boxit.Player {
    private Edge last = new Edge(0, 0, WEST);
    /** Picks the next edge in the sequence describe {@link LazyCat here}. */
    public Edge pickEdge() throws RemoteException
    {
	int c = last.getCol();
	int r = last.getRow();
	int e = last.getEdge();
	c++;
	if (c == bd.getSizeX()) {
	    c = 0;
	    r++;
	}
	if (r == bd.getSizeY()) {
	    r = 0;
	    e = nextSide(e);
	}
	last = new Edge(c, r, e);
	return (Edge) last;
    }
    /** The permutation (NORTH, SOUTH, EAST, WEST). */
    public final static int nextSide(int side) 
    {
	if (side == NORTH)
	    return SOUTH;
	if (side == SOUTH) 
	    return EAST;
	if (side == EAST) 
	    return WEST;
	if (side == WEST) 
	    return NORTH;
	return side;
    }	
}
