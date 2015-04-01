package cross.xam.lostinthewoods;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

public class Wolf extends Character {

	ImageView ivWolf;
	
	LayoutInflater inflater;
	
	Context context;
	
	GameActivity currentActivity;
	
	Wolf closestWolf;
	
	public Wolf (Context context, String name){
		super(context);
		this.name = name;
		currentActivity = (GameActivity) context;
	}

	/**
	 * Sets position of this Ranger character according to the coordinates passed to it.
	 * Also, removes character from the field he had been previously and puts him on the field he currently occupies.
	 * Also, calls {@link #setMovesCostOfNextMove(Terrain)} method to determine the cost of ranger's next move
	 */
	@Override
	public void setCharacterPosition(int x, int y){
		if (!isDead){
			super.setCharacterPosition(this.getXPosition(), this.getYPosition());
			GameField previousField = this.getGameFieldPosition();
			previousField.removeCharacterFromField(this);
			super.setCharacterPosition(x, y);

			this.getGameFieldPosition().addCharacterToField(this);
		}
		else {
			Log.d("myLog", this + " is dead");
		}
	}
	
	@Override
	public void setMovesCostOfNextMove(GameField destinationField) {
		Terrain terrain = destinationField.getFieldTerrain();
		switch (terrain.getNumValue()){
		case 0:
			this.movesCostOfNextMove = 1;
			break;
		case 1:
			this.movesCostOfNextMove = 1;
			break;
		case 2:
			this.movesCostOfNextMove = 100;
			break;
		}
	}
	
	/**
	 * This wolf eats ranger
	 * Ranger image gets removed from the board
	 */
	public void eatRanger(){
		if (!isDead){
			Log.d("myLog", this + " has eaten " + currentActivity.getRanger());
			currentActivity.getRanger().getsKilled();
		}
	}

	@Override
	public void getsKilled() {
		this.getGameFieldPosition().removeCharacterFromField(this);
		this.isDead = true;
	}
	
	/**
	 * Determines where the wolf will go this turn (two moves or less)
	 * Destination game field is determined by the location of the ranger and other wolves (if any of them are still alive)
	 */
	public void AIWolfTurn(){
		
		if (rangerInSight()){
			huntRanger();
			return;
		}
		findClosestWolf();
		if (closestWolf != null && !closestWolf.isDead && otherWolfInSight()){
			followWolfPack();
		}
	}
	
	private void findClosestWolf() {
		int xDistanceToClosestWolf = 100;
		int yDistanceToClosestWolf = 100;
		for (Wolf wolf : currentActivity.getWolves()){
			int xDistanceToCurrentWolf = Math.abs(wolf.getXPosition() - this.getXPosition());
			int yDistanceToCurrentWolf = Math.abs(wolf.getYPosition() - this.getYPosition());
			if ((xDistanceToClosestWolf + yDistanceToClosestWolf) > (xDistanceToCurrentWolf + yDistanceToCurrentWolf)){
				xDistanceToClosestWolf = xDistanceToCurrentWolf;
				yDistanceToClosestWolf = yDistanceToCurrentWolf;
				closestWolf = wolf;
			}
		}
	}

	/**
	 * Make this wolf stay close to his pack
	 */
	private void followWolfPack() {
		
	}

