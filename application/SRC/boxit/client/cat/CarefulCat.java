package boxit.client.cat;

import boxit.RmtBoard;
import boxit.Edge;
import java.rmi.RemoteException;
import java.util.Iterator;

/** CrefulCat is first greedy and then picks a neutral edge, if 
 * there is one, otherwise it's hazy. */
public final class CarefulCat extends ContainerCat implements boxit.Player
{
    private GreedyCat greedy = new GreedyCat();
    private Cat hazy = new HazyCat();
    public Edge pickEdge() throws RemoteException 
    {
	greedy.setBoard(bd);
	Edge e = greedy.giveEdge();
	if (e != null) {
	    return e;
	}
	readBoard();
	Iterator<Box> it = boxes.iterator();
	while (it.hasNext()) {
	    Box testBox = it.next(); 
	    for (int i = 0; i < testBox.sides().size(); i++)
		if ( isNeutral(testBox, testBox.sides().get(i)) )
		    return testBox.giveEdge(testBox.sides().get(i));
	}
	hazy.setBoard(bd);
	return hazy.pickEdge();
    }
}
