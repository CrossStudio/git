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
		currentRanger.move(Character.MOVE_LEFT);
		if (currentGameActivity.haveWolvesFoundRanger() != null){
			currentRanger.setMovesLeftThisTurn(0);
		}
		Log.d("myLog", currentRanger + "'s position:[" + currentRanger.getXPosition() +
				", " + currentRanger.getYPosition() + "]");
		GameField [] shortestRouteToWolf = currentRanger.getShortestRouteToGameField(currentGameActivity.getBoard().
				getGameField(currentGameActivity.getWolves().get(0).getXPosition(),
				currentGameActivity.getWolves().get(0).getYPosition()));
		for (GameField tempField : shortestRouteToWolf){
			Log.d("myLog", "" + tempField);
		}
	}

}
