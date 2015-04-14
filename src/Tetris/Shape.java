package Tetris;
/*
 * ********************************************************
 * Note: Within this class, negative y values are DOWN.
 * ********************************************************
 */
import java.awt.Color;

//Contains the information on all of the shapes
/*
 * The number to piece referencer!
 * 0 = line
 * 1 = Box
 * 2 = Pyramid
 * 3 = L
 * 4 = reverse L
 * 5 = S
 * 6 = Reverse S
 */
public class Shape {
	//array of the shapes of the tetronimos
	private final int[][][] TETRONIMOS = new int[][][]{
			{{0, 0}, {1, 0}, {2, 0}, {3, 0}},//Straight Line
			{{0, 1}, {1, 1}, {1, 0}, {0, 0}},// Box Shape
			{{0, 0}, {1, 1}, {1, 0}, {2, 0}},//Pyramid Shape
			{{0, 2}, {0, 1}, {0, 0}, {1, 0}},//L Shape
			{{0, 0}, {1, 0}, {1, 1}, {1, 2}}, //Reverse L Shape
			{{0, 0}, {1, 0}, {1, 1}, {2, 1}},//S Shape
			{{0, 1}, {1, 1}, {1, 0}, {2, 0}}//Reverse S Shape
	};
	
	
	//an array of the colors for the pieces
	private final Color[] COLORS = new Color[]{
		new Color(255, 84, 28), //Orange line
		new Color(255, 0, 0), // Red box
		new Color(255, 224, 28),//Yellow Pyramid
		new Color(38, 30, 255), //dark blue L
		new Color(191, 0 , 255),//Purple Reverse L
		new Color(102, 181, 255),//Light blue S
		new Color(46, 217, 0) //Green Reverse S
	};
	
	public int[][] tShape;//the shape of the chosen tetronimo
	public int quadrant; //the quadrant of rotation of the piece. starts as 1
	public final Color TCOLOR;
	public final int SHAPETYPE;
	public Shape(int type){
		tShape = TETRONIMOS[type];
		TCOLOR = COLORS[type];
		SHAPETYPE = type;
		quadrant = 1;
		
		//center the piece
		for(int i = 0; i < 4; i++){
			tShape[i][0] --;
			tShape[i][1]--;
		}
	}
	
	public Shape(Shape s){
		this.tShape = s.tShape;
		TCOLOR = COLORS[s.SHAPETYPE];
		SHAPETYPE = s.SHAPETYPE;
		quadrant = 1;
	}
	
	public Color getColor(){
		return TCOLOR;
	}
	
	//return how far left it stretches
	public int getLeft(){
		int min = 0;
		for(int i = 0; i < 4; i++){
			if(tShape[i][0] < min)
				min = tShape[i][0];
		}
		return Math.abs(min);
	}
	
	public int getRight(){
		int max = 0;
		for(int i = 0; i < 4; i++){
			if(tShape[i][0] > max)
				max = tShape[i][0];
		}
		return max;
	}
	
	public int getBottom(){
		int max = 0;
		for(int i = 0; i < 4; i++){
			if(tShape[i][1] < max)
				max = tShape[i][1];
		}
		return max;
	}
	
	public int[] getXValuesAtYPos(int yTest){
		int[] vals;
		int valSize = 0;
		int nextPos = 0;
		for(int i = 0; i < 4; i++){
			if(tShape[i][1] == yTest)
				valSize++;
		}
		vals = new int[valSize];
		for(int i = 0; i < 4; i++){
			if(tShape[i][1] == yTest){
				vals[nextPos] = tShape[i][0];
				nextPos++;
			}
		}
		
		
		return vals;
	}
	
	public int getHeight(){
		int max = 0, min = 0;
		for(int i = 0; i < 4; i++){
			if(tShape[i][1] > max)
				max = tShape[i][1];
			if(tShape[i][1] < min)
				min = tShape[i][1];
		}
			return Math.abs(max - min + 1);
	}
	
	public int getWidth(){
		int max = 0, min = 0;
		for(int i = 0; i < 4; i++){
			if(tShape[i][0] > max)
				max = tShape[i][0];
			if(tShape[i][0] < min)
				min = tShape[i][0];
		}
			return Math.abs(max - min + 1);
	}
	
}
