package boxit.client.cat;

import boxit.Edge;
import boxit.RmtBoard;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class DaCat extends ChainCat implements boxit.Player 
{
    protected final ArrayList<Edge> neutral = new ArrayList<Edge>(); 
    private boolean upToDate = false;
    private Boolean IamFirst = null;
    
    private Cat hazy = new HazyCat();
    private GreedyCat greedy = new GreedyCat();
    public Edge pickEdge() throws RemoteException
    {
	sortChains();
	neutral.clear();
	if (IamFirst == null) {             // find out who went first
	    IamFirst = (neutral.size() == cols * rows * 4) ? true : false;
	}
	Iterator<Box> it = boxes.iterator();
	while (it.hasNext()) {              // find all neutral edges
	    Box testBox = it.next(); 
	    for (int i = 0; i < testBox.sides().size(); i++) {
		if ( isNeutral(testBox, testBox.sides().get(i)) )
		    neutral.add(testBox.giveEdge(testBox.sides().get(i)));
	    }
	}
	if (neutral.isEmpty()) {    //NO NEUTRAL EDGES LEFT (forced)
	    if (nBoxes.isEmpty())                //-> no chains left 
		return openChain();                    // -> open one
	    if (nBoxes.size() == chains.size())  //-> only chains left 
		return take(chainForBox(nBoxes.get(0)));// -> take them  
	    if (nBoxes.size() == 1) {            //-> only one chain
		ArrayList<Box> chain = chainForBox(nBoxes.get(0));
		if (chain.size() == 2)
		    return doubleDeal(chain);          //-> double deal
		return take(chain);                    //   or take if long 
	    }
	    //Now at least 2 chains and some pchains are left
	    it = nBoxes.iterator();
	    int minLength = rows * cols + 1;
	    boolean haveLength2 = false;
	    while (it.hasNext()) {
		ArrayList<Box> chain = chainForBox(it.next());
		minLength = Math.min(minLength, chain.size());
		if (chain.size() == 2)
		    haveLength2 = true;
		if ( (chain.size() > 3) ) 
		    return take(chain);//first take long chains
	    }
	    //Now >= 2 chains of length 1, 2 or 3 and some pchains are left
	    //NOTE: loops end as two chains: a sigleton and one of length 3!
	    if (minLength > 1)       // So if all chains have length 2 or 3, 
		return take(nBoxes); // there is no danger and we take.
	    if (haveLength2) {    // If there is a length 2 chain, 
		it = nBoxes.iterator(); 
		while (it.hasNext()) {
		    ArrayList<Box> chain =  chainForBox(it.next());
		    if (chain.size() != 2) //then we take all others first
			return take(chain);
		}
		//if we get here the there are at least 2 chains of length 2
		return take(chainForBox(nBoxes.get(0))); //and we take one
	    }
	    it = nBoxes.iterator(); 
	    List<ArrayList<Box>> loops = new ArrayList<ArrayList<Box>>();
	    while (it.hasNext()) { // If there is is no length 2 chain
		ArrayList<Box> chain =  chainForBox(it.next());
		if ( chain.size() == 3 ) {
		    if (!isLoop(chain)) {
			return take(chain); // then we clear non loops
		    }
		    else {
			loops.add(chain);
		    }
		}
	    }
	    if (loops.size() > 1)
		return take(loops.get(0));
	    else if (loops.size() == 1)
		return doubleDeal(loops.get(0));
	    return take(nBoxes);
	}
	else {                       //NEUTRAL EDGES LEFT
	    if (!nBoxes.isEmpty()) { // take boxes if there are
		Box b = nBoxes.get(0);
		return b.giveEdge(b.sides().get(0));
	    }                        // or random neutral edge
	    return neutral.get( (int) (Math.random() * neutral.size()) );
	}
    }
    protected boolean isLoop(ArrayList<Box> chain)
    {
	if (chain.size() != 3) {
	    return false;
	}
	return nBoxes.contains(chain.get(2).follow(lastEdge(chain)));
    }
    protected Edge openChain()
    {
	ArrayList<Box> chain = shortestChain();
	if (chain == null) 
	    return null;//this is the end of the game
	if (chain.size() != 2)  //give it if it's long
	    return take(chain);
	return hardDeal(chain); //open both boxes if it's short 
    }
    protected ArrayList<Box> shortestChain()
    {
    	Iterator<ArrayList<Box>> list = chains.iterator();
	ArrayList<Box> chain = null;
	int minLength = cols * rows + 1;
	while (list.hasNext()) {
	    ArrayList<Box> newChain = list.next();
	    if (newChain.size() <= minLength) {
		chain = newChain;
		minLength = chain.size();
	    }
	}
	return chain;
    }
    protected Edge hardDeal(ArrayList<Box> chain)
    {
	Box b = chain.get(0);
	Compass dir = b.sides().get(0);
	int id = 1;
	if ( b.follow(dir).equals(chain.get(1)) )
	    id = 0;
	return take(b, b.sides().get(id));
    }
    private Edge doubleDeal(ArrayList<Box> chain) 
    {
	Box b = chain.get(1);
	Compass dir = chain.get(0).sides().get(0);
	return take(b, b.otherSide(opp(dir)));
    }
    private Edge take(Box b, Compass dir) 
    {
	return b.giveEdge(dir);
    }
    private Edge take(ArrayList<Box> chain)
    {
	return take(chain.get(0), chain.get(0).sides().get(0));
    }
}
