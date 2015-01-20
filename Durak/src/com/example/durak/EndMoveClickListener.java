package com.example.durak;

import android.view.View;
import android.view.View.OnClickListener;

public class EndMoveClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		System.out.println(GameActivity.getInstance().getCurrentPlayer() + " clicked End Move");
		if (!GameActivity.getInstance().getCurrentPlayer().isDefender()){
			GameActivity.getInstance().letNextAttackerMove();
		}
		else {
			if (Table.getUnbeatenCards().size() > 0){
				GameActivity.getInstance().getCurrentPlayer().overwhelmed = true;
				System.out.println("Defender is overwhelmed");
				GameActivity.currentPlayerIndex = -1;
				GameActivity.getInstance().letNextAttackerMove();
			}
		}
	}

}
