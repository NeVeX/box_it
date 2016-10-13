package boxit.client.human;

import boxit.client.InputFieldFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import java.awt.Component; 
import java.awt.Color; 
import java.awt.FlowLayout; 
import java.awt.GridLayout; 
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

final class StyleManager extends JFrame implements ActionListener {
    private Style style, oldStyle;

    private String name;
    private final int width[] = new int[7];
    private final Color col[] = new Color[5];
    private final JTextField[] field = new JTextField[7];
    private final String fieldName[] = {"Syle name", "Left margin", "Top margin", 
				      "Box width", "Dot width", "Separation", 
				       "Line width" };
    private final String colorName[] = {"Background", "Dot", "Line",
				       "Player 1", "Player 2" };
    private final JButton[] color = new JButton[5];
    private final JButton[] button = new JButton[5];
    { button[0] = new JButton("Load");
    button[0].setActionCommand("load");
    button[1] = new JButton("Save");
    button[1].setActionCommand("save");
    button[2] = new JButton("Cancel");
    button[2].setActionCommand("cancel");
    button[3] = new JButton("Reset");
    button[3].setActionCommand("reset");
    button[4] = new JButton("OK");
    button[4].setActionCommand("submit");
    }
    Style getStyle(Component where, Style oldStyle) {
	this.oldStyle = oldStyle;
	int xOff, yOff;
	if (where == null) {
	    xOff = 400; yOff = 300;
	}
	else {
	    xOff = where.getX() + 80;
	    yOff = where.getY() + 80;
	}
	setLocation(xOff, yOff);
	style = oldStyle;
	readFromStyle();
	fillSelectionFields();
	setVisible(true);
	Style st = null;
	boolean dataOk = false;
	while (true) {
	    synchronized(this) {
		try {
		    wait();
		    st = style;
		    break;
		}
		catch (Exception e) { }
	    }
	}
	setVisible(false);
	return st;
    }
    public void actionPerformed(ActionEvent e) {
	style = null;
	String cmd = e.getActionCommand();
	if (cmd.equals("load")) {
	    load();
	    return;
	}
	if (cmd.equals("save")) {
	    if (inputValid())
		save();
	    return;
	}
	if (cmd.equals("reset")) {
	    style = oldStyle;
	    readFromStyle();
	    fillSelectionFields();
	    return;
	}
	if (e.getActionCommand().equals("cancel")) {
	    synchronized(this) { 
		style = null;
		notify(); 
	    }
	    return;
	}
	if (e.getActionCommand().equals("color")) {
	    JButton b = (JButton) e.getSource();
	    Color c = JColorChooser.showDialog(this, "Select "+ b.getName() + " color", b.getBackground());
	    if (c != null) {
		b.setBackground(c);
	    }
	    return;
	}
	// Now either the submit button was clicked or 'Return' entered in field
	if (!inputValid()) {
	    return;
	}
	synchronized(this) {
	    makeStyle();
	    notify();
	}
    }
    
    private boolean inputValid() {
	// check for name
	if (field[0].getText().equals("") || field[0].getText() == null) {
	    JOptionPane.showMessageDialog(this, "Provide a name for the style.");
	    return false;
	}
	name = field[0].getText();
	// get colours
	for (int i = 0; i < color.length; i++) {
	    col[i] = color[i].getBackground();
	}
	// colour sanity check
	for (int i = 1; i < color.length; i++) {
	    if ( col[0].equals(col[i]) ) {
		JOptionPane.showMessageDialog(this, color[i].getName() + " colour and background are the same.");
	    return false;
	    }
	}
	if ( col[3].equals(col[4]) ) {
	    JOptionPane.showMessageDialog(this, "Both player's colours are the same.");
	    return false;
	}
	// get widths
	for (int i = 1; i < field.length; i++) {
	    try {
		    width[i] = Integer.parseInt(field[i].getText());
		    if (width[i] <= 0)
			throw new Exception("negative number");
	    }
	    catch (Exception ex) {
		JOptionPane.showMessageDialog(this, field[i].getName() + " must be a positive integer.");
                return false;
	    }
	}
	// width sanity check
	if (width[3] <= 2 * width[5]) {
	    JOptionPane.showMessageDialog(this, "Saparation too large.\nMust be less than half a box width.");
	    return false;
	}
	if (width[3] <= width[4]) {
	    JOptionPane.showMessageDialog(this, "Dot width too large.\nMust be less than box width.");
	    return false;
	}
	return true;
    }
    // FILE METHODS
    private void load() {
	System.err.println("Load Clicked");
    }
    private void save() {
	System.err.println("Save Clicked");
    }
    private void fillSelectionFields() {
	field[0].setText(name);
	for (int i = 1; i < field.length; i++) 
	    field[i].setText("" + width[i]);
	for (int i = 0; i < color.length; i++) 
	    color[i].setBackground(col[i]);
    }
    
