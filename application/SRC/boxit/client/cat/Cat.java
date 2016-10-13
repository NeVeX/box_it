package boxit.client.cat;

import boxit.Edge;
import boxit.RmtBoard;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
/**
 * Convenience implementation of {@link boxit.Player} but still abstract. 
 * The remote method {@link #pickEdge} is the only abstract method of 
 * this class. A {@link Cat} plays on a {@link #bd RmtBoard}.
 */
public abstract class Cat implements boxit.Player {
    /**
     * {@link boxit.RmtBoard} on which cat plays.
     */
    protected RmtBoard bd;
    /**
     * This implementation of {@link boxit.Player#setBoard} assigns the 
     * parameter to the field {@link #bd}
     * @param board this cal will play on.
     */
    public void setBoard(RmtBoard board) throws RemoteException 
    {
	bd = board;
    }
    /**
     * Chains to the stupid {@link RmtBoard} API. (Players must 
     * return Edges, while they only get access to numbers.)
     */
    public final boolean isSelected(Edge e) throws RemoteException
    {
	return bd.isSelected(e.getCol(), e.getRow(), e.getEdge());
    }
    /** 
     * Implementation of {@link boxit.Player#update} which does nothing.
     */
    public void update() throws RemoteException {}
    /**
     * Override this method and you have a real cat 
     * that plays on {@link #bd}. 
     */
    abstract public Edge pickEdge() throws RemoteException;
}
