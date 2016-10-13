package boxit.client.cat;

//import boxit.Edge;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** 
 * ChainCat sorts the boxes in ConatinerCat's bags more elaborately
 * into chains, connected collections of boxes with 2 or 1 availabe side.*/
abstract public class ChainCat extends ContainerCat
{
    protected final List<ArrayList<Box>> chains = new ArrayList<ArrayList<Box>>(10);
    
    public void sortChains() 
    {
	readBoard();
	chains.clear();
	while ( !nBoxes.isEmpty() || !hBoxes.isEmpty() ) {
	    ArrayList<Box> chain = new ArrayList<Box>();   // for a new chain
	    chains.add(chain);                             // we keep 
	    Box startBox;
	    Compass dir;
	    int count = 0;  // the number of directions to complete
	    if (!nBoxes.isEmpty()) {             // Find the first box of 
		startBox = nBoxes.remove(0); // either a chain
		count = 1;
	    }
	    else {
		startBox = hBoxes.remove(0);  // or a pchain,
		count = 2;
	    }
	    chain.add(startBox);             // and open up chain.
	    while (count > 0) {
		count--;
		Compass fromDir = startBox.sides().get(count);   
		Box nextBox = startBox.follow(fromDir);
		while (hBoxes.contains(nextBox)) { // the chain continues
		    nextBox = hBoxes.remove(hBoxes.indexOf(nextBox));
		    chain.add(nextBox);
		    // so lets proceed
		    fromDir = nextBox.otherSide(opp(fromDir));
		    nextBox = nextBox.follow(fromDir);
		}
	    } 
	}
	readBoard(); // let ContainerCat fill its bags
    }
    public final ArrayList<Box> chainForBox(Box box) 
    {
	Iterator<ArrayList<Box>> it = chains.iterator();
	while (it.hasNext()) {
	    ArrayList<Box> testChain = it.next();
	    if ( testChain.contains(box) )
		return testChain;
	}
	return null;
    }
    public Compass lastEdge(ArrayList<Box> chain)
    {
	if (chain.size() > 0) {
	    Box b = chain.get(0);
	    Compass dir = b.sides().get(0);
	    int pos = 1;
	    while (pos < chain.size()) {
		b = chain.get(pos);
		dir = b.otherSide(opp(dir));
		pos++;
	    }
	    return dir;
	}
	return null;
    }
    public String chainString() 
    {
	String ret = "ChainCat:";
	Iterator<ArrayList<Box>> it = chains.iterator();
	while (it.hasNext()) 
	    ret += "\nchain:" + boxString(it.next());
	return ret;
    }
    public String toString() 
    {
	return chainString() + "\n" + super.toString();
    }
    public static Compass opp(Compass dir) 
    {
	if (dir == Compass.NORTH) 
	    return Compass.SOUTH;
	else if (dir == Compass.SOUTH) 
	    return Compass.NORTH;
	else if (dir == Compass.WEST) 
	    return Compass.EAST;
	else if (dir == Compass.EAST) 
	    return Compass.WEST;
	return null;
    }	    
}
