package Tetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class KeyListener extends KeyAdapter {
	TetrisBoard board;
	public KeyListener(TetrisBoard b){
		board = b;
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_UP && board.sX - board.s.getBottom() < 15 && board.canRotate()){
			board.rotateShapeRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN && !board.isShapeCollided()){
			board.sY++;
			board.increaseScore(5);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT && !board.willShapeCollideLeft()){
			board.sX--;
		}
			
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && !board.willShapeCollideRight())
			board.sX++;
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE && board.getGameMode() == board.GAMEOVER){
			System.out.println("RESTARTING");
			board.restartGame();
		}
		board.repaint();
	}
}
