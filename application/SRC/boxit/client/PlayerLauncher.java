package boxit.client;

import boxit.Player;
import boxit.PlayerRegister;
import javax.transaction.InvalidTransactionException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JOptionPane;
/**
 * Implementation of {@link boxit.Launcher} using {@link PlayerWindow}
 * for easy user interaction. It is used in {@link boxit.arena.Arena Arena} 
 * and extented to a full GUI by {@link GUILauncher}.
 */
public class PlayerLauncher implements boxit.Launcher {
    private PlayerRegister pr;
    private final PlayerWindow playerWindow = new PlayerWindow();
    private Map<String, String> players = new HashMap<String,String>(10);
    private boolean search;
    protected String registerRmiURL; 

    
    /** Returns an instance connected to the {@link boxit.PlayerRegister}
     * know by the given <u>registerRmiURL</u>.
     */
    public PlayerLauncher(String registerRmiURL) throws RemoteException 
    {
	this.registerRmiURL = registerRmiURL;
	UnicastRemoteObject.exportObject(this);
	connectRegister();
    }
    /** Tries to lookup the PlayerRegister and get a reference.*/
    private void findRegister() 
    {
	try {
	    pr = (PlayerRegister) Naming.lookup(registerRmiURL);
	    search = false;
	}
	catch (Exception e) { 
	    search = true;
	}
    }
    
    private void connectRegister() throws RemoteException 
    {
	try {
	    pr = (PlayerRegister) Naming.lookup(registerRmiURL);
	    search = false;
	}
	catch (NotBoundException e) {
	    JOptionPane.showMessageDialog(playerWindow, 
					  "No PlayerRegister at" + registerRmiURL,
					  "No Register Found", 
					  JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	    System.exit(1);
	}
	catch (MalformedURLException e) {
	    JOptionPane.showMessageDialog(playerWindow, 
					  "Bad RMI URL: " + registerRmiURL,
					  "Malformed URL", 
					  JOptionPane.ERROR_MESSAGE);
	    //e.printStackTrace();
	    System.exit(1);
	}
	//System.err.println("pl up and registry set");
    }
    private void log(String msg) 
    {
	System.err.println("PlayerLauncher (" + this + "): " + msg);
    }
    /** GUI for registering players by class name. */
    public void addPlayer()  
    {
	if (search) 
	    findRegister();
	if (search)
	    return;
	String[] info = playerWindow.getPlayerInfo(null);
	if ( info == null ) {
	    return;
	}
	if (players.containsKey(info[0])) {
	    JOptionPane.showMessageDialog(playerWindow, 
					  "Player '" + info[0] + "' already registered.",
					  "Player Known", 
					  JOptionPane.ERROR_MESSAGE);
	    return;
	}
	try {
	    pr.add(info[0], this);          // add to register
	    players.put(info[0], info[1]);  // add to our hash map
	    //log("added \"" + info[0] + "\" of type \"" + info[1] + "\"");
	}
	catch (RemoteException e) {
	    JOptionPane.showMessageDialog(playerWindow, e.getMessage() + "\nTRY AGAIN -:)",
					  "RemoteException", 
					  JOptionPane.ERROR_MESSAGE);
	    search = true;
	    return;
	}
	}
	/** Lets the user choose a player to be unregistered. */
	public void removePlayer() {
	if (search) 
	    findRegister();
	String[] pl = getPlayers();
	if (pl == null) {
	    JOptionPane.showMessageDialog(null, "There are no players registered!", "No Players",
					  JOptionPane.ERROR_MESSAGE);
	    return;
	}
	String nick = (String) JOptionPane.showInputDialog(null, "Select player to be removed.", 
							   "BoXiT Player Removal",
							   JOptionPane.OK_CANCEL_OPTION, 
							   null, pl,  pl[0]);
	if (nick != null) {
	    try {
		remove(nick);
	    }
	    catch (RemoteException e) {
		JOptionPane.showMessageDialog(null, 
					      "Player " + nick + " coudl not be unregistered.\nTRY AGAIN -:)", 
					      "Error",
					      JOptionPane.ERROR_MESSAGE);
		search = true;
	    }
	}
    }
    /** This is a GUI free remove not catching any exceptions. */
    protected void remove(String nick) throws RemoteException { 
	pr.remove(nick);
	players.remove(nick);
    }
    public Player  get(String nickName) throws RemoteException  
    {
	Player p = null;
	try {
	    p = (Player) Class.forName(players.get(nickName)).newInstance();
	    UnicastRemoteObject.exportObject(p);
	}
	catch (ClassNotFoundException e) {
	    JOptionPane.showMessageDialog(null, 
					  e.getMessage(),
					  "Error",
					  JOptionPane.ERROR_MESSAGE);
	}
	catch (InstantiationException e) {
	    JOptionPane.showMessageDialog(null, 
					  e.getMessage(),
					  "Error",
					  JOptionPane.ERROR_MESSAGE);
	}
	catch (IllegalAccessException e) {
	    JOptionPane.showMessageDialog(null, 
					  e.getMessage(),
					  "Error",
					  JOptionPane.ERROR_MESSAGE);
	}
	//System.err.println("pl returning player: " + p);
	return p;
    }
    String[] getPlayers() 
    {
	Set<String> pl = players.keySet();
	if (pl.isEmpty())
	    return null;
	String[] s = new String[pl.size()];
	return players.keySet().toArray(s);
    }
}
