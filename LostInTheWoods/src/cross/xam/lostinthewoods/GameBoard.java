package cross.xam.lostinthewoods;

import android.util.Log;

public class GameBoard {
	
	private int horizontalSize;
	
	private int verticalSize;
	
	private GameField[] gameBoardFields;
	
	public GameBoard (int horizontalSize, int verticalSize){
		this.horizontalSize = horizontalSize;
		this.verticalSize = verticalSize;
		assignGameBoardFieldsValues();
	}

	public int getHorizontalSize(){
		return this.horizontalSize;
	}
	
	public int getVerticalSize(){
		return this.verticalSize;
	}
	
	private void assignGameBoardFieldsValues() {
		this.gameBoardFields = new GameField[horizontalSize * verticalSize];
		for (int y = 0; y < verticalSize; y++){
			for (int x = 0; x < horizontalSize; x++){
				gameBoardFields[x + y * horizontalSize] = new GameField(x, y);
			}
		}
	}

	public int getGameBoardSize(){
		return gameBoardFields.length;
	}
	
	public GameField getGameField(int x, int y){
		return gameBoardFields[x + y * horizontalSize];
	}
	
}
