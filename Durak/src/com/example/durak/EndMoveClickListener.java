package com.example.durak;

import android.view.View;
import android.view.View.OnClickListener;

public class EndMoveClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		if (GameActivity.currentPlayerIndex < GameActivity.getInstance().getPlayers().size() - 1){
			GameActivity.getInstance().setCurrentPlayer(GameActivity.currentPlayerIndex + 1);
			GameActivity.getInstance().playersMove();
		}
		else {
			GameActivity.getInstance().endTurn();
		}
		
	}

}
