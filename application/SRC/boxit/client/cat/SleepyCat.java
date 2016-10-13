package boxit.client.cat;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import static boxit.Edge.*;
import boxit.Edge;
/**
 * SpleepyCat breaks the arena.
 */
public final class SleepyCat extends Cat implements boxit.Player {
    public Edge pickEdge() throws RemoteException
    {
	return new Edge(0, 0, WEST);

    }	
}