	/**
	 * Checks whether closest relative to this wolf is within this wolf's sight or scent
	 * @return true if closest relative to this wolf is close enough for this wolf to see (hear or scent him), false otherwise
	 */
	private boolean otherWolfInSight() {
		int xDistanceToClosestWolf = Math.abs(closestWolf.getXPosition() - this.getXPosition());
		int yDistanceToClosestWolf = Math.abs(closestWolf.getYPosition() - this.getYPosition());
		if ((xDistanceToClosestWolf + yDistanceToClosestWolf) <= 3){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Make this wolf try to catch and eat ranger
	 */
	private void huntRanger() {
		this.route = getShortestRouteToGameField(currentActivity.getRanger().getGameFieldPosition());
		moveByRoute(this.movesLeftThisTurn);
	}

	/**
	 * Makes this character spend the number of moves passed as a parameter on his current route
	 * @param moves - number of moves this character will spend moving by his route
	 */
	private void moveByRoute(int moves) {
		Log.d("myLog", "Moving by route " + this.route);
		for (GameField nextField : this.route){
			Log.d("myLog", "" + nextField);
			if (moves > 0){
				moveTo(nextField);
				moves--;
			}
			else {
				break;
			}
		}
		
	}

	/**
	 * Checks whether ranger is within this wolf's sight or scent
	 * @return true if ranger is close enough for this wolf to see (hear or scent him), false otherwise
	 */
	private boolean rangerInSight() {
		GameField rangerPosition = currentActivity.getRanger().getGameFieldPosition();
		int xDistanceToRanger = Math.abs(rangerPosition.getXCoordinate() - this.getXPosition());
		int yDistanceToRanger = Math.abs(rangerPosition.getYCoordinate() - this.getYPosition());
		if ((xDistanceToRanger + yDistanceToRanger) <= 3){
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public ArrayList<GameField> getAllAccessibleFields() {
		ArrayList<GameField> accessibleFields = new ArrayList<GameField>();
		for (GameField field : currentActivity.getBoard().getGameBoardFields()){
			if (accessibleField(field)){
				accessibleFields.add(field);
			}
		}
		return accessibleFields;
	}

	/**
	 * Checks whether passed game field can be accessed by this wolf
	 * @param field - GameField to be checked for accessibility
	 * @return true if the field is accessible and false otherwise
	 */
	private boolean accessibleField(GameField field) {
		if (field.getFieldTerrain() == Terrain.LAKE){
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<GameField> getShortestRouteToGameField(GameField destination) {
		ArrayList<GameField> shortestRoute = implementLeeAlgorithm(destination);
		return shortestRoute;
	}

	private ArrayList<GameField> implementLeeAlgorithm(GameField destination) {
		ArrayList<GameField> routeByLee = new ArrayList<GameField>();
		ArrayList<GameField> allAccessibleFields = getAllAccessibleFields();
		int [] fieldMarks = new int [allAccessibleFields.size()];
		int currentMark = 0;
		for (int i = 0; i < fieldMarks.length; i++){
			fieldMarks[i] = -1;
		}
		ArrayList<GameField> currentWaveOfFields = new ArrayList<GameField>();
		currentWaveOfFields.add(this.getGameFieldPosition());
		int originIndex = allAccessibleFields.indexOf(currentWaveOfFields.get(0));
		//Check if 
		if (originIndex == -1){
			return null;
		}
		fieldMarks[originIndex] = currentMark;
		//Wave expansion part of Lee algorithm
		while (true){
			if (currentWaveOfFields.size() == 0){
				break;			
			}
			for (GameField currentField : currentWaveOfFields){
				ArrayList<GameField> accessibleNeighbors = new ArrayList<GameField>();
				ArrayList<GameField> accessibleUnmarkedNeighbors = new ArrayList<GameField>();
				//----MARKING (NOT ALGORITHM)----
				for (GameField neighbor : currentField.getNeighborFields()){
					if (allAccessibleFields.contains(neighbor)){
						accessibleNeighbors.add(neighbor);
					}
				}
				for (GameField accessibleNeighbor : accessibleNeighbors){
					if (fieldMarks[allAccessibleFields.indexOf(accessibleNeighbor)] == -1){
						accessibleUnmarkedNeighbors.add(accessibleNeighbor);
					}
				}
				//----END OF MARKING----
				
				//----PART OF ALGORITHM----
				if (accessibleUnmarkedNeighbors.size() > 0){
					for (GameField accessibleUnmarkedNeighbor : accessibleUnmarkedNeighbors){
						fieldMarks[allAccessibleFields.indexOf(accessibleUnmarkedNeighbor)] = currentMark + 1;
					}
					currentMark++;
				}
				else {
					break;
				}
				//----END OF PART OF ALGORITHM
			}
			currentWaveOfFields.clear();
			for (int i = 0; i < fieldMarks.length; i++){
				if (fieldMarks[i] == currentMark){
					currentWaveOfFields.add(allAccessibleFields.get(i));
				}
			}
		}
		
		//Backtrace part of Lee algorithm
		/*
		while (!this.getGameFieldPosition().equals(currentField)){
				ArrayList<GameField> accessibleNeighbors = new ArrayList<GameField>();
				for (GameField neighbor : currentField.getNeighborFields()){
					if (allAccessibleFields.contains(neighbor)){
						accessibleNeighbors.add(neighbor);
					}
				}
				for (GameField accessibleNeighbor : accessibleNeighbors){
					if (fieldMarks[allAccessibleFields.indexOf(accessibleNeighbor)] == currentMark - 1){
						Log.d("myLog", "" + routeByLee);
						routeByLee.add(accessibleNeighbor);
						currentMark--;
						currentField = accessibleNeighbor;
						break;
					}
				}
				
			}
		 */
		return routeByLee;
	}
		
		
	
}
