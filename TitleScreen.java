import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
 
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
public class TitleScreen extends JFrame{

    static double xScale = 1;
    static double yScale = 1;
    int width = (int)(Math.floor(1000*xScale));
    int height = (int)(Math.floor(800*yScale));
    static JPanel titleScreen = new JPanel();
    static JLabel label = new JLabel("Enter username:");
    static JTextField userName = new JTextField(20);
    static TitleScreen screen = new TitleScreen();
    static boolean isRunning = true;
 
    

    public static void main(String[] args){
	titleScreen.add(label);
    	titleScreen.add(userName);
        screen.add(titleScreen);
	while(isRunning){
		screen.run();
	}
    }

    public void run() {
        screen.setVisible(true);
    }
}
