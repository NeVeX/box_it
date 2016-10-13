package boxit.client.human;

import boxit.Edge;
import boxit.RmtBoard;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.util.Observer;
import java.util.Observable;
import java.rmi.RemoteException;
/**
 * This class can be given a {@link #showBoard RmtBoard} to display
 * It needs to be registered with an {@link java.util.Observable observable}.
 * <b>Hint:</b>Override {@link boxit.Player#update} to call {@link #update} 
 * and your {@link boxit.Player engine} has a view attached.
 */
public class View extends JPanel implements Observer {
    /** 
     *Last {@link Style} used by any view in this JVM. */
    private static Style lastStyle = new Style();
    /** 
     * Our preferred size*/
    private Dimension dim;      
    /** 
     * The {@link Style} this view uses. */
    protected Style s;
    /** 
     *Remote board to be displayed. */
    protected RmtBoard board;        // board to display
    /** 
     *Board dimensions. */
    protected int column, row;
    /** 
     * Status string to display. */
    protected String status = "";
    /** 
     * Field to indicate whose turn it is in status. */
    protected boolean turn;
    /** Returns a view with no board attached. */
    public View() { 
	setStyle(lastStyle);
	addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    if (e.getButton() == MouseEvent.BUTTON3) {
			new Thread(new Runnable() {
				public void run() {
				    Style style = new StyleManager().getStyle(View.this, lastStyle);
				    if (style != null) {
					setStyle(style);
					//((JFrame) getParent().getParent()).pack();
				    }
				}
			    }).start();
			
		    }
		}
	    });
    }
    public Dimension getPreferredSize() { 
	return dim; 
    }
    public Style getStyle() 
    {
	return s;
    }
    /** Calls {@link javax.swing.JComponent#repaint() repaint()}. */
    public void update(Observable o, Object arg) { 
	repaint(); 
    }
    private void setStyle(Style style) { 
	lastStyle = s = style; 
	if (board != null) 
	    showBoard(board);
    }
    public void paint(Graphics gr) {
	Graphics2D g = (Graphics2D) gr;
	// clear everything and produce the background color
	g.clearRect(0, 0, getWidth(), getHeight());
	g.setColor(s.bgColor);
	g.fillRect(0, 0, getWidth(), getHeight());
	// draw the dots
	g.setColor(s.dotColor);
	for (int i = s.marginTop; i <= s.marginTop + row * s.boxWidth; i += s.boxWidth) 
	    for (int j = s.marginLeft; j <= s.marginLeft + column * s.boxWidth; j += s.boxWidth) 
		g.fillOval(j, i, s.dotWidth, s.dotWidth);
	// draw the edges
	g.setColor(s.lineColor);
	g.setStroke(new BasicStroke(s.lineWidth));
	try {
	    for (int local_row = 0; local_row < row; local_row++) {
		for (int col = 0; col < column; col++) {
		    if ( board.isSelected(col, local_row, Edge.NORTH) ) {
			int xOff = s.marginLeft + col * s.boxWidth;
			int yOff = s.marginTop + local_row * s.boxWidth + s.dotWidth / 2;
			g.drawLine(xOff + s.dotWidth + s.ldSep, yOff,
				   xOff + s.boxWidth - s.ldSep, yOff);
		    }
		    int owner = board.getOwner(col, local_row);
		    if ( owner != -1 ) {
			int xOff = s.marginLeft + s.dotWidth + (s.boxWidth - s.dotWidth) / 4 + col * s.boxWidth;
			int yOff = s.marginTop + s.dotWidth + (s.boxWidth - s.dotWidth) / 4 + local_row * s.boxWidth;
			int width = (s.boxWidth - s.dotWidth) / 2;
			g.setColor(s.plColor[owner]);
			g.fillOval(xOff, yOff, width, width);
			g.setColor(s.lineColor);
		    }
		    if ( board.isSelected(col, local_row, Edge.WEST) ) {
			int xOff = s.marginLeft + col * s.boxWidth + s.dotWidth / 2;
			int yOff = s.marginTop + local_row * s.boxWidth;
			g.drawLine(xOff, yOff + s.dotWidth + s.ldSep,
				   xOff, yOff + s.boxWidth - s.ldSep);
		    }
		}
		if ( board.isSelected(column - 1, local_row, Edge.EAST) ) {
		    int xOff = s.marginLeft + column * s.boxWidth + s.dotWidth / 2;
		    int yOff = s.marginTop + local_row * s.boxWidth;
		    g.drawLine(xOff, yOff + s.dotWidth + s.ldSep,
			       xOff, yOff + s.boxWidth - s.ldSep);
		}
	    }
	    for (int col = 0; col < column; col++) {
		if ( board.isSelected(col, row - 1, Edge.SOUTH) ) {
		    int xOff = s.marginLeft + col * s.boxWidth;
		    int yOff = s.marginTop + row * s.boxWidth + s.dotWidth / 2;
		    g.drawLine(xOff + s.dotWidth + s.ldSep, yOff,
			       xOff + s.boxWidth - s.ldSep, yOff);
		}
	    } 
	    int score[] = board.getScore();
	    status = "Player 1: " + score[RmtBoard.PLAYER_1] + " - Player 2: " + score[RmtBoard.PLAYER_2];
	    if (board.isFull()) 
		status += "   GAME OVER!";
	    else
		status += (turn) ? "   Your turn!" : "                ";
	    g.setColor(s.lineColor);
	    g.drawString(status, s.marginLeft / 2 , 15);
	}
	catch (RemoteException e) { 
	    e.printStackTrace();
	}
    }
    /** Call this to register a board. Then add this view as Observer*/
    public void showBoard(RmtBoard board) {
	this.board = board;
	try {
	    column = board.getSizeX();	
	    row = board.getSizeY();
	}
	catch (RemoteException e) {
	    e.printStackTrace();
	}
	dim = new Dimension(2 * s.marginLeft + s.boxWidth * column + s.dotWidth, 2 * s.marginTop + s.dotWidth + s.boxWidth * row);
	repaint();
    }
}
