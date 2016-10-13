package boxit.client.cat;

import boxit.Edge;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** 
 * ContainerCat uses a new kind of {@link Compass} and {@link Box} which
 * it sorts into bags of ({@link #owned owned}, {@link #nBoxes near boxes}, 
 * {@link #hBoxes half boxes}, {@link #boxes (other) boxes}. It can also 
 * print their contents nicely.
 */
abstract public class ContainerCat extends Cat
{
    protected int cols, rows;
    protected final List<Compass> sides = Compass.DIRECTIONS;
    /** Contains all boxes with at least 3 available edges. */ 
    protected final ArrayList<Box>  boxes = new ArrayList<Box>();
    /** Contains all boxes with only one available edge. */
    protected final ArrayList<Box>  nBoxes = new ArrayList<Box>();
    /** Contains all boxes with exactly 2 available edge. */
    protected final ArrayList<Box>  hBoxes = new ArrayList<Box>();
    /** Contains all boxes owned by either player. */
    protected final ArrayList<Box> owned = new ArrayList<Box>();

    /** This just reselects all sides of a box and removes the 
     * ones which are already taken on the cat's {@link #bd board}. */
    public final void updateBox(Box box) throws RemoteException
    {
	box.setAll();
	int col = box.getCol();
	int row = box.getRow();
	if (bd.isSelected(col, row, Edge.NORTH)) 
	    box.remove(Compass.NORTH);
	if (bd.isSelected(col, row, Edge.EAST)) 
	    box.remove(Compass.EAST);
	if (bd.isSelected(col, row, Edge.SOUTH)) 
	    box.remove(Compass.SOUTH);
	if (bd.isSelected(col, row, Edge.WEST)) 
	    box.remove(Compass.WEST);
    }
    /** Reads a {@link boxit.RmtBoard} and sorts 
     * boxes into bags (see {@link ContainerCat}). */
    public final void readBoard() 
    {
    	boxes.clear();
	owned.clear(); //should not change much??
	hBoxes.clear();
	nBoxes.clear();
	try {
	    cols = bd.getSizeX();    // 
	    rows = bd.getSizeY();
	    boxes.ensureCapacity(rows * cols);
	    for (int row = 0; row < rows; row++) {
		for (int col = 0; col < cols; col++) {
		    Box box = new Box(col, row); //are new boxes all time good?
		    updateBox(box);
		    switch (box.edgeCount()) {
		    case 4:
		    case 3:
			boxes.add(box);
			continue;
		    case 2:
			hBoxes.add(box);
			break;
		    case 1:
			nBoxes.add(box);
			break;
		    case 0:
			owned.add(box);
			continue;
		    default:
			break;
		    }
		}
	    }
	}
	catch (RemoteException re) {
	    System.err.println("ChainCat: problem with board" + re.getMessage());
	    re.printStackTrace();
	}
    }
    /** Returns <code>true</code> if the specified 
     * edge does not complete a box.
     *<code>true</code> if the specified edge does not complete a box. */
    public final boolean isNeutral(Box box, Compass dir) 
    {
	Box other = box.follow(dir);
	int col = other.getCol(), row = other.getRow();
	if (col >= 0 && col < cols && row >= 0 && row < rows)
	    return (boxes.contains(other) && boxes.contains(box));
	return boxes.contains(box);

    }
    /** Returns a String telling what ContainerCat has in its bags.
     * @return a String telling what ContainerCat has in its bags.
     */
    public String toString() 
    {
	String ret = "ContainerCat: owned: " + boxString(owned) + "\n";
	ret += nearBoxString() + "\n";
	ret += halfBoxString() + "\n";
	ret += boxString();
	return ret;
    }
    public final String boxString() 
    {
	return "boxes: " + boxString(boxes);
    }
    /** Returns a String representing a collection of boxes in
     * the order as given by an {@link Iterator}. Override it if you like. */
    public String boxString(Collection<Box> l) 
    {
	String ret = "";
	Iterator<Box> it = l.iterator();
	while (it.hasNext()) {
	    ret += "\n->" + (it.next());
	}
	return ret;
    }
    /***/
    protected final String halfBoxString()
    {
	return "half boxes:" + boxString(hBoxes);
    }
    /***/
    protected final String nearBoxString() 
    {
	return  "near boxes:" + boxString(nBoxes);
    }
}
