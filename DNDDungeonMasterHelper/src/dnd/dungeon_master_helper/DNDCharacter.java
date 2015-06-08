package dnd.dungeon_master_helper;

import java.util.ArrayList;

/**
 * Class represents a general character from DND game with loads of parameters along with their getters and setters
 * @author XAM
 *
 */
public class DNDCharacter {
	
	private static ArrayList<DNDCharacter> characters = new ArrayList<>();
	
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

    /**
     * Basic constructor of the DNDCharacter
     * @param name - character's name
     * @param race - character's race
     * @param charClass - character's class
     */
    private DNDCharacter(String name, String charClass, int charInitiativeEncounter, int charMaxHP){
    	this.charName = name;
    	this.charClass = charClass;
    	this.charInitiativeEncounter = charInitiativeEncounter;
    	this.charHPMax = charMaxHP;
    }
    
    /**
     * Method that adds one new character with three passed parameters to the game
     * by adding it to the list of characters that is also passed to it
     * @param name - character's name
     * @param race - character's race
     * @param charClass - character's class
     * @param listOfChars - list of characters to get the newly created character
     */
    public static void addNewCharacterToGame(String name, String charClass, int charInitiativeEncounter, int charMaxHP){
    	characters.add(new DNDCharacter(name, charClass, charInitiativeEncounter, charMaxHP));
    }

    /**
     * Basic getter of list of characters
     * @return list of all characters in the game
     */
    public static ArrayList<DNDCharacter> getCharacters() {
    	return characters;
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
}
