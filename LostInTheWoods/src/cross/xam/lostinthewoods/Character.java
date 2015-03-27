package cross.xam.lostinthewoods;

import java.util.ArrayList;

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
	
	private GameField gameFieldPosition;
	
	protected int movesLeftThisTurn;
	
	protected int movesCostOfNextMove;
	
	private GameActivity currentActivity;
	
	private Context context;
	
	private GameBoard board;
	
	protected Character (Context context){
		this.context = context;
		currentActivity = (GameActivity) this.context;
		board = currentActivity.getBoard();
		this.setCharacterPosition(position[0], position[0]);
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
	
	public GameField getGameFieldPosition(){
		return gameFieldPosition;
	}
	
	public void setCharacterPosition(int x, int y){
		position[0] = x;
		position[1] = y;
		gameFieldPosition = board.getGameField(x, y);
	}
	
	public void setMovesLeftThisTurn(int movesLeft){
		if (!isDead){
			this.movesLeftThisTurn = movesLeft;
		}
	}
	
	public int getMovesLeftThisTurn(){
		return this.movesLeftThisTurn;
	}
	
	public abstract void setMovesCostOfNextMove(GameField destinationField);
	
	public int getMovesCostOfNextMove(){
		return movesCostOfNextMove;
	}
	
	/**
	 * Character makes 1 move in the passed direction (Y-coordinate is inversed)
	 * @param direction - direction of character's move. MOVE_LEFT = 0, MOVE_UP = 1, MOVE_RIGHT = 2, MOVE_DOWN = 3
	 */
	public void moveInDirection(int direction){
		if (!isDead){
			switch(direction) {
			case MOVE_LEFT:
				if (this.getXPosition() > 0){
					this.setMovesCostOfNextMove(board.getGameField(this.getXPosition() - 1, this.getYPosition()));
					if (this.movesLeftThisTurn >= this.movesCostOfNextMove){
						this.setCharacterPosition(this.getXPosition() - 1, this.getYPosition());
						this.movesLeftThisTurn -= this.movesCostOfNextMove;
					}
					else {
						Log.d("myLog", "You have not got enough moves");
					}	
				}
				else {
					Log.d("myLog", "You are about to go out of the borders");
					return;
				}
				break;
			case MOVE_UP:
				if (this.getYPosition() > 0){
					this.setMovesCostOfNextMove(board.getGameField(this.getXPosition(), this.getYPosition() - 1));
					if (this.movesLeftThisTurn >= this.movesCostOfNextMove){
						this.setCharacterPosition(this.getXPosition(), this.getYPosition() - 1);
						this.movesLeftThisTurn -= this.movesCostOfNextMove;
					}
					else {
						Log.d("myLog", "You have not got enough moves");
					}	
				}
				else {
					Log.d("myLog", "You are about to go out of the borders");
					return;
				}
				break;
			case MOVE_RIGHT:
				if (this.getXPosition() + 1 < board.getHorizontalSize()){
					this.setMovesCostOfNextMove(board.getGameField(this.getXPosition() + 1, this.getYPosition()));
					if (this.movesLeftThisTurn >= this.movesCostOfNextMove){
						this.setCharacterPosition(this.getXPosition() + 1, this.getYPosition());
						this.movesLeftThisTurn -= this.movesCostOfNextMove;
					}
					else {
						Log.d("myLog", "You have not got enough moves");
					}
				}
				else {
					Log.d("myLog", "You are about to go out of the borders");
					return;
				}
				
				break;
			case MOVE_DOWN:
				if (this.getYPosition() + 1 < board.getVerticalSize()){
					this.setMovesCostOfNextMove(board.getGameField(this.getXPosition(), this.getYPosition() + 1));
					if (this.movesLeftThisTurn >= this.movesCostOfNextMove){	
						this.setCharacterPosition(this.getXPosition(), this.getYPosition() + 1);
						this.movesLeftThisTurn -= this.movesCostOfNextMove;
					}
					else {
						Log.d("myLog", "You have not got enough moves");
					}
				}
				else {
					Log.d("myLog", "You are about to go out of the borders");
					return;
				}
				break;
			}
			Log.d("myLog", this.name + " has " + this.movesLeftThisTurn + " moves left");
		}
		else {
			Log.d("myLog", this + " is dead");
		}
	}
	
	/**
	 * Character moves to the field passed to this method if he has enough moves
	 * @param destinationField - field to move to
	 */
	public void moveTo(GameField destinationField){
		int x = destinationField.getXCoordinate();
		int y = destinationField.getYCoordinate();
		this.setMovesCostOfNextMove(destinationField);
		if (x >= 0 && x < board.getHorizontalSize() && y >= 0 && y < board.getVerticalSize()){
			if (this.movesLeftThisTurn >= this.movesCostOfNextMove){
				this.setCharacterPosition(x, y);
				this.movesLeftThisTurn -= this.movesCostOfNextMove;
			}
			else {
				Log.d("myLog", "You have not got enough moves");
			}
		}
		else {
			Log.d("myLog", "You are about to go out of the borders");
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
	public ArrayList<GameField> getShortestRouteToGameField(GameField destination){
		int xDistance = destination.getXCoordinate() - this.getXPosition();
		int yDistance = destination.getYCoordinate() - this.getYPosition();
		ArrayList<GameField> routeToDestination = new ArrayList<GameField>();
		GameField tempGameField = new GameField(context, this.getXPosition(), this.getYPosition(),
				this.getGameFieldPosition().getFieldTerrain());
		for (int i = 0; i < Math.abs(xDistance) + Math.abs(yDistance); i++){
			GameField nextStepGameField;
			if (Math.abs(xDistance) >= Math.abs(yDistance)){
				if (xDistance > 0){
					nextStepGameField = board.getGameField(tempGameField.getXCoordinate() + 1, tempGameField.getYCoordinate());
					if (nextStepGameField.gameFieldIsOnBoard()){
						if (nextStepGameField.getFieldTerrain() == Terrain.LAKE){
							if (Math.random()*10 > 5){
								nextStepGameField = board.getGameField(tempGameField.getXCoordinate(),
										tempGameField.getYCoordinate() + 1);
								if (nextStepGameField.gameFieldIsOnBoard()){
									if (nextStepGameField.getFieldTerrain() != Terrain.LAKE){
										tempGameField = nextStepGameField;
									}
									else {
										
									}
								}
							}
							else {
								
							}
						}
						else {
							tempGameField = board.getGameField(tempGameField.getXCoordinate() + 1,
									tempGameField.getYCoordinate());
						}
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
				else if (xDistance < 0){
					if (tempGameField.getXCoordinate() > 0){
						tempGameField = board.getGameField(tempGameField.getXCoordinate() - 1,
								tempGameField.getYCoordinate());
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
			}
			else {
				if (yDistance > 0){
					if (tempGameField.getYCoordinate() < board.getVerticalSize()){
						tempGameField = board.getGameField(tempGameField.getXCoordinate(),
								tempGameField.getYCoordinate() + 1);
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
				else if (yDistance < 0){
					if (tempGameField.getYCoordinate() > 0){
						tempGameField = board.getGameField(tempGameField.getXCoordinate(),
							tempGameField.getYCoordinate() - 1);
					}
					else {
						Log.d("myLog", "Your route is about to go out of the borders");
					}
				}
			}
			Log.d("myLog", "Game field " + tempGameField + " added to route");
			routeToDestination.add(tempGameField);
			xDistance = destination.getXCoordinate() - tempGameField.getXCoordinate();
			yDistance = destination.getYCoordinate() - tempGameField.getYCoordinate();
		}
		
		return routeToDestination;
	}


	
}