    private void readFromStyle() {
	name = style.name;
	width[1] = style.marginLeft;
	width[2] = style.marginTop;
	width[3] = style.boxWidth;
	width[4] = style.dotWidth;
	width[5] = style.ldSep;
	width[6] = style.lineWidth;
	col[0] = style.bgColor;
	col[1] = style.dotColor;
	col[2] = style.lineColor;
	col[3] = style.plColor[0];
	col[4] = style.plColor[1];
    }
    private void makeStyle() {
	style = new Style();
	style.name = name;
	style.marginLeft = width[1]; 
	style.marginTop = width[2];
	style.boxWidth = width[3];
	style.dotWidth = width[4]; 
	style.ldSep = width[5];
	style.lineWidth = width[6];
	style.bgColor = col[0];
	style.dotColor = col[1];
	style.lineColor = col[2];
	style.plColor[0] = col[3];
	style.plColor[1] = col[4];
    }
    StyleManager() {
	super("BoXiT Style Selection"); 
	setSize(450, 260);
	setResizable(false);
	// set up the name field
	field[0] = new JTextField(15);
	field[0].setName(fieldName[0]);
	field[0].addActionListener(this);
	// set up the width fields
	for (int i = 1; i < field.length; i++) {
	    field[i] = new JTextField(3);
	    field[i].setName(fieldName[i]);
	    field[i].addActionListener(this);
	}
	// set up the color buttons    
	for (int i =0; i < color.length; i++) {
	    color[i] = new JButton();
	    color[i].addActionListener(this);
	    color[i].setName(colorName[i]);
	    color[i].setActionCommand("color");
	    color[i].setText("   ");
	}
	// put them all into place
	setLayout(new GridLayout(6,1));
	// first row for style name
	JPanel name = new JPanel(new FlowLayout(FlowLayout.CENTER));
	name.add(InputFieldFactory.make(fieldName[0], field[0]));
	add(name);
	// first two rows for width parameters
	JPanel sizes = new JPanel(new GridLayout(1,3));
	for (int i = 1; i < 4; i++)
	    sizes.add(InputFieldFactory.make(fieldName[i], field[i]));
	add(sizes);
	sizes = new JPanel(new GridLayout(1,3));
	for (int i = 4; i < 7; i++)
	    sizes.add(InputFieldFactory.make(fieldName[i], field[i]));
	add(sizes);
	// next two rows for colour buttons
	JPanel colors = new JPanel(new GridLayout(1,3));
	for (int i = 0; i < 3; i++)
	    colors.add(InputFieldFactory.make(colorName[i], color[i]));
	add(colors);
	colors = new JPanel(new GridLayout(1,2));
	for (int i = 3; i < 5; i++)
	    colors.add(InputFieldFactory.make(colorName[i], color[i]));
	add(colors);
	//last row for buttons
	JPanel buts = new JPanel(new FlowLayout(FlowLayout.CENTER));
	for (int i = 0; i < button.length; i++) {
	    buts.add(button[i]); 
	    button[i].addActionListener(this);
	}
	add(buts);
	setVisible(false);
    }
    public static class Test {
	public static void main(String args[]) {
	    StyleManager w = new StyleManager();
	    while (true) {
		try {
		    Thread.currentThread().sleep(3000);
		}
		catch (Exception e) {}
		Style s = w.getStyle(null, new Style());
		if (s == null) {
		    System.out.println("Input cancelled.");
		    continue;
		}
		System.out.println(s);
	    }
	}
    }
}
