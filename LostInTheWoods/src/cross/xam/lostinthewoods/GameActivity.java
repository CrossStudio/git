package cross.xam.lostinthewoods;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GameActivity extends Activity {

	RelativeLayout rlGame;
	
	private GameBoard board;
	
	private Ranger ranger;
	
	private ArrayList<Wolf> wolves = new ArrayList<Wolf>();
	
	private int numberOfTurns = 0;
	
	Button btnLeft;
	Button btnUp;
	Button btnRight;
	Button btnDown;
	Button btnEndTurn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		board = new GameBoard(this,8,8);
		drawGameBoard();
		
		ranger = new Ranger(this,"XAM");
		ranger.setCharacterPosition(3,5);		
		
		wolves.add(new Wolf(this, "Akela"));
		wolves.get(0).setCharacterPosition(0, 1);
		
		assignButtonViews();
		
		newTurn();
	}
	
	private void drawGameBoard() {
		rlGame = (RelativeLayout) findViewById(R.id.rlGame);
		rlGame.addView(board.getBoardLayout());
		
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
		btnEndTurn = (Button) findViewById(R.id.btnEndTurn);
		btnEndTurn.setOnClickListener(new ButtonEndTurnClickListener());
	}
	
	public GameBoard getBoard(){
		return board;
	}
	
	public Ranger getRanger(){
		return ranger;
	}
	
	public ArrayList<Wolf> getWolves(){
		return wolves;
	}
	
	/**
	 * Starts a new turn
	 * Refreshes characters' moves available
	 */
	private void newTurn() {
		numberOfTurns++;
		Log.d("myLog", "Turn " + numberOfTurns);
		ranger.setMovesLeftThisTurn(2);
		for (Wolf wolf : wolves){
			wolf.setMovesLeftThisTurn(2);
		}
	}
	
	/**
	 * Ends current turn
	 */
	public void endTurn(){
		Log.d("myLog", "Turn has ended");
		for (Wolf currentWolf : getWolves()){
			currentWolf.AIWolfTurn();
		}
		Wolf wolf = haveWolvesFoundRanger();
		if (wolf != null){
			letTheFightBegin(wolf);
		}
		newTurn();
	}

	/**
	 * Checks whether ranger and a wolf/wolves have met and returns that wolves that ranger has met
	 * @return wolf that the ranger has met
	 */
	public Wolf haveWolvesFoundRanger() {
		GameField rangerField = board.getGameField(getRanger().getXPosition(), getRanger().getYPosition());
		for (Wolf wolf : wolves){
			if (rangerField.equals(board.getGameField(wolf.getXPosition(), wolf.getYPosition()))){
				if (!wolf.isDead){
					Log.d("myLog", "The foes have met");
					return wolf;
				}
			}
		}
		return null;
	}
	
	/**
	 * Emulates battle between ranger and wolf to determine who kills whom
	 * @param wolf - wolf that is going to fight with ranger
	 */
	private void letTheFightBegin(Wolf wolf) {
		if (ranger.getShotgunShells() > 0){
			ranger.shotsWolf(wolf);
			wolf.getsKilled();
		}
		else {
			Log.d("myLog", "Ranger is out of ammo, Ah!");
			wolf.eatRanger();
			ranger.getsKilled();
		}			
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
