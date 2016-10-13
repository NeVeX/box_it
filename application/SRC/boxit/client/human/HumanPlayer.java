package boxit.client.human;

import boxit.Edge;
import boxit.RmtBoard;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
/**
 * Instances of this class will pop up an interactive view for human 
 * players onto a BoxIt board. :-)<br/>Since the person who runs 
 * the {@link boxit.arena.Arena Arena} decides who plays who, you 
 * might have to wait some time ;-(
 */
public final class HumanPlayer extends View implements boxit.Player {
    private Edge edge = null;
    private final Object lock = new Object();
    private final JFrame f = new JFrame("Human Box-It Player");

    public HumanPlayer() {
	addMouseListener(new MouseKeeper());
    }
    // remote methods
    public void setBoard(RmtBoard b)  throws RemoteException {
	showBoard(b);
	f.add(this);
	f.pack();
	//f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	f.setVisible(true);
    }
    public Edge pickEdge()  throws RemoteException {
	Edge ret = null;
	String oldStatus = status;
	turn = true;
	repaint();
	while (ret == null) {
	    synchronized (lock) {
		try {
		    Thread.currentThread().sleep(200);
		    lock.wait();
		    ret = edge;
		    turn = false;
		    repaint();
		    //paintImmediately(0, 0, getWidth(), getHeight());
		}
		catch (InterruptedException e) {	
		    ret = null;
		}
	    }
	}
	return edge;
    }
    public void update() throws RemoteException {
	update(null, null);
    }
    // member class
    private class MouseKeeper extends MouseAdapter {
	public void mouseClicked(MouseEvent e) {
	    if (e.getButton() != MouseEvent.BUTTON1) {
		return;
	    }
	    try {
		if (board.isFull()) {
		    //f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	    }
	    catch (RemoteException ex) {
		// in case a board dies or gets uncontcatable
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    }
	    int mx = e.getX() - s.marginLeft; 
	    int my = e.getY() - s.marginTop;
	    int local_row, col, eg;
	    if ( mx < 0                                   // too far left
		 || my < 0                                // too high
		 || mx > (column) * s.boxWidth + s.dotWidth // too far right
		 || my > row * s.boxWidth + s.dotWidth)  // too low
		return; 
	    // Get the box indicees; They may be one too large still!
	    col = mx / s.boxWidth;
	    local_row = my / s.boxWidth;
	    if ( mx % s.boxWidth <= s.dotWidth
		 && my % s.boxWidth > s.dotWidth) { // Got a vertical edge.
		eg = (col < column)? Edge.WEST : Edge.EAST;
		col = (col == column) ? col - 1 : col;
	    }
	    else if ( my % s.boxWidth <= s.dotWidth 
		      && mx % s.boxWidth > s.dotWidth) { // A horizontal edge.
		eg = (local_row < row) ? Edge.NORTH : Edge.SOUTH;
		local_row = (local_row == row) ? local_row - 1 : local_row;
	    }
	    else 
		return; // click was on bottom right dot
	    synchronized(lock) {
		edge = new Edge(col, local_row, eg);
		lock.notify();
	    }
	}
    }
}
