//this class is the menu of the game, it gives the user option to choose a size
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
public class Menu extends JFrame implements ActionListener{
	JButton[] options=new JButton[3];
	//the 3 modes
	String[] words={"Newchie","Medium","Expert"};
	public static int returnsize=-1;						//returnsize is the size that the user choose
	public Menu(){
		super("Menu");
		setLayout(null);									//add the three jbuttons
		for (int i=0;i<3;i++){
			options[i]=new JButton(words[i]);
			options[i].addActionListener(this);
			options[i].setSize(100,30);
			options[i].setLocation(50+120*i,200);
			add(options[i]);
		}
		setSize(500,500);
		setVisible(true);
		MenuPanel myM=new MenuPanel();
		myM.setLocation(0,150);
		add(myM);
	}
	public void actionPerformed(ActionEvent evt){
		Object source=evt.getSource();
		for (int i=0;i<3;i++){								//break if the user chooses any of the three buttons, in voltorbsweeper class, there is a while loop that breaks
															//when returnsize does not equal to -1, so if user chooses anything
			if (source==options[i]){
				setVisible(false);
				returnsize=i;
			}
		}
		repaint();
	}
	//public void paintComponent(Graphics g){
	//	g.drawImage(voltorb,200,200,this);
	//}
}
class MenuPanel extends JPanel{
	Image voltorb=new ImageIcon("voltorb.png").getImage();
	public MenuPanel(){
		setSize(200,200);
		setVisible(true);
	}
	public void paintComponent(Graphics g){
		g.drawImage(voltorb,200,200,this);
	}
}