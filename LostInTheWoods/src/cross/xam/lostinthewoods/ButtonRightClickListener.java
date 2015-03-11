package cross.xam.lostinthewoods;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class ButtonRightClickListener implements OnClickListener {

	Ranger currentTraveller;
	
	@Override
	public void onClick(View v) {
		GameActivity currentGameActivity = (GameActivity) v.getContext();
		currentTraveller = currentGameActivity.xam;
		currentTraveller.move(Character.MOVE_RIGHT);
		Log.d("myLog", currentTraveller + "'s position:[" + currentTraveller.getXPosition() +
				", " + currentTraveller.getYPosition() + "]");
	}

}
