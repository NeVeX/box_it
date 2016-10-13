package boxit.client.cat;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import boxit.Edge;
/**
 * HazyCat jumps randomly. */
public class HazyCat extends Cat implements boxit.Player {
    int lastEdge = Edge.NORTH;
    public Edge pickEdge() 
    {
	int col = 0, row = 0;
	try{ 
	    col = (int) (bd.getSizeX() * Math.random());
	    row = (int) (bd.getSizeY() * Math.random());
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	
	lastEdge = LazyCat.nextSide(lastEdge);
	return new Edge(col, row, lastEdge);
    }
}
    
