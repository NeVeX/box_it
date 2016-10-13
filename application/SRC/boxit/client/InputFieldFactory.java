package boxit.client;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComponent;
import java.awt.LayoutManager;
import java.awt.FlowLayout;
/** Provides static methods for creation of input fields with labels. **/
public class InputFieldFactory {
    private InputFieldFactory() {}
    /** Returns a labelled input field according to the parameters.
	@param l the label 
	@param f the input field
	@param lm a layoutmanager
	@return A {@link javax.swing.JPanel JPanel}
     **/
    public static JPanel make(String l, JComponent f, LayoutManager lm) {
	JPanel tmp = new JPanel(lm);
	tmp.add(new JLabel(l));
	tmp.add(f);
	return tmp;
    }
    /** Returns a labelled input field.
	@param l the label 
	@param f the input field
	@return A {@link JPanel}
    **/
    public static JPanel make(String l, JComponent f) {
	return make(l,f,new FlowLayout(FlowLayout.LEFT));
    }
}
	
