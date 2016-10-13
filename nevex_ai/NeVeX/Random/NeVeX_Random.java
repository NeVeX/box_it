import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NeVeX extends View implements Player {
    private Object lock = new Object();
    private Edge edge;
    
    void setGo(Boolean go)
    {
	try{   
	    if(go)
		status = "Please take your turn    ";
	    else
		status = "                         ";
	    int scores[] = viewBoard.getScore();
	    score = "Player 1: "+scores[0]+"    Player 2: "+scores[1];
	}
	catch (RemoteException ex) {
            ex.printStackTrace();
        }
        repaint();
    }
    
    /** Creates a new instance of Nevex */
    public NeVeX() {
        
        System.out.println("nevex PLAYER");
        //addMouseListener(mouseAdapter);
    }
    
    public void setBoard(RmtBoard b) throws RemoteException
    {
        showBoard(b);
        JFrame frame = new JFrame();
        frame.setTitle("The NeVeX Experience");
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update() throws RemoteException
    {
            this.update(null, null);
    }
    
    public Edge pickEdge() throws RemoteException
    {
        Edge tempEdge = null;
        setGo(true);
        while(tempEdge == null) {   
                  tempEdge = start_the_fun();
                  System.out.println("got an edge");

        }

        
        System.out.println("got an edge");
        setGo(false);
        return tempEdge;
}
        
      private Edge start_the_fun() {
	    Edge tempEdge = null;
            boolean proceed = true;
            boolean wrong_edge = true;
            int rand_col = 0, rand_row = 0, rand_edge = 0;
            int seed = 976;
            
            while(proceed) {
            // toPlay = new Random().nextInt(2);   
            rand_col = (seed * (int) (Math.random()*seed)) % column;

            rand_row =  (seed * (int) (Math.random()*seed)) % row;

            rand_edge =  (seed * (int) (Math.random()*seed)) % 4;
            seed++;
            
            
             System.out.println("col = "+rand_col);
             System.out.println("row = "+rand_row);
             System.out.println("edge = "+rand_edge);
            try {
                proceed = viewBoard.isSelected(rand_col, rand_row, rand_edge );
            } 
            catch (RemoteException ex) {
                ex.printStackTrace();
            }
    
            }
            tempEdge = new Edge(rand_col, rand_row, rand_edge);

            return tempEdge;

	   

        }
}
    
