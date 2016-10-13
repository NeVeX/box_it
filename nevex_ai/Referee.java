/*
 * Referee.java
 *
 * Created on 30 October 2007, 17:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import java.rmi.RemoteException;
import java.util.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author gearoid
 */
public class Referee implements Runnable, Observer {
    private int toPlay;
    private Board board;
    private View view;
    private Player player[] = new Player[2];
    
    Referee(int noOfCols, int noOfRows, Player player1, Player player2)
    {
        try {
            board = new Board(noOfCols, noOfRows);
            player[0] = player1;
            player[1] = player2;
            toPlay = new Random().nextInt(2);
	    UnicastRemoteObject.exportObject(board);
            System.out.println("this is player 0 "+player[0]);
            System.out.println("this is player 1 "+player[1]);            
            player[0].setBoard((RmtBoard) board);
            player[1].setBoard((RmtBoard) board);
        } 
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run()
    {
	try
	{
	    while(!board.isFull())
	    {
		if(board.acceptMove(player[toPlay].pickEdge(), toPlay)==true)
		    toPlay = (toPlay + 1) %2;
	    }
	    player[0].update();
            player[1].update();
	}
	catch (RemoteException ex) {
	    ex.printStackTrace();   
        }
    }

    public void update(Observable o, Object arg){
        try {
            player[0].update();
            player[1].update();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
    
    Board getBoard()
    {
        return board;
    }
    
}
