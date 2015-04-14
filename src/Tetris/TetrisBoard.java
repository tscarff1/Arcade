package Tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import javax.swing.JPanel;

public class TetrisBoard extends JPanel implements ActionListener{
	
	//an array of the colors with which to paint the board
	private final Color[] COLORS = new Color[]{
		new Color(0,0,0),//default
		new Color(255, 84, 28), //Orange line
		new Color(255, 0, 0), // Red box
		new Color(255, 224, 28),//Yellow Pyramid
		new Color(38, 30, 255), //dark blue L
		new Color(191, 0 , 255),//Purple Reverse L
		new Color(102, 181, 255),//Light blue S
		new Color(46, 217, 0) //Green Reverse S
	};
	
	TetrisGame tg;
	private final int WIDTH;
	private final int HEIGHT;
	public final int SQUARESIZE;
	
	public Shape s;
	public int sX = 5;
	public int sY = 0;
	
	private int[][] board = new int[16][10];

	//these are to be referenced by gameMode in actionPerformed
	public final int PLAYING = 0;
	public final int PAUSED = 1;
	public final int GAMEOVER = 2;
	
	private int gameMode = PLAYING;
	
	public final int DELAY = 800;
	Timer gameTimer;

	//The random number which shall determine the type of piece to place
	private int randNum;
	private int nextNum = (int) (Math.random() * 7);
	
	private int score = 0;
	//the multiplier will increase with each line that is cleared consecutively
	private int multiplier  = 1;
	
	private int linesClear = 0;
	
	public TetrisBoard(TetrisGame tg){
		this.tg = tg;
		WIDTH = tg.WIDTH - 200;
		HEIGHT = tg.HEIGHT;
		SQUARESIZE = tg.SQUARESIZE;
		fillBlankBoard();
		
		createNewShape();
		
		gameTimer =  new Timer(DELAY, this);
		gameTimer.start();
		
		setFocusable(true);
		this.addKeyListener(new KeyListener(this));
	}
	
	public void restartGame(){
		score = 0;
		linesClear = 0;
		
		fillBlankBoard();
		
		createNewShape();
		
		gameTimer =  new Timer(DELAY, this);
		gameTimer.start();
		gameMode = PLAYING;
		setFocusable(true);
	}
	
