/**
 *
 *
 * @ aMarXproduction partnered with Mickey's Mickeys Inc.
 *
 *
 *
 * presents The NeVeX Experience
 *      powered by Chain Hunter
 *
 *
 *
 */

import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.File;
import java.lang.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;


public class NeVeX extends View implements Player {
   private Object lock = new Object();
   private Edge edge;
   boolean never_search_neutral_again = false;
   //int temp_col = 0, temp_row = 0, temp_edge = 0;
   int seed = 976, my_limit;
   boolean player_first = true, search_initial = true;
   /*Chain Hunter Stuff, don't touch Mark*/
   int chainAmount, edgeAmount, boxCount, chainCounter, boxCounter, nextChain, nextBox,
       pchainAmount, pboxCount, pchainCounter, pboxCounter, limit, is_it_potential;
   boolean  validCoord = false;
   Edge chainData[][], pchainData[][], tempChainData[][], coordinates;


   
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
                 tempEdge = NeVeX_Computing();
       }

       setGo(false);
       return tempEdge;
}

private Edge NeVeX_Computing() throws RemoteException {
    // player 1 == pink
    // player 2 == green
           Edge tempEdge = null;
           int i;
           int my_col,my_row;
           int box_edges, above_box_edges, below_box_edges,
            left_box_edges, right_box_edges;
           boolean box_edge[][][] = new boolean[column][row][4];
           //int neutral_edge[][][] = new int [column][row][1];
           boolean neutral_edge_exists = false;
           int neutral_edge_row = 0, neutral_edge_col = 0, neutral_edge = 0;
           int start_col, start_row;
           boolean proceed = true;
           boolean wrong_edge = true;
           int rand_col = 0, rand_row = 0, rand_edge = 0;
           int search_loop;
           int delete = 10, start_edge = 0;
           //int temp_col = 0, temp_row = 0, temp_edge = 0;
           boolean picked = false;
           int edges_left = 0;

            huntChains(0,0);
           //int potential_chain[][] = new int[delete][delete];
           int chain_length[] = new int[chainAmount*2];
           int p_chain_length[] = new int[pchainAmount*2];
           //int chain[][] = new int[delete][delete];
           boolean is_there_chain = false;
           boolean is_there_potential_chain = false;
           int smallest_potential = 0, potential_chain_use = 0, temp_smallest = 0;
           int smallest_chain = 0, temp_smallest_chain = 0, chain_use = 0;


           // call mick's chain hunter here
           //System.out.println("-------going into chainhunter");
           //new ChainHunter();
           
            
           
          // System.out.println("-------coming out of chainhunter");
           // and then access his fields ??????


          // the sound for selecting edges
         /*  try{
               AudioInputStream stream = AudioSystem.getAudioInputStream(new
        File("c:\\test1.wav"));
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
   if ( search_initial == true) {     
       for ( my_col = 0; my_col < row; my_col++) { // row first
           for ( my_row = 0; my_row < column; my_row++) {
               if (    (viewBoard.isSelected(my_row, my_col, 0))||
                       (viewBoard.isSelected(my_row, my_col, 1)) ||
                       (viewBoard.isSelected(my_row, my_col, 2)) ||
                       (viewBoard.isSelected(my_row, my_col, 3))) {
                   player_first = false;
               }
           }
       }
       search_initial = false;
       
        if ( player_first == false) {
               System.out.println("---------I WAS SECOND TO START---------");
               System.out.println("---------SECOND PLAYER IS GREEN--------");
            }
        else {
           System.out.println("---------I WAS FIRST TO START---------");
           System.out.println("---------FIRST PLAYER IS PINK--------");
        }
   }
   
   
   

       // THE NEUTRAL EDGE CASE:

          start_col = (seed * (int) (Math.random()*seed)) % row;
          start_row = (seed * (int) (Math.random()*seed)) % column;

   if ( never_search_neutral_again == false ) {
      loop_edge_search: for (search_loop =0; search_loop < 2; search_loop++) {
          for ( my_col = start_col; my_col < row; my_col++) { // row first
               for ( my_row = start_row; my_row < column; my_row++) {

                   box_edges=0; above_box_edges=0; below_box_edges=0;
                    left_box_edges=0; right_box_edges=0;

                   if (!viewBoard.isSelected(my_row, my_col, 0)) {
                       //System.out.println("this box has north edge not selected");
                       box_edges++;
                   }
                   if (!viewBoard.isSelected(my_row, my_col, 1)) {
                        //System.out.println("this box has EAST edge not selected");
                       //box_edge[my_row][my_col][1] = false;
                       box_edges++;
                   }
                   if (!viewBoard.isSelected(my_row, my_col, 2)) {
                        //System.out.println("this box has SOUTH edge not selected");
                       //box_edge[my_row][my_col][2] = false;
                       box_edges++;
                   }
                   if (!viewBoard.isSelected(my_row, my_col, 3)) {
                        //System.out.println("this box has WEST edge not selected");
                       //box_edge[my_row][my_col][3] = false;
                       box_edges++;
                   }


                   if (box_edges >= 3) {  // box is good, but check neighbouring boxes

                       if (my_col > 0) { // top of box
                           if ((viewBoard.isSelected(my_row, my_col,0))) { // obviously need to check
                                   above_box_edges = 0;
                           }
                           else {
                               if (!viewBoard.isSelected(my_row,my_col-1, 0)) {
                                   above_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row,my_col-1, 1)) {
                                   above_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row,my_col-1, 2)) {
                                   above_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row,my_col-1, 3)) {
                                   above_box_edges++;
                               }
                           }
                       }
                       if (my_row < column - 1) { // east side of board
                           if ((viewBoard.isSelected(my_row, my_col,1))) { // obviously need to check
                                   right_box_edges = 0;
                           }
                           else {
                               if (!viewBoard.isSelected(my_row+1,my_col, 0)) {
                                   right_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row+1,my_col, 1)) {
                                   right_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row+1,my_col, 2)) {
                                   right_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row+1,my_col, 3)) {
                                   right_box_edges++;
                               }
                          }
                       }

                       if (my_col < row - 1) { // very bottom of board
                           if ((viewBoard.isSelected(my_row, my_col,2))) { // obviously need to check
                                   below_box_edges = 0;
                           }
                           else {
                               if (!viewBoard.isSelected(my_row,my_col+1, 0)) {
                                   below_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row,my_col+1, 1)) {
                                   below_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row,my_col+1, 2)) {
                                   below_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row,my_col+1, 3)) {
                                   below_box_edges++;
                               }
                           }
                       }

                       if (my_row > 0) { // west side
                           if ((viewBoard.isSelected(my_row, my_col,3))) { // obviously need to check
                                   left_box_edges = 0;
                           }
                           else {
                               if (!viewBoard.isSelected(my_row-1,my_col, 0)) {
                                   left_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row-1,my_col, 1)) {
                                   left_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row-1,my_col, 2)) {
                                   left_box_edges++;
                               }
                               if (!viewBoard.isSelected(my_row-1,my_col, 3)) {
                                   left_box_edges++;
                               }
                           }
                       }
                   }
                       if ( above_box_edges >= 3 || (box_edges >= 3&& my_col == 0 && (!viewBoard.isSelected(my_row, my_col, 0)))) {
                            //north edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 0;
                           neutral_edge_exists = true;
                           break loop_edge_search;
                       }
                       else if ( right_box_edges >= 3 || (box_edges>= 3 && my_row == (column-1) && (!viewBoard.isSelected(my_row, my_col,1)))) {  //right edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 1;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }
                       else if ( below_box_edges >= 3 || (box_edges>= 3 && my_col == (row-1) && (!viewBoard.isSelected(my_row, my_col,2)))) {  //bottom edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 2;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }
                       else if ( left_box_edges >= 3 || (box_edges >=3 && my_row == 0 && (!viewBoard.isSelected(my_row, my_col, 3)))) {//left edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 3;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }


                       //System.out.println("row = "+my_row+ " andcol is "+my_col);
               }
          }

          // if random edge search didnt work restart loop with original values
         // System.out.println("--------RANDOM DIDN'T WORK -------");
          start_col = 0;
          start_row = 0;
      }
   }
    // neutral edge is either found or doesn't exist be this stage
    if ( !neutral_edge_exists ) {
        this.never_search_neutral_again = true;
       }
          
          
    // FIND IF THERE ARE ANY CHAINS
          
   /*       int chainAmount, edgeAmount, boxCount, chainCounter, boxCounter, nextChain, nextBox,
       pchainAmount, pboxCount, pchainCounter, pboxCounter, limit, is_it_potential;
   boolean  validCoord = false;
   Edge chainData[][], pchainData[][], tempChainData[][], coordinates;      */
          
  
  /* System.out.println("chainamount = "+chainAmount);
   System.out.println("edgeAmount = "+edgeAmount);
   System.out.println("boxCount = "+boxCount);
   System.out.println("chainCounter = "+chainCounter);
   System.out.println("boxCounter = "+boxCounter);
   System.out.println("pchainAmount = "+pchainAmount);
   System.out.println("pboxCount = "+pboxCount);
   System.out.println("pchainCounter = "+pchainCounter);
   System.out.println("pboxCounter = "+pboxCounter);    */
    
   edges_left = (((row+1)*column) + ((column+1)*row));   // edges on the board    

   
   if (chainAmount >= 1) {  // get the length of the chains
       is_there_chain = true;
       int length_chain = 1;
       int chain_number, box = 2;
       
       for (chain_number = 1; chain_number <= chainAmount; chain_number++ ) {
           while( chainData[chain_number][box].getCol() <= limit ) {
               length_chain++;
               box++;
           }
           chain_length[chain_number] = length_chain; 
           //System.out.println("chain length for chain "+chain_number+" is "+chain_length[chain_number]);
       }   
   }
   if (pchainAmount >= 1) {  // get the length of the potential chains
       is_there_potential_chain = true;
       int p_length_chain = 1;
       int p_chain_number, box = 2;
       
       for (p_chain_number = 1; p_chain_number <= pchainAmount; p_chain_number++ ) {
           while( pchainData[p_chain_number][box].getCol() <= limit ) {
               p_length_chain++;
               box++;
           }
           p_chain_length[p_chain_number] = p_length_chain; 
           //System.out.println("chain length for chain "+p_chain_number+" is "+p_chain_length[p_chain_number]);
       } 
   }
   
    // chains and potential chains
   if (is_there_chain == true && is_there_potential_chain == true && picked == false) {
       if ( neutral_edge_exists == true) {
           System.out.println("PICKING ALL THE CHAINS AS THERE'S A NEUTRAL EDGE 1");
           tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
           picked = true;
       }
       else { // get all chains till the last one where we check if the p.chain is really small
           temp_smallest_chain = chain_length[1];
           chain_use = 1;

           for ( i = 2; i <= chainAmount; i++) {
               smallest_chain = chain_length[i];
               if(smallest_chain < temp_smallest_chain) {
                   chain_use = i;
               }
           }
           // check if we are in last chain i.e chain[] is not > 0

           if (chainAmount == 1)  { // we are in last chain
               
               temp_smallest = p_chain_length[1]; // first p.chain
               potential_chain_use = 1;

            for( i = 2; i <= pchainAmount; i++) {
              smallest_potential = p_chain_length[i];
               if (smallest_potential < temp_smallest) {
                   potential_chain_use = i;
               }
            }
               // change the following lines, maybe better not to finish a chain, rather have the correct number of chains left
               // we could also need to follow the next few lines
            if (p_chain_length[potential_chain_use] < 3)  { //only two boxes to scarafice
                   System.out.println("SCARAFICING A CHAIN 2");
                 tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
                 picked = true;
            }
            else if(chain_length[chain_use] == 2 && chainAmount == 1) {    // double cross move here
                 if ( edges_left == 2) { // just select the last edges 
                     System.out.println("SELECTING LAST EDGES IN LAST CHAIN 3"); 
                    tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
                    picked = true;
                 }
                 else {
                     System.out.println("DOUBLE CROSSING 4");
                     tempEdge = new Edge(chainData[chain_use][2].getCol(),chainData[chain_use][2].getRow(),chainData[chain_use][2].getEdge());
                     picked = true;
                 }
            }
           }
           else {
               System.out.println("CHOOSING SMALLEST CHAIN EDGES 5"); 
               tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
               picked = true;
           }
       }
   }
  // only potential chain
   else if (is_there_chain == false && is_there_potential_chain ==true && picked == false)  { // no chain but only p.chains

       if (neutral_edge_exists == true)  {   // neutral edgee exists
           System.out.println("PICKING A NEUTRAL EDGE 6");
           System.out.println("row is "+neutral_edge_row+" col is "+neutral_edge_col+" edge is "+neutral_edge);
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);
           picked = true;
       }
       else {  // find smallest potential chain and give it away
           temp_smallest = p_chain_length[1]; // first p.chain
           potential_chain_use = 1;
           System.out.println("pchain length "+1+" is "+p_chain_length[1]);
           for( i = 2; i <= pchainAmount; i++) {
               smallest_potential = p_chain_length[i];
               System.out.println("pchain length "+i+" is "+p_chain_length[i]);
               if (smallest_potential < temp_smallest) {
                   potential_chain_use = i;
               }
           }
            System.out.println("pchain amount is "+pchainAmount); 
           System.out.println("PICKING SMALLEST POTENTIAL CHAIN EDGES 7"); 
           System.out.println("row is "+pchainData[potential_chain_use][1].getRow()+" col is "+pchainData[potential_chain_use][1].getCol()+" edge is "+pchainData[potential_chain_use][1].getEdge());
         tempEdge = new Edge(pchainData[potential_chain_use][1].getCol(),pchainData[potential_chain_use][1].getRow(),pchainData[potential_chain_use][1].getEdge());
         picked = true;
       }
   }
    // only exists a chain
   else if (is_there_chain == true && is_there_potential_chain == false && picked == false) {
      if ( neutral_edge_exists == true) {
           // select all the chains
          System.out.println("PICKING ALL THE CHAINS EDGES 8"); 
           tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
           picked = true;
       }
       else { //select chains starting with smallest
           temp_smallest_chain = chain_length[1];
           chain_use = 1;
           for ( i = 2; i <= chainAmount; i++) {
               smallest_chain = chain_length[i];
               if(smallest_chain < temp_smallest_chain) {
                   chain_use = i;
               }
           }
           if(chain_length[chain_use] == 2 && chainAmount == 1 ){    // double cross move here
               if ( edges_left == 2) { // just select the last edges 
                   System.out.println("PICKING THE LAST CHAINS LAST EDGES 9"); 
                    tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
                    picked = true;
                 }
               else {
                 System.out.println("DOUBLE CROSSING 10");
                 tempEdge = new Edge(chainData[chain_use][2].getCol(),chainData[chain_use][2].getRow(),chainData[chain_use][2].getEdge());
                 picked = true;
               }
           }
           else {
               System.out.println("PICKING SMALLEST CHAINS EDGES 11"); 
               tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
               picked = true;
           }
       }

   }
          
    
   // no chains or potential chains
   if (is_there_chain == false && is_there_potential_chain == false && neutral_edge_exists == true && picked == false) {
            System.out.println("PICKING NEUTRAL EDGE 12"); 
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);    // this is correct although names say otherwise
           picked = true;
   }    
   else if (picked == false) {
                System.out.println("PICKING RANDOM EDGE 13"); 
              seed++;    
              start_col = (seed * (int) (Math.random()*seed)) % column;
              start_row = (seed * (int) (Math.random()*seed)) % row;
              start_edge = (seed * (int) (Math.random()*seed)) % 4;

              tempEdge = new Edge(start_col, start_row, start_edge);
              picked = true;
   }

    return tempEdge;
    
} // nevex_computing end


            /********************************************/
            /*        CHAIN HUNTER ALGORITHM            */
            /********************************************/


    public void huntChains(int c, int r) throws RemoteException {
   


        //System.out.println("-------IM IN THE chainhunter----------");

         int edgeCount = 0, unselect =0, i,j,k;                       //i = column, j = row, k = edge
         limit = column*row; pchainAmount = 0; chainAmount = 0;                     //Assigning varriables
         chainData = new Edge[limit+1][limit+1]; pchainData = new Edge[limit+1][limit+1];
         
         
         for(int a=0;a<limit;a++) {
             for(int b=0;b<limit;b++) {
                 chainData[a][b] = new Edge(limit+1,limit+1,0);      //Assigns a value that will never be used to each array of the data arrays
                 pchainData[a][b] = new Edge(limit+1,limit+1,0);     //Basically acts like a NULL value
             }
         }
         my_limit = limit;
          for(i=c;i<column;i++) {
                for(j=r;j<row;j++) {                                //Loop for searching board for chains
                  for(k=0;k<4;k++) {
                          if(viewBoard.isSelected(i,j,k) == true) {
                              edgeCount++;                              //Determines how many edges in box 
                          }
                          else
                              unselect = k;                         //Gives a name to the unselected edge
                   }
                   if(edgeCount == 3) {
                      chainAmount++;                                            //Finds beginning of chain and records its coords & edge
                      boxCount = 1;
                      chainData[chainAmount][boxCount] = new Edge(i,j,unselect);
                      //System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
                      //+chainData[chainAmount][boxCount].getRow()+chainData[chainAmount][boxCount].getEdge());                        //Some of your code in here
                      stalkChains(i, j, unselect);                  //Goes in to find the neighbouring box
                   }
                  edgeCount = 0;
                }
         }
                  
              for(i=c;i<column;i++) {
                for(j=r;j<row;j++) {        //Counts board for pot. chain data
                  for(k=0;k<4;k++) {
                          if(viewBoard.isSelected(i,j,k) == true) {  //Determines amount of edges in box & chooses the unselected edge
                              edgeCount++;                           //of a possible two
                          }
                          else
                              unselect = k;
                   }
                  
                   if(edgeCount == 2) {
                      coordinates = new Edge(i,j,unselect);
                      validCoord = true;                        //Finds a box for a pot. chain & checks that it isn't already recorded in chain data
                      for(int a=0;a<limit;a++) {
                          for(int b=0;b<limit;b++) {
                              if(coordinates.getCol() == chainData[a][b].getCol() && coordinates.getRow() == chainData[a][b].getRow()) {
                                    validCoord = false;
                              }
                          }
                      }
                      for(int a=0;a<limit;a++) {
                          for(int b=0;b<limit;b++) {            //Checks that the box isn't already recorded in pot. chain data
                              if(coordinates.getCol() == pchainData[a][b].getCol() && coordinates.getRow() == pchainData[a][b].getRow()) {
                                    validCoord = false;
                              }
                          }
                      }
                      if(validCoord == true) {
                                        pchainAmount++;
                                        is_it_potential = 1;   //If box isn't already recorded goes on to check it's neighbouring box
                                        stalkChains(i, j, unselect);
                                        validCoord = false;
                      }
                   }
                   validCoord = false;
                   edgeCount = 0;
             }
         }
         //regurgatateChains();
    }

    public void eatChains(int cols, int rows, int prevEdge) throws RemoteException {
          int nextEdge = 4, edgeAmount = 0;


          for(int k=0;k<4;k++) {
              // IT DOES NOT LIKE THIS LINE MICK I think - it might be a box type chain think (i.e. a circle)
              if(viewBoard.isSelected(cols,rows,k) == true) {  //Determines amount of edges in the current box
                  edgeAmount++;
              }
          }

          for(int k=0;k<4;k++) {
              if(edgeAmount == 2 && k != prevEdge && viewBoard.isSelected(cols,rows,k) == false) {
                   nextEdge = k;                                //Gets the next unselected edge of that box
              }
          }

          if(edgeAmount == 2)  {

                if(boxCount >= 1) {
                    boxCount++;                                 //Records coordinates of the current box if in a chain
                    chainData[chainAmount][boxCount] = new Edge(cols,rows,nextEdge);
                    //System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
                    //+chainData[chainAmount][boxCount].getRow()+chainData[chainAmount][boxCount].getEdge());
                }
                else if(is_it_potential == 2) {
                    pboxCount++;                                //Records coordinates of the current box if in pot. chain
                    pchainData[pchainAmount][pboxCount] = new Edge(cols,rows,nextEdge);
                    //System.out.println("pot. chainData for pot. chain "+pchainAmount+ " is "+pchainData[pchainAmount][pboxCount].getCol()
                    //+pchainData[pchainAmount][pboxCount].getRow()+pchainData[pchainAmount][pboxCount].getEdge());
                }
                stalkChains(cols, rows, nextEdge);              //Goes on to check next box
          }

          else if(is_it_potential == 1)  {
                is_it_potential++;                          //Determines if the box is the start of a pot. chain
                stalkChains(cols, rows, prevEdge);
          }
          else if(is_it_potential == 2) {
                is_it_potential = 0;
                pboxCount = 0;                              //Determines if the box is the end of a pot. chain
                return;
          }
          else 
                 boxCount=0;                                //Box is end of a chain
                 return;
}
          
    public void stalkChains(int cols, int rows, int nextEdge) throws RemoteException {
        
              /* NORTH SEARCH */
              
              if( nextEdge == Edge.NORTH) {
                if(rows > 0) {
                  rows--;                       //If box has north edge unselected, as long a it's not in row zero, neighbouring north box is selected 
                  eatChains(cols, rows, 2);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;          //Determines if the box is start of a pot. chain & sends it back to get it's neighbour
                  eatChains(cols, rows, 0);
                }
                else {
                  boxCount=0;                      //box is end of a chain so terminates and resets all necessary variables
                  pboxCount = 0;
                  is_it_potential = 0;
                  return;
                }
              }

              /*EAST SEARCH*/

              if( nextEdge == Edge.EAST) {
                if(cols < column-1) {
                  cols++;
                  eatChains(cols, rows, 3);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;
                  eatChains(cols, rows, 1);
                }
                else {
                  boxCount=0;
                  pboxCount = 0;
                  is_it_potential = 0;
                  return;
                }
              }
              
              /*SOUTH SEARCH*/

              if( nextEdge == Edge.SOUTH) {
                if(rows < row-1) {
                  rows++;                               //This search algorithm does the same as above search except with different edge & end column or row
                  eatChains(cols, rows,0);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;
                  eatChains(cols, rows,2);
                }
                else {
                  boxCount=0;
                  pboxCount = 0;
                  is_it_potential = 0;
                  return;
                }
              }
              
              /*WEST SEARCH*/
              
              if( nextEdge == Edge.WEST) {
                if(cols > 0) {
                  cols--;
                  eatChains(cols, rows, 1);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;
                  eatChains(cols, rows, 3);
                }
                else {
                  boxCount=0;
                  pboxCount = 0;
                  is_it_potential = 0;
                return;
                }
              }
}
    //Following method isn't used just ignore it
    public void regurgatateChains() throws RemoteException {

       // System.out.println("Im in");
        
        int next = 0, limit = (column*row), cols, rows, edges;
        tempChainData = new Edge[limit+1][limit+1];
        
        for(int x=0;x<(column*row);x++) {
            for(int y=0;y<(column*row);y++) {
                if(chainData[x][y].getCol() == limit+1 && chainData[x][y].getRow() == limit+1) {
                    //System.out.println("in here aswell");
                    tempChainData[x][y] = chainData[x][y];
               
                for(int chain=next;chain<(column*row);chain++) {
                    for(int box=next;box<(column*row);box++) {
                        if(chainData[chain][box].getCol() != limit+1 && chainData[chain][box].getRow() != limit+1) {
                        //System.out.println("and here");
                        cols = chainData[x][y].getCol();
                        rows = chainData[x][y].getRow();
                        edges = chainData[x][y].getEdge();
                        tempChainData[x][y] = new Edge(cols, rows, edges);
                        chainData[x][y] = tempChainData[x][y];

                        next++;
                        }
                        }
                    }
                }
            }
        }
        
       for(int i=0;i<(column*row);i++) {
            for(int j=0;j<(column*row);j++) {
                //System.out.println("sorted chain data is"+chainData[i][j].getCol()+chainData[i][j].getRow()+chainData[i][j].getEdge());
            } 
       }
    }
}