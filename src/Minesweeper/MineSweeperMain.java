package Minesweeper;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class MineSweeperMain{

	public enum GameType{
		BEGINNER(8,8,10), INTERMEDIATE(16,16,40), EXPERT(32,16,99);
		
		public final int ROWS;
		public final int COLS;
		public final int MINES;
		
		GameType(int r, int c, int m){
			ROWS = r;
			COLS = c;
			MINES = m;
		}
	};
	
	public final int WIDTH;
	public final int HEIGHT;
	public final int sqSize = 20;
	
	//Initialize menu stuffs
	JMenuBar menuBar;
	JMenu optionsMenu;
	JMenuItem newGame;
	
	JRadioButtonMenuItem begRB, intRB, expRB;
	
	Listener listener = new Listener(this);
	
	
	//Initialize the field (The first line MUST come before the layout)
	private int rows = 8,cols = 8, mines = 10;
	private int spacesLeft= rows*cols-mines, minesLeft = mines;
	private int roundTime = 0;
	private boolean timerStarted = false;
	private boolean canClick = true;
	private boolean win = false;
	
	//Initializing the layout
	private MSPanel panel = new MSPanel(this);
	private HeaderPanel hpanel = new HeaderPanel(this);
	private JButton smileyBut = new JButton("RESET");
	private MSFrame frame = new MSFrame();
	
	public int getWidth(){
		return rows;
	}
	
	public int getHeight(){
		return cols;
	}
	
	public int getMines(){
		return mines;
	}
	
	public void setGameMode(GameType type){
		rows = type.ROWS;
		cols = type.COLS;
		mines = type.MINES;
		spacesLeft = rows*cols - mines;
		minesLeft = mines;
		}
	
	public void resetGame(){
		panel.reset();
		spacesLeft = rows*cols - mines;
		minesLeft = mines;
		
		frame.setSize(rows*sqSize + 8, cols*sqSize + 80);
		hpanel.setSize(rows*sqSize + 8, 26);
		smileyBut.setBounds((rows*sqSize + 8)/2 - 40, -1, 80, 30);
		panel.setBounds(0, 27, rows*sqSize + 8, cols*sqSize+8);
	}
	
	@SuppressWarnings("serial")
	private class MSFrame extends JFrame{
			private MSFrame(){
				//initialize the generic parts of the frame
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setSize(250, 280);
				setResizable(false);
				setLocationRelativeTo(null);
				setTitle("Minesweeper");
				setVisible(true);
				
				//****************
				//Create the menu
				//****************
				menuBar = new JMenuBar();
				
				optionsMenu = new JMenu("Options");
				menuBar.add(optionsMenu);
				
				//add new game option
				newGame = new JMenuItem("New Game",
						KeyEvent.VK_N);
				optionsMenu.add(newGame);
				
				optionsMenu.addSeparator();
				
				//add difficulty choices
				ButtonGroup bg = new ButtonGroup();
				begRB = new JRadioButtonMenuItem("Beginner");
				intRB = new JRadioButtonMenuItem("Intermediate");
				expRB = new JRadioButtonMenuItem("Expert");
				begRB.setSelected(true);
				
				bg.add(begRB);
				bg.add(intRB);
				bg.add(expRB);
				
				optionsMenu.add(begRB);
				optionsMenu.add(intRB);
				optionsMenu.add(expRB);
				
				begRB.addActionListener(listener);
				intRB.addActionListener(listener);
				expRB.addActionListener(listener);
				newGame.addActionListener(listener);
				//Add the menu bar to the frame
				setJMenuBar(menuBar);
				
				//Add action listener to the reset button
				smileyBut.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						resetGame();
					}
					
				});
				
				
				//**********************
				//**Setting the layout**
				//**********************
				
				Container pane = getContentPane();
				setLayout(null);
				Insets insets = pane.getInsets();
				int frameWidth = rows*sqSize + 8;
				
				add(hpanel);
				hpanel.setBounds(insets.left, insets.top, frameWidth, 30);
				hpanel.setLayout(null);
				hpanel.add(smileyBut);
				smileyBut.setBounds(frameWidth/2 - 40, -1, 80, 30);
				add(panel);
				panel.setBounds(insets.left, insets.top + 30, frameWidth, cols*sqSize+8);
				
				
			}
			
			
		}
	
	@SuppressWarnings("serial")
	public class HeaderPanel extends JPanel{
		private MineSweeperMain overlord;
		public HeaderPanel(MineSweeperMain msm){
			overlord = msm;
		}
		
		public void paintComponent(Graphics g){
			super.paintComponents(g);
			
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(minesLeft), 20,20);
		}
	}
	
	public class MSPanel extends JPanel{
		private int width;
		private int height;
		private int mines;
		private MineSweeperMain overlord;
		
		private Image iSquare= new ImageIcon(this.getClass().getResource("square.png")).getImage();
		private Image iSelect = new ImageIcon(this.getClass().getResource("selected.png")).getImage();
		private Image iUhoh = new ImageIcon(this.getClass().getResource("uhoh.png")).getImage();
		private Image iMine = new ImageIcon(this.getClass().getResource("mine.png")).getImage();
		private Image iFlag = new ImageIcon(this.getClass().getResource("flag.png")).getImage();
		private Image[] iNumbers = new Image[8];
		
		private int[][] field;
		private int[][] clicked;
		
		public MSPanel(MineSweeperMain msm){
			//populate the array of numbers images
			for(int a = 1; a <= 8; a++){
				iNumbers[a-1] = new ImageIcon(this.getClass().getResource(a + ".png")).getImage();
			}
			
			field = new int[width][height];
			overlord = msm;
			reset();
			repaint();
			addMouseListener(new MouseListener());
		}
		public void reset(){
			width = overlord.getWidth();
			height = overlord.getHeight();
			field = new int[width][height];
			clicked = new int[width][height];
			mines = overlord.getMines();
			placeMines();
			spacesLeft = rows*cols-mines;
			canClick = true;
			win = false;
			repaint();
		}
		
		private void placeMines(){
			for(int i = 0; i < mines; i++){
				int x =(int)(Math.random() * width);
				int y =(int)(Math.random() * height);
				//check to see if a mine has already been placed in this position
				if(field[x][y] != -1)
					field[x][y] = -1;
				else//if it has we just run an extra iteration
					i--;
			}
		}
		
		//get the number of mines surrounding a point
		private int getSurroundingMines(int x, int y){
			int num = 0;
			//brute force check each of the 8 surrounding spots
			//start with above
			if(y > 0){
				if(x > 0 && field[x-1][y-1] == -1)
					num++;
				if(field[x][y-1] == -1)
					num++;
				if(x < width - 1 && field[x+1][y-1] == -1)
					num++;
			}
			//now directly horizontal
			if(x > 0 && field[x-1][y] == -1)
				num++;
			if(x < width - 1 && field[x+1][y] == -1)
				num++;
			//and for below
			if(y < height - 1){
				if(x > 0 && field[x-1][y+1] == -1)
					num++;
				if(field[x][y+1] == -1)
					num++;
				if(x < width - 1 && field[x+1][y+1] == -1)
					num++;
			}
			return num;
		}
		
		//return if the given coordinates contain a mine
		public boolean isBomb(int x, int y){
			return (field[x][y] == -1);
		}
		
		//set a coordinate to have been selected
		public void setClicked(int x, int y){
			try{
				if(clicked[x][y] != 1){
					if(!panel.isBomb(x, y)){
						spacesLeft--;
					}
					clicked[x][y] = 1;
					if(spacesLeft == 0)
						win();
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				System.err.println("Clicked out of bounds.");
			}
			repaint();
		}

		public void win(){
			canClick = false;
			win = true;
		}
		
		//set off the bombs with the given point being highlighted
		public void setBombs(int x, int y){
			//System.out.println("set point (" + x + ", " + y+ ") as uhoh");
			
			//just say the rest have been clicked so they display regularly
			for(int i = 0; i < cols; i++){
				for(int j = 0; j <rows; j++){
					if(field[j][i] == -1)
						clicked[j][i] = 1;
				}
			}
			
			clicked[x][y] = 2;//Clicked and has bomb, UH-OH!
			canClick = false;
		}
		
		//Clear surrounding points. Works recursively until the checkQueue is empty
		private void clearNearby(int x, int y){
			//System.out.println("CHECK NEARBY CALLED");
			Queue<Point> checkQueue = new LinkedList<Point>();
			//add the point to the checkQueue
			checkQueue.add(new Point(x, y));
			//check all of the surrounding points, clear them, and if blank add them to the checkQueue
			while(!checkQueue.isEmpty()){
				//Pop the point from the front of the queue
				Point p = checkQueue.poll();
				//check the row above our point
				if(p.y > 0){
					//top left
					if(p.x > 0){
						//check if it needs to be added to the checker queue
						if(getSurroundingMines(p.x-1,p.y-1) == 0 && clicked[p.x-1][p.y-1] == 0)
							checkQueue.add(new Point(p.x-1,p.y-1));
						//now make it clicked!
						setClicked(p.x-1, p.y-1);
					}

					//top-mid
					
					if(getSurroundingMines(p.x,p.y-1) == 0 && clicked[p.x][p.y-1] == 0)
						checkQueue.add(new Point(p.x,p.y-1));
					setClicked(p.x,p.y-1);
					
					//top right
					if(p.x < rows - 1){
						if(getSurroundingMines(p.x+1,p.y-1) == 0 && clicked[p.x+1][p.y-1] == 0)
							checkQueue.add(new Point(p.x+1,p.y-1));
						setClicked(p.x+1, p.y-1);
					}
				}
				
				//Now for the middle row
					//left
					if(p.x > 0){
						if(getSurroundingMines(p.x-1,p.y) == 0 && clicked[p.x-1][p.y] == 0)
							checkQueue.add(new Point(p.x-1,p.y));
						setClicked(p.x-1, p.y);
					}

					//right
					if(p.x < rows -1){
						if(getSurroundingMines(p.x+1,p.y) == 0 && clicked[p.x+1][p.y] == 0)
							checkQueue.add(new Point(p.x+1,p.y));
						setClicked(p.x+1, p.y);
					}
				//check the row below our point
				if(p.y < cols-1){
					//bot left
					if(p.x > 0){
						if(getSurroundingMines(p.x-1,p.y+1) == 0 && clicked[p.x-1][p.y+1] == 0)
							checkQueue.add(new Point(p.x-1,p.y+1));
						setClicked(p.x-1, p.y+1);
					}

					//bot-mid
					if(getSurroundingMines(p.x,p.y+1) == 0 && clicked[p.x][p.y+1] == 0)
						checkQueue.add(new Point(p.x,p.y+1));

					setClicked(p.x,p.y+1);
					//bot right
					if(p.x < rows -1){
						if(getSurroundingMines(p.x+1,p.y+1) == 0 && clicked[p.x+1][p.y+1] == 0)
							checkQueue.add(new Point(p.x+1,p.y+1));
						setClicked(p.x+1, p.y+1);
					}
				}
			}
		}
		
		//add a flag to a coordinate
		public void flag(int x, int y){
			if(clicked[x][y] == 0){
				clicked[x][y] = -1;
				minesLeft--;
				hpanel.repaint();
			}
			else if(clicked[x][y] == -1)
				clicked[x][y] = 0;
				minesLeft++;
				hpanel.repaint();
			repaint();
		}
		
		public boolean isFlagged(int x, int y){
			if(clicked[x][y] == -1)
				return true;
			return false;
		}
		
		public void paint(Graphics g){
			super.paint(g);
			//Draw a blank board
			for(int i = 0 ; i < height; i++){
				for(int j = 0; j < width; j++){
					//System.out.println("(j:" + j + ", i:" + i + ") is " + clicked[j][i]);
					
					//<= 0 because a -1 means flagged, so you will want the blank square
					if(clicked[j][i] <= 0)
						g.drawImage(iSquare,j*sqSize, i*sqSize, this);

					else if(clicked[j][i] == 1){
						g.drawImage(iSelect,j*sqSize, i*sqSize, this);
						if(field[j][i] != -1)
							drawNumber(g, j, i);
					}
					else if(clicked[j][i] == 2)
							g.drawImage(iUhoh, j*sqSize, i*sqSize, this);
					
				
					if(clicked[j][i] == -1){
							g.drawImage(iFlag, j*sqSize, i*sqSize, this);
					}
						
					//drawing where the mines are
					if(field[j][i] == -1){
						//g.setColor(Color.black);
						//g.drawString("X", j*sqSize, i*sqSize + sqSize);
						if(clicked[j][i] > 0)
							g.drawImage(iMine,j*sqSize, i*sqSize, this);

					}
				}
				if(win){
					g.setColor(Color.WHITE);
					g.fillRect(35,20, 60, 15);
					g.setColor(Color.red);
					g.drawString("YOU WIN", 40, 30);
				}
			}
		}
		
		private void drawNumber(Graphics g, int x, int y){
			int num = getSurroundingMines(x, y);
			if(num > 0){
				g.drawImage(iNumbers[num-1],x*sqSize, y*sqSize, this);
			}
			
		}
	}//end MSPanel
	
	private MineSweeperMain(){
		WIDTH = 500;
		HEIGHT = 450;
	}
	
	private class Listener implements ActionListener, ItemListener{
		MineSweeperMain msw;
		public Listener(MineSweeperMain mineSweeperMain) {
			msw = mineSweeperMain;
		}

		public void actionPerformed(ActionEvent e){
			if(e.getSource() == newGame){
				msw.resetGame();
				//System.out.println("ACTION LISTENER DETECTED NEW GAME");
			}
			
			if(e.getSource() == begRB){
				msw.setGameMode(GameType.BEGINNER);
				msw.resetGame();
			}
			if(e.getSource() == intRB){
				msw.setGameMode(GameType.INTERMEDIATE);
				msw.resetGame();
			}
			if(e.getSource() == expRB){
				msw.setGameMode(GameType.EXPERT);
				msw.resetGame();
			}
		}

		public void itemStateChanged(ItemEvent e){
			if(e.getSource() == newGame){
				msw.resetGame();
				//System.out.println("ITEM LISTENER DETECTED NEW GAME");
			}
		}
	}
	
	private class MouseListener extends MouseAdapter{
		public void mouseReleased(MouseEvent e){
			if(canClick){
				int rX = (int) (e.getX()/sqSize);
				int rY = (int) (e.getY()/sqSize);
				if(rX > -1 && rX < rows && rY > -1 && rY < cols){
					if(e.getButton() == 1 && !panel.isFlagged(rX, rY)){
						panel.setClicked(rX, rY);
						if(panel.isBomb(rX, rY)){
							panel.setBombs(rX,rY);
						}
						else if(panel.getSurroundingMines(rX, rY) == 0){
							panel.clearNearby(rX, rY);

						}
					}
					if(e.getButton() == 3)//3 is right mouse button
						panel.flag(rX, rY);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		MineSweeperMain runner = new MineSweeperMain();
		runner.resetGame();
	}
	
}
