package cross.xam.lostinthewoods;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class ButtonEndTurnClickListener implements OnClickListener {

	Context context;
	
	GameActivity currentActivity;
	
	@Override
	public void onClick(View v) {
		currentActivity = (GameActivity) v.getContext();
		currentActivity.endTurn();
	}

}
