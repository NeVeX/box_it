package boxit;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This remote interface must be implemented by clases that want play
 * Dots and Boxes (BoxIt) in the {@link boxit.arena.Arena}. All you have 
 * do to is code an engine that implements this interface or ...
 * @see boxit.client.cat.Cat Cat for a convenience implementation. 
 */
public interface Player extends Remote { 
    /** 
     * Called once at the start of a game. 
     * @param b the board to be played on.
     */ 
    public void setBoard(RmtBoard b) throws RemoteException;
    /**
     * Called each time the board has changed. 
     */
    public void update() throws RemoteException;
    /** 
     * Called when this player is to pick an edge.
     * @see boxit.Edge
     */
    public Edge pickEdge() throws RemoteException;
}
