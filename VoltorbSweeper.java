//Bill Gan
//VoltorbSweeper.java
//Simple Game
//this class is main strcture of this program. It contains methods such as the main, actionperformed.
//import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
public class VoltorbSweeper extends JFrame implements ActionListener{
	GamePanel mygame;														//the main logic of the program
	Timer myTimer;															//Still not sure what this does, but this starts the whole program
	private static long startTime;											//since I am using current.millis, I cannot reset that time, so I need to reset the start time
	public static int size;													//the size of the gameboard: 1-newchie, 2-median, 3-expert
	public static void main(String[] args){
		// the menu part
	 	Menu m=new Menu();
  
	 	while (m.returnsize==-1){	//breaks once the user chooses something
            System.out.println("Processing");
	 	}
	 	//gets the user's option
	 	size=m.returnsize;
	 	//the main program
  
		VoltorbSweeper game=new VoltorbSweeper();

		startTime = System.currentTimeMillis();								//the initial starting time
	}
	public VoltorbSweeper(){
		super("VoltorbSweeper");											//YEAH TITLE IS COPYRIGHTED
		//blah blah blah necessary stuff
		setLayout(null);
		setSize(800,600);
		setVisible(true);
		mygame=new GamePanel(size,this);
		mygame.setLocation(50,50);
		add(mygame);
		myTimer=new Timer(10,this);
		myTimer.start();
	}
	public void actionPerformed(ActionEvent evt){
		repaint();															//keep drawing everything
	}
	public long getTime(){
		//return the time that has passed in each game
		return (System.currentTimeMillis()-startTime)/1000;
	}
	public void setStartTime(long num){
		//once the game finishes, it resets the current time
		startTime+=num;
	}
}
//This class is the main logic of this game, it contains methods that draw everything, and it has mouseevent, which is the controls of this game
class GamePanel extends JPanel implements MouseListener{
	Image gridPic=new ImageIcon("grid.png").getImage();						//the picture of squares, they are blue!!!
	Image[] gridInfo=new Image[11];											//picture inside of the grid, contains white square, number 1-9, flag
	Image voltorb=new ImageIcon("voltorb.png").getImage();					//the mines!! draw them after the user loses, show the user where all the mines are
	Image vbomb=new ImageIcon("voltorb1.png").getImage();					//draw at the bottom right corner, indicate how many flags you have been marked
	Image happy=new ImageIcon("hvoltorb.png").getImage();					//draw on the top of the panel, draw this when the user wins the game
	Image sad=new ImageIcon("svoltorb.png").getImage();						//draw on the top of the panel, draw this when the user loses the game
	Image normal=new ImageIcon("nvoltorb.png").getImage();					//draw on the top of the panel, draw this when the user is still playing
	boolean[] buttons=new boolean[3];										//the 3 buttons, I guess I don't really need the middle one
	int mx,my,mb;															//mousex, mousey, mousebutton
	int[][] display;														//the same board as displayBoard in Board class, display the user the board, show what's hidden,
																			//how many bombs are arund a certain grid, and if this grid has been flagged
	int boardSize;															//0 being newchie 9x9, 1 being median, 16x16, 2 being expert 30x16 or the other way around
	Board curboard;															//your current board
	VoltorbSweeper frame;													//your voltorbsweeper class, you need it since you need to reset time and take in parameter
	long time=0;															//your current time
	Font font = new Font("Serif", Font.BOLD, 20);							//the font of all the txts
	public GamePanel(int size,VoltorbSweeper myframe){
		super();
		//blah blah blah sets everything up
		frame=myframe;
		Point[] sizes={new Point(270,270),									//the sizes of the gamepanel in different levels(newchie, median and expert)
		  	           new Point(420,420),
		               new Point(700,420)};
		for (int i=0;i<3;i++){												//left, middle and right buttons
			buttons[i]=false;
		}
		this.addMouseListener(this);
		for (int i=0;i<11;i++){
			gridInfo[i]=new ImageIcon("grid"+(i+1)+".png").getImage();
		}
		setSize((int)sizes[size].getX(),(int)sizes[size].getY());			//setting up the size of the game panel
		//setting up the size of voltorbsweeper panel
		frame.setSize((int)sizes[size].getX()+100,(int)sizes[size].getY()+100);
		boardSize=size;
		curboard=new Board(boardSize);
	}
	//this method gets what you input in the mouse
	public void mousePressed(MouseEvent e){
		for (int i=0;i<3;i++){
			int temp=1;
			//deep binary stuff. e.getModifiersEx() returns 2^10 when left click, 2^11 middle, and 2^12 right click, if left click and right click together, it returns 2^10+2^12
			if ((e.getModifiersEx()&(temp<<(10+i)))>0){						//this for loop gets what button has been pressed, but since I only care for left, middle and right
																			//I start from 10(2^10), binary shifts to the right in the for loop, so it gets left, middle and right click
				buttons[i]=true;											//1024 & 4096=0, 1024 & 5120=1024 and other stuff
			}
		}
	}
	//this method does the functions. I realize that we can not exactly simutanously left and right click at the same time, instead we will yield left click and then right click,
	//so we cannot perform our events inside of mousepressed, the events only function when you release the mousebutton
	//if left and right click, e.getmodifiersex() returns 1024 and then 5120(two values)
	public void mouseReleased(MouseEvent e){
		if (!curboard.getGameOver()){										//this events only functions when the game is still going
			mx=e.getX();
			my=e.getY();
			//apparently (49-50)/20=0 make sure the mouse in inside of the board
			if ((mx-50)<0 ||(my-50)<0){
				return;
			}
			//make sure that everything is inside of the board
			if (curboard.inBoard(new Point((mx-50)/20,(my-50)/20))){
				//left right click same time
				//on yeah I noticed something weird, once I double clicked and released, e.getmodifiersex becomes 256, not sure what that button is,
				//but since the only three buttons that I care are 1024, 2048 and 4096, so it doesn't matter
				if (e.getModifiersEx()<1024&&buttons[0]&&buttons[2]){
					//reset the button and does the function
					buttons[0]=false;
					buttons[2]=false;
					curboard.fliparound((mx-50)/20,(my-50)/20);
				}
				//left click
				else if (e.getModifiersEx()<1024&&buttons[0]){
					buttons[0]=false;
					curboard.flip((mx-50)/20,(my-50)/20);
				}
				//right click
				else if (e.getModifiersEx()<1024&&buttons[2]){
					buttons[2]=false;
					curboard.markMine((mx-50)/20,(my-50)/20);
				}
			}
			//once the game is over, ask the player if continue playing or exit, close the message box means automatically exiting
			if (curboard.getGameOver()){
				Replay r=new Replay(curboard.getWin(),frame.getTime(),boardSize,frame);
				//if the user choose to replay
				if (r.replay==0){
					//reset the time, since frame.getTime is in seconds, need to make it in millisecond to match system.currentTimeMillis()
					frame.setStartTime(frame.getTime()*1000);
					//resr the board, sorry no option to rechoose the mode
					curboard=new Board(boardSize);
				}
				else{
					System.exit(0);
				}
			}
		}
	}
	// the rest are overrides, but I don't need them
	public void mouseEntered(MouseEvent e){
	}
	public void mouseExited(MouseEvent e){
	}
	public void mouseClicked(MouseEvent e){
	}
	//this method draws everything, including all the info, the board
	public void paintComponent(Graphics g){
		//layer
		g.setColor(new Color(222,222,222));
		g.fillRect(0,0,getWidth(),getHeight());
		//board
		display=curboard.getDisplayBoard();
		for (int i=0;i<display.length;i++){
			for (int j=0;j<display[i].length;j++){
				drawGrid(display[i][j],50+i*20,50+j*20,g);
			}
		}
		// text info
		g.setColor(new Color(255,0,0));
		g.setFont(font);
		drawInfo(display.length*20+10,display[0].length*20+75,g);
		//draw the happy or sad or normal voltorb
		if (curboard.getGameOver()){
			if (curboard.getWin()){
				g.drawImage(happy,25+display.length*20/2,0,this);
			}
			else{
				showMine(g);
				g.drawImage(sad,25+display.length*20/2,0,this);
			}

		}
		else{
			g.drawImage(normal,25+display.length*20/2,0,this);
		}
	}
	//this method draws individual grid given a postion and a index number of the pictures
	public void drawGrid(int num,int posx, int posy,Graphics g){
		g.drawImage(gridPic,posx,posy,this);
		g.drawImage(gridInfo[num+1],posx+1,posy+1,this);

	}
	//this methods draws all the mines on the board, only display when the player loses
	public void showMine(Graphics g){
		int[][] myboard=curboard.getBoard();
		for (int i=0;i<myboard.length;i++){
			for (int j=0;j<myboard[i].length;j++){
				if (myboard[i][j]==1){
					g.drawImage(voltorb,50+i*20+1,50+j*20+1,this);
				}
			}
		}
	}
	//this method draws the box that displays number of flags you have marked, and time elasped
	public void drawInfo(int x, int y,Graphics g){
		g.drawImage(vbomb,x-30,y-25,this);
		g.drawString(":"+(curboard.getMine()-curboard.getFlag()),x,y);
		if(!curboard.getGameOver()){								//this way time does continue when the game is over
			time=frame.getTime();
		}
		g.drawString("Time: "+time,50,y);
	}
}