package boxit.arena;

import boxit.Launcher;
import boxit.PlayerRegister;
import boxit.Player;
import javax.transaction.InvalidTransactionException;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;

class PlayerManager extends UnicastRemoteObject implements PlayerRegister {
    private Map<String, Launcher> players = Collections.synchronizedMap(new HashMap<String, Launcher>(10));
    
    PlayerManager() throws RemoteException { }
    public void add(String nickName, Launcher launcher) throws RemoteException {
	if (players.containsKey(nickName)) 
	    throw new InvalidTransactionException("'" + nickName + "' already registered");
	players.put(nickName, launcher);
    }
    public void remove(String nickName) throws RemoteException {
	players.remove(nickName);
    }
    Player get(String nickName) {
	Player p = null;
	Launcher l = players.get(nickName);
	try {
	    p = l.get(nickName);
	}
	catch (Exception e) {
	    //System.err.println("Problem getting remote player.");
	    //e.printStackTrace();
	}
	return p;
    }
    String[] getPlayers() {
	Set<String> pl = players.keySet();
	if (pl.isEmpty())
	    return null;
	String[] s = new String[pl.size()];
	return players.keySet().toArray(s);
    }
}


    
    
