package boxit;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * This is {@link boxit.arena.Arena Arena}'s remote API for 
 * (un)registering {@link Player}s. There is no need to implement this
 * interface, the {@link boxit.client.PlayerLauncher} does the job and
 * you even get a {@link boxit.client.GUILauncher GUILauncher}.<br/><br/>
 * If you want to implement this, then you need to have an instantiable 
 * {@link Launcher} that can return an instance of {@link Player} when 
 * its {@link Launcher#get} method is called with a nick name, that it
 * previously registered with {@link #add}, as parameter. One reason 
 * for doing this could be the limitation of 
 * {@link boxit.client.PlayerLauncher} to only one {@link PlayerRegister}.
 */
public interface PlayerRegister extends Remote {
    /**
     * Call this when you want to register a new <code>nickName</code> for a 
     * {@link Player} class that can be launched by <code>launcher</code>. 
     */
    public void add(String nickName, Launcher launcher) throws RemoteException;
    /**
     * Call this when you want to unregister the <code>nickName</code> from
     * the {@link boxit.arena.Arena Arena}'s {@link PlayerRegister}
     */
    public void remove(String nickName)throws RemoteException;
}

