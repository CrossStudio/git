package cross.xam.lostinthewoods;

import java.util.ArrayList;

import android.content.Context;
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
	
	Context context;
	
	public GameField(Context context, int x, int y, Terrain terrain){
		super(context);
		this.context = context;
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
	
	public void setObjectToField(GameObject object){
		this.fieldObject = object;
		reDrawField(context);
	}
	
	public void removeObjectFromField(GameObject object){
		this.fieldObject = null;
		reDrawField(context);
	}
	
}
