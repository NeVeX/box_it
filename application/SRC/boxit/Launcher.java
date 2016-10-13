package boxit;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Implementing class must return an instance of a class 
 * that implements {@link Player}.<br/>
 * <b>Warning:</b> If you implement this interface yourself, 
 * remember to rmic the returned class and call 
 * {@link java.rmi.server.UnicastRemoteObject#exportObject} before
 * returning the object reference.
 */
public interface Launcher extends Remote { 
    /**
     * Called remotely by  {@link boxit.arena.Arena}'s {@link PlayerRegister} 
     * @param nickName A players nick name.
     * @return A {@link Player} instance 
     */
    public boxit.Player get(String nickName) throws RemoteException;
}
