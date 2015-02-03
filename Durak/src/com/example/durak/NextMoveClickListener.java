package com.example.durak;

import android.view.View;
import android.view.View.OnClickListener;

public class NextMoveClickListener implements OnClickListener {

	private static GameActivity activity;
	
	static {
		activity = GameActivity.getInstance();
	}
	
	@Override
	public void onClick(View v) {
		activity.btnEndMove.setOnClickListener(new EndMoveClickListener());
		activity.playersMove();
	}

}
