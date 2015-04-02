package cross.xam.lostinthewoods;

import java.util.ArrayList;
import java.util.Collection;

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
	 * Set initial value of all the markers for all accessible GameFields this wolf has to -1
	 */
	private void initializeGameFieldMarkers() {
		this.gameFieldMarkers = new ArrayList<Integer>();
		for (int i = 0; i < getAllAccessibleFields().size(); i++){
			this.gameFieldMarkers.add(-1);
		}
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
		if (!this.isDead){
			initializeGameFieldMarkers();
			Log.d("myLog", this.name + " position: " + this.getGameFieldPosition());
			if (rangerInSight()){
				huntRanger();
				return;
			}
			findClosestWolf();
			if (closestWolf != null && !closestWolf.isDead && otherWolfInSight()){
				followWolfPack();
			}
		}
	}
	
	/**
	 * 
	 */
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
			if (moves > 0){
				Log.d("myLog", "" + nextField);
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
		ArrayList<GameField> path = new ArrayList<GameField>();
		
		initializeLeeAlgorithm();
		
		boolean waveExpansionSuccessful = false;
		
		waveExpansionSuccessful = leeAlgorithmWaveExpansion(destination);
		
		if (waveExpansionSuccessful){
			path = leeAlgorithmBacktrace(destination);
		}
		
		return path;
	}

	/**
	 * Sets the marker of the current position of the Wolf to 0
	 */
	private void initializeLeeAlgorithm() {
		
		this.gameFieldMarkers.set(getAllAccessibleFields().indexOf(this.getGameFieldPosition()), 0);
		
		/*for (int i = 0; i < this.gameFieldMarkers.size(); i++){
			Log.d("myLog", "Marker " + i + " = " + this.gameFieldMarkers.get(i));
		}*/
	}

	/**
	 * Part of Lee Algorithm for finding shortest path to some field that assigns appropriate markers to GameFields on its way to some destination GameField
	 * @param destination - GameField that is the target for the algorithm to reach
	 */
	private boolean leeAlgorithmWaveExpansion(GameField destination) {
		int waveNumber = 0;
		int check = 0;
		
		while (true){
			check = leeAlgorithmAssignMarkersInWaveNumber(destination, waveNumber);
			if (check == 0){
				//wave expansion has not reached destination field yet but there is space for expanding
				waveNumber++;
			}
			else if (check == 1){
				//wave expansion has reached destination field
				return true;
			}
			else {
				//wave expansion cannot reach destination field, abort expansion
				return false;
			}
		}
		
	}
	
	/**
	 * Part of wave expansion part of Lee Algorithm that assigns markers for wave number waveNumber + 1 
	 * @param destination - GameField that is the target for the algorithm to reach
	 * @param waveNumber - wave for each the markers should be assigned
	 * @return - true if destination GameField has been found on this wave, false otherwise
	 */
	private int leeAlgorithmAssignMarkersInWaveNumber(GameField destination, int waveNumber) {
		ArrayList<GameField> currentWaveOrigins = new ArrayList<GameField>();
		ArrayList<GameField> currentWaveNeighbors = new ArrayList<GameField>();
		
		currentWaveOrigins = leeAlgorithmGetGameFieldsOfCurrentWave(waveNumber);
		
		currentWaveNeighbors = leeAlgorithmGetCurrentWaveNeighbors(currentWaveOrigins, waveNumber);
		
		//Checks if destination is reachable (no neighbors means it is unreachable)
		if (currentWaveNeighbors.size() == 0){
			return -1;
		}
		
		for (GameField neighbor : currentWaveNeighbors){
			this.gameFieldMarkers.set(getAllAccessibleFields().indexOf(neighbor), waveNumber + 1);
			if (neighbor.equals(destination)){
				Log.d("myLog", "Destination found : " + neighbor);
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Part of wave expansion part of Lee Algorithm that returns GameFields  with markers equal to the passed wave number
	 * @param waveNumber - wave number to compare markers with
	 * @return - GameFields with markers equal to waveNumber passed
	 */
	private ArrayList<GameField> leeAlgorithmGetGameFieldsOfCurrentWave(int waveNumber) {
		ArrayList<GameField> accessibleFields = getAllAccessibleFields();
		ArrayList<GameField> currentWaveFields = new ArrayList<GameField>();
		for (int i = 0; i < this.gameFieldMarkers.size(); i++){
			if (this.gameFieldMarkers.get(i) == waveNumber){
				currentWaveFields.add(accessibleFields.get(i));
			}
		}
		return currentWaveFields;
	}
	
	/**
	 * Part of wave expansion part of Lee Algorithm that returns neighbor GameFields for a list of GameFields of a certain wave
	 * @param currentWaveOrigins - GameFields that require their neighbors to be found
	 * @param waveNumber - wave number of the origin GameFields
	 * @return ArrayList of GameField neighbors for the given wave number
	 */
	private ArrayList<GameField> leeAlgorithmGetCurrentWaveNeighbors(ArrayList<GameField> currentWaveOrigins, int waveNumber) {
		
		ArrayList<GameField> currentWaveNeighbors = new ArrayList<GameField>();
		for (GameField origin : currentWaveOrigins){
			currentWaveNeighbors.addAll(leeAlgorithmGetUnmarkedNeighborFields(origin));
		}
		return currentWaveNeighbors;
	}

	/**
	 * Part of wave expansion part of Lee Algorithm that returns unmarked (marked with -1) GameFields for a set origin GameField
	 * @param origin - GameField that requires its unmarked neighbors to be found
	 * @return ArrayList of GameField unmarked neighbors (marked with -1)
	 */
	private ArrayList<GameField> leeAlgorithmGetUnmarkedNeighborFields(GameField origin) {
		ArrayList<GameField> accessibleNeighbors = getAccessibleNeighborFields(origin);
		ArrayList<GameField> originUnmarkedNeighbors = new ArrayList<GameField>();
		for (GameField neighbor : accessibleNeighbors){
			if (this.gameFieldMarkers.get(getAllAccessibleFields().indexOf(neighbor)) < 0){
				originUnmarkedNeighbors.add(neighbor);
			}
		}
		Log.d("myLog", "unmarked neighbors : " + originUnmarkedNeighbors);
		return originUnmarkedNeighbors;
	}
	
	/**
	 * Returns a list of GameFields neighbors that are accessible by this character from the passed origin GameField
	 * @param origin - GameField that requires its accessible neighbors to be found
	 * @return ArrayList of GameField accessible neighbors
	 */
	private ArrayList<GameField> getAccessibleNeighborFields(GameField origin) {
		ArrayList<GameField> accessibleNeighbors = new ArrayList<GameField>();
		for (GameField neighbor : origin.getNeighborFields()){
			if (getAllAccessibleFields().contains(neighbor)){
				accessibleNeighbors.add(neighbor);
			}
		}
		Log.d("myLog", "accessible neighbors : " + accessibleNeighbors);
		return accessibleNeighbors;
	}
	
	private ArrayList<GameField> leeAlgorithmBacktrace(GameField destination) {
		ArrayList<GameField> path = new ArrayList<GameField>();
		path.add(destination);
		
		GameField currentField = destination;
		
		while (true){
			currentField = getNextFieldInPath(currentField);
			if (!currentField.equals(this.getGameFieldPosition())){
				Log.d("myLog", currentField + " added to path");
				path.add(0, currentField);
			}
			else {
				break;
			}
			
		}
				
		return path;
		
	}

	private GameField getNextFieldInPath(GameField currentField) {
		ArrayList<GameField> accessibleNeighbors = getAccessibleNeighborFields(currentField);
		int currentFieldMark = this.gameFieldMarkers.get(getAllAccessibleFields().indexOf(currentField));
		Log.d("myLog", "Current GameField mark in backtrace is : " + currentFieldMark);
		Log.d("myLog", "AccessibleNeighbors : " + accessibleNeighbors);
		for (GameField neighbor : accessibleNeighbors){
			Log.d("myLog", "neighbor index = " + getAllAccessibleFields().indexOf(neighbor));
			Log.d("myLog", "marker of neighbor = " + this.gameFieldMarkers.get(getAllAccessibleFields().indexOf(neighbor)));
			if (this.gameFieldMarkers.get(getAllAccessibleFields().indexOf(neighbor)) == currentFieldMark - 1){
				return neighbor;
			}
		}
		Log.d("myLog", "Has not found a valid neighbor for backtrace");
		return null;
	}
	
}
