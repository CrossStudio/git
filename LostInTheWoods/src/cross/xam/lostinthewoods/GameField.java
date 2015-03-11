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
	
	Context context;
	
	public GameField(Context context, int x, int y){
		super(context);
		this.context = context;
		this.xCoordinate = x;
		this.yCoordinate = y;
		drawField(context);
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

	private void drawField(Context context) {
		if (fieldTerrain == null) {
			if (fieldObject == null){
				if (charactersOnField.size() == 0){
					inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					inflater.inflate(R.layout.game_field, this);
				}
				else {
					for (Character character : charactersOnField){
						if (character.getClass().equals(Ranger.class)){
							inflater.inflate(R.layout.ranger, this);
						}
					}
				}
			}
		}
	}
	
	public void setFieldTerrain(Terrain terrain){
		this.fieldTerrain = terrain;
	}
	
	public void addCharacterToField(Character character){
		Log.d("myLog", "addCharacterToField called");
		charactersOnField.add(character);
		drawField(context);
	}
	
	public void removeCharacterFromField(Character character){
		charactersOnField.remove(character);
		drawField(context);
	}
	
	public void setObjectToField(GameObject object){
		this.fieldObject = object;
		drawField(context);
	}
	
	public void removeObjectFromField(GameObject object){
		this.fieldObject = null;
		drawField(context);
	}
	
}
