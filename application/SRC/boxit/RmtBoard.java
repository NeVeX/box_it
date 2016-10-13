package boxit;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * The remote interface for the boards in the {@link boxit.arena.Arena}. 
 * {@link boxit.Player} implementing classes must use this API
 * in order to learn about their game.
 */
public interface RmtBoard extends Remote {

     public int getEdgesLeft() throws RemoteException;

    /**Constant returned by {@link #getOwner}. 
     * @see #getOwner  
     */
    public static final int NONE = -1;
    /**Constant returned by {@link #getOwner}. 
     * @see #getOwner  
     */
    public static final int PLAYER_1 = 0;
    /**Constant returned by {@link #getOwner}. 
     * @see #getOwner  
     */
    public static final int PLAYER_2 = 1;
    /**
     * Returns {@link #NONE}, {@link #PLAYER_1} or {@link #PLAYER_2}. 
     * It is not guarantied that PLAYER_1 goes first.
     */
    public int getOwner(int col, int row) throws RemoteException;
    /**
     * Returns the number of boxes in one row on the board. 
     * @return number of boxes in one row on the board. 
     */
    public int getSizeX() throws RemoteException;
    /**
     * Returns the number of boxes in one column on the board. 
     * @return number of boxes in one column on the board. 
     */
    public int getSizeY() throws RemoteException;
    /** 
     * Returns true when the edge specified by the parameters is selected.
     * @param col column of a box adjacent to the edge
     * @param row row of a box adjacent to the edge
     * @param side the side of the above box; use {@link Edge#NORTH NORTH}, 
     * {@link Edge#EAST EAST}, {@link Edge#SOUTH SOUTH} and {@link Edge#WEST WEST}
     */
    public boolean isSelected(int col, int row, int side) throws RemoteException;
    /*
     * Returns <key>true</key> when the board is full. 
     */
    public boolean isFull() throws RemoteException;
    /** 
     * Returns the score. Use {@link #PLAYER_1} and {@link #PLAYER_2} to 
     * index into the returned array.
     */
    public int[] getScore() throws RemoteException;
}
