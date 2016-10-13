package boxit.client.human;
import java.awt.Color;

public final class Style {
    String name = "default"; 
    // style related fields
    int marginTop = 40; 
    int marginLeft = 40;
    int boxWidth = 50;
    int dotWidth = 8;
    int lineWidth = 3;
    int ldSep = 3;
    Color bgColor = Color.black;
    Color dotColor = Color.yellow;
    Color lineColor = new Color(204, 204, 0);
    Color plColor[] = { new Color(0, 204, 204), new Color(51, 204, 0) };

    Style() {}
    
    public String toString() {
	String ret = "Style name: " + name + "\n";
	ret +=   "Margins: top  = " + marginTop;
	ret += "\n         left = " + marginLeft;
	ret += "\nWidths: box = " + boxWidth;
	ret += "\n        dot = " + dotWidth;
	ret += "\n separation = " + ldSep;
	ret += "\n       line = " + lineWidth;
	ret += "\nColours: dots: " + dotColor;
	ret += "\n        lines: " + lineColor;  
	ret += "\n   background: " + bgColor;       
	ret += "\n     Player 1: " + plColor[0];
	ret += "\n     Player 2: " + plColor[1];
	return ret;
    }
    public Color[] getPlayerColors() 
    {
	return plColor;
    }
    public Color getLineColor() 
    {
	return lineColor;
    }
}
