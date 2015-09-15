//this class is used for checking if the player wants to replay. It displays a message box.
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
public class Replay extends JFrame implements ActionListener{
	public static int replay=1;				//if close the box it also exits
	//found this thing online
	public Replay(boolean win, long t,int s,VoltorbSweeper myFrame){
		String txt;
		String[] modes={"Newchie","Median","Expert"};
		Object[] options={"Replay","Exit"};
		if (win){
			txt="Congrats, you beat the "+modes[s]+" level!"+"\n"+"Your time is "+t+" second(s)!";
		}
		else{
			txt="Sorry, you lost the game..."+"\n"+"Better luck next time.";
		}
		replay=JOptionPane.showOptionDialog(myFrame,
    		txt,
    		"                 What the voltorb?",							//the space is used to center the message
    		JOptionPane.YES_NO_OPTION,
    		JOptionPane.INFORMATION_MESSAGE,
    		null,     //do not use a custom Icon
    		options,  //the titles of buttons
    		null); //default button title
	}
	public void actionPerformed(ActionEvent evt){
	}

}