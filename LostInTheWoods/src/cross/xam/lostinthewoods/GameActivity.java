package cross.xam.lostinthewoods;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GameActivity extends Activity {

	RelativeLayout rlGame;
	public GameBoard board;
	
	public Ranger xam;
	
	Button btnLeft;
	Button btnUp;
	Button btnRight;
	Button btnDown;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		board = new GameBoard(this,8,8);
		drawGameBoard();
		
		xam = new Ranger(this,"XAM");
		xam.setCharacterPosition(3,5);		
		
		drawTraveller();
		
		assignButtonViews();
	}

	private void drawGameBoard() {
		rlGame = (RelativeLayout) findViewById(R.id.rlGame);
		rlGame.addView(board.getBoardLayout());
		
	}

	private void drawTraveller() {
		//TODO add your code here
	}
	
	private void assignButtonViews() {
		btnLeft = (Button) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new ButtonLeftClickListener());
		btnUp = (Button) findViewById(R.id.btnUp);
		btnUp.setOnClickListener(new ButtonUpClickListener());
		btnRight = (Button) findViewById(R.id.btnRight);
		btnRight.setOnClickListener(new ButtonRightClickListener());
		btnDown = (Button) findViewById(R.id.btnDown);
		btnDown.setOnClickListener(new ButtonDownClickListener());
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
}
