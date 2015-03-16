package cross.xam.lostinthewoods;

import android.content.Context;
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
	
	private GameActivity currentActivity;
	
	private Context context;
	
	protected Character (Context context){
		this.context = context;
		currentActivity = (GameActivity) this.context;
	}
	
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
					if (this.getXPosition() > 0){
						this.setCharacterPosition(this.getXPosition() - 1, this.getYPosition());
					}
					else {
						Log.d("myLog", "You are about to go out of the borders");
						return;
					}
					break;
				case MOVE_UP:
					if (this.getYPosition() > 0){
						this.setCharacterPosition(this.getXPosition(), this.getYPosition() - 1);
					}
					else {
						Log.d("myLog", "You are about to go out of the borders");
						return;
					}
					break;
				case MOVE_RIGHT:
					if (this.getXPosition() + 1 < currentActivity.getBoard().getHorizontalSize()){
						this.setCharacterPosition(this.getXPosition() + 1, this.getYPosition());
					}
					else {
						Log.d("myLog", "You are about to go out of the borders");
						return;
					}
					break;
				case MOVE_DOWN:
					Log.d("myLog", "current Y position = " + this.getYPosition());
					Log.d("myLog", "current board height = " + currentActivity.getBoard().getVerticalSize());
					if (this.getYPosition() + 1 < currentActivity.getBoard().getVerticalSize()){
						this.setCharacterPosition(this.getXPosition(), this.getYPosition() + 1);
					}
					else {
						Log.d("myLog", "You are about to go out of the borders");
						return;
					}
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
	
	/**
	 * Finds the shortest route to the game field from the currently occupied game field
	 * @param destination - game field where the route should end
	 * @return array of game fields that form the shortest route to the given game field
	 */
	public GameField[] getShortestRouteToGameField(GameField destination){
		int xDistance = destination.getXCoordinate() - this.getXPosition();
		int yDistance = destination.getYCoordinate() - this.getYPosition();
		GameField [] routeToDestination = new GameField[Math.abs(xDistance) + Math.abs(yDistance)];
		GameField tempGameField = new GameField(context, this.getXPosition(), this.getYPosition(),
				currentActivity.getBoard().getGameField(this.getXPosition(), this.getYPosition()).getFieldTerrain());
		for (int i = 0; i < routeToDestination.length; i++){
			
			if (Math.abs(xDistance) >= Math.abs(yDistance)){
				if (xDistance > 0){
					if (tempGameField.getXCoordinate() < currentActivity.getBoard().getHorizontalSize()){
						tempGameField = currentActivity.getBoard().getGameField(tempGameField.getXCoordinate() + 1,
								tempGameField.getYCoordinate());
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
				else if (xDistance < 0){
					if (tempGameField.getXCoordinate() > 0){
						tempGameField = currentActivity.getBoard().getGameField(tempGameField.getXCoordinate() - 1,
								tempGameField.getYCoordinate());
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
			}
			else {
				if (yDistance > 0){
					if (tempGameField.getYCoordinate() < currentActivity.getBoard().getVerticalSize()){
						tempGameField = currentActivity.getBoard().getGameField(tempGameField.getXCoordinate(),
								tempGameField.getYCoordinate() + 1);
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
				else if (yDistance < 0){
					if (tempGameField.getYCoordinate() > 0){
						tempGameField = currentActivity.getBoard().getGameField(tempGameField.getXCoordinate(),
							tempGameField.getYCoordinate() - 1);
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
			}
			Log.d("myLog", "Game field " + tempGameField + " added to route");
			routeToDestination[i] = tempGameField;
			xDistance = destination.getXCoordinate() - tempGameField.getXCoordinate();
			yDistance = destination.getYCoordinate() - tempGameField.getYCoordinate();
		}
		
		return routeToDestination;
	}
	
}