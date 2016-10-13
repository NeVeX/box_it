package boxit.arena;

import boxit.RmtBoard;
import boxit.Player;
import boxit.Edge;
import boxit.client.human.View;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Observer;
import java.util.Observable;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

final public class Referee implements Runnable, Observer {
    private int toPlay;
    private Board board;
    private Player player[] = new Player[2];
    private final View view = new View();
    private final Object lock = new Object();
    private boolean stepping, stop;
    private final MouseListener stepListener = new MouseAdapter() {
	    public void mouseClicked(MouseEvent ev) {
		//System.err.println("Referee's step listener");
		if (ev.getButton() == MouseEvent.BUTTON1) {
		    synchronized(lock) {
			lock.notify();
		    }
		}
		else if (ev.getButton() == MouseEvent.BUTTON2){
		    stop = true;
		    synchronized(lock) {
			lock.notify();
		    }
		    synchronized(lock) {
			lock.notify();
		    }
		}
	    }
	};
    
    Referee(int noOfColumns, 
	    int noOfRows, 
	    Player player1, 
	    Player player2,
	    boolean stepping) 
    throws RemoteException 
    {
	board = new Board(noOfColumns, noOfRows);
	UnicastRemoteObject.exportObject(board);
	board.addObserver(this);
	view.showBoard((RmtBoard) board);
	view.addMouseListener(stepListener);
	board.addObserver(view);
	player1.setBoard((RmtBoard) board);
	player2.setBoard((RmtBoard) board);
	player[0] = player1;
	player[1] = player2;
	this.stepping = stepping;
	toPlay = (int) (2 * Math.random());
    }
    View getView() 
    {
	return view;
    }
    RmtBoard getBoard() 
    {
	return (RmtBoard) board;
    }
    void setStepping(boolean onOff) 
    {
	stepping = onOff;
    }
    public void update(Observable o, Object arg) {
	try {
	    player[0].update();
	    player[1].update();
	}
	catch (RemoteException e) {
	    e.printStackTrace();
	    // SERIOUS BUG
	    //what does a ref do when the players just leave?
	    // ignore for now but could be 
	}
    }
    public void run() {
	int pCount = 0, bCount = 0;;
	boolean step = stepping;
	try {
	    while ( (!board.isFull()) && (!stop) ) {
		if (step) {
		    view.showBoard((RmtBoard) board);
		    synchronized(lock) {
			try { lock.wait(); }
			catch (Exception e) {}
		    }
		}
		try {
		    Edge e = player[toPlay].pickEdge();
		    if (e == null)
			continue;
		    if (board.acceptMove(e, toPlay)) {
			toPlay = (toPlay + 1) % 2;
			step = stepping;
		    }
		    else 
			step = false;
		}
		catch (RemoteException e) {
		    pCount++;
		    try { Thread.currentThread().sleep(500); }
		    catch (Exception ex) { }
		    if (pCount == 5) {
			view.getGraphics().drawString("A player left ;-(", 10, view.getHeight() - 10);
			stop = true;
		    }
		}
		Thread.currentThread().yield();
	    } // now the game is over or has been stopped!
	    update(null, null); // one last update for the players
	}
	catch (Exception e) { 
	    e.printStackTrace();
	    view.getGraphics().drawString("Lost board ;-(", 10, view.getHeight() - 10);
	}
    }
}
