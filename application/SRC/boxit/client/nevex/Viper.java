package boxit.client.nevex;
/**
 *
 *
 * @ aMarXproduction partnered with Mickey's Mickeys Inc.
 *
 *
 *
 * presents The NeVeX Experience codenamed Viper
 *      powered by The Chain Hunter
 *
 *
 * Authors :
 *      Mark Cunningham - NeVeX Computing
 *      Micheal 0' Hara - Chain Hunter
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
 * The NeVeX Experience codenamed 'Viper' powered by the Chain Hunter.<br>
 *<br>
 * Authors :<br>
 *      Mark Cunningham - Viper Computing.<br>
 *      Micheal 0' Hara - Chain Hunter.<br>
 *<br>
 */
public class Viper extends View implements Player {
   private Object lock = new Object();
   private Edge edge;
   boolean never_search_neutral_again = false;
   //int temp_col = 0, temp_row = 0, temp_edge = 0;
   int seed = 347, my_limit, edges_left = 0;
   boolean player_first = true, search_initial = true;
   /*Chain Hunter Stuff, don't touch Mark*/
   int chainAmount, edgeAmount, boxCount, chainCounter, boxCounter, nextChain, nextBox,
       pchainAmount, pboxCount, pchainCounter, pboxCounter, limit, is_it_potential;
   boolean  validCoord = false, loopChain = false, noParameters = false;;
   Edge chainData[][], pchainData[][], loopChainData, coordinates;
   int total_original_edges = 0;
   int my_score = 0, his_score = 0;
   boolean should_flip = false, pick_it = false;

/** Sets up the board nicely for Viper and Chain Hunter to devour. */
public void setBoard(RmtBoard b) throws RemoteException {
       showBoard(b);
   }
/** Update everything related to the board and game. */
public void update() throws RemoteException {
           this.update(null, null);
   }
/** Calls Viper_Computing to ignite and fire up the A.I. Engine. */
public Edge pickEdge() throws RemoteException {
       Edge tempEdge = null;
       while(tempEdge == null) {
        tempEdge = Viper_Computing();
       }
       return tempEdge;
}
/**
 *  Created by Mark Cunningham.<br>
 *  This Artificial Intelligence Engine is used for winning games.<br>
 *  It is the 2nd Strongest A.I. Engine employed by NeVeX.<br>
 *<br>
 *  Returns : The A.I. Engine's selected Edge.<br>
 *<br>
 */
private Edge Viper_Computing() throws RemoteException {
   
    /*
     *
     *             |
     *             |            \                  /          \  
     *             |             \                /            \          
     *             |      ______  \              /       ______ \          TM 
     *      |      |    |          \            /    |           \      /  
     *      |\     |    |           \          /     |            \    /
     *      | \    |    |            \        /      |             \  /
     *      |  \   |    |----         \      /       |----          \/
     *      |   \  |    |              \    /        |              /\
     *      |    \ |    |               \  /         |             /  \
     *      |     \|    |_______         \/          |_______     /    \
     *      |                                                           \
     *      |             _  _        _      |-        _                 \  
     *      |            |_ |_| |\/| |_| |_| |_ | |\| |_|                 \
     *      |                        |             _____|                  \
     *
     *
     *
     */ 

   huntChains(0,0); 
   Edge tempEdge = null;
   int i;
   int my_col,my_row;
   int box_edges, above_box_edges, below_box_edges,
    left_box_edges, right_box_edges;
   int neutral_edge_row = 0, neutral_edge_col = 0, neutral_edge = 0;
   int start_col, start_row;
   int rand_col = 0, rand_row = 0, rand_edge = 0;
   int search_loop;
   int start_edge = 0;
   int chain_length[] = new int[chainAmount*2];
   int p_chain_length[] = new int[pchainAmount*2];
   int four_box[][] = new int [row*column][8];
   int smallest_potential = 0, potential_chain_use = 0, temp_smallest = 0;
   int smallest_chain = 0, temp_smallest_chain = 0, chain_use = 0;
   int smallest_chain_now, smallest_potential_chain_now;
   int p_length_chain = 1;
   int p_chain_number = 0, box = 2;
   int length_chain = 1;
   int chain_number = 0, temp_smallest_potential = 0;
   int temp_biggest_potential = 0;
   int how_many_boxes = 0, how_many_empty_boxes = 0, count = 1;
   int total_chain_boxes = 0, total_pchain_boxes = 0, how_many_overlaps = 0;

   boolean box_edge[][][] = new boolean[column][row][4]; 
   boolean neutral_edge_exists = false;
   boolean proceed = true;
   boolean wrong_edge = true;
   boolean picked = false;
   boolean is_there_chain = false;
   boolean is_there_potential_chain = false;
   boolean should_double_cross = false;
   boolean chainAmount_changed = false;
   boolean select_vert_neg = false, select_hori_neg = false, select_vert = false, select_hori = false;
   boolean select_vert_neg_2 = false, select_hori_neg_2 = false, select_vert_2 = false, select_hori_2 = false;
   boolean giving_odd_away = false;   
   boolean is_there_box = false;
   boolean north_box = false, east_box = false, south_box = false, west_box = false;
   boolean cannot_choose_it = false;
           
   edges_left = board.getEdgesLeft();
    
   // find out if you are first or second and get the total edges left
   if ( search_initial == true) { 
       total_original_edges = board.getEdgesLeft();
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

                   if (!board.isSelected(my_row, my_col, 0)) {
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 1)) {
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 2)) {
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 3)) {
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
               }
          }
          // if random edge search didnt work restart loop with original values
          start_col = 0;
          start_row = 0;
      }
   }

  // neutral edge is either found or doesn't exist be this stage
    if ( !neutral_edge_exists ) {
        this.never_search_neutral_again = true;
       }
  
  // searching for boxes 
  // find an empty box
   box_search: for ( my_row = 0; my_row < row; my_row++) { // row first
            for ( my_col = 0; my_col < column; my_col++) {
                if ( my_row == row - 1 || my_col == column - 1) {
                 }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    how_many_empty_boxes++;
                }
            }   
   }
   // find a box with an edge selected within - need this for double crossing
   box_search: for ( my_row = 0; my_row < row; my_row++) { // row first
            for ( my_col = 0; my_col < column; my_col++) {
                if ( my_row == row - 1 || my_col == column - 1) {
                 }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    north_box = true;
                    is_there_box = true;
                    break box_search;
                    
                }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    east_box = true;
                    is_there_box = true;
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    break box_search;
                
                }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    west_box = true;
                    is_there_box = true;
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    break box_search;
                
                }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && board.isSelected(my_col+1, my_row+1, 3)) {
                    south_box = true;
                    is_there_box = true;
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    break box_search;
                
                }

            }
        }
   // get the length of the chains
   if (chainAmount > 0) {  
       is_there_chain = true;
       box = 2;
       for (chain_number = 1; chain_number <= chainAmount; chain_number++ ) {
           while( chainData[chain_number][box].getCol() <= limit ) {
               length_chain++;
               box++;
           }
           chain_length[chain_number] = length_chain; 
           box = 2;
           length_chain = 1;
       }
       temp_smallest_chain = chain_length[1];
       chain_use = 1;

       for ( i = 2; i <= chainAmount; i++) {
        smallest_chain = chain_length[i];
          if(smallest_chain < temp_smallest_chain) {
             chain_use = i;
             temp_smallest_chain = smallest_chain;
          }
       }
   }
   // get the length of the potential chains
   if (pchainAmount > 0) {  
       is_there_potential_chain = true;
       box = 2;
       
       for (p_chain_number = 1; p_chain_number <= pchainAmount; p_chain_number++ ) {
           while( pchainData[p_chain_number][box].getCol() <= limit ) {
               p_length_chain++;
               box++;
           }
           p_chain_length[p_chain_number] = p_length_chain; 
           p_length_chain = 1;
           box = 2;
       }
       
       temp_smallest_potential = p_chain_length[1]; // first p.chain
       temp_biggest_potential = p_chain_length[1];
       potential_chain_use = 1;

       for( i = 2; i <= pchainAmount; i++) {
        smallest_potential = p_chain_length[i];
         if (smallest_potential < temp_smallest_potential) {
            potential_chain_use = i;
            temp_smallest_potential = smallest_potential;
            //////////System.out.println("pot chain "+(i)+" has length "+p_chain_length[i]+" and is smaller than pot chain "+(i-1)+" with length "+p_chain_length[i-1]);  
         }
         else if (smallest_potential > temp_biggest_potential ) {
             temp_biggest_potential = smallest_potential;
         }
       }
   }
   
   for( i = 1; i <= chainAmount; i++ ) {
       while( chainData[i][count].getCol() <= limit) {
           total_chain_boxes++;
           count++; 
       }
       count = 1;
   }
   count = 1;
   for( i = 1; i <= pchainAmount; i++ ) {
       while( pchainData[i][count].getCol() <= limit) {
           total_pchain_boxes++;
           count++;
       }
       count = 1;
   }
   count = 1;
   i = 1;
   while ( i <= pchainAmount) {
       i++;
   }

   
   
   // EVERYTHING IS KNOWN AS THIS STAGE
   // CHAINS, CHAIN LENGTHS, NEUTRAL EDGE, PLAYER FIRST ETC....
   
   
   
   
   /*
   // should I double cross
   if ( picked == false ) {
       int counting = 0;
       if( !neutral_edge_exists && is_there_chain && is_there_potential_chain && temp_smallest_potential == 1) {
          should_double_cross = false;
       }
       else if ( edges_left == 2 ) {
           should_double_cross = false;
       }
       else if ( chainAmount == 1 && chain_length[1] > 1) {
           should_double_cross = true;
       }
       else if (!neutral_edge_exists && is_there_chain && is_there_potential_chain) {
           boolean is_there_overlap = false;
           // find some overlaps
               for  ( i = 1; i < chainAmount; i++) {
                 for ( int y = 2; y <= chainAmount; y++) {
                    if ( i == y) {
                    }
                    else if (chainData[i][2].getCol() == chainData[y][chain_length[y]].getCol()
                             && chainData[i][3].getCol() == chainData[y][chain_length[y-1]].getCol()
                             && chainData[i][2].getRow() == chainData[y][chain_length[y]].getRow()
                             && chainData[i][3].getRow() == chainData[y][chain_length[y-1]].getRow()) {
                        // these two chains have the same traits and are wrongly thought to have 2 extra boxes,
                        // so we simply subtract for every instance we find
                        is_there_overlap = true;
                        how_many_overlaps++;
                    }
                 }
               }
         if ( chainAmount > 2) {
               should_double_cross = false;
         }
       }
   }
    **/
    // chains and potential chains
   if (is_there_chain == true && is_there_potential_chain == true && picked == false) {
       if ( neutral_edge_exists == true) {
           // pick the edge and then the neutral
           tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
           picked = true;
       } 
       else { // obtain the chains starting with the smallest
           if (chainAmount == 1) {
               if ( chain_length[1] == 1 ) {
                   // we cant double cross here unfortunatly and the other player will prob win
                   tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
                   picked = true;
                   ////System.out.println("*NEW* Other player should win - debug qw");
               }
               else if (chain_length[1] == 2) {
                   // double cross here
                   tempEdge = new Edge( chainData[1][2].getCol(), chainData[1][2].getRow(), chainData[1][2].getEdge());
                   picked = true;
                   ////System.out.println("*NEW* Other player should win - debug qwe");
               }
               else {
                   // get the chain size down to 2 so we can double cross
                   tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
                   picked = true;
                   ////System.out.println("*NEW* reducing the chain size - debug qwer");
               }
           }
         if ( chainAmount == 2 && picked == false) {
            if( is_there_box == true && edges_left > 3) {
                    if ( north_box ) {
                        tempEdge = new Edge(four_box[0][0]+1,four_box[0][1]+1,3);
                        ////System.out.println(" debug18");
                    }
                    else if ( east_box ) {
                        tempEdge = new Edge(four_box[0][0],four_box[0][1],2);
                        ////System.out.println(" debug19");
                    } 
                    else if ( west_box ) {
                        tempEdge = new Edge(four_box[0][0]+1,four_box[0][1]+1,0);
                        ////System.out.println(" debug20");
                    }
                    else if ( south_box ) {
                        tempEdge = new Edge(four_box[0][0],four_box[0][1],1);
                        ////System.out.println(" debug21");
                    }
                picked = true;
            }
         }
         // checking if two chains are in a the same chain, i.e the same except the last and first locations
         if ( chainAmount >= 2 && picked == false) { // this should be if two chains are the same, not just 2 chains     
              /*if( temp_smallest_chain != 3) { 
                tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
                picked = true;
                ////System.out.println(" debug22");
              } */
              //else {
                // there is one chain now with length 2 at least.
                // we need to double cross here aswell.
                // first we should get all the bigger chains if there exists
                // a 3 chain with an extra 1 attached.
                
                  // just apply for the double cross first.
                int length_3_found;  
         length_3:for(int checking = 1; checking <= chainAmount; checking++) {
                    if ( chain_length[checking] == 3) {
                        length_3_found = checking;
                        for (int checking2 = 1; checking2 <= chainAmount; checking2++) {
                            if (chain_length[checking2] == 3) {
                                // now check to see that this one chain is actually
                                // belonging to the 3 chain found earlier.
                                // this is checked by checking that this one chain 
                                // opens to the three chain.
                                ////System.out.println("I HAVE FOUND TWO CHAINS OF LENGTH 3");
                                if ( chainData[checking2][1].getEdge() == 0 ) {
                                    if ( chainData[checking][3].getCol() == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow()+1 == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 2) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 800");
                                        break length_3;
                                    }
                                }
                                else if ( chainData[checking2][1].getEdge() == 1 ) {
                                    if ( chainData[checking][3].getCol()-1  == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow() == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 3) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 801");
                                        break length_3;
                                    }
                                }
                                else if ( chainData[checking2][1].getEdge() == 2 ) {
                                    if ( chainData[checking][3].getCol()  == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow()-1 == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 0) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 802");
                                        break length_3;
                                    }
                                }
                                else if ( chainData[checking2][1].getEdge() == 3 ) {
                                    if ( chainData[checking][3].getCol()+1  == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow() == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 1) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 803");
                                        break length_3;
                                        
                                    }
                                }
                            }
                        }
                    }
                }  

              /*else { // here mark
                  i = 1;
                   int how_many_length_three = 0;
                      while( i <= chainAmount) {
                        if(chain_length[i] == 3) {
                           how_many_length_three++;
                         }
                          i++;
                      }
                  boolean found = false;    
                  if( how_many_length_three >= 2 && picked == false) {
                       i = 1; int y = 1;
                  this_looped: for  ( i = 1; i < chainAmount; i++) {
                           for ( y = 2; y <= chainAmount; y++) {
                               if ( i == y) {
                               }
                               else if (chainData[i][2].getCol() == chainData[y][3].getCol()
                                   && chainData[i][3].getCol() == chainData[y][2].getCol()
                                   && chainData[i][2].getRow() == chainData[y][3].getRow()
                                   && chainData[i][3].getRow() == chainData[y][2].getRow()) {
                               // found a chain with similiar traits
                               found = true;
                                // if we want to double cross we must do it here
                                   if ( should_double_cross == false ) {
                                       tempEdge = new Edge(chainData[i][1].getCol(),chainData[i][1].getRow(),chainData[i][1].getEdge()); // odd one of the three length chains
                                       picked = true;
                                       ////System.out.println(" debug23");
                                       break this_looped;
                                   }
                                   else { // double crossing here
                                        // make sure only two chains left
                                        if ( chainAmount == 2 && chain_length[1] == 3 && chain_length[2] == 3) { // need to include the boxes 
                                            tempEdge = new Edge(chainData[i][2].getCol(),chainData[i][2].getRow(),chainData[i][2].getEdge());
                                            picked = true;
                                            ////System.out.println(" debug24");
                                            break this_looped;
                                        }
                                        else if ( chainAmount >= 3) {
                                            //select every chain that is not i or y
                                            if( i != 1) {
                                                tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge()); 
                                                picked = true;
                                                ////System.out.println(" debug25");
                                                break this_looped;
                                            }
                                            else if( y != 2) {
                                                tempEdge = new Edge(chainData[2][1].getCol(),chainData[2][1].getRow(),chainData[2][1].getEdge()); 
                                                picked = true;
                                                ////System.out.println(" debug26");
                                                break this_looped;
                                            }
                                            else if ( i == 1 && y == 2 && chainAmount >= 3) {
                                                tempEdge = new Edge(chainData[3][1].getCol(),chainData[3][1].getRow(),chainData[3][1].getEdge()); 
                                                picked = true;
                                                ////System.out.println(" debug27");
                                                break this_looped;
                                            }
                                        }
                                   }
                                }
                           }
                           if ( found == false ) {
                               tempEdge = new Edge(chainData[i][1].getCol(),chainData[i][1].getRow(),chainData[i][1].getEdge()); // odd one of the three length chains
                               picked = true;
                               ////System.out.println(" debug28");
                               break this_looped;
                           }
                       }
                  }
                  else if ( how_many_length_three < 2 && picked == false) {
                      tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
                      picked = true; 
                      ////System.out.println(" debug29");
                  }
              }*/
     }
       
           // leave this
      if ( picked == false) {
               tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
               picked = true;
               ////System.out.println(" debug3");
           }
       }
   }
  // only potential chains
   else if (is_there_chain == false && is_there_potential_chain == true && picked == false)  { // no chain but only p.chains
       if (neutral_edge_exists == true)  {   // neutral edgee exists
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);
           picked = true;
           ////System.out.println(" debug31");
           //cannot_choose_it = true;
       }
       else {  // find smallest potential chain and give it away
           if ( temp_smallest_potential == 1 && pchainAmount >= 2 ) {
               int r = 1, badly = -1;
               boolean another = false, one = false;
               boolean p_another = false, two = false;
               while ( r <= pchainAmount ) {
                   if ( p_chain_length[r] == 1 ) {
                       int w = r + 1;
                       one = true;
                       while ( w <= pchainAmount ) {
                           if ( p_chain_length[w] == 1) {
                               another = true;
                           }
                           w++;
                       }
                   }
                   r++;
               }
               if ( another == false && one == true) {
                // find a two chain if it exists
                 int u = 1;
                 while ( u <= pchainAmount ) {
                   if ( p_chain_length[u] == 2 ) {
                      int p = u + 1;
                      two = true;
                      badly = u;
                         while ( p <= pchainAmount ) {
                             if ( p_chain_length[p] == 2) {
                                   p_another = true;
                             }
                             p++;
                         }
                   }
                   u++;
                 }
               }
               if ( another == false && one == true && p_another == true && two == true ) {
                   // pick the two chain badly 
                   // get the two chain
                   int this_row, this_col, next_row, next_col;
                       this_col = pchainData[badly][1].getCol();
                       this_row = pchainData[badly][1].getRow();
                       next_col = pchainData[badly][2].getCol();
                       next_row = pchainData[badly][2].getRow();
                       
                       if ( this_row == next_row - 1 && this_col == next_col ) { // south edge i must select
                        tempEdge = new Edge(this_col,this_row,2);
                        picked = true;
                        ////System.out.println(" debug32s");
                       }
                       else if ( this_row == next_row + 1 && this_col == next_col ) { // north edge i must select
                        tempEdge = new Edge(this_col,this_row,0);
                        picked = true;
                        ////System.out.println(" debug33s1");
                       }
                       else if ( this_row == next_row && this_col == next_col - 1 ) { // east edge i must select
                        tempEdge = new Edge(this_col,this_row,1);
                        picked = true;
                        ////System.out.println(" debug34s2");
                       }
                       else if ( this_row == next_row && this_col == next_col + 1 ) { // west edge i must select
                        tempEdge = new Edge(this_col,this_row,3);
                        picked = true;
                        ////System.out.println(" debug35s3");
                       }
               }
           }  
      if ( !picked ) {  
           //check for two p_chains of length 2
           int p_chain_length_size_2 = 0;
           for(int check = 1; check <= pchainAmount; check++) {
               if (p_chain_length[check] == 2) {
                   p_chain_length_size_2++;
               }
           } 
           if ( temp_smallest_potential == 2 && p_chain_length_size_2 % 2 == 1) {
               // pick the edge that wont allow double crossing
               int y = 1;
               while ( y <= pchainAmount) {
                   if ( p_chain_length[y] == 2) {
                       int this_row, this_col, next_row, next_col;
                       this_col = pchainData[y][1].getCol();
                       this_row = pchainData[y][1].getRow();
                       next_col = pchainData[y][2].getCol();
                       next_row = pchainData[y][2].getRow();
                       
                       if ( this_row == next_row - 1 && this_col == next_col ) { // south edge i must select
                        tempEdge = new Edge(this_col,this_row,2);
                        picked = true;
                        ////System.out.println(" debug32");
                        break;
                       }
                       else if ( this_row == next_row + 1 && this_col == next_col ) { // north edge i must select
                        tempEdge = new Edge(this_col,this_row,0);
                        picked = true;
                        ////System.out.println(" debug33");
                        break;
                       }
                       else if ( this_row == next_row && this_col == next_col - 1 ) { // east edge i must select
                        tempEdge = new Edge(this_col,this_row,1);
                        picked = true;
                        ////System.out.println(" debug34");
                        break;
                       }
                       else if ( this_row == next_row && this_col == next_col + 1 ) { // west edge i must select
                        tempEdge = new Edge(this_col,this_row,3);
                        picked = true;
                        ////System.out.println(" debug35");
                        break;
                       }
                   }
                   y++;
               }
           }
           if ( temp_smallest_potential == 2 && p_chain_length_size_2 % 2 == 0) {
               // do not pick badly. 
               tempEdge = new Edge( pchainData[potential_chain_use][1].getCol(), pchainData[potential_chain_use][1].getRow(), pchainData[potential_chain_use][1].getEdge());
               picked = true;
               ////System.out.println("*NEW* debug 900");
           }
      }
         if ( picked == false) {
             tempEdge = new Edge(pchainData[potential_chain_use][1].getCol(),pchainData[potential_chain_use][1].getRow(),pchainData[potential_chain_use][1].getEdge());
             picked = true;
             ////System.out.println(" debug37 should of fecking not picked badly");
          }
       }
   }
    // only exists a chain
   else if (is_there_chain == true && is_there_potential_chain == false && picked == false) {
      if ( neutral_edge_exists == true) {
          tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
          picked = true;
      }
       else { //select chains starting with smallest
          tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
          picked = true;
          
       }
          
       
         /*      if ( edges_left == 2) { // just select the last edges 
                   //////////System.out.println("PICKING THE LAST CHAINS LAST EDGES 9"); 
                    tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
                    picked = true;
                    ////System.out.println(" debug48");
                    ////////System.out.println("Picked an Edge without neutral edge 20");
                 }
              else if ( should_double_cross == true) {
                   ////System.out.println(" debug49");
                tempEdge = new Edge(chainData[chain_use][2].getCol(),chainData[chain_use][2].getRow(),chainData[chain_use][2].getEdge());
                picked = true;
              }
       
           if (picked == false) {
                   ////System.out.println(" debug50");
               tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
               picked = true;
               ////////System.out.println("Picked an Edge without neutral edge 21");
           }
       }*/

   }
   // no chains or potential chains
   if (is_there_chain == false && is_there_potential_chain == false && neutral_edge_exists == true && picked == false && cannot_choose_it == false) {
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);    // this is correct although names say otherwise
           picked = true;
           ////System.out.println(" debug51");
           ////////System.out.println("Picked an Edge with neutral edge 22");
           //cannot_choose_it = true;
   } 
   
   
   if ( tempEdge == null && neutral_edge_exists && picked == false ) {
        tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);    // this is correct although names say otherwise
        picked = true;
        ////System.out.println(" debug75");
        // fool proof
    }
   
   if ( tempEdge == null) {
        ////System.out.println("--ERROR--     No Edge Got Selected    --ERROR--");
   }
   /*
   for(int qwe = 1; qwe <= chainAmount; qwe++) {
       ////System.out.println("chain "+qwe+" is length "+chain_length[qwe]+ " and has co-ords at "+chainData[qwe][1].getCol()+","+chainData[qwe][1].getRow()+","+chainData[qwe][1].getEdge());
   }
   */
    ////System.out.println("DID I CHANGE AT ALL MARK? and edge is is "+chainData[1][0].getEdge() );
    return tempEdge;
   
} // NeVeX_Computing End


            /********************************************/
            /*        CHAIN HUNTER ALGORITHM            */
            /********************************************/


    public void huntChains(int c, int r) throws RemoteException {
   


        //////////System.out.println("-------IM IN THE chainhunter----------");

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
                      //////////System.out.println("Chainfound");
                      boxCount = 1;
                      chainData[chainAmount][boxCount] = new Edge(i,j,unselect);
                      //////////System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
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
                                       // ////////System.out.println("pot. Chainfound");
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
                //////////System.out.println("chain data for chain "+a+" box "+b+" is "+chainData[a][b].getCol()
                //+chainData[a][b].getRow()+chainData[a][b].getEdge());
                }
                
            } 
         }
         for(int a=0;a<limit;a++) {
            for(int b=0;b<limit;b++) {
                if(pchainData[a][b].getCol() != limit+1) {
                //////////System.out.println("pot. chain data for pot. chain "+a+" box "+b+" is "+pchainData[a][b].getCol()
                //+pchainData[a][b].getRow()+pchainData[a][b].getEdge());
                }
                
            } 
         }
         //regurgatateChains();
    }

    public void eatChains(int cols, int rows, int prevEdge) throws RemoteException {
          int nextEdge = 4, edgeAmount = 0, loopParameter = 1;
          coordinates = new Edge(cols, rows, prevEdge);
          //////////System.out.println("determining info. about box");

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
          //////////System.out.println("noParameters = "+noParameters);
          
          if((coordinates.getCol() == loopChainData.getCol() && coordinates.getRow() == loopChainData.getRow() 
          && is_it_potential == loopParameter) || (noParameters == true)) {
              is_it_potential = 2;
              loopParameter = 2;
              noParameters = true;
              //////////System.out.println("in a looped pot. chain");
              //////////System.out.println("noParameters = "+noParameters);
                      for(int a=0;a<limit;a++) {
                          for(int b=0;b<limit;b++) {                         //This function sorts out when the pot. chain goes in to loop
                              if(coordinates.getCol() == pchainData[a][b].getCol() && coordinates.getRow() == pchainData[a][b].getRow()) {
                                  //////////System.out.println("looped chain turned on");
                                  //////////System.out.println("I AM THE BEST PROGRAMMER IN THE WORLD!!!!!!!!!!!");
                                    loopChain = true;
                              }
                          }
                      }
          }


          if(edgeAmount == 2 && loopChain == false)  {
              

                if(boxCount >= 1) {
                    boxCount++;                                 //Records coordinates of the current box if in a chain
                    chainData[chainAmount][boxCount] = new Edge(cols,rows,nextEdge);
                    //////////System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
                    //+chainData[chainAmount][boxCount].getRow()+chainData[chainAmount][boxCount].getEdge());
                }
                else if(is_it_potential == 2) {
                    pboxCount++;                    //Records coordinates of the current box if in pot. chain
                    pchainData[pchainAmount][pboxCount] = new Edge(cols,rows,nextEdge);
                    //////////System.out.println("pot. chainData for pot. chain "+pchainAmount+ " box "+pboxCount+" is "+pchainData[pchainAmount][pboxCount].getCol()
                    //+pchainData[pchainAmount][pboxCount].getRow()+pchainData[pchainAmount][pboxCount].getEdge());
                }
                stalkChains(cols, rows, nextEdge);              //Goes on to check next box
          }

          else if(is_it_potential == 1)  {
                is_it_potential++;
                //////////System.out.println("beginning of pot. chain found");//Determines if the box is the start of a pot. chain
                loopChain = false;
                stalkChains(cols, rows, prevEdge);
          }
          else if(is_it_potential == 2) {
                is_it_potential = 0;
                //////////System.out.println("end of pot. chain");
                loopChain = false;
                noParameters = false;
                loopParameter = 1;
                pboxCount = 0;                              //Determines if the box is the end of a pot. chain
                return;
          }
          else 
                 //////////System.out.println("end of chain");
                 boxCount=0;                                //Box is end of a chain
                 return;
}
          
    public void stalkChains(int cols, int rows, int nextEdge) throws RemoteException {
        loopChain = false;
        //////////System.out.println("going on to check neighbouring box");
        
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

       // ////////System.out.println("Im in");
        
        int next = 0, limit = (column*row), cols, rows, edges;
        //tempChainData = new Edge[limit+1][limit+1];
        
        for(int x=0;x<(column*row);x++) {
            for(int y=0;y<(column*row);y++) {
                if(chainData[x][y].getCol() == limit+1 && chainData[x][y].getRow() == limit+1) {
                    //////////System.out.println("in here aswell");
                    //tempChainData[x][y] = chainData[x][y];
               
                for(int chain=next;chain<(column*row);chain++) {
                    for(int box=next;box<(column*row);box++) {
                        if(chainData[chain][box].getCol() != limit+1 && chainData[chain][box].getRow() != limit+1) {
                        //////////System.out.println("and here");
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
                //////////System.out.println("sorted chain data is"+chainData[i][j].getCol()+chainData[i][j].getRow()+chainData[i][j].getEdge());
            } 
       }
    }
}
