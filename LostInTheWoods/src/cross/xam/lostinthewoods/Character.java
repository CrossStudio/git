package cross.xam.lostinthewoods;

import android.util.Log;

public abstract class Character {
	
	public static final int MOVE_LEFT = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_RIGHT = 2;
	public static final int MOVE_DOWN = 3;
	
	protected String name;
	
	protected boolean isDead = false;
	
	private int[] position = {0,0};
	
	private int movesLeftThisTurn;
	
	protected int movesCostOfNextMove;
	
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
	
	public void setCharacterPosition(int x, int y){
		position[0] = x;
		position[1] = y;
	}
	
	public void setMovesLeftThisTurn(int movesLeft){
		if (!isDead){
			this.movesLeftThisTurn = movesLeft;
		}
	}
	
	public int getMovesLeftThisTurn(){
		return this.movesLeftThisTurn;
	}
	
	public abstract void setMovesCostOfNextMove();
	
	public int getMovesCostOfNextMove(){
		return movesCostOfNextMove;
	}
	
	/**
	 * Character makes 1 move in the passed direction (Y-coordinate is inversed)
	 * @param direction - direction of character's move. MOVE_LEFT = 0, MOVE_UP = 1, MOVE_RIGHT = 2, MOVE_DOWN = 3
	 */
	public void move(int direction){
		if (!isDead){
			this.setMovesCostOfNextMove();
			if (this.movesLeftThisTurn >= this.movesCostOfNextMove){
				switch(direction) {
				case MOVE_LEFT:
					this.setCharacterPosition(this.getXPosition() - 1, this.getYPosition());
					break;
				case MOVE_UP:
					this.setCharacterPosition(this.getXPosition(), this.getYPosition() - 1);
					break;
				case MOVE_RIGHT:
					this.setCharacterPosition(this.getXPosition() + 1, this.getYPosition());
					break;
				case MOVE_DOWN:
					this.setCharacterPosition(this.getXPosition(), this.getYPosition() + 1);
					break;
				}
				this.movesLeftThisTurn -= this.movesCostOfNextMove;
				Log.d("myLog", this.name + " has " + this.movesLeftThisTurn + " moves left");
			}
			else {
				Log.d("myLog", this.name + " is out of moves");
			}
		}
		else {
			Log.d("myLog", this + " is dead");
		}
	}

	/**
	 * This character gets killed and removed from the board
	 */
	public abstract void getsKilled();
	
	
}
