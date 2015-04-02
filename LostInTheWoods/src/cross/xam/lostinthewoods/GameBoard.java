package cross.xam.lostinthewoods;

import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridLayout;

public class GameBoard{
	
	private int horizontalSize;
	
	private int verticalSize;
	
	private GameField[] gameBoardFields;
	
	Context context;
	
	GridLayout boardLayout;
	
	LayoutInflater inflater;
	
	public GameBoard (Context context, int horizontalSize, int verticalSize){
		this.context = context;
		this.horizontalSize = horizontalSize;
		this.verticalSize = verticalSize;
		assignBoardLayout();
		assignGameBoardFieldsValues();
		addGameFieldViewsToGameBoard();
	}

	/**
	 * Creates passed number of wolves on current GameBoard (board must be created first!)
	 * @param numberOfWolves - number of wolves to be added
	 */
	public Wolf addWolfToGameBoard() {
		return generateRandomPositionForWolf();
	}

	/**
	 * Generates a random position for a wolf and places this wolf on it if the position is unoccupied and is accessible
	 * @return newly created wolf
	 */
	private Wolf generateRandomPositionForWolf() {
		Wolf newWolf = new Wolf(context, "Wolf");
		
		Random rand = new Random();
		int randomXCoordinate = rand.nextInt(this.horizontalSize);
		int randomYCoordinate = rand.nextInt(this.verticalSize);
		
		GameField originForWolf = getGameField(randomXCoordinate, randomYCoordinate);
		
		while (true){
			if (originForWolf.getCharactersOnField().size() > 0 || originForWolf.getFieldTerrain().equals(Terrain.LAKE)){
				randomXCoordinate = rand.nextInt(this.horizontalSize);
				randomYCoordinate = rand.nextInt(this.verticalSize);
				originForWolf = getGameField(randomXCoordinate, randomYCoordinate);
			}
			else {
				newWolf.setCharacterPosition(randomXCoordinate, randomYCoordinate);
				break;
			}
		}
		return newWolf;
	}

	public int getHorizontalSize(){
		return this.horizontalSize;
	}
	
	public int getVerticalSize(){
		return this.verticalSize;
	}
	
	private void assignBoardLayout(){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		boardLayout = (GridLayout) inflater.inflate(R.layout.game_board, null);
		boardLayout.setColumnCount(this.horizontalSize);
	}
	
	private void assignGameBoardFieldsValues() {
		this.gameBoardFields = new GameField[horizontalSize * verticalSize];
		for (int y = 0; y < verticalSize; y++){
			for (int x = 0; x < horizontalSize; x++){
				Random rand = new Random();
				int randomTerrainId = rand.nextInt(3);
				Terrain terrain = Terrain.values()[randomTerrainId];
				gameBoardFields[x + y * horizontalSize] = new GameField(context, x, y, terrain);
				gameBoardFields[x + y * horizontalSize].setOnClickListener(new GameFieldClickListener());
			}
		}
	}

	private void addGameFieldViewsToGameBoard() {
		for (GameField gameField : this.getGameBoardFields()){
			boardLayout.addView(gameField);
		}
	}
	
	public int getGameBoardSize(){
		return gameBoardFields.length;
	}
	
	/**
	 * Returns GameField with passed coordinates from gameBoardFields array in case such exists, otherwise returns null
	 * @param x - x-Coordinate of GameField to be found on board
	 * @param y - y-Coordinate of GameField to be found on board
	 * @return - GameField with given coordinates or null
	 */
	public GameField getGameField(int x, int y){
		if (x + y * horizontalSize < gameBoardFields.length){
			return gameBoardFields[x + y * horizontalSize];
		}
		else {
			return null;
		}
	}
	
	public GameField[] getGameBoardFields(){
		return gameBoardFields;
	}
	
	public GridLayout getBoardLayout(){
		return boardLayout;
	}
	
	
}
