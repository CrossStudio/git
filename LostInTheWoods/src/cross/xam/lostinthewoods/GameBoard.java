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
				Log.d("myLog", ""+terrain);
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
	
	public GameField getGameField(int x, int y){
		return gameBoardFields[x + y * horizontalSize];
	}
	
	public GameField[] getGameBoardFields(){
		return gameBoardFields;
	}
	
	public GridLayout getBoardLayout(){
		return boardLayout;
	}
	
}
