package boxit.client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.FlowLayout;
import java.awt.GridLayout;
//import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

/** 
 * This is the user client application which allows to 
 * register implementations of the {@link boxit.Player} remote interface
 * with a {@link boxit.PlayerRegister} of a BoXiT arena, e.g.<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp; 
 * <key>java boxit.client.GUILauncher rmi://some.host.nu/BoXiTrEg</key>
 *
 * @see boxit.client.cat.Cat Cat for an abstract convenience implementation
 * of the Player remote interface. 
 */
public final class GUILauncher extends PlayerLauncher 
{
    private static final String buttonTitle[] = { "Launch Player", "a", "Retire Player", "r", "Quit", "q" };
    
    private ActionListener al = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "a") 
		    new Thread(new Runnable() {
			    public void run() {
				addPlayer();
			    }
			}).start();
		else if (e.getActionCommand() == "r") 
		    new Thread(new Runnable() {
			    public void run() {
				removePlayer();
			    }
			}).start();
		else if (e.getActionCommand() == "q") {
		    cleanup();
		    System.exit(0);
		}
	    }
	};
    
    private GUILauncher(String registerRmiURL) throws RemoteException 
    {
	super(registerRmiURL);
	
    }
    private void cleanup() {
	al = null; // disable al to avoid new registrations
	String plr[] = getPlayers();
	if (plr == null) 
	    return;
	int tries = 0;
	System.err.print("GUILauncher: shutting down .. ");
	for (int i = 0; i < plr.length; i++) {
	    try {
		Thread.currentThread().sleep(500);
		remove(plr[i]);
		System.out.print("..");
	    }
	    catch (InterruptedException e) {
		i--;       // try again
	    }
	    catch (RemoteException e) {
		if (tries++ < 3)
		    i--;  // try again
		else {
		    tries = 0;
		    System.err.println("giving up on " + plr[i]);
		}
	    }
	}
	System.exit(0);
    }
    private void runGUI() 
    {
	JFrame f = new JFrame("BoXiT GUILauncher");
	f.setSize(440, 460);
	f.setResizable(false);
	f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	f.setLayout(new GridLayout(2, 1));	
	f.add(new JLabel("Connected to Arena at " + registerRmiURL));
	JPanel bl = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
	JButton b;
	for (int i = 0; i < buttonTitle.length; i++) {
	    b = new JButton(buttonTitle[i]);
	    b.addActionListener(al);
	    b.setSize(80,30);
	    b.setActionCommand(buttonTitle[++i]);
	    bl.add(b);
	}    
	f.add(bl);
	f.pack();
	f.setVisible(true);
    }
    /** Fires up the GUI :-) Tell it an rmi address of an arena. 
     * Call with <u>java boxit.client.GUILauncher registerRmiURL</u>,
     * E.g. <key>java boxit.client.GUILauncher rmi://some.host.nu/BoXiTrEg</key>
     */
    public static void main(final String[] registerRmiURL) {
	if (registerRmiURL.length != 1) {
	    System.err.println("Usage: java GUILaucher registerHandle\n\nwhere registryHandle is the full rmi URL for a PlayerRegister of an Arena.\nEXAMPLE:\n        java boxit.client.GUILauncher rmi://maths.nuigalway.ie/BoXiTrEg");
	    System.exit(1);
	}
	SwingUtilities.invokeLater(new Runnable() {
                public void run() {
		    try {
			new GUILauncher(registerRmiURL[0]).runGUI();
		    }
		    catch (Exception e) {
			System.err.println(e.getMessage() +"\n" + e.getCause());
			System.exit(1);
		    }
		}
            });
    }
	}
