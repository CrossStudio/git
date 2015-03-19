package cross.xam.lostinthewoods;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

public class Ranger extends Character {
	
	ImageView ivRanger;
	
	LayoutInflater inflater;
	
	Context context;
	
	GameActivity currentActivity;
	
	private int shotgunShells = 2;
	
	public Ranger (Context context, String name){
		super(context);
		this.name = name;
		currentActivity = (GameActivity) context;
	}
	
	/**
	 * Sets position of this Ranger character according to the coordinates passed to it.
	 * Also, removes character from the field he had been previously and puts him on the field he currently occupies.
	 * Also, calls {@link #setMovesCostOfNextMove(Terrain)} method to determine the cost of ranger's next move
	 */
	@Override
	public void setCharacterPosition(int x, int y){
		if (!isDead){
			Log.d("myLog", "Game field position of ranger = " + this.getXPosition() + " : " + this.getYPosition());
			super.setCharacterPosition(this.getXPosition(), this.getYPosition());
			GameField previousField = this.getGameFieldPosition();
			
			previousField.removeCharacterFromField(this);
			super.setCharacterPosition(x, y);
			
			this.getGameFieldPosition().addCharacterToField(this);
		}
		else {
			Log.d("myLog", this + " is dead");
		}
	}
	
	/**
	 * Sets move cost of the current GameField taking into account terrain of this field
	 */
	@Override
	public void setMovesCostOfNextMove(GameField destinationField) {
		Terrain terrain = destinationField.getFieldTerrain();
		switch (terrain.getNumValue()){
		case 0:
			this.movesCostOfNextMove = 1;
			break;
		case 1:
			this.movesCostOfNextMove = 2;
			break;
		case 2:
			this.movesCostOfNextMove = 2;
			break;
		}
		Log.d("myLog", "Cost of next move = " + movesCostOfNextMove);
	}
	
	/**
	 * Returns number of shotgun shells currently at ranger's possession
	 * @return number of shotgun shells currently at ranger's possession
	 */
	public int getShotgunShells(){
		return this.shotgunShells;
	}
	
	/**
	 * Shoots and kills the wolf
	 * @param wolf - wolf to be killed
	 */
	public void shotsWolf(Wolf wolf){
		if (!isDead){
			Log.d("myLog", this + " has shot " + wolf);
			this.shotgunShells--;
		}
	}
	
	@Override
	public void getsKilled() {
		this.getGameFieldPosition().removeCharacterFromField(this);
		this.isDead = true;
	}
	
	
}
