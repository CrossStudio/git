package dnd.dungeon_master_helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a general character from DND game with loads of parameters along with their getters and setters
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
	private ArrayList<Power> charPowers = new ArrayList<Power>();
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
     * @param name - character's name
     * @param race - character's race
     * @param charClass - character's class
     * @param charMaxHP - character's maximum health points
     */
    private DNDCharacter(String name, String charClass, int charInitiativeEncounter, int charMaxHP){
    	this.charName = name;
    	this.charClass = charClass;
    	this.charInitiativeEncounter = charInitiativeEncounter;
    	this.charHPMax = charMaxHP;
    	this.charHPCurrent = this.charHPMax;
    }
    
    /**
     * Constructor to be used to load the character from the database
     * @param name
     * @param charClass
     * @param charInitiativeEncounter
     * @param charMaxHP
     * @param charCurrentHP
     * @param modifiers
     */
    private DNDCharacter(String name, String charClass, int charMaxHP, int charCurrentHP, ArrayList<String> modifiers){
    	this.charName = name;
    	this.charClass = charClass;
    	this.charInitiativeEncounter = (int) (Math.random() * 10);
    	this.charHPMax = charMaxHP;
    	this.charHPCurrent = charCurrentHP;
    	this.listOfAppliedModifiers = modifiers;
    }
    
    /**
     * Method that adds one new character with four passed parameters to the game
     * by adding it to the list of allCharacters that is also passed to it
     * @param name - character's name
     * @param race - character's race
     * @param charClass - character's class
     * @param listOfChars - list of allCharacters to get the newly created character
     * @return - newly created character
     */
    public static DNDCharacter addNewCharacterToGame(String name, String charClass, int charInitiativeEncounter, int charMaxHP){
    	DNDCharacter newCharacter = new DNDCharacter(name, charClass, charInitiativeEncounter, charMaxHP);
    	allCharacters.add(newCharacter);
    	return newCharacter;
    }
    
    /**
     * Method that adds one new character with 6 passed parameters to the game 
     * @param name
     * @param charClass
     * @param charInitiativeEncounter
     * @param charMaxHP
     * @param charCurrentHP
     * @param modifiers
     * @return - newly created character
     */
    public static DNDCharacter addNewCharacterToGame(String name, String charClass, int charMaxHP, int charCurrentHP,
    		ArrayList<String> modifiers){
    	DNDCharacter newCharacter = new DNDCharacter(name, charClass, charMaxHP, charCurrentHP, modifiers);
    	allCharacters.add(newCharacter);
    	return newCharacter;
    }

    public static DNDCharacter addNewCharacterToGame(DNDCharacter character){
    	allCharacters.add(character);
    	return character;
    }
    
    /**
     * Creates a dummy of a character, character parameters are set to default values "" and 0
     * @return dummy character
     */
    public static DNDCharacter createDummyCharacter(){
    	DNDCharacter character = new DNDCharacter("", "", 0, 0);
		return character;
    }
    
    /**
     * Creates a copy of a given character with slightly changed name
     * @param characterToBeCopied - character that will be copied
     * @return copy of a given character
     */
    public DNDCharacter copyCharacter(DNDCharacter characterToBeCopied){
    	
    	this.charName = characterToBeCopied.getCharName();
		this.charClass = characterToBeCopied.getCharClass();
		this.charHPMax = characterToBeCopied.getCharHPMax();
		this.charHPCurrent = characterToBeCopied.getCharHPCurrent();
		this.listOfAppliedModifiers = new ArrayList<>(characterToBeCopied.getListOfAppliedModifiers());
		this.charPowers = new ArrayList<>(characterToBeCopied.getCharPowers());
    	
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
     * Basic getter of list of character's powers
     * @return list of character's powers
     */
    public ArrayList<Power> getCharPowers(){
    	return charPowers;
    }
    
    /**
     * Basic getter for character's class
     * @return character's class
     */
    public String getCharClass(){
    	return this.charClass;
    }
    
    /**
     * Basic setter for character's class
     * @param charClass
     */
    public void setCharClass(String charClass){
    	this.charClass = charClass;
    }
    
    /**
     * Basic getter for character's name
     * @return character's name
     */
    public String getCharName() {
        return charName;
    }

    /**
     * Basic setter of character's name
     * @param charName - character's name to be set
     */
    public void setCharName(String charName) {
        this.charName = charName;
    }

    /**
     * Basic getter for character's encounter initiative
     * @return character's encounter initiative
     */
    public int getCharInitiativeEncounter(){
    	return charInitiativeEncounter;
    }
    
    /**
     * Basic setter of character's encounter initiative
     * @param initiative - character's encounter initiative to be set
     */
    public void setCharInitiativeEncounter(int initiative){
    	this.charInitiativeEncounter = initiative;
    }
    
    /**
     * Basic getter for character's maximum Health Points
     * @return character's maximum Health Points
     */
    public int getCharHPMax() {
        return charHPMax;
    }

    /**
     * Basic setter of character's maximum Health Points
     * @param charHPMax - character's maximum Health Points to be set
     */
    public void setCharHPMax(int charHPMax) {
        this.charHPMax = charHPMax;
    }
    
    /**
     * Basic getter for character's current Health Points
     * @return character's current Health Points
     */
    public int getCharHPCurrent() {
        return charHPCurrent;
    }

    /**
     * Basic setter of character's current Health Points
     * @param charHPCurrent - character's current Health Points
     */
    public void setCharHPCurrent(int charHPCurrent) {
        this.charHPCurrent = charHPCurrent;
    }

    /**
     * Basic getter for character's attack modifier
     * @return character's attack modifier
     */
    public int getCharAttackModifier() {
        return charAttackModifier;
    }

    /**
     * Basic setter of character's attack modifier
     * @param charAttackModifier - character's attack modifier
     */
    public void setCharAttackModifier(int charAttackModifier) {
        this.charAttackModifier = charAttackModifier;
    }

    /**
     * Basic getter for character's Armor Class modifier
     * @return character's Armor Class modifier
     */
    public int getCharACModifier() {
        return charACModifier;
    }

    /**
     * Basic setter of character's Armor Class modifier
     * @param charACModifier - character's Armor Class modifier
     */
    public void setCharACModifier(int charACModifier) {
        this.charACModifier = charACModifier;
    }

    /**
     * Basic getter for character's ongoing damage suffered
     * @return character's ongoing damage suffered
     */
    public int getCharOngoingDamage() {
        return charOngoingDamage;
    }

    /**
     * Basic setter of character's ongoing damage suffered
     * @param charOngoingDamage - character's ongoing damage suffered
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
}
