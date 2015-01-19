package com.example.durak;

import android.view.View;
import android.view.View.OnClickListener;

public class EndMoveClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		if (GameActivity.getInstance().getCurrentPlayer().isDefender()){
			GameActivity.getInstance().letNextAttackerMove();
		}
	}

}
