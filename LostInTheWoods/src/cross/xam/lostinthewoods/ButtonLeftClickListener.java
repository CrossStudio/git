package cross.xam.lostinthewoods;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ButtonLeftClickListener implements OnClickListener {
	
	Ranger currentRanger;
	
	@Override
	public void onClick(View v) {
		GameActivity currentGameActivity = (GameActivity) v.getContext();
		currentRanger = currentGameActivity.getRanger();
		currentRanger.moveInDirection(Character.MOVE_LEFT);
		if (currentGameActivity.haveWolvesFoundRanger() != null){
			currentRanger.setMovesLeftThisTurn(0);
		}
		Log.d("myLog", currentRanger + "'s position:[" + currentRanger.getXPosition() +
				", " + currentRanger.getYPosition() + "]");
	}

}
