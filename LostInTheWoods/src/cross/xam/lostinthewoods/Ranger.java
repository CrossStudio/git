package cross.xam.lostinthewoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

public class Ranger extends Character {
	
	ImageView ivRanger;
	
	LayoutInflater inflater;
	
	Context context;
	
	public Ranger (Context context, String name){
		this.context = context;
		this.name = name;
		assignRangerLayout();
	}
	
	private void assignRangerLayout(){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ivRanger = (ImageView) inflater.inflate(R.layout.ranger, null);
	}
	
	@Override
	public void setCharacterPosition(int x, int y){
		GameActivity currentActivity = (GameActivity) context;
		int previousX = this.getXPosition();
		int previousY = this.getYPosition();
		currentActivity.board.getGameField(previousX, previousY).removeCharacterFromField(this);
		super.setCharacterPosition(x, y);
		currentActivity.board.getGameField(x, y).addCharacterToField(this);
	}
}
