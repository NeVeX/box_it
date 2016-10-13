package boxit.client.nevex;


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

import boxit.client.human.View;
import boxit.Player;
import boxit.Edge;
import boxit.RmtBoard;

/** aMarXproduction partnered with Mickey's Mickeys Inc presents: <br>
 *<br>
 * The NeVeX 'Version 1' Experience powered by the Chain Hunter.<br>
 *<br>
 * Authors :<br>
 *      Mark Cunningham - NeVeX Version 1 Computing.<br>
 *      Micheal 0' Hara - Chain Hunter.<br>
 *<br>
 */
public class NeVeX_V1 extends View implements Player {
   private Object lock = new Object();
   private Edge edge;
   boolean never_search_neutral_again = false;
   //int temp_col = 0, temp_row = 0, temp_edge = 0;
   int seed = 1034, my_limit, edges_left = 0;
   boolean player_first = true, search_initial = true;
   /*Chain Hunter Stuff, don't touch Mark*/
   int chainAmount, edgeAmount, boxCount, chainCounter, boxCounter, nextChain, nextBox,
       pchainAmount, pboxCount, pchainCounter, pboxCounter, limit, is_it_potential;
   boolean  validCoord = false, loopChain = false, noParameters = false;;
   Edge chainData[][], pchainData[][], loopChainData, coordinates;


/** Sets up the board nicely for NeVeX_V1 and Chain Hunter to devour. */
public void setBoard(RmtBoard b) throws RemoteException {
       showBoard(b);
}
/** Update everything related to the board and game. */
public void update() throws RemoteException {
           this.update(null, null);
   }
/** Calls NeVeX_V1_Computing to ignite and fire up the A.I. Engine. */
public Edge pickEdge() throws RemoteException {
       Edge tempEdge = null;
       while(tempEdge == null) {
                 tempEdge = NeVeX_V1_Computing();
       }
       return tempEdge;
}

/**
 *  Created by Mark Cunningham.<br>
 *  This Artificial Intelligence Engine is used for winning games.<br>
 *  It is the 3rd Strongest A.I. Engine employed by NeVeX.<br>
 *<br>
 *  Returns : The A.I. Engine's selected Edge.<br>
 *<br>
 */
private Edge NeVeX_V1_Computing() throws RemoteException {
    
    
           huntChains(0,0);
           
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
           int start_edge = 0;
           //int temp_col = 0, temp_row = 0, temp_edge = 0;
           boolean picked = false;

           int chain_length[] = new int[chainAmount*2];
           int p_chain_length[] = new int[pchainAmount*2];
           //int my_p_boxes[] = new int[pchainAmount];
           //int my_boxes[] = new int [chainAmount];
           boolean is_there_chain = false;
           boolean is_there_potential_chain = false;
           int smallest_potential = 0, potential_chain_use = 0, temp_smallest = 0;
           int smallest_chain = 0, temp_smallest_chain = 0, chain_use = 0;
           int smallest_chain_now, smallest_potential_chain_now;
           int p_length_chain = 1;
           int p_chain_number = 0, box = 2;
           int length_chain = 1;
           int chain_number = 0, temp_smallest_potential = 0;
           boolean should_double_cross = false;
           int temp_biggest_potential = 0;
           boolean chainAmount_changed = false;
           boolean select_vert_neg = false, select_hori_neg = false, select_vert = false, select_hori = false;
           boolean select_vert_neg_2 = false, select_hori_neg_2 = false, select_vert_2 = false, select_hori_2 = false;
           boolean giving_odd_away = false;   

           // call mick's chain hunter here
           //////System.out.println("-------going into chainhunter");
           //new ChainHunter();
           
            
           
          // ////System.out.println("-------coming out of chainhunter");
           // and then access his fields ??????


          

          
      /*     
   
           for ( i = 1; i <= chainAmount; i++) {
               ////System.out.println("+++++++++++ chainlength of chain["+i+"] is "+my_boxes[i]+"++++++++++");
           }
            for ( i = 1; i <= pchainAmount; i++) {
               ////System.out.println("+++++++++++ pchainlength of pchain["+i+"] is "+my_p_boxes[i]+"++++++++++");
           }
           
          */

           
   edges_left = board.getEdgesLeft();        
    
   if ( search_initial == true) {     
       for ( my_col = 0; my_col < row; my_col++) { // row first
           for ( my_row = 0; my_row < column; my_row++) {
               if (    (board.isSelected(my_row, my_col, 0))||
                       (board.isSelected(my_row, my_col, 1)) ||
                       (board.isSelected(my_row, my_col, 2)) ||
                       (board.isSelected(my_row, my_col, 3))) {
                   player_first = false;
                   
               }
           }
       }
       search_initial = false;
       
        if ( player_first == false) {
              // ////System.out.println("---------NeVeX_v1 WAS SECOND TO START---------");
               //S/ystem.out.println("---------SECOND PLAYER IS GREEN--------");
            }
        else {
           //////System.out.println("---------NeVeX_v1 WAS FIRST TO START---------");
           //////System.out.println("---------FIRST PLAYER IS PINK--------");
        }
   }

   //////System.out.println("-----------------Edges Left On Board is "+edges_left);
   
   

       // THE NEUTRAL EDGE CASE:

          start_col = (seed * (int) (Math.random()*seed)) % row;
          start_row = (seed * (int) (Math.random()*seed)) % column;

   if ( never_search_neutral_again == false ) {
      loop_edge_search: for (search_loop =0; search_loop < 2; search_loop++) {
          for ( my_col = start_col; my_col < row; my_col++) { // row first
               for ( my_row = start_row; my_row < column; my_row++) {

                   box_edges=0; above_box_edges=0; below_box_edges=0;
                    left_box_edges=0; right_box_edges=0;

                   if (!board.isSelected(my_row, my_col, 0)) {
                       //////System.out.println("this box has north edge not selected");
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 1)) {
                        //////System.out.println("this box has EAST edge not selected");
                       //box_edge[my_row][my_col][1] = false;
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 2)) {
                        //////System.out.println("this box has SOUTH edge not selected");
                       //box_edge[my_row][my_col][2] = false;
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 3)) {
                        //////System.out.println("this box has WEST edge not selected");
                       //box_edge[my_row][my_col][3] = false;
                       box_edges++;
                   }


                   if (box_edges >= 3) {  // box is good, but check neighbouring boxes

                       if (my_col > 0) { // top of box
                           if ((board.isSelected(my_row, my_col,0))) { // obviously need to check
                                   above_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row,my_col-1, 0)) {
                                   above_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col-1, 1)) {
                                   above_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col-1, 2)) {
                                   above_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col-1, 3)) {
                                   above_box_edges++;
                               }
                           }
                       }
                       if (my_row < column - 1) { // east side of board
                           if ((board.isSelected(my_row, my_col,1))) { // obviously need to check
                                   right_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row+1,my_col, 0)) {
                                   right_box_edges++;
                               }
                               if (!board.isSelected(my_row+1,my_col, 1)) {
                                   right_box_edges++;
                               }
                               if (!board.isSelected(my_row+1,my_col, 2)) {
                                   right_box_edges++;
                               }
                               if (!board.isSelected(my_row+1,my_col, 3)) {
                                   right_box_edges++;
                               }
                          }
                       }

                       if (my_col < row - 1) { // very bottom of board
                           if ((board.isSelected(my_row, my_col,2))) { // obviously need to check
                                   below_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row,my_col+1, 0)) {
                                   below_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col+1, 1)) {
                                   below_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col+1, 2)) {
                                   below_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col+1, 3)) {
                                   below_box_edges++;
                               }
                           }
                       }

                       if (my_row > 0) { // west side
                           if ((board.isSelected(my_row, my_col,3))) { // obviously need to check
                                   left_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row-1,my_col, 0)) {
                                   left_box_edges++;
                               }
                               if (!board.isSelected(my_row-1,my_col, 1)) {
                                   left_box_edges++;
                               }
                               if (!board.isSelected(my_row-1,my_col, 2)) {
                                   left_box_edges++;
                               }
                               if (!board.isSelected(my_row-1,my_col, 3)) {
                                   left_box_edges++;
                               }
                           }
                       }
                   }
                       if ( above_box_edges >= 3 || (box_edges >= 3&& my_col == 0 && (!board.isSelected(my_row, my_col, 0)))) {
                            //north edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 0;
                           neutral_edge_exists = true;
                           break loop_edge_search;
                       }
                       else if ( right_box_edges >= 3 || (box_edges>= 3 && my_row == (column-1) && (!board.isSelected(my_row, my_col,1)))) {  //right edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 1;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }
                       else if ( below_box_edges >= 3 || (box_edges>= 3 && my_col == (row-1) && (!board.isSelected(my_row, my_col,2)))) {  //bottom edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 2;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }
                       else if ( left_box_edges >= 3 || (box_edges >=3 && my_row == 0 && (!board.isSelected(my_row, my_col, 3)))) {//left edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 3;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }


                       //////System.out.println("row = "+my_row+ " andcol is "+my_col);
               }
          }

          // if random edge search didnt work restart loop with original values
         // ////System.out.println("--------RANDOM DIDN'T WORK -------");
          start_col = 0;
          start_row = 0;
      }
   }
          /*
   ////System.out.println("chainamount = "+chainAmount);
   ////System.out.println("edgeAmount = "+edgeAmount);
   ////System.out.println("boxCount = "+boxCount);
   ////System.out.println("chainCounter = "+chainCounter);
   ////System.out.println("boxCounter = "+boxCounter);
   ////System.out.println("pchainAmount = "+pchainAmount);
   ////System.out.println("pboxCount = "+pboxCount);
   ////System.out.println("pchainCounter = "+pchainCounter);
   ////System.out.println("pboxCounter = "+pboxCounter);    
          
      */    
          
    // neutral edge is either found or doesn't exist be this stage
    if ( !neutral_edge_exists ) {
        this.never_search_neutral_again = true;
       }
         
   if (this.never_search_neutral_again == true) {
            //////System.out.println("--------------------THERE ARE NO MORE NEUTRAL EDGES ---------------- = ");  
   }
             
   if (chainAmount > 0) {  // get the length of the chains
       is_there_chain = true;
       box = 2;
       
       for (chain_number = 1; chain_number <= chainAmount; chain_number++ ) {
           while( chainData[chain_number][box].getCol() <= limit ) {
               length_chain++;
               box++;
           }
           
           chain_length[chain_number] = length_chain; 
           //////System.out.println("chain length for CHAIN "+chain_number+" is "+chain_length[chain_number]);
           //////System.out.println("chain INFO START for CHAIN "+chain_number+" is "+chainData[chain_number][1].getCol()+","+chainData[chain_number][1].getRow()+","+chainData[chain_number][1].getEdge());
           box = 2;
           length_chain = 1;
       }   
   }
   if (pchainAmount > 0) {  // get the length of the potential chains
       is_there_potential_chain = true;
       box = 2;
       
       for (p_chain_number = 1; p_chain_number <= pchainAmount; p_chain_number++ ) {
           while( pchainData[p_chain_number][box].getCol() <= limit ) {
               p_length_chain++;
               box++;
           }
           p_chain_length[p_chain_number] = p_length_chain; 
           //////System.out.println("chain length for POTENTIAL CHAIN "+p_chain_number+" is "+p_chain_length[p_chain_number]);
           //////System.out.println("chain START INFO for POTENTIAL CHAIN "+p_chain_number+" is "+pchainData[p_chain_number][1].getCol()+","+pchainData[p_chain_number][1].getRow()+","+pchainData[p_chain_number][1].getEdge());
           p_length_chain = 1;
           box = 2;
       }
   }   
   if ( player_first == true) { // need to give away a odd chain, one or three
              if ( edges_left % 2 == 0) {
                  if (is_there_chain == false && is_there_potential_chain == true && picked == false) {
            thisloop: for ( i = 1; i <= pchainAmount; i++) {
                // ////System.out.println("this loop 20");
                          if ( p_chain_length[i] == 1 || p_chain_length[i] == 3 ) {
                             // ////System.out.println("giving chain away so that i can flip the game");
                              if ( !board.isSelected(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge())) {
                                  tempEdge = new Edge(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge());
                                  picked = true;
                                  break thisloop;
                                  }
                          }
                      }
                  }
              }
   }
          
   if ( player_first == false) {
          if ( edges_left % 2 == 0) { // need to give away a even chain
               if (is_there_chain == false && is_there_potential_chain == true && picked == false) {
            thisloop: for ( i = 1; i <= pchainAmount; i++) {
                //////System.out.println("this loop 20");
                          if ( p_chain_length[i] == 2 || p_chain_length[i] == 4 ) {
                             // ////System.out.println("giving chain away so that i can flip the game");
                              if ( !board.isSelected(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge())) {
                                tempEdge = new Edge(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge());
                                picked = true;
                                break thisloop;
                              }
                              
                          }
                      }
                  }
          }
   }   

    // chains and potential chains
   if (is_there_chain == true && is_there_potential_chain == true && picked == false) {
       if ( neutral_edge_exists == true) {
           //////System.out.println("PICKING ALL THE CHAINS AS THERE'S A NEUTRAL EDGE 1");
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
                   temp_smallest_chain = smallest_chain;
                    //////System.out.println("chain "+(i)+" has length "+chain_length[i]+" and is smaller than chain "+(i-1)+" with length "+chain_length[i-1]);
               }
           }
           
           if (chainAmount == 2) {
               
                // check when edge is picked horizontially
               if (((chainData[1][1].getRow()+1) == chainData[2][1].getRow()) && (((chainData[1][1].getRow()+1) +chainData[1][1].getCol()+1)  == (chainData[2][1].getRow() + chainData[2][1].getCol()+1))) { // number 1
                   if (chainData[1][1].getCol() < column - 1) {
                            if (!board.isSelected(chainData[1][1].getCol()+1,chainData[1][1].getRow(),2)) {
                                this.chainAmount = 1;
                                chainAmount_changed = true;
                                select_hori = true;
                                //////System.out.println("edge was SELCETED HORIZONTIALLY ");
                            }
                   }
               }
               else if (((chainData[1][1].getCol()+1) == chainData[2][1].getCol()) && (((chainData[1][1].getRow()+1) +chainData[1][1].getCol()+1)  == (chainData[2][1].getRow()+1 + chainData[2][1].getCol()))) { // number 2
                   if (chainData[1][1].getRow() < row - 1) {
                          if (!board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow()+1,1)) {  
                            this.chainAmount = 1;
                            chainAmount_changed = true;
                             select_vert = true;
                            //////System.out.println("edge was SELCETED vertically ");
                          }
                   }
               }
               else if (((chainData[1][1].getRow()-1) == chainData[2][1].getRow()) && (((chainData[1][1].getRow()-1) +chainData[1][1].getCol()-1)  == (chainData[2][1].getRow() + chainData[2][1].getCol()-1))) {
                     if (chainData[1][1].getRow() > 0) {     
                         if (!board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow()-1,3)) {  
                            this.chainAmount = 1;
                            chainAmount_changed = true;
                            select_hori_neg = true;
                            //////System.out.println("edge was SELCETED HORIZONTIALLY the chainamount got changed to one ???????????????????????????????????");
                          }
                     }
               }
               else if (((chainData[1][1].getCol()-1) == chainData[2][1].getCol()) && (((chainData[1][1].getRow()-1) +chainData[1][1].getCol()-1)  == (chainData[2][1].getRow()-1 + chainData[2][1].getCol()))) {
                       if (chainData[1][1].getCol() > 0) {    
                          if (!board.isSelected(chainData[1][1].getCol()-1,chainData[1][1].getRow(),0)) {    
                            this.chainAmount = 1;
                            chainAmount_changed = true;
                             select_vert_neg = true;
                            //////System.out.println("edge was SELCETED vertically the chainamount got changed to one ???????????????????????????????????");
                          }
                       }
               }
               
           }
           
           
           // check if we are in last chain i.e chain[] is not > 0

           if (chainAmount == 1)  { // we are in last chain
               //////System.out.println("IM HERE BABY 13");
               //////System.out.println("chain amount should be 1 here and it is in fact : "+chainAmount);
               temp_smallest_potential = p_chain_length[1]; // first p.chain
               temp_biggest_potential = p_chain_length[1];
               potential_chain_use = 1;

            for( i = 2; i <= pchainAmount; i++) {
                   smallest_potential = p_chain_length[i];
               if (smallest_potential < temp_smallest_potential) {
                   potential_chain_use = i;
                   temp_smallest_potential = smallest_potential;
                  //////System.out.println("pot chain "+(i)+" has length "+p_chain_length[i]+" and is smaller than pot chain "+(i-1)+" with length "+p_chain_length[i-1]);  
               }
               else if (smallest_potential > temp_biggest_potential ) {
                       temp_biggest_potential = smallest_potential;
                       
               }
            }
            if ( chainAmount == 1 ) {
                   if ( temp_biggest_potential >= 3) {
                       should_double_cross = true;
                   }
                   else should_double_cross = false;
            }
               //////System.out.println("IM HERE BABY 12");

               // change the following lines, maybe better not to finish a chain, rather have the correct number of chains left
               // we could also need to follow the next few lines
            if (should_double_cross == false)  { //only two boxes to gain
                // ////System.out.println("SCARAFICING A CHAIN 2");
                 tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
                 picked = true;
            }
            else if ((should_double_cross == true && chain_length[1] == 2) || (should_double_cross == true && chainAmount_changed == true)) { // my system, probably wrong
                     //////System.out.println("DOUBLE CROSSING 4");
                     if ( chainAmount_changed == true) { // inside a box
                         //////System.out.println("DECIDING INSIDE THE BOX");
                         if ( select_hori == true) {
                             if (!board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow(),1)) {
                                 if ( chainData[1][1].getCol() < column - 1) {
                                    if ( !board.isSelected(chainData[1][1].getCol()+1,chainData[1][1].getRow(), 2)) {
                                        //////System.out.println("THIS ONE 1");
                                        tempEdge = new Edge(chainData[1][1].getCol() + 1,chainData[1][1].getRow(), 2);
                                        picked = true;
                                    }
                                 }
                             }
                             else if ( chainData[1][1].getCol() > 0 ) {
                                 if ( !board.isSelected(chainData[1][1].getCol() -1 ,chainData[1][1].getRow(), 2)) {
                                     //////System.out.println("THIS ONE 2");
                                     tempEdge = new Edge(chainData[1][1].getCol()-1,chainData[1][1].getRow(), 2);
                                     picked = true;
                                 }
                             }
                         }
                         else if ( select_vert == true) {
                                 if ( !board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow(), 2)) {
                                     if ( chainData[1][1].getRow() < row -1 ) {
                                         if ( !board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow()+1, 1)) {
                                             //////System.out.println("THIS ONE 3");
                                            tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow()+1, 1);
                                            picked = true;
                                         }
                                     }
                                 }
                                 else if ( chainData[1][1].getRow() > 0 ) {
                                    if (!board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow()-1, 1)) {
                                       // ////System.out.println("THIS ONE 4");
                                         tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow()-1, 1);
                                        picked = true;
                                    }
                                 }
                                 
                         }
                         else if ( select_hori_neg == true) {
                           if ( !board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow(), 0)) {  
                               if ( chainData[1][1].getRow() > 0 ) {
                                 if ( !board.isSelected(chainData[1][1].getCol() ,chainData[1][1].getRow()-1, 3)) {
                                     //////System.out.println("THIS ONE 5");
                                    tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow()-1, 3);
                                    picked = true;
                                 }
                               }
                             
                           }
                           else if ( chainData[1][1].getRow() < row - 1) {
                             if (!board.isSelected(chainData[1][1].getCol() ,chainData[1][1].getRow()+1, 3)) {
                                 ///////System.out.println("THIS ONE 6");
                                 tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow()+1, 3);
                                picked = true;
                             }
                           }
                           
                         }
                         else if (select_vert_neg == true ) {
                           if ( !board.isSelected(chainData[1][1].getCol(),chainData[1][1].getRow(), 3)) {
                               if ( chainData[1][1].getCol() < column - 1 ) {
                                   if ( !board.isSelected(chainData[1][1].getCol()+1,chainData[1][1].getRow(), 0)) {
                                       ////System.out.println("THIS ONE 7");
                                        tempEdge = new Edge(chainData[1][1].getCol()+1,chainData[1][1].getRow(), 0);
                                        picked = true;
                                     }
                               }
                           }
                           else if ( chainData[1][1].getRow() > 0 ) {
                                if ( !board.isSelected(chainData[1][1].getCol() -1 ,chainData[1][1].getRow(), 0)) {
                                    ////System.out.println("THIS ONE 8");
                                     tempEdge = new Edge(chainData[1][1].getCol()-1,chainData[1][1].getRow(), 0);
                                     picked = true;
                                 }
                           }
                         }
                     }
                     else {
                         //////System.out.println("THIS ONE 9");
                        tempEdge = new Edge(chainData[1][2].getCol(),chainData[1][2].getRow(),chainData[1][2].getEdge());
                        picked = true;
                     }
            }
           }
           // leave this
           if ( picked == false) {
               //////System.out.println("chainAmount is "+chainAmount);
               //////System.out.println("CHOOSING SMALLEST CHAIN EDGES 5"); 
               //////System.out.println("smallest chain size is "+chain_length[chain_use]); 
               tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
               picked = true;
           
           }
       }
   }
  // only potential chains
   else if (is_there_chain == false && is_there_potential_chain == true && picked == false)  { // no chain but only p.chains

       if (neutral_edge_exists == true)  {   // neutral edgee exists
           //////System.out.println("PICKING A NEUTRAL EDGE 6");
           //////System.out.println("row is "+neutral_edge_row+" col is "+neutral_edge_col+" edge is "+neutral_edge);
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);
           picked = true;
       }
       else {  // find smallest potential chain and give it away
           
          temp_smallest_potential = p_chain_length[1]; // first p.chain
          temp_biggest_potential = p_chain_length[1];
          potential_chain_use = 1;
           //////System.out.println("pchain length "+1+" is "+p_chain_length[1]);
           for( i = 2; i <= pchainAmount; i++) {
               smallest_potential = p_chain_length[i];
               ////////System.out.println("pchain length "+i+" is "+p_chain_length[i]);
               if (smallest_potential < temp_smallest_potential) {
                   potential_chain_use = i;
                   temp_smallest_potential = smallest_potential;
                   //////System.out.println("pot chain "+(i)+" has length "+p_chain_length[i]+" and is smaller than pot chain "+(i-1)+" with length "+p_chain_length[i-1]);
               }
               else if (smallest_potential > temp_biggest_potential ) {
                       temp_biggest_potential = smallest_potential;    
               }
           }
            ////////System.out.println("pchain amount is "+pchainAmount); 
           //////System.out.println("PICKING SMALLEST POTENTIAL CHAIN EDGES 7");
           //////System.out.println("smallest pchain size is "+p_chain_length[potential_chain_use]); 
           //////System.out.println("row is "+pchainData[potential_chain_use][1].getRow()+" col is "+pchainData[potential_chain_use][1].getCol()+" edge is "+pchainData[potential_chain_use][1].getEdge());
         tempEdge = new Edge(pchainData[potential_chain_use][1].getCol(),pchainData[potential_chain_use][1].getRow(),pchainData[potential_chain_use][1].getEdge());
         picked = true;
       }
   }
    // only exists a chain
   else if (is_there_chain == true && is_there_potential_chain == false && picked == false) {
      if ( neutral_edge_exists == true) {
           // select all the chains
          //////System.out.println("PICKING ALL THE CHAINS EDGES 8"); 
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
                   temp_smallest_chain = smallest_chain;
                    //////System.out.println("chain "+(i)+" has length "+chain_length[i]+" and is smaller than chain "+(i-1)+" with length "+chain_length[i-1]);
               }
           }
           //if(chain_length[chain_use] == 2 && chainAmount == 1 ){    // double cross move here
               if ( edges_left == 2 && should_double_cross == false) { // just select the last edges 
                   //////System.out.println("PICKING THE LAST CHAINS LAST EDGES 9"); 
                    tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
                    picked = true;
                 }
           // MAYBE NEED THIS FOR GETTING THE number of chains BACK so that i can win
              else if ( should_double_cross == true) {
                //////System.out.println("DOUBLE CROSSING 10");
                tempEdge = new Edge(chainData[chain_use][2].getCol(),chainData[chain_use][2].getRow(),chainData[chain_use][2].getEdge());
                picked = true;
              }
       
           if (picked == false) {
               //////System.out.println("PICKING SMALLEST CHAINS EDGES 11"); 
               //////System.out.println("smallest chain size is "+chain_length[chain_use]); 
               tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
               picked = true;
           }
       }

   }
          
    
   // no chains or potential chains
   if (is_there_chain == false && is_there_potential_chain == false && neutral_edge_exists == true && picked == false) {
            //////System.out.println("PICKING NEUTRAL EDGE 12"); 
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);    // this is correct although names say otherwise
           picked = true;
   }    /*
   else if (is_there_chain == false && is_there_potential_chain == false && neutral_edge_exists == false && picked == false) {
                //////System.out.println("----- Giving away a chain as there is no  13"); 
              seed++;    
              start_col = (seed * (int) (Math.random()*seed)) % column;
              start_row = (seed * (int) (Math.random()*seed)) % row;
              start_edge = (seed * (int) (Math.random()*seed)) % 4;

              tempEdge = new Edge(start_col, start_row, start_edge);
              picked = true;
   }*/
      
   if (this.never_search_neutral_again == true) {
            //////System.out.println("--------------------THERE ARE NO MORE NEUTRAL EDGES ---------------- = ");  
   }
   if ( tempEdge == null) {
              ////System.out.println("-------------------------------------------------------no edge got selected......................");
    } 
   /*
    i = 1;
    while ( i <= pchainAmount) {
        ////System.out.println("the size of pchain "+i+" is "+p_chain_length[i]);
        i++;
    } 
    i = 1;
    while ( i <= chainAmount) {
        ////System.out.println("the size of chain "+i+" is "+chain_length[i]);
        i++;
    } 
          
          */
          
    return tempEdge;
    
} // nevex_computing end


            /********************************************/
            /*        CHAIN HUNTER ALGORITHM            */
            /********************************************/


    public void huntChains(int c, int r) throws RemoteException {
   


        //////System.out.println("-------IM IN THE chainhunter----------");

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
                          if(board.isSelected(i,j,k) == true) {
                              edgeCount++;                              //Determines how many edges in box 
                          }
                          else
                              unselect = k;                         //Gives a name to the unselected edge
                   }
                   if(edgeCount == 3) {
                      chainAmount++;                   //Finds beginning of chain and records its coords & edge
                      //////System.out.println("Chainfound");
                      boxCount = 1;
                      chainData[chainAmount][boxCount] = new Edge(i,j,unselect);
                      //////System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
                      //+chainData[chainAmount][boxCount].getRow()+chainData[chainAmount][boxCount].getEdge());                        //Some of your code in here
                      stalkChains(i, j, unselect);                  //Goes in to find the neighbouring box
                   }
                  edgeCount = 0;
                }
         }
                  
              for(i=c;i<column;i++) {
                for(j=r;j<row;j++) {        //Counts board for pot. chain data
                  for(k=0;k<4;k++) {
                          if(board.isSelected(i,j,k) == true) {  //Determines amount of edges in box & chooses the unselected edge
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
                                        is_it_potential = 1;
                                        loopChainData = new Edge(i,j,unselect);
                                       // ////System.out.println("pot. Chainfound");
                                        loopChainData = new Edge(i,j,unselect);     //If box isn't already recorded goes on to check it's neighbouring box
                                        stalkChains(i, j, unselect);
                                        validCoord = false;
                      }
                   }
                   validCoord = false;
                   edgeCount = 0;
             }
         }
         for(int a=0;a<limit;a++) {
            for(int b=0;b<limit;b++) {
                if(chainData[a][b].getCol() != limit+1) {
                //////System.out.println("chain data for chain "+a+" box "+b+" is "+chainData[a][b].getCol()
                //+chainData[a][b].getRow()+chainData[a][b].getEdge());
                }
                
            } 
         }
         for(int a=0;a<limit;a++) {
            for(int b=0;b<limit;b++) {
                if(pchainData[a][b].getCol() != limit+1) {
                //////System.out.println("pot. chain data for pot. chain "+a+" box "+b+" is "+pchainData[a][b].getCol()
                //+pchainData[a][b].getRow()+pchainData[a][b].getEdge());
                }
                
            } 
         }
         //regurgatateChains();
    }

    public void eatChains(int cols, int rows, int prevEdge) throws RemoteException {
          int nextEdge = 4, edgeAmount = 0, loopParameter = 1;
          coordinates = new Edge(cols, rows, prevEdge);
          //////System.out.println("determining info. about box");

          for(int k=0;k<4;k++) {
              // IT DOES NOT LIKE THIS LINE MICK I think - it might be a box type chain think (i.e. a circle)
              if(board.isSelected(cols,rows,k) == true) {  //Determines amount of edges in the current box
                  edgeAmount++;
              }
          }

          for(int k=0;k<4;k++) {
              if(edgeAmount == 2 && k != prevEdge && board.isSelected(cols,rows,k) == false) {
                   nextEdge = k;                                //Gets the next unselected edge of that box
              }
          }
          //////System.out.println("noParameters = "+noParameters);
          
          if((coordinates.getCol() == loopChainData.getCol() && coordinates.getRow() == loopChainData.getRow() 
          && is_it_potential == loopParameter) || (noParameters == true)) {
              is_it_potential = 2;
              loopParameter = 2;
              noParameters = true;
              //////System.out.println("in a looped pot. chain");
              //////System.out.println("noParameters = "+noParameters);
                      for(int a=0;a<limit;a++) {
                          for(int b=0;b<limit;b++) {                         //This function sorts out when the pot. chain goes in to loop
                              if(coordinates.getCol() == pchainData[a][b].getCol() && coordinates.getRow() == pchainData[a][b].getRow()) {
                                  //////System.out.println("looped chain turned on");
                                  //////System.out.println("I AM THE BEST PROGRAMMER IN THE WORLD!!!!!!!!!!!");
                                    loopChain = true;
                              }
                          }
                      }
          }


          if(edgeAmount == 2 && loopChain == false)  {
              

                if(boxCount >= 1) {
                    boxCount++;                                 //Records coordinates of the current box if in a chain
                    chainData[chainAmount][boxCount] = new Edge(cols,rows,nextEdge);
                    //////System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
                    //+chainData[chainAmount][boxCount].getRow()+chainData[chainAmount][boxCount].getEdge());
                }
                else if(is_it_potential == 2) {
                    pboxCount++;                    //Records coordinates of the current box if in pot. chain
                    pchainData[pchainAmount][pboxCount] = new Edge(cols,rows,nextEdge);
                    //////System.out.println("pot. chainData for pot. chain "+pchainAmount+ " box "+pboxCount+" is "+pchainData[pchainAmount][pboxCount].getCol()
                    //+pchainData[pchainAmount][pboxCount].getRow()+pchainData[pchainAmount][pboxCount].getEdge());
                }
                stalkChains(cols, rows, nextEdge);              //Goes on to check next box
          }

          else if(is_it_potential == 1)  {
                is_it_potential++;
                //////System.out.println("beginning of pot. chain found");//Determines if the box is the start of a pot. chain
                loopChain = false;
                stalkChains(cols, rows, prevEdge);
          }
          else if(is_it_potential == 2) {
                is_it_potential = 0;
                //////System.out.println("end of pot. chain");
                loopChain = false;
                noParameters = false;
                loopParameter = 1;
                pboxCount = 0;                              //Determines if the box is the end of a pot. chain
                return;
          }
          else 
                 //////System.out.println("end of chain");
                 boxCount=0;                                //Box is end of a chain
                 return;
}
          
    public void stalkChains(int cols, int rows, int nextEdge) throws RemoteException {
        loopChain = false;
        //////System.out.println("going on to check neighbouring box");
        
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

       // ////System.out.println("Im in");
        
        int next = 0, limit = (column*row), cols, rows, edges;
        //tempChainData = new Edge[limit+1][limit+1];
        
        for(int x=0;x<(column*row);x++) {
            for(int y=0;y<(column*row);y++) {
                if(chainData[x][y].getCol() == limit+1 && chainData[x][y].getRow() == limit+1) {
                    //////System.out.println("in here aswell");
                    //tempChainData[x][y] = chainData[x][y];
               
                for(int chain=next;chain<(column*row);chain++) {
                    for(int box=next;box<(column*row);box++) {
                        if(chainData[chain][box].getCol() != limit+1 && chainData[chain][box].getRow() != limit+1) {
                        //////System.out.println("and here");
                        cols = chainData[x][y].getCol();
                        rows = chainData[x][y].getRow();
                        edges = chainData[x][y].getEdge();
                        //tempChainData[x][y] = new Edge(cols, rows, edges);
                       // chainData[x][y] = tempChainData[x][y];

                        next++;
                        }
                        }
                    }
                }
            }
        }
        
       for(int i=0;i<(column*row);i++) {
            for(int j=0;j<(column*row);j++) {
                //////System.out.println("sorted chain data is"+chainData[i][j].getCol()+chainData[i][j].getRow()+chainData[i][j].getEdge());
            } 
       }
    }
}