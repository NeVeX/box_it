import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


public class NeVeX extends View implements Player {
    private Object lock = new Object();
    private Edge edge;
    boolean never_search_neutral_again = false;
    
    void setGo(Boolean go) {
	try{   
	    if(go)
		status = "The NeVeX Engine Is Computing....    ";
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
    
public void setBoard(RmtBoard b) throws RemoteException {
        showBoard(b);
        JFrame frame = new JFrame();
        frame.setTitle("The NeVeX Experience");
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        
    }
    
public void update() throws RemoteException {
            this.update(null, null);
    }
    
public Edge pickEdge() throws RemoteException {
        Edge tempEdge = null;
        setGo(true);
        
        while(tempEdge == null) {   
                  tempEdge = start_the_fun();
        }
        
        setGo(false);
        return tempEdge;
}
        
private Edge start_the_fun() throws RemoteException {
	    Edge tempEdge = null;
            int my_col,my_row;
            int box_edges, above_box_edges, below_box_edges, left_box_edges, right_box_edges;
            boolean box_edge[][][] = new boolean[column][row][4];
            //int neutral_edge[][][] = new int [column][row][1];
            boolean neutral_edge_exists = false;
            int neutral_edge_row = 0, neutral_edge_col = 0, neutral_edge = 0;
            int start_col, start_row;
            boolean proceed = true;
            boolean wrong_edge = true;
            int rand_col = 0, rand_row = 0, rand_edge = 0;
            int seed = 976, search_loop;
            
           // the sound for selecting edges 
          /*  try{      
		AudioInputStream stream = AudioSystem.getAudioInputStream(new File("c:\\test1.wav"));      
		AudioFormat format = stream.getFormat();      
		DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());      
		Clip clip = (Clip) AudioSystem.getLine(info);         
		clip.open(stream);      
		clip.start();
	} catch (Exception e)
	{      
	 	e.printStackTrace();    
	}  
        try {
            //Thread.sleep(3000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
           */
                
            // THE NEUTRAL EDGE CASE:
            
           start_col = (seed * (int) (Math.random()*seed)) % row;
           start_row = (seed * (int) (Math.random()*seed)) % column;
            
    if ( never_search_neutral_again == false ) {
       loop_edge_search: for (search_loop =0; search_loop < 2; search_loop++) {
           for ( my_col = start_col; my_col < row; my_col++) { // row first
                for ( my_row = start_row; my_row < column; my_row++) {
                    
                    box_edges=0; above_box_edges=0; below_box_edges=0; left_box_edges=0; right_box_edges=0;
                    
                    if (!viewBoard.isSelected(my_row, my_col, 0)) {
                        System.out.println("this box has north edge not selected");
                        box_edges++;
                    }
                    if (!viewBoard.isSelected(my_row, my_col, 1)) {
                         System.out.println("this box has EAST edge not selected");
                        //box_edge[my_row][my_col][1] = false;
                        box_edges++;
                    }
                    if (!viewBoard.isSelected(my_row, my_col, 2)) {
                         System.out.println("this box has SOUTH edge not selected");
                        //box_edge[my_row][my_col][2] = false;
                        box_edges++;
                    }
                    if (!viewBoard.isSelected(my_row, my_col, 3)) {
                         System.out.println("this box has WEST edge not selected");
                        //box_edge[my_row][my_col][3] = false;
                        box_edges++;
                    }

                             
                    if (box_edges >= 3) {  // box is good, but check neighbouring boxes
                        
                        if (my_col > 0) { // top of box
                            if ((viewBoard.isSelected(my_row, my_col, 0))) { // obviously need to check
                                    above_box_edges = 0;
                            }
                            else {
                                if (!viewBoard.isSelected(my_row, my_col-1, 0)) {
                                    above_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row, my_col-1, 1)) {
                                    above_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row, my_col-1, 2)) {
                                    above_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row, my_col-1, 3)) {
                                    above_box_edges++;
                                }
                            }
                        }
                        if (my_row < row - 1) { // east side of board
                            if ((viewBoard.isSelected(my_row, my_col, 1))) { // obviously need to check
                                    right_box_edges = 0;
                            }
                            else {
                                if (!viewBoard.isSelected(my_row+1, my_col, 0)) {
                                    right_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row+1, my_col, 1)) {
                                    right_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row+1, my_col, 2)) {
                                    right_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row+1, my_col, 3)) {
                                    right_box_edges++;
                                }
                           }
                        }
                        
                        if (my_col < column - 1) { // very bottom of board
                            if ((viewBoard.isSelected(my_row, my_col, 2))) { // obviously need to check
                                    below_box_edges = 0;
                            }
                            else {
                                if (!viewBoard.isSelected(my_row, my_col+1, 0)) {
                                    below_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row, my_col+1, 1)) {
                                    below_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row, my_col+1, 2)) {
                                    below_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row, my_col+1, 3)) {
                                    below_box_edges++;
                                }
                            }
                        }
                         
                        if (my_row > 0) { // west side
                            if ((viewBoard.isSelected(my_row, my_col, 3))) { // obviously need to check
                                    left_box_edges = 0;
                            }
                            else {
                                if (!viewBoard.isSelected(my_row-1, my_col, 0)) {
                                    left_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row-1, my_col, 1)) {
                                    left_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row-1, my_col, 2)) {
                                    left_box_edges++;
                                }
                                if (!viewBoard.isSelected(my_row-1, my_col, 3)) {
                                    left_box_edges++;
                                }
                            }
                        }
                    }
                        if ( above_box_edges >= 3 || (box_edges >= 3 && my_col == 0 && (!viewBoard.isSelected(my_row, my_col, 0)))) {  //north edge can be selected
                            neutral_edge_row = my_row;
                            neutral_edge_col = my_col;
                            neutral_edge = 0;
                            neutral_edge_exists = true;
                            break loop_edge_search;
                        }
                        else if ( right_box_edges >= 3 || (box_edges >= 3 && my_row == (column-1) && (!viewBoard.isSelected(my_row, my_col, 1)))) {  //right edge can be selected
                            neutral_edge_row = my_row;
                            neutral_edge_col = my_col;
                            neutral_edge = 1;
                            neutral_edge_exists = true;
                        break loop_edge_search;
                        }
                        else if ( below_box_edges >= 3 || (box_edges >= 3 && my_col == (row-1) && (!viewBoard.isSelected(my_row, my_col, 2)))) {  //bottom edge can be selected
                            neutral_edge_row = my_row;
                            neutral_edge_col = my_col;
                            neutral_edge = 2;
                            neutral_edge_exists = true;
                        break loop_edge_search;
                        } 
                        else if ( left_box_edges >= 3 || (box_edges >= 3 && my_row == 0 && (!viewBoard.isSelected(my_row, my_col, 3)))) {  //left edge can be selected
                            neutral_edge_row = my_row;
                            neutral_edge_col = my_col;
                            neutral_edge = 3;
                            neutral_edge_exists = true;
                        break loop_edge_search;
                        }
                    
                        
                        System.out.println("row = "+my_row+ " and col is "+my_col); 
                }                
           }

           // if random edge search didnt work
           System.out.println("--------RANDOM DIDN'T WORK -------");
           start_col = 0; 
           start_row = 0;
       }
    }
            
            if ( !neutral_edge_exists ) {
                this.never_search_neutral_again = true;
            }
                        
            
            if(!never_search_neutral_again) {
            System.out.println("this is neutral edge "+neutral_edge_col+","+neutral_edge_row+","+neutral_edge);
            tempEdge = new Edge(neutral_edge_row, neutral_edge_col, neutral_edge);   
            System.out.println("this is neutral edge "+neutral_edge_col+","+neutral_edge_row+","+neutral_edge);
            }
            else {
                System.out.println("No neutral edge exists");
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


                    }
                
                        
                  

            return tempEdge; 
            

            

	   

        }
}
    
