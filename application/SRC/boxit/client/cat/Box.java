package boxit.client.cat;

import static boxit.RmtBoard.*;

import boxit.Edge;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
//import java.util.List;

/** Class repesenting a box in a Dots and Boxes (Box-It) game. 
 * It provides some convenience methods. */
public final class Box 
{
    /** Only {@link Compass#NORTH Compass constants} go in here! 
     * Directions are roads to freedom which a box has taken away
     * from, in other words, a direction is in this list if 
     * the corresponding Edge is still up for grabs. */
    private final ArrayList<Compass> sides = new ArrayList<Compass>(4); 
    private int owner = NONE;
    private int row, col;
    /** Constructs a Box with the given coordinates as its 
     *location and all edges selected, as in still available. */
    public Box(int col, int row) 
    {
	this.col = col;
	this.row = row;
	setAll();
    }
    /** Selects all available directions in {@link Compass#DIRECTIONS}. */
    public void setAll() 
    {
	sides.clear();
	Iterator it = Compass.DIRECTIONS.iterator();
	while (it.hasNext()) 
	    sides.add((Compass) it.next());
    }
    /** Returns true when <ul>other</u> and 
     * {@link Box <u>this</u> Box} are at the same location. */
    public boolean equals(Object other) 
    {
	if ( !(other instanceof Box) )
	    return false;
	Box box = (Box) other;
	return (col == box.getCol() && row == box.getRow());
    }
    /** Overridden to fulfil the contract that hashCode() returns the 
     * same value on objcet a and b whenever a.equals(b) returns true.*/
    public int hashCode() 
    {
	int d = Math.max(Math.abs(col), Math.abs(row));
	int m;
	if (col == d)
	    m = 7 * d + row;
	else if (row == d) 
	    m = d - col;
	else if (row == -d)
	    m = 5 * d + col;
	else
	    m = 3 * - row;
	return m + 1 + 4 * d * (d - 1);
    }
    /** Returns the box in the direction <u>dir</u> from <u>this</u> box. 
     * <p><b>Warning:</b>Only the coordinates are reliable. 
     * An infinite board is assumed!</p> */
    public Box follow(Compass dir) 
    {
	if (dir == Compass.NORTH) 
	    return  new Box(col, row - 1);
	if (dir == Compass.SOUTH) 
	    return new Box(col, row + 1);
	if (dir == Compass.EAST) 
	    return new Box(col + 1, row);
	if (dir == Compass.WEST) 
	    return new Box(col - 1, row);
	return null;
    }
    /** Adds the side at the compass direction to this box.
     * Illegal arguments have no effect.
     * @param side*/
    public void remove(Compass side) 
    {
	    sides.remove(side);
    }
    /** Returns an available side of this box other than <u>side</u>.
     * @return an available side of this box other than <u>side</u>. */
    public Compass otherSide(Compass side) 
    {
	if (!sides.remove(side)) 
	    return null;
	sides.add(side);
	return sides.get(0);
    }
    /** Returns the column of this boxes location. 
     * @return the column of this boxes location. */
    public int getCol() 
    {
	return col;
    }
    /** Returns the row of this boxes location. 
     * @return the row of this boxes location. */
    public int getRow() 
    {
	return row;
    }
    /** Returns true if the edge in the direction <u>dir</u> 
     * of this box was not selected before, flase if it was.
     * @return <code>true</code> if the edge in the direction 
     * <u>dir</u> is available. */
    public boolean hasSide (Compass dir) throws IllegalArgumentException {
	if (dir == null) 
	    throw new IllegalArgumentException("need Compass constant");
	return sides.contains(dir);
    }
    /** Returns the number of edges present on this box. 
     * @return the number of edges present on this box. */
    public int edgeCount() 
    {
	return sides.size();
    }
    /** Returns a read-only list of edges present on this box. 
     * @return a read-only list of edges present on this box. */
    public ArrayList<Compass> sides() 
    {
	return sides;
    }
    /** Returns the {@link boxit.Edge Edge} corresponding to the 
     * side of this box in  direction <u>dir</u>.*/
    public Edge giveEdge(Compass dir) 
    {
	if (dir == Compass.NORTH) 
	    return new Edge(col, row, Edge.NORTH);
	if (dir == Compass.SOUTH) 
	    return new Edge(col, row, Edge.SOUTH);
	if (dir == Compass.EAST) 
	    return new Edge(col, row, Edge.EAST);
	if (dir == Compass.WEST) 
	    return new Edge(col, row, Edge.WEST);
	return null;
    }
    public String toString() 
    {
	String ret = "(" + col + ", " + row + ", ";
	for (Iterator it = sides.iterator(); it.hasNext(); )
	    ret += " " + (Compass) it.next();
	return ret + ")";
    }
}