	public void fillBlankBoard(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 16; j++){
				board[j][i] = 0;
			}
		}
	}

	public void putShapeOnBoard(){
		int posX, posY;
		
		if(board[0][4] != 0){
			setGameMode(GAMEOVER);
		}
		try{
			for(int  i = 0; i < 4; i++){
					posY = sY - s.tShape[i][1];
					posX = sX + s.tShape[i][0];
					board[posY][posX] = s.SHAPETYPE + 1;
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			gameMode = GAMEOVER;
		}
	}
	
	public void createNewShape(){
		randNum = nextNum;
		nextNum = (int) (Math.random() * 7);
		
		s = new Shape(randNum);
		sX = 5;
		sY = 0 + s.getBottom();
	}
	
	//Rotate a piece clockwise
	public void rotateShapeRight(){
		int temp;
		//quadrants are quadrants on a cartesian plane and are used to center the piece
		for(int i = 0; i < 4; i++){
			if(s.quadrant == 1)
				s.tShape[i][0]++;
			if(s.quadrant == 2)
				s.tShape[i][1]--;
			if(s.quadrant == 3)
				s.tShape[i][0]--;
			if(s.quadrant == 4)
				s.tShape[i][1]++;
		}
		
		for(int i = 0; i < 4; i++){
			
			
			temp = s.tShape[i][1];
			s.tShape[i][1] = -1 * s.tShape[i][0] - 1;
			s.tShape[i][0] = temp;
			
			if(s.quadrant == 1)
				s.tShape[i][1]++;
			if(s.quadrant == 2)
				s.tShape[i][0]++;
			if(s.quadrant == 3)
				s.tShape[i][1]--;
			if(s.quadrant == 4)
				s.tShape[i][0]--;
		}	
			s.quadrant++;
			if(s.quadrant > 4)
				s.quadrant = 1;
		
		repaint();
	}
	
	//Rotate a piece clockwise
	public void rotateShapeRight(Shape shape){
		int temp;
		//quadrants are quadrants on a cartesian plane and are used to center the piece
		for(int i = 0; i < 4; i++){
			if(shape.quadrant == 1)
				shape.tShape[i][0]++;
			if(shape.quadrant == 2)
				shape.tShape[i][1]--;
			if(shape.quadrant == 3)
				shape.tShape[i][0]--;
			if(shape.quadrant == 4)
				shape.tShape[i][1]++;
		}
		
		for(int i = 0; i < 4; i++){
			
			
			temp = shape.tShape[i][1];
			shape.tShape[i][1] = -1 * s.tShape[i][0] - 1;
			shape.tShape[i][0] = temp;
			
			if(shape.quadrant == 1)
				shape.tShape[i][1]++;
			if(shape.quadrant == 2)
				shape.tShape[i][0]++;
			if(shape.quadrant == 3)
				shape.tShape[i][1]--;
			if(shape.quadrant == 4)
				shape.tShape[i][0]--;
		}	
			shape.quadrant++;
			if(shape.quadrant > 4)
				shape.quadrant = 1;
	}
	
	//UNUSED NOW. WAS ONLY USED FOR TESTING
	/*
	public void rotateShapeLeft(){
		int temp;
			for(int i = 0; i < 4; i++){
				temp = s.tShape[i][0];
				s.tShape[i][0] = -1 * s.tShape[i][1] - 1;
				s.tShape[i][1] = temp;
			}
			repaint();
			
			s.quadrant--;
			if(s.quadrant < 1)
				s.quadrant = 4;
	}
	*/
	
	public int getShapeLeft(){
		return sX - s.getLeft();
	}
	
	public int getShapeRight(){
		return sX + s.getRight();
	}
	
	public int getShapeBottom(){
		return sY + -1 * s.getBottom();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
			//g.setColor(Color.blue);
			//g.fillRect(0, 0, WIDTH, HEIGHT);
			drawBoard(g);
			drawShape(g);
			drawScore(g);
			drawLinesClear(g);
			drawNextShape(g);
					
			if(gameMode == GAMEOVER){
				g.setColor(Color.WHITE);
				g.drawString("GAME OVER, Press SPACE to try again", 100, 200);
			}
	}
	
	public void drawShape(Graphics g){
		g.setColor(s.getColor());
		int startX,startY;
		for(int i =0; i < 4; i++){
			startX = s.tShape[i][0];
			startY = s.tShape[i][1];
			g.fillRect(sX * SQUARESIZE + SQUARESIZE * startX, sY * SQUARESIZE - SQUARESIZE * startY, SQUARESIZE, SQUARESIZE);
		}
	}
	
	public void drawNextShape(Graphics g){
		Shape shape = new Shape(nextNum);
		int sSize = 19;
		g.setColor(shape.getColor());
		int startX,startY;
		for(int i =0; i < 4; i++){
			startX = shape.tShape[i][0];
			startY = shape.tShape[i][1];
			g.fillRect(480 + sSize * startX, 120 - sSize * startY, sSize, sSize
					
			);
		}
	}
	public void drawBoard(Graphics g){
		for(int y = 0; y < 16; y++){
			for(int x = 0; x < 10; x++){
				g.setColor(COLORS[board[y][x]]);
				g.fillRect(x * SQUARESIZE, y * SQUARESIZE, SQUARESIZE, SQUARESIZE);
			}
		}
	}
	
	public void drawScore(Graphics g){
		g.setColor(Color.black);
		g.drawString("Score: " + score, 420, 20);
		g.drawString("Next Piece", 420, 50);
		g.fillRect(420, 60, 160, 160);
	}
	
	public void drawLinesClear(Graphics g){
		g.setColor(Color.black);
		g.drawString("Lines Clear: " + linesClear, 420, 300);
	}
	
	public int[] getXValuesAtYPos(int yPos){
		int[] xVals;
		int size = 0;
		int pos = 0;
		for(int i = 0; i < 10; i++){
			if(board[yPos][i] > 0)
				size++;
		}
		xVals = new int[size];
		for(int i = 0; i < 10; i++){
			if(board[yPos][i] > 0){
				xVals[pos] = i;
				pos++;
			}
		}
		
		return xVals;
	}
	
	//actually checks the area below the piece is occupied
	public boolean isShapeCollided(){
		//first, check to see if the piece has reached the bottom!
		//that counts as a collision
		if(getShapeBottom() >= 15)
			return true;
		
		//check every part of the piece only if the piece is on the board
		if(sY > 0){
			for(int i = 0; i < 4; i++){
				if(board[sY - s.tShape[i][1] + 1][sX + s.tShape[i][0]] > 0)
					return true;
			}
		}
		else{
			int xvals[] = s.getXValuesAtYPos(s.getBottom());
			for(int i = 0; i < xvals.length; i++){
				if(board[sY - s.getBottom()][sX + xvals[i]] > 0)
					return true;
			}
		}
		return false;
	}
	
	public boolean willShapeCollideLeft(){
		if(sY - s.getBottom() < 0){
			return true;
		}
		if(sX - s.getLeft() <= 0){
			return true;
		}
		else if(sX - s.getLeft()> 0){	
			for(int i = 0; i < 4; i++){
				if(sY - s.tShape[i][1] < 0)
					return true;
				
				if(board[sY - s.tShape[i][1]][sX + s.tShape[i][0] - 1] > 0)
					return true;
			}
		}
		return false;
	}
	
	public boolean willShapeCollideRight(){
		int rightSide = 9;
		if(s.SHAPETYPE == 0)
			rightSide++;
		
		if(sY - s.getBottom() < 0){
			return true;
		}
		if(sX + s.getRight() >= rightSide){
			System.out.println("COLLISION DETECTED1");
			return true;
		}
		else if(sX + s.getRight() < 9){	
			for(int i = 0; i < 4; i++){
				if(sY - s.tShape[i][1] < 0)
					return true;
				
				if(board[sY - s.tShape[i][1]][sX + s.tShape[i][0] + 1] > 0)
					return true;
			}
		}
		return false;
	}
	/*
	 * The following set of methods are used in deterining if you are allowed to rotate the shape
	 */
	public int[][] getRotatedShapeCoords(){
		int[][] coords = new int[4][2];
		for(int  i = 0; i < 4; i++){
			for(int j = 0; j < 2; j++){
				coords[i][j] = s.tShape[i][j];
			}
		}
		int temp;
		//quadrants are quadrants on a cartesian plane and are used to center the piece
		for(int i = 0; i < 4; i++){
			if(s.quadrant == 1)
				coords[i][0]++;
			if(s.quadrant == 2)
				coords[i][1]--;
			if(s.quadrant == 3)
				coords[i][0]--;
			if(s.quadrant == 4)
				coords[i][1]++;
		}
		
		for(int i = 0; i < 4; i++){
			
			
			temp = coords[i][1];
			coords[i][1] = -1 * coords[i][0] - 1;
			coords[i][0] = temp;
			
			if(s.quadrant == 1)
				coords[i][1]++;
			if(s.quadrant == 2)
				coords[i][0]++;
			if(s.quadrant == 3)
				coords[i][1]--;
			if(s.quadrant == 4)
				coords[i][0]--;
		}	
		return coords;
	}
	
	//Check to see if a shape is in a place it shouldnt be
	//Used when turning a piece
	public boolean isCollision(int[][] coords){
		for(int i = 0; i < 4; i++){	
			if(sX - coords[i][0] < 0)
				return true;
			if(sX + coords[i][0] > 9)
				return true;
			if(sY - coords[i][1] > 15)
				return true;
			if(sY - coords[i][1] < 0)
				return true;
			if(board[sY - coords[i][1]][sX + coords[i][0]] > 0)
				return true;
		}
			return false;
	}
	
	public boolean canRotate(){
		int[][] coords = getRotatedShapeCoords();
		if(!isCollision(coords))
			return true;
		return false;
	}
	/*
	 * The next set of methods are used to check if a line needs to be cleared, and to actually do so
	 */
	
	public int[] getFullLines(){
		int[] lines = new int[4]; //no more than 4 lines will ever need to be cleared
		int pos = 0;
		boolean full = true;
		//first i will fill the array with -1's
		for(int i = 0; i < 4; i++)
			lines[i] = -1;
		
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 10; j++){
				if(board[i][j] == 0){
					full = false;
				}
			}
			if(full == true){
				//only reaches this if it ever finishes a line
				lines[pos] = i;
				pos++;
			}
			full = true;
		}
		return lines;
	}
	
	public void moveLinesDown(int start){
		for(int i = start; i > 0; i--){
			for(int j = 0; j < 10; j++)
				board[i][j] = board[i - 1][j];
		}
		
		for(int i = 0; i < 10; i++){
			board[0][i] = 0;
		}
	}
	public void clearFullLines(){
		int[] linesToClear = getFullLines();
		for(int i = 0; i < 4; i++){
			if(linesToClear[i] > -1){
				for(int j = 0; j < 10; j++){
					board[linesToClear[i]][j] = 0;
					multiplier++;
					linesClear++;
				}
				score += 10 * multiplier;
				multiplier = 0;
				moveLinesDown(linesToClear[i]);
				//reset the gameTimer to be slightly faster based on number of cleared lines
				gameTimer.stop();
				gameTimer =  new Timer(DELAY - linesClear/2, this);
				gameTimer.start();
			}
		}
		
	}
	
	public void increaseScore(int amount){
		score += amount;
	}
	
	private void setGameMode(int mode){
		gameMode = mode;
		
		if(gameMode == GAMEOVER)
			gameTimer.stop();
	}
	
	public int getGameMode(){
		return gameMode;
	}
	
		public void actionPerformed(ActionEvent e){
		
		if(gameMode == PLAYING){
			if(!isShapeCollided()){
				sY++;
				score += 5;
			}
			else{
				putShapeOnBoard();
				createNewShape();
				clearFullLines();
				}
			repaint();
		}
		
	}
}

