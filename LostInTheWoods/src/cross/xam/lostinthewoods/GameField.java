package cross.xam.lostinthewoods;

import java.util.ArrayList;

public class GameField {

	private ArrayList<Character> charactersOnField = new ArrayList<Character>();
	
	private Terrain fieldTerrain;
	
	private GameObject fieldObject;
	
	private int xCoordinate;
	
	private int yCoordinate;
	
	public GameField(int x, int y){
		this.xCoordinate = x;
		this.yCoordinate = y;
	}
	
	public int getX(){
		return this.xCoordinate;
	}
	
	public int getY(){
		return this.yCoordinate;
	}
	
	public String toString(){
		return "[" + this.getX() + ", " + this.getY() + "]";
	}
	
}
