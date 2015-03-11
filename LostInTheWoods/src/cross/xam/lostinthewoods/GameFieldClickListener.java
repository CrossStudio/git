package cross.xam.lostinthewoods;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class GameFieldClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		GameField clickedField = (GameField) v;
		
		Log.d("myLog", ""+clickedField);
	}

}
