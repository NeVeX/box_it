package boxit.client;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/** Simple GUI for entering player nick name and class name. 
 * @see 
PlayerLauncher PlayerLauncher */
final class PlayerWindow extends JFrame implements ActionListener {
    private String nickName, className;
    private boolean abort;
    private final JTextField nickNameField = new JTextField("daCat", 15);
    private final JTextField classNameField = new JTextField("boxit.client.cat.DaCat", 15);
    private final JButton cbut = new JButton("Cancel");

    String[] getPlayerInfo(Component where) {
	String ret[] = new String[2];
	abort = false;
	int xOff, yOff;
        if (where == null) {
            xOff = 400; yOff = 300;
        }
        else {
            xOff = where.getX() + 80;
            yOff = where.getY() + 80;
        }
	setLocation(xOff, yOff);
	setVisible(true);
	while (true) {
	    synchronized(this) {
		try {
		    wait();
		    if (!abort) {
			ret[0] = nickName;
			ret[1] = className;
		    }
		    else {
			ret = null;
		    }
		    break;
		}
		catch (Exception e) { 
		    abort = false;
		}
	    }
	}
	setVisible(false);
	return ret;
    }
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == cbut) {
	    synchronized(this) { 
		abort = true;
		notify(); 
	    }
	    return;
	}
	synchronized(this) {
	    nickName = nickNameField.getText();
	    className = classNameField.getText();
	    // sanity check
	    try {
		if ( nickName.equals("") ) {
		    JOptionPane.showMessageDialog(this, 
						  "Please enter a nick name!",
						  "No Nick Name", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for (int i = 0; i < nickName.length(); i++) {
		    if ( nickName.charAt(i) == ' ' ) {
			JOptionPane.showMessageDialog(this, 
						      "A nick name must not contain spaces!",
						      "Bad Nick Name", 
						      JOptionPane.ERROR_MESSAGE);
			return;
		    }
		}
		Class c = Class.forName(className);
		Class type[] = c.getInterfaces();
		boolean isPlayer = false;
		for (int i = 0; i < type.length; i++) 
		    if ( type[i].getSimpleName().equals("Player") ) 
			isPlayer = true;
		if (!isPlayer) {
		    JOptionPane.showMessageDialog(this, 
						  "A player's class must\nimplement Player.",
						  "Wrong Type", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		notify();
	    }
	    catch (ClassNotFoundException ex) {
		JOptionPane.showMessageDialog(this, "Can't find class '" + className + "'.", 
					      "Class Not Found", JOptionPane.ERROR_MESSAGE);
	    }
	}
    }
    PlayerWindow() {
	super("BoXiT Player Registration"); 
	setSize(300, 160);
	setResizable(false);
	setLayout(new GridLayout(4,1));
	//first some guidance
	add(new JLabel("Enter player registration information."));
	// first two rows for nick name and class name fields
	add(InputFieldFactory.make("Nick Name ", nickNameField));
	add(InputFieldFactory.make("Class Name", classNameField));
	nickNameField.addActionListener(this);
	classNameField.addActionListener(this);
	JPanel buts = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JButton sbut = new JButton("Submit");
	buts.add(cbut); 
	buts.add(sbut);
	sbut.addActionListener(this);
	cbut.addActionListener(this);
	add(buts);
	setVisible(false);
    }
    /*    public static class Test {
	public static void main(String args[]) {
	    
	    PlayerWindow w = new PlayerWindow();
	    while (true) {
		try {
		    Thread.currentThread().sleep(1000);
		}
		catch (Exception e) {}
		String[] ans = w.getPlayerInfo(null);
		if (ans == null) {
		    System.out.println("Input cancelled.");
		    continue;
		}
		System.out.println("Nick name: " + ans[0] + " is a player of tyoe " + ans[1]);
	    }
	}
	}*/
}
