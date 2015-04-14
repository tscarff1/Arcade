package Tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JApplet;

public class TetrisGame extends JApplet{
	public final int WIDTH;
	public final int HEIGHT;
	public final int SQUARESIZE;
	
	public TetrisGame(){
		SQUARESIZE = 40;
		WIDTH = 10 * SQUARESIZE + 200;
		HEIGHT = 16 * SQUARESIZE;
	}
	
	public void init(){
		setSize(WIDTH, HEIGHT);
		getContentPane().add(new TetrisBoard(this));
	}
}
/*
import javax.swing.JPanel;

public class TetrisGame extends JApplet
{
    public class DemoPanel extends JPanel{
    	private int blueValue = 0; // Starts out as zero
    	void DemoPanel(){
     // Does nothing here
    		}
  public void paintComponent(Graphics page){
     super.paintComponent(page);
     Color currentColor = new Color(0,0,blueValue); // Updates currentColor with the new color
     setBackground(currentColor); // Sets the background to the new color
     blueValue++;
     	if(blueValue > 255) // If blueValue is greater than maximum, reset it
     		blueValue = 0;
     	repaint();
  		}
    }
    public void init(){
    	setSize(500,256);
    	getContentPane().add(new DemoPanel());
    }
}
*/