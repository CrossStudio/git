package cross.xam.lostinthewoods;

import android.util.Log;

public class Character {
	
	
	public static final int MOVE_LEFT = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_RIGHT = 2;
	public static final int MOVE_DOWN = 3;
	
	protected String name;
	
	private int[] position = new int[2];
	
	private int movesLeftThisTurn;
	
	private int movesCostOfNextMove;
	
	public String getName(){
		return name;
	}
	
	public String toString (){
		return name;
	}
	
	public int getXPosition(){
		return position[0];
	}
	
	public int getYPosition(){
		return position[1];
	}
	
	public void setXPosition(int x){
		position[0] = x;
	}
	
	public void setYPosition(int y){
		position[1] = y;
	}
	
	public void setMovesLeftThisTurn(int movesLeft){
		this.movesLeftThisTurn = movesLeft;
	}
	
	public int getMovesLeftThisTurn(){
		return this.movesLeftThisTurn;
	}
	
	public void setMovesCostOfNextMove(int movesCost){
		this.movesCostOfNextMove = movesCost;
	}
	
	public int getMovesCostOfNextMove(){
		return movesCostOfNextMove;
	}
	
	/**
	 * Character makes 1 move in the passed direction 
	 * @param direction - direction of character's move. MOVE_LEFT = 0, MOVE_UP = 1, MOVE_RIGHT = 2, MOVE_DOWN = 3
	 */
	public void move(int direction){
		if (this.movesLeftThisTurn >= this.movesCostOfNextMove){
			switch(direction) {
			case MOVE_LEFT:
				this.setXPosition(this.getXPosition() - 1);
				Log.d("myLog", this.name + " has moved left");
				break;
			case MOVE_UP:
				this.setYPosition(this.getYPosition() + 1);
				Log.d("myLog", this.name + " has moved up");
				break;
			case MOVE_RIGHT:
				this.setXPosition(this.getXPosition() + 1);
				Log.d("myLog", this.name + " has moved right");
				break;
			case MOVE_DOWN:
				this.setYPosition(this.getYPosition() - 1);
				Log.d("myLog", this.name + " has moved down");
				break;
			}
			this.movesLeftThisTurn -= this.movesCostOfNextMove;
		}
		else {
			Log.d("myLog", this.name + " is out of moves");
		}
		
	}
	
}
