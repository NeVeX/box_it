package boxit.client.cat;

import boxit.Edge;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/** GreddyCat relies on ContainerCat's capabilities and looks at
 * near boxes if they exist, otherwise picks the first availabe edge.
 */
public final class GreedyCat extends ContainerCat implements boxit.Player {
    private Cat hazy = new HazyCat();

    public Edge pickEdge() throws RemoteException
    {
	Edge e = giveEdge();
	if (e != null)
	    return e;
	if (!nBoxes.isEmpty()) {
	    Box box = nBoxes.get(0);
	    return box.giveEdge(box.sides().remove(0));
	}
	//System.err.println("GreedyCat using HazyCat");
	hazy.setBoard(bd);
	return hazy.pickEdge();
    }	
    /** Returns an edge that completes a box or null.
     * @return an edge that completes a box or null. */
    public final Edge giveEdge() 
    {
	readBoard();
    	if (!nBoxes.isEmpty()) {
	    Box box = nBoxes.get(0);
	    return box.giveEdge(box.sides().remove(0));
	}
	return null;
    }
}
