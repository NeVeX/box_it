package boxit.arena;

public final class Box {
    private boolean edge[] = new boolean[4];
    private int owner = -1;
    /** Returns true if the side of this box was not 
     * selected before, flase if it was*/
    public boolean selectEdge(int side) {
	if (edge[side])
	    return false;
	edge[side] = true;
	return true;
    }
    /** Returns true if the named side of this box is present. */
    public boolean isPresent(int side) {
	return edge[side];
    }
    /** Returns true of this box has four complete edges. */
    public boolean isComplete() {
	return (edge[0] && edge[1] && edge[2] && edge[3]);
    }
    /** Sets the owner of this box. */
    public void setOwner(int player) {
	if (owner != -1) 
	    System.err.println("BUG: Setting owner of an already owned box, player = " + player); 
	owner = player;
    }
    /** Returns th eowner of this box, see {@link boxit.RmtBoard#PLAYER_1}, {@link boxit.RmtBoard#PLAYER_2}. */
    public int getOwner() { 
	return owner;
    }
}
