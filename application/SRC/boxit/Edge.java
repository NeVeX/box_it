package boxit;
/**
 * Class representing an egde in a Dots and Boxes (BoxIt) game by 
 * box coordinates and an orientation for which it provides named constants. 
 */
public final class Edge implements java.io.Serializable {
    /** 
     *Constant for north edge. */
    public static final int NORTH = 0;
    /** 
     * Constant for east edge. */
    public static final int EAST = 1;
    /** 
     * Constant for south edge. */
    public static final int SOUTH = 2;
    /** 
     * Constant for west edge. */
    public static final int WEST = 3;
    /** 
     * Column of a neighbouring box. */
    private int col;
    /** 
     * Column of a neighbouring box. */
    private int row;
    /** 
     * Type of edge: {@link #NORTH NORTH}, {@link #EAST EAST}, 
     * {@link #SOUTH SOUTH} or {@link #WEST WEST}.  
     */
    private int edge;
    /** 
     * Returns an instance representing the named edge of the box (col, row).
     * @param col column of adjacent box
     * @param row row of adjacent box 
     * @param edge which side of above box; use {@link #NORTH NORTH}, 
     * {@link #EAST EAST}, {@link #SOUTH SOUTH} and {@link #WEST WEST}
     * @see boxit.Edge
     */
    public Edge(int col, int row, int edge) {
	this.col = col;
	this.row = row;
	this.edge = edge;
    }
    /**
     * Returns the column of a neighbouring box.  */
    public int getCol() { return col; }    
    /**
     * Returns the row of a neighbouring box. */
    public int getRow() { return row; }    
    /**
     * Return the side of this edge in relation to 
     * the box ({@link #getCol}, {@link #getRow}). */
    public int getEdge(){ return edge; }
    public String toString() 
    {
	String ret = "(" + col + ", " + row + ", "; 
	if (edge == NORTH) {  ret += "NORTH)"; }
	if (edge == SOUTH) {  ret += "SOUTH)"; }
  	if (edge == EAST) {  ret += "EAST)";  }
	if (edge == WEST)  {  ret += "WEST)"; }
	return ret;
    }
}

