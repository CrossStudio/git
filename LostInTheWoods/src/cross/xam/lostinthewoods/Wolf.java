package cross.xam.lostinthewoods;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

public class Wolf extends Character {

	ImageView ivWolf;
	
	LayoutInflater inflater;
	
	Context context;
	
	GameActivity currentActivity;
	
	public Wolf (Context context, String name){
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
			int previousX = this.getXPosition();
			int previousY = this.getYPosition();
			GameField previousField = currentActivity.getBoard().getGameField(previousX, previousY);
			previousField.removeCharacterFromField(this);
			super.setCharacterPosition(x, y);
			
			GameField currentField = currentActivity.getBoard().getGameField(x, y);
			currentField.addCharacterToField(this);
		}
		else {
			Log.d("myLog", this + " is dead");
		}
	}
	
	@Override
	public void setMovesCostOfNextMove() {
		Terrain terrain = currentActivity.getBoard().getGameField(getXPosition(), getYPosition()).getFieldTerrain();
		switch (terrain.getNumValue()){
		case 0:
			this.movesCostOfNextMove = 1;
			break;
		case 1:
			this.movesCostOfNextMove = 1;
			break;
		case 2:
			this.movesCostOfNextMove = 100;
			break;
		}
	}
	
	/**
	 * This wolf eats ranger
	 * Ranger image gets removed from the board
	 */
	public void eatRanger(){
		if (!isDead){
			Log.d("myLog", this + " has eaten " + currentActivity.getRanger());
			currentActivity.getRanger().getsKilled();
		}
	}

	@Override
	public void getsKilled() {
		currentActivity.getBoard().getGameField(this.getXPosition(), this.getYPosition()).removeCharacterFromField(this);
		this.isDead = true;
	}
	
}
