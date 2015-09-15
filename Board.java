//this class is for the board, it contains methods like generate the board, flip the mines, mark the flags, count mines, count flags, and return other variables in this class
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
public class Board{
	private int[][] myBoard;													//this board is for mines and safe grids, 1 is mine and 0 is nothing
	private int[][] displayBoard;												//this board is for displaying number of mines around a grid, hidden grid and flags
	//-1 hidden, 0 no mine, 1-8 #number of mines around it, 9-flag
	private int mines;															//number of mines in the game
	private int flags=0;														//number of flags that you have marked
	private boolean Gameover=false;												//check if the game is over
	private boolean win=false;													//if the game is over, check if you win or not
	private boolean start=false;												//if start, generate the board, starts become true once the user clicked on the first grid
	public Board(int size){
		//size 0: 9x9 10 mines, size 1: 16x16 40 mines, size 3: 16x30 100 mines
		if (size==0){
			myBoard=new int[9][9];
			displayBoard=new int[9][9];
			mines=10;
		}
		else if (size==1){
			myBoard=new int[16][16];
			displayBoard=new int[16][16];
			mines=40;
		}
		else if (size==2){
			myBoard=new int[30][16];
			displayBoard=new int[30][16];
			mines=100;
		}
		for (int i=0;i<myBoard.length;i++){
			for (int j=0;j<myBoard[i].length;j++){
				myBoard[i][j]=0;												//every grid is safe before generating the board
				displayBoard[i][j]=-1;											//set every single grids to hidden
			}
		}
	}
	//two getter methods
	public int[][] getDisplayBoard(){
		return displayBoard;
	}
	public int[][] getBoard(){
		return myBoard;
	}
	// this method generates randomly generates the board, but it makes sure that no mine will be placed at or adjcent to the initial position
	public int[][] generate(int initx, int inity){
		int px,py;
		int nummine=0;
		Random pos=new Random();
		//generates nummine number of mines
		while (nummine<mines){
			px=pos.nextInt(myBoard.length);
			py=pos.nextInt(myBoard[0].length);
			//make sure that it's not at or adjecent grid
			if (myBoard[px][py]!=1 && Math.abs(initx-px)>1 || myBoard[px][py]!=1 && Math.abs(inity-py)>1){
				myBoard[px][py]=1;
				nummine+=1;
			}
		}
		return myBoard;
	}
	//this method is used for double clicking. If the number of flags around it is the same as the number of mines around it, flip all the adjecent hidden grids
	public void fliparound(int x,int y){
		// only able to do it on a flipped grid
		if (displayBoard[x][y]>0 && displayBoard[x][y]<9){
			if (countflag(x,y)==displayBoard[x][y]){
				//flip all the hidden grids around this grid
				for (int i=x;i<x+3;i++){
					for (int j=y;j<y+3;j++){
						if (inBoard(new Point(i-1,j-1))&& displayBoard[i-1][j-1]==-1){			//if the point is in the board and it's hidden
							flip(i-1,j-1);
						}
					}
				}
			}
		}
	}
	//this method is used for right click, basically flag a hidden grid or unflag a flagged grid
	public void markMine(int x,int y){
		if (displayBoard[x][y]==-1){
			displayBoard[x][y]=9;
			flags+=1;
		}
		else if (displayBoard[x][y]==9){
			displayBoard[x][y]=-1;
			flags-=1;
		}
	}
	// this method is used for left click, flip a hidden grid
	public void flip(int x, int y){
		// if this is the initial click
		if (!start){
			generate(x,y);
			start=true;
		}
		//if this grid is not in the board, return
		if (!inBoard(new Point(x,y))){
			return;
		}
		//if this grid is not hidden, return
		if (displayBoard[x][y]!=-1){						//you are only allowed to flip the hidden grids
			return;
		}
		//if you flipped a hidden grid with mine, BOOM you looooooooose!
		if (myBoard[x][y]==1){
			Gameover=true;
		}
		else{
			//FLOODFILL ALL OVER AGAIN
			int count;
			ArrayList <Point> points=new ArrayList<Point>();
			points.add(new Point(x,y));
			while (points.size()>0){
				//why bother counting it again if it already has been added to the board
				if (displayBoard[(int)points.get(0).getX()][(int)points.get(0).getY()]!=-1){
					count=displayBoard[(int)points.get(0).getX()][(int)points.get(0).getY()];
				}
				else{
					count=countmine((int)points.get(0).getX(),(int)points.get(0).getY());
				}
				//this floodfill needs to add the adjcent 8 squares
				//flip all the grids around the first grid in the arraylist if this grid has no mines around it
				if (count==0 &&displayBoard[(int)points.get(0).getX()][(int)points.get(0).getY()]==-1){
					Point[] addpoints={new Point((int)points.get(0).getX()+1,(int)points.get(0).getY()),
									   new Point((int)points.get(0).getX()-1,(int)points.get(0).getY()),
									   new Point((int)points.get(0).getX(),(int)points.get(0).getY()+1),
									   new Point((int)points.get(0).getX(),(int)points.get(0).getY()-1),
									   new Point((int)points.get(0).getX()+1,(int)points.get(0).getY()+1),
									   new Point((int)points.get(0).getX()+1,(int)points.get(0).getY()-1),
									   new Point((int)points.get(0).getX()-1,(int)points.get(0).getY()+1),
									   new Point((int)points.get(0).getX()-1,(int)points.get(0).getY()-1)};
					for (int i=0;i<addpoints.length;i++){
						if (inBoard(addpoints[i])){
							points.add(addpoints[i]);
						}
					}

				}
				//change the displayboard from hidden to a number(number of mines
				displayBoard[(int)points.get(0).getX()][(int) points.get(0).getY()]=count;
				points.remove(points.get(0));
			}
			//check if you beat the game
			if (finish()){
				Gameover=true;
				win=true;
			}
		}
	}
	//count the number of mines around a given grid
	public int countmine(int x, int y){										//x,y is not a mine since you just opened it
		int tot=0;
		for (int i=x;i<x+3;i++){
			for (int j=y;j<y+3;j++){
				if (inBoard(new Point(i-1,j-1))){
					if (myBoard[i-1][j-1]==1){
						tot+=1;
					}
				}
			}
		}
		return tot;
	}
	//count the number of flags around a given grid
	public int countflag(int x, int y){
		int tot=0;
		for (int i=x;i<x+3;i++){
			for (int j=y;j<y+3;j++){
				if (inBoard(new Point(i-1,j-1))){
					if (displayBoard[i-1][j-1]==9){
						tot+=1;
					}
				}
			}
		}
		return tot;
	}
	//check if you beat the game
	public boolean finish(){
		int tot=0;
		for (int i=0;i<displayBoard.length;i++){
			for (int j=0;j<displayBoard[i].length;j++){
				//check number of flipped squares, if it equals length*width-number of mines, it means that you win
				if (displayBoard[i][j]>=0 && displayBoard[i][j]<9){
					tot+=1;
				}
			}
		}
		return displayBoard.length*displayBoard[0].length-tot==mines;
	}
	//this method checks if a given point is in the board
	public boolean inBoard(Point p){
		return (int)p.getX()>=0 &&(int)p.getX()<myBoard.length && (int) p.getY()>=0 && (int)p.getY()<myBoard[0].length;
	}
	//getter methods
	public boolean getGameOver(){
		return Gameover;
	}
	public boolean getWin(){
		return win;
	}
	public int getMine(){
		return mines;
	}
	public int getFlag(){
		return flags;
	}
}