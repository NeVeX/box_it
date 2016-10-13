package boxit.arena;

//import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JFrame;

final class ThreadMonitor implements Runnable  
{
    private int xPos, yPos;
    ThreadMonitor() 
    {
	this(20, 140);
    }
    ThreadMonitor(int xPos, int yPos) 
    {
	this.xPos = xPos;
	this.yPos = yPos;
    }
    public void run() 
    {
	final JFrame f = new JFrame("BoXiT ThreadMonitor"); 
	f.setSize(300, 550);
	f.setLocation(xPos, yPos);
	f.setResizable(false);
	final JList l = new JList();
	f.add(l);
	//f.pack();
	f.setVisible(true);
	Thread me, grp[];
	while (f.isShowing()) {
	    me = Thread.currentThread();
	    grp = new Thread[Thread.activeCount()+2];
	    me.getThreadGroup().enumerate(grp);
	    me.setName("ThreadMonitor");
	    l.setListData(grp);
	    try {
		Thread.currentThread().sleep(500);
	    }
	    catch (Exception e) { 
		e.printStackTrace();
	    }
	}
    }
}
