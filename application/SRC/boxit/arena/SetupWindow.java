package boxit.arena;

import boxit.client.InputFieldFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
/**
 * Only package visible class. Should maybe be an inner class of {@link Arena}.
 */
final class SetupWindow extends JFrame implements ActionListener, ItemListener {
    private int rows, cols;
    private String player1, player2;
    private boolean steppingOn;
    private boolean abort;
    private final JTextField colField = new JTextField("3", 3);
    private final JTextField rowField = new JTextField("3", 3);
    private final JComboBox pl1Field = new JComboBox();
    private final JComboBox pl2Field = new JComboBox();
    private final JCheckBox sField = new JCheckBox();
    private final JButton cbut = new JButton("Cancel");

    /** What happens when the stepping check box is changed. */
    public void itemStateChanged(ItemEvent ie) 
    {
	if ( ie.getItemSelectable() != sField) 
	    return;
	if (ie.getStateChange() == ItemEvent.SELECTED) 
	    steppingOn = true;
	else if (ie.getStateChange() == ItemEvent.DESELECTED)
	    steppingOn = false;
	sField.setText( ((steppingOn) ? " ON " : " OFF") );
    }
    /** What our ActionListener does. */
    public void actionPerformed(ActionEvent e) 
    {
	if (e.getSource() == cbut) {
	    synchronized(this) { 
		abort = true;
		notify(); 
	    }
	    return;
	}
	synchronized(this) {
	    player1 = (String) pl1Field.getSelectedItem();
	    player2 = (String) pl2Field.getSelectedItem();
	    try {
		cols = Integer.parseInt(colField.getText());
		rows = Integer.parseInt(rowField.getText());
		if (cols < 1 || rows < 1) 
		    throw (new Exception("Positive values, please :-)"));
		//System.err.println("will notify: " + Thread.currentThread());
		abort = false;
		notify();
	    }
	    catch (Exception ex) {
		JOptionPane.showMessageDialog(this, ex.getMessage(), 
					      "Wrong Input", JOptionPane.ERROR_MESSAGE);
	    }
	}
	return;
    }
    /** 
     * Returns data validated for a BoxIt game or null, in this order:<br/> 
     * noOfCols:int, noOfRows:int, nickName_1:String, nickName_2:String 
     */
    Object[] getInfo(Component where, String list[]) {
	if (list == null) 
	    return null;
	Object ret[] = new Object[5];
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
	pl1Field.removeAllItems();
	pl2Field.removeAllItems();
	for (int i = 0; i < list.length; i++) {
	    pl1Field.addItem(list[i]);
	    pl2Field.addItem(list[i]);
	}
	setVisible(true);
	while (true) {
	    synchronized(this) {
		try {
		    wait(); 
		}
		catch (Exception e) { 
		    continue;
		    
		}
		if (abort) {
		    //System.err.println("found abort");
		    ret = null;
		}
		else {
		    //System.err.println("found data");
		    ret[0] = cols;
		    ret[1] = rows;
		    ret[2] = player1;
		    ret[3] = player2;
		    ret[4] = steppingOn;
		}
	    }
	    break; // out of the while loop
	}
	setVisible(false);
	return ret;
    }
    /** Package accessible constructor only. */
    SetupWindow() 
    {
	super("BoXiT Match Setup"); 
	setSize(310, 180);
	setResizable(false);
	setLayout(new GridLayout(4,1));
	// first row for board size parameters
	JPanel size = new JPanel(new GridLayout(1,2));
	size.add(InputFieldFactory.make("Columns", colField));
	size.add(InputFieldFactory.make("Rows", rowField));
	colField.addActionListener(this);
	rowField.addActionListener(this);
	add(size);
	//second row for one player
	JPanel pl = new JPanel(new GridLayout(1,2));
	pl.add(InputFieldFactory.make("Player 1", pl1Field));
	pl.add(InputFieldFactory.make("Player 2", pl2Field));
	add(pl);
	//third row for second player andstepping check box 
	JPanel sp = new JPanel(new FlowLayout(FlowLayout.CENTER));
	sField.addItemListener((ItemListener) this);
	sp.add(InputFieldFactory.make("Stepping", sField));
	itemStateChanged(new ItemEvent(sField, 0, null, 
				       ((steppingOn) ? ItemEvent.SELECTED : ItemEvent.DESELECTED))); 
	add(sp);
	//last row for buttons
	JPanel buts = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JButton sbut = new JButton("Submit");
	buts.add(cbut); 
	buts.add(sbut);
	sbut.addActionListener(this);
	cbut.addActionListener(this);
	add(buts);
	setVisible(false);
    }
    /** convenience executable class for testing. DON'T SHIP*/
    public static class Test 
    {
	public static void main(String args[]) {
	    
	    String l[] = { "Human", "Idiot", "Genius", "Engin" };
	    SetupWindow w = new SetupWindow();
	    while (true) {
		try {
		    Thread.currentThread().sleep(1000);
		}
		catch (Exception e) {}
		Object[] ans = w.getInfo(null, l);
		if (ans == null) {
		    System.out.println("Input cancelled.");
		    continue;
		}
		for (int i = 0; i < ans.length; i++) 
		    System.err.println("ans[" + i + "] = " + ans[i]);
	    }
	}
    }
}
