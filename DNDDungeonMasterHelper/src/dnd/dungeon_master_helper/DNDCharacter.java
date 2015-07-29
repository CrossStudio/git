package dnd.dungeon_master_helper;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

/**
 * Class represents a general currentCharacter from DND game with loads of parameters along with their getters and setters
 * @author XAM
 *
 */
public class DNDCharacter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6313883700901597100L;
	
	private static ArrayList<DNDCharacter> allCharacters;
	private static ArrayList<DNDCharacter> selectedCharacters;
	private static ArrayList<DNDCharacter> notSelectedCharacters;
	private static DNDCharacter dummyCharacter;
	private ArrayList<Power> charPowers = new ArrayList<Power>();
	private ArrayList<String> charHPChanges = new ArrayList<String>();
	
	
	static {
		if (allCharacters == null){
			allCharacters = new ArrayList<>();
		}
		if (selectedCharacters == null){
			selectedCharacters = new ArrayList<>();
		}
		if (notSelectedCharacters == null){
			notSelectedCharacters = new ArrayList<>();
		}
	}
	private boolean selected = false;
	private boolean bloodied;
	
	private String charName;
    private String charRace;
    private String charClass;
    private int charLevel = 1;
    private String charSize = "M";
    private int charInitiativeBase;
    private int charInitiativeEncounter;
    private int charStrength = 10;
    private int charConstitution = 10;
    private int charDexterity = 10;
    private int charIntelligence = 10;
    private int charWizdom = 10;
    private int charCharisma = 10;
    private int charAC;
    private int charFortitude;
    private int charReflex;
    private int charWill;
    private int charSpeed = 6;
    private int charHPMax;
    private int charHPCurrent;
    private int charHPBloodied;
    private int charSurgeValue;
    private int charSurgesPerDay;
    private int charSurgesCurrent;
    private int charOngoingDamage = 0;
    private int charAttackModifier = 0;
    private int charACModifier = 0;
    private int charFortModifier = 0;
    private int charRefModifier = 0;
    private int charWillModifier = 0;

    private ArrayList<String> listOfAppliedModifiers = new ArrayList<>();
    
    /**
     * Basic constructor of the DNDCharacter
     * @param name - currentCharacter's name
     * @param race - currentCharacter's race
     * @param charClass - currentCharacter's class
     * @param charMaxHP - currentCharacter's maximum health points
     */
    private DNDCharacter(String name, String charClass, int charInitiativeEncounter, int charMaxHP){
    	this.charName = name;
    	this.charClass = charClass;
    	this.charInitiativeEncounter = charInitiativeEncounter;
    	this.charHPMax = charMaxHP;
    	this.charHPCurrent = this.charHPMax;
    	this.charHPBloodied = this.charHPMax / 2;
    }
    
    /**
     * Constructor to be used to load the currentCharacter from the database
     * @param name
     * @param charClass
     * @param charInitiativeEncounter
     * @param charMaxHP
     * @param charCurrentHP
     * @param modifiers
     */
    private DNDCharacter(String name, String charClass, int charMaxHP, int charCurrentHP,
    		int charInitiativeEncounter, ArrayList<String> modifiers, ArrayList<String> curHPLog){
    	this.charName = name;
    	this.charClass = charClass;
    	this.charInitiativeEncounter = charInitiativeEncounter;
    	this.charHPMax = charMaxHP;
    	this.charHPCurrent = charCurrentHP;
    	this.charHPBloodied = this.charHPMax / 2;
    	this.listOfAppliedModifiers = modifiers;
    	this.charHPChanges = curHPLog;
    }
    
    /**
     * Method that adds one new currentCharacter with four passed parameters to the game
     * by adding it to the list of allCharacters that is also passed to it
     * @param name - currentCharacter's name
     * @param race - currentCharacter's race
     * @param charClass - currentCharacter's class
     * @param listOfChars - list of allCharacters to get the newly created currentCharacter
     * @return - newly created currentCharacter
     */
    public static DNDCharacter addNewCharacterToGame(String name, String charClass, int charInitiativeEncounter, int charMaxHP){
    	DNDCharacter newCharacter = new DNDCharacter(name, charClass, charInitiativeEncounter, charMaxHP);
    	allCharacters.add(newCharacter);
    	return newCharacter;
    }
    
    /**
     * Method that adds one new currentCharacter with 6 passed parameters to the game 
     * @param name
     * @param charClass
     * @param charInitiativeEncounter
     * @param charMaxHP
     * @param charCurrentHP
     * @param modifiers
     * @return - newly created currentCharacter
     */
    public static DNDCharacter addNewCharacterToGame(String name, String charClass, int charMaxHP, int charCurrentHP,
    		int charInitiativeEncounter, ArrayList<String> modifiers, ArrayList<String> curHPLog){
    	DNDCharacter newCharacter = new DNDCharacter(name, charClass, charMaxHP, charCurrentHP, 
    			charInitiativeEncounter, modifiers, curHPLog);
    	allCharacters.add(newCharacter);
    	return newCharacter;
    }

    public static DNDCharacter addNewCharacterToGame(DNDCharacter character){
    	allCharacters.add(character);
    	return character;
    }
    
    /**
     * Creates a dummy of a currentCharacter, currentCharacter parameters are set to default values "" and 0
     * @return dummy currentCharacter
     */
    private static DNDCharacter createDummyCharacter(){
    	DNDCharacter character = new DNDCharacter("", "", 0, 0);
		return character;
    }
    
    /**
     * Creates a copy of a given currentCharacter with slightly changed name
     * @param characterToBeCopied - currentCharacter that will be copied
     * @return copy of a given currentCharacter
     */
    public DNDCharacter copyCharacter(DNDCharacter characterToBeCopied){

    	this.charName = characterToBeCopied.getCharName();
		this.charClass = characterToBeCopied.getCharClass();
		this.charHPMax = characterToBeCopied.getCharHPMax();
		this.charHPCurrent = characterToBeCopied.getCharHPCurrent();
		this.charHPBloodied = this.charHPMax / 2;
		this.listOfAppliedModifiers = new ArrayList<>(characterToBeCopied.getListOfAppliedModifiers());
		this.charPowers = new ArrayList<>(characterToBeCopied.getCharPowers());
		this.charHPChanges = new ArrayList<>(characterToBeCopied.getCharHPChanges());
    	
    	return characterToBeCopied;
    }

	/**
     * Basic getter of list of allCharacters
     * @return list of all allCharacters in the game
     */
    public static ArrayList<DNDCharacter> getAllCharacters() {
    	return allCharacters;
    }
    
    /**
     * Basic getter of list of selected characters
     * @return list of selected characters in the game
     */
    public static ArrayList<DNDCharacter> getSelectedCharacters(){
    	return selectedCharacters;
    }
    
    /**
     * Basic getter of list of not selected characters
     * @return list of selected characters in the game
     */
    public static ArrayList<DNDCharacter> getNotSelectedCharacters(){
    	return notSelectedCharacters;
    }
    
    /**
     * Basic getter of list of currentCharacter's powers
     * @return list of currentCharacter's powers
     */
    public ArrayList<Power> getCharPowers(){
    	return charPowers;
    }
    
    /**
     * Basic getter of currentCharacter's bloodied HP value
     * @return
     */
    public int getCharBloodiedValue(){
    	return this.charHPBloodied;
    }
    
    /**
     * Basic getter of bloodied value of currentCharacter
     * @return true if bloodied, false if not
     */
    public boolean isBloodied(){
    	if (charHPBloodied >= charHPCurrent){
    		this.bloodied = true;
    	}
    	else {
    		this.bloodied = false;
    	}
    	return bloodied;
    }
    
    /**
     * Basic getter for currentCharacter's class
     * @return currentCharacter's class
     */
    public String getCharClass(){
    	return this.charClass;
    }
    
    /**
     * Basic setter for currentCharacter's class
     * @param charClass
     */
    public void setCharClass(String charClass){
    	this.charClass = charClass;
    }
    
    /**
     * Basic getter for currentCharacter's name
     * @return currentCharacter's name
     */
    public String getCharName() {
        return charName;
    }

    /**
     * Basic setter of currentCharacter's name
     * @param charName - currentCharacter's name to be set
     */
    public void setCharName(String charName) {
        this.charName = charName;
    }

    /**
     * Basic getter for currentCharacter's encounter initiative
     * @return currentCharacter's encounter initiative
     */
    public int getCharInitiativeEncounter(){
    	return charInitiativeEncounter;
    }
    
    /**
     * Basic setter of currentCharacter's encounter initiative
     * @param initiative - currentCharacter's encounter initiative to be set
     */
    public void setCharInitiativeEncounter(int initiative){
    	this.charInitiativeEncounter = initiative;
    }
    
    /**
     * Basic getter for currentCharacter's maximum Health Points
     * @return currentCharacter's maximum Health Points
     */
    public int getCharHPMax() {
        return charHPMax;
    }

    /**
     * Basic setter of currentCharacter's maximum Health Points
     * @param charHPMax - currentCharacter's maximum Health Points to be set
     */
    public void setCharHPMax(int charHPMax) {
        this.charHPMax = charHPMax;
    }
    
    /**
     * Basic getter for currentCharacter's current Health Points
     * @return currentCharacter's current Health Points
     */
    public int getCharHPCurrent() {
        return charHPCurrent;
    }

    /**
     * Basic setter of currentCharacter's current Health Points
     * @param charHPCurrent - currentCharacter's current Health Points
     */
    public void setCharHPCurrent(int charHPCurrent) {
        this.charHPCurrent = charHPCurrent;
    }

    /**
     * Basic getter for currentCharacter's attack modifier
     * @return currentCharacter's attack modifier
     */
    public int getCharAttackModifier() {
        return charAttackModifier;
    }

    /**
     * Basic setter of currentCharacter's attack modifier
     * @param charAttackModifier - currentCharacter's attack modifier
     */
    public void setCharAttackModifier(int charAttackModifier) {
        this.charAttackModifier = charAttackModifier;
    }

    /**
     * Basic getter for currentCharacter's Armor Class modifier
     * @return currentCharacter's Armor Class modifier
     */
    public int getCharACModifier() {
        return charACModifier;
    }

    /**
     * Basic setter of currentCharacter's Armor Class modifier
     * @param charACModifier - currentCharacter's Armor Class modifier
     */
    public void setCharACModifier(int charACModifier) {
        this.charACModifier = charACModifier;
    }

    /**
     * Basic getter for currentCharacter's ongoing damage suffered
     * @return currentCharacter's ongoing damage suffered
     */
    public int getCharOngoingDamage() {
        return charOngoingDamage;
    }

    /**
     * Basic setter of currentCharacter's ongoing damage suffered
     * @param charOngoingDamage - currentCharacter's ongoing damage suffered
     */
    public void setCharOngoingDamage(int charOngoingDamage) {
	        this.charOngoingDamage = charOngoingDamage;
	    }
    
    /**
     * Basic getter for a list of applied modifiers
     * @return list of applied modifiers
     */
    public ArrayList<String> getListOfAppliedModifiers(){
    	return listOfAppliedModifiers;
    }

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public String toString(){
		return this.charName + " (" + this.charClass + ")";
	}

	/**
	 * @return the charHPChanges
	 */
	public ArrayList<String> getCharHPChanges() {
		return charHPChanges;
	}
	
	/**
	 * Heals currentCharacter for an amount sent to the method
	 * @param healing - amount of healing to be received by currentCharacter
	 */
	public void getHealing(int healing){
		this.charHPCurrent += healing;
		this.charHPChanges.add("Healing received: " + healing + " HP");
	}
	
	/**
	 * Inflicts the sent amount of damage to the currentCharacter
	 * @param damage - amount of damage to be inflicted upon the currentCharacter
	 */
	public void sufferDamage(int damage){
		this.charHPCurrent -= damage;
		this.charHPChanges.add("Damage suffered: " + damage + " HP");
	}

	/**
	 * Creates and returns new or returns existing dummy currentCharacter
	 * @return dummy currentCharacter
	 */
	public static DNDCharacter getDummyCharacter() {
		if (dummyCharacter == null){
			dummyCharacter = createDummyCharacter();
			Log.d("myLog", "New Dummy Character created!");
		}
		return dummyCharacter;
	}
	
	/**
	 * Sets dummy currentCharacter to null
	 */
	public static void removeDummyCharacter(){
		dummyCharacter = null;
	}

}
