package boxit.client.cat;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
/** A typesafe enumeration class wiht the four major directions of
 * a compass. See <a href="http://java.sun.com/docs/books/effective/index.html"><i>J. Bloch's</i> <b>Effective Java</b></a>. A serialisable version 
 * is also in that book.*/
public final class Compass
{
    private final String name;
    private Compass(String name) 
    {
	this.name = name;
    }
    /** Returns the common name for this direction. */
    public String toString() 
    { 
	return name;
    }
    /** The Compass instance representing north. */
    public static final Compass NORTH = new Compass("north");
    /** The Compass instance representing south. */
    public static final Compass SOUTH = new Compass("south");
    /** The Compass instance representing east. */
    public static final Compass EAST = new Compass("east"); 
    /** The Compass instance representing west. */
    public static final Compass WEST = new Compass("west");
    
    private static final Compass[] VALUES = { NORTH, EAST, SOUTH, WEST };
    /** List of all all available directions. */
    public static final List<Compass> DIRECTIONS = Collections.unmodifiableList(Arrays.asList(VALUES));
}
