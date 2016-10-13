package boxit.arena;

import boxit.Player;
import boxit.RmtBoard;
import boxit.PlayerRegister;
import boxit.client.PlayerLauncher;
import boxit.client.human.View;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JSeparator;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.io.IOException;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.rmi.RemoteException;

/**
 * The main BoXiT GUI arena. Have people all over the world program 
 * Dots and Boxes (Box It) players and set them up against each other.
 * @author Claas R&ouml;ver &copy; 2007 with CS424
 * @version 1.1
 */
public final class Arena {
    private final JFrame f = new JFrame("BoXiT Arena");
    private final MyMenuListener listener = new MyMenuListener();    
    private final JTabbedPane tabs = new JTabbedPane();
    private final SetupWindow setupWindow = new SetupWindow();
    private PlayerManager pm;
    private PlayerLauncher launcher;

    /** private default constructor to ensure use of runGUI() method */
    private Arena() { }
    /** first method in {@link #main}. */
    private void runGUI(String registersRmiHandle) {
	try { 
	    pm = new PlayerManager();
	    Naming.rebind(registersRmiHandle, (PlayerRegister) pm);

        }
	catch (IOException e) { 
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(f, 
					  "Problem registering remote object.\n            Can't continue!\nCheck that rmiregistry is running?", 
					  "Fatal Error", 
					  JOptionPane.ERROR_MESSAGE);
	    System.exit(1);
	}
	try {
	    launcher = new PlayerLauncher("rmi://192.168.0.40/" + registersRmiHandle);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(f, 
					  "Problem registering launcher with register.\n            Can't continue!", 
					  "Fatal Error", 
					  JOptionPane.ERROR_MESSAGE);
	    System.exit(1);
	}
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setLocation(300,120);
	f.setSize(500, 500);
	f.setJMenuBar(getMenuBar());
	f.add(tabs);
	f.setVisible(true);
    }
    /** Build the menu bar and returns it. */
    private JMenuBar getMenuBar() {
	JMenuItem quitMenu = new JMenuItem("Quit");
	quitMenu.setActionCommand("q");
	quitMenu.addActionListener(listener);
	JMenuItem newMenu = new JMenuItem("New Game");
	newMenu.setActionCommand("n");
	newMenu.addActionListener(listener);

	JMenuItem  bestOf = new JMenuItem("Best of x");
	bestOf.setActionCommand("B");
	bestOf.addActionListener(listener);
	JMenuItem  league = new JMenuItem("League");
	league.setActionCommand("L");
	league.addActionListener(listener);
	JMenuItem  knockOut = new JMenuItem("Knock Out");
	knockOut.setActionCommand("K");
	knockOut.addActionListener(listener);
	JMenuItem  groups = new JMenuItem("Group Pre-Round");
	groups.setActionCommand("G");
	groups.addActionListener(listener);
	JMenu tourMenu = new JMenu("Tournament");
	tourMenu.add(bestOf);
	tourMenu.add(league);
	tourMenu.add(knockOut);
	//tourMenu.add(groups);
	JMenu menu = new JMenu("Game");
	menu.add(newMenu);
	menu.add(tourMenu);
	menu.add(new JSeparator());
	menu.add(quitMenu);
	JMenuItem addMenu = new JMenuItem("Launch Player");
	addMenu.setActionCommand("a");
	addMenu.addActionListener(listener);
	JMenuItem removeMenu = new JMenuItem("Retire Player");
	removeMenu.setActionCommand("r");
	removeMenu.addActionListener(listener);
	JMenu playerMenu = new JMenu("PlayerLauncher");
	playerMenu.add(addMenu);
	playerMenu.add(removeMenu);
	JMenuItem threadMenu = new JMenuItem("Thread Control");
	threadMenu.setActionCommand("t");
	threadMenu.addActionListener(listener);
	JMenuItem regMenu = new JMenuItem("Register Control");
	regMenu.setActionCommand("g");
	regMenu.addActionListener(listener);
	JMenu adminMenu = new JMenu("Admin");
	adminMenu.add(threadMenu);
	adminMenu.add(regMenu);
	JMenuBar bar = new JMenuBar();
	bar.add(menu);
	bar.add(playerMenu);
	bar.add(adminMenu);
	return bar;
    }
    /** Called by Game->New Game menu. Creates a {@link Referee} and sets 
     * it going on its own thread! 
     */
    private void newGame() 
    {
	if ( pm.getPlayers() == null ) {
	    JOptionPane.showMessageDialog(f, 
					  "There are no players registered!", 
					  "No Players",
					  JOptionPane.ERROR_MESSAGE);
	    return;
	}
	Object data[] = setupWindow.getInfo(f, pm.getPlayers());
	if (data == null) 
	    return;
	Referee r = getReferee(data);
	if (r == null) {
	    JOptionPane.showMessageDialog(f, 
					  "Problem starting a Referee!", 
					  "No Players?",
					  JOptionPane.ERROR_MESSAGE);
	    return;
	}
	String title = (String) data[2] + " vs. " + (String) data[3];
	final View v = r.getView(); 
	tabs.addTab(title, v);
	tabs.setBackgroundAt(tabs.indexOfComponent(v), new Color(92, 174, 255));
	tabs.setSelectedIndex(tabs.indexOfComponent(v));
	r.run();
        int player_score[] = new int[2];
        
        try {
            player_score = r.getBoard().getScore();
        } 
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
        String winner = null;
        if ( player_score[0] == player_score[1] ) {
            winner = "It was a Draw.";
        }
        else if ( player_score[0] > player_score[1] ) {
            winner = (String) data[2] + " was the Winner.";
        }
        else winner = (String) data[3] + " was the Winner.";
        // tell the user who won
        JOptionPane.showMessageDialog(f, 
					""+winner+"\nFinal Score:\n"+(String) data[2]+" - "+player_score[0]+" : "+player_score[1]+" - "+(String) data[3], 
					"...And The Winner Is",
					JOptionPane.INFORMATION_MESSAGE);
        
        waitForView(v);
	tabs.remove(v);
	r = null; // release the referee
        
        
    }
    private void waitForView(View v) 
    {
	if (v == null)
	    return;
	final Object lock = new Object();
	v.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) 
		{
		    if (e.getButton() != MouseEvent.BUTTON2)
			return;
		    synchronized(lock) {
			lock.notify();
		    }
		    
		}
	    });

	synchronized(lock) {
	    try {
		lock.wait();
	    }
	    catch(Exception e) {
	    }
	}
    }
    private Referee getReferee(Object[] data)
    {	
	try {
	    return new Referee( ((Integer) data[0]).intValue(), 
				((Integer) data[1]).intValue(), 
				pm.get((String) data[2]),
				pm.get((String) data[3]),
				(Boolean) data[4]);
	}
	catch (Exception e) {
	    return null;
	}
    }
    private void runBestOf() 
    {
	int maxRound  = 0;
	while (true) {
	    String count = JOptionPane.showInputDialog(f, 
						       "Max rounds?", 
						       "BoXiT Best Of X Rounds",
						       JOptionPane.QUESTION_MESSAGE);
	    if (count == null)
		return; // input cancelled
	    try {
		if ( (maxRound = Integer.parseInt(count)) < 1) 
		    throw new Exception();;
	    }
	    catch (Exception e) {
		JOptionPane.showMessageDialog(f, "Positive value please :-)");
		continue;
	    }
	    break;
	}
	final Object data[] = setupWindow.getInfo(f, pm.getPlayers());
	if (data == null) {
	    JOptionPane.showMessageDialog(f, "No players registered");
	    return;
	}
	final String title = (String) data[2] + " vs. " + (String) data[3];
	final JTabbedPane tp = new JTabbedPane();
	tabs.addTab("Best of " + maxRound + ": " + title, tp);
	tabs.setSelectedIndex(tabs.indexOfComponent(tp));
	tabs.setBackgroundAt(tabs.indexOfComponent(tp), new Color(233, 143, 57));
	//tabs.setForeground(Color.GREEN);
	//tp.setBackground(Color.RED);
	int[] wins = new int[2];
	int need = (maxRound / 2);
	int round = 0;
	View view = null;;
	
    	for (round = 1; round <= maxRound; round++) {
	    if ( (wins[0] > need) || (wins[1] > need) ) {
		break;
            }
            if( round > 5) {
                int this_tab = round - 5;
                tp.remove(tp.indexOfTab("Game " +this_tab));
            }
            
	    Referee r = getReferee(data);
	    view = r.getView();
	    tp.addTab("Game " + round, view);
	    tp.setSelectedIndex(tp.indexOfTab("Game " + round));
	    r.run(); 
	    int score[] = new int[2];
	    Color[] colors = view.getStyle().getPlayerColors();
	    
	    try { 
		score = r.getBoard().getScore();
		int index = tp.indexOfComponent(tp.getSelectedComponent());
		if (score[0] > score[1]) {
		    wins[0]++;
		    tp.setBackgroundAt(index, colors[0]);
		}
		else if (score[1] > score[0]){
		    wins[1]++;
		    tp.setBackgroundAt(index, colors[1]);
		}
		else {
		    tp.setBackgroundAt(index, view.getStyle().getLineColor());
		}
	    }
	    catch (Exception e) {
	    }

            
	    Thread.currentThread().yield();
	    
	} // the tournament is over
        
        
        String winner = null;
        if ( wins[0] > need ) {
            winner = (String) data[2] + " was the Winner.";
        }
        else if ( wins[1] > need ) {
            winner = (String) data[3] + " was the Winner.";
        }
        else if ( wins[0] == wins[1] ) {
            winner = "It was a Draw.";
        }
        // tell the user who won
        JOptionPane.showMessageDialog(f, 
					""+winner+"\nFinal Score:\n"+(String) data[2]+" - "+wins[0]+" : "+wins[1]+" - "+(String) data[3], 
					"...And The Winner Is",
					JOptionPane.INFORMATION_MESSAGE);
        
	int index = tp.indexOfComponent(tp.getSelectedComponent());
	tp.setTitleAt(index, "Overall: " + wins[0] + ":" + wins[1]);
	waitForView(view);
	tabs.remove(tp);
    }
    private void league() {
    }
    /** Method called by Knock Out tournament menu item.
     * Here we do the interactive part, 
     * { @link #knockOut(JTabbedPane tp, int col, int row, List<String> players) 
     * handles the recursive part.
     */
    /*private void knockOut() { // players will play each other randomly each round
	List<String> players = getList();
	int col, row;
	JTabbedPane tp = new JTabbedPane();
	tabs.addTab("Knock Out", tp);
	tabs.setSelectedIndex(tabs.indexOfComponent(tp));
	knockOut(tp, col, row, players);
    }
    private void knockOut(JTabbedPane tp, int col, int row, List<String> players) {
	List<String> survivors = new List<String>();
	Iterator it = players.iterator();
	View v = null;
	while (it.hasNext()) {
	    String pl1 = (String) it.next();
	    if (!it.hasNext()) {
		survivors.add(pl1);
		break;
	    }
	    String pl2 = (String) it.next();
	    Object data[] = {col, row, pl1, pl2};
	    Referee r = getReferee(data);
	    if (r == null)
		return;
	    v = r.getView();
	    p.addTab(pl1 + " vs. " + pl2, v);
	    tp.setSelectedIndex(tp.indexOfTab(pl1 + " vs. " + pl2));
	    r.run();
	    int[] score = r.getBoard().getScore();
	    if (score[0] > score[1])
		survivors.add(pl1);
	    else if (score[1] > score[0])
		survivors.add(pl2);
	    else {
		//ignore this possiblity for the moment
	    } 
	}
	waitForView(v);
	}
    */
    private void groups() {
    }
    
    /** listener for menus */
    private class MyMenuListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    String cmd = e.getActionCommand();
	    if ( cmd.equals("q") ) {
		System.exit(0);
	    }
	    if ( cmd.equals("n") ) {
		new Thread(new Runnable() {
			public void run() {
			    newGame();
			}
		    }, "Referee").start();
	    }
	    else if ( cmd.equals("a") ) { //PlayerLauncher->Launch
		new Thread(new Runnable() {
			public void run() {
			    launcher.addPlayer();  
			}
		    }, "PlayerLauncher").start();
	    }
	    else if ( cmd.equals("r") ) { //PlayerLauncher->Launch
		new Thread(new Runnable() {
			public void run() {
			    launcher.removePlayer();  
			}
		    }, "PlayerLauncher").start();
	    }
	    else if ( cmd.equals("t") ) { //Admin->Threads menu
		new Thread(new ThreadMonitor(f.getX()+f.getWidth(), f.getY())).start();
	    }
	    else if ( cmd.equals("g") ) {  //Admin->Register menu
		new Thread(new Runnable() {
			public void run() {
			    
			}
		    }); //.start();
	    }
	    else if ( cmd.equals("B") ) {  //Best Of x Tounament 
		new Thread(new Runnable() {
			public void run() {
			    runBestOf();
			}
		    }, "BestOf").start();
	    }
	    /*	    else if ( cmd.equals("K") ) {  //Knock Out Tounament 
		new Thread(new Runnable() {
			public void run() {
			    knockOut();
			}
		    }, "KnockOut").start();
		}
	    */
	}	
    }
    /**
     * Fires up your BoXiT arena :-) Name your baby.
     */
    public static void main(final String[] args) {
	//Schedule a job for the event-dispatching thread:
	//creating and showing this application's GUI.
	if (args.length < 1) {
	    System.err.println("Usage: java Arena registerRmiHandle");
	    System.exit(1);
	}
        SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    new Arena().runGUI(args[0]);
		}
	    });
    }
}
