package com.example.practiceset2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	int scoreTeamA = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void displayTeamAScore(int score){
		TextView tvTeamAScore = (TextView) findViewById(R.id.tvTeamAScore);
		tvTeamAScore.setText(String.valueOf(score));
	}
	
	public void teamAScoredThreePoints(View v){
		scoreTeamA += 3;
		displayTeamAScore(scoreTeamA);
	}
	
	public void teamAScoredTwoPoints(View v){
		scoreTeamA += 2;
		displayTeamAScore(scoreTeamA);
	}
	
	public void teamAScoredOnePoint(View v){
		scoreTeamA += 1;
		displayTeamAScore(scoreTeamA);
	}
}
