package cross.xam.lostinthewoods;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GameField extends RelativeLayout{

	private ArrayList<Character> charactersOnField = new ArrayList<Character>();
	
	private Terrain fieldTerrain;
	
	private GameObject fieldObject;
	
	private int xCoordinate;
	
	private int yCoordinate;
	
	LayoutInflater inflater;
	
	ImageView ivGameField;
	
	private GameActivity currentActivity;
	
	private Context context;
	
	private static GameBoard board;
	
	public GameField(Context context, int x, int y, Terrain terrain){
		super(context);
		this.context = context;
		this.currentActivity = (GameActivity) context;
		board = currentActivity.getBoard();
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.fieldTerrain = terrain;
		reDrawField(context);
	}
	
	public int getXCoordinate(){
		return this.xCoordinate;
	}
	
	public int getYCoordinate(){
		return this.yCoordinate;
	}
	
	public String toString(){
		return "[" + this.getXCoordinate() + ", " + this.getYCoordinate() + "]";
	}

	/**
	 * Refreshes this game field view. Is used after the movement of characters has occurred to move them visually
	 * @param context
	 */
	private void reDrawField(Context context) {
		this.removeAllViews();
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.game_field, this);
		if (fieldTerrain != null) {
			setTerrainImageDrawable();
		}
		if (charactersOnField.size() > 0) {
			for (Character character : charactersOnField){
				if (character.getClass().equals(Ranger.class)){
					inflater.inflate(R.layout.ranger, this);
				}
				else if (character.getClass().equals(Wolf.class)){
					inflater.inflate(R.layout.wolf, this);
				}
			}
		}
		
	}
	
	/**
	 * Sets appropriate images of terrain for various terrain types
	 */
	private void setTerrainImageDrawable() {
		ImageView ivTerrain = (ImageView) this.getChildAt(0);
		switch(fieldTerrain.getNumValue()){
		case 0:
			ivTerrain.setImageResource(R.drawable.green_grass);
			break;
		case 1:
			ivTerrain.setImageResource(R.drawable.woods);
			break;
		case 2:
			ivTerrain.setImageResource(R.drawable.lake_forest);
			break;
		}
				
	}

	public void setFieldTerrain(Terrain terrain){
		this.fieldTerrain = terrain;
	}
	
	public Terrain getFieldTerrain(){
		return this.fieldTerrain;
	}
	
	public void addCharacterToField(Character character){
		charactersOnField.add(character);
		reDrawField(context);
	}
	
	public void removeCharacterFromField(Character character){
		charactersOnField.remove(character);
		reDrawField(context);
	}
	
	public ArrayList<Character> getCharactersOnField(){
		return this.charactersOnField;
	}
	
	public void setObjectToField(GameObject object){
		this.fieldObject = object;
		reDrawField(context);
	}
	
	public void removeObjectFromField(GameObject object){
		this.fieldObject = null;
		reDrawField(context);
	}
	
	/**
	 * Check whether this GameField with such coordinates exists on current Game board
	 * @param gameField
	 * @return true if such field exists and false otherwise
	 */
	protected boolean gameFieldIsOnBoard() {
		if (board.getGameField(this.xCoordinate, this.yCoordinate) != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns arrayList of neighbor GameFields for this GameField (without considering their availability for moves)
	 * @return arrayList of neighbor GameFields
	 */
	public ArrayList<GameField> getNeighborFields(){
		board = currentActivity.getBoard();
		ArrayList<GameField> neighbors = new ArrayList<GameField>();
		int x = this.xCoordinate;
		int y = this.yCoordinate;
		if ((x + 1) < board.getHorizontalSize()){
			neighbors.add(board.getGameField(x + 1, y));
		}
		if ((y + 1) < board.getVerticalSize()){
			neighbors.add(board.getGameField(x, y + 1));
		}
		if ((x - 1) >= 0){
			neighbors.add(board.getGameField(x - 1, y));
		}
		if ((y - 1) >= 0){
			neighbors.add(board.getGameField(x, y - 1));
		}
		return neighbors;
	}
	
}