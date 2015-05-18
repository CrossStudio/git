package dnd.dungeon_master_helper;

import java.util.ArrayList;

public class DNDCharacter {
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

    private DNDCharacter(String name, String race, String charClass){
    	this.charName = name;
    	this.charRace = race;
    	this.charClass = charClass;
    }
    
    public static void addNewCharacterToGame(String name, String race, String charClass, ArrayList<DNDCharacter> listOfChars){
    	listOfChars.add(new DNDCharacter(name, race, charClass));
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public int getCharHPMax() {
        return charHPMax;
    }

    public void setCharHPMax(int charHPMax) {
        this.charHPMax = charHPMax;
    }

    public int getCharHPCurrent() {
        return charHPCurrent;
    }

    public void setCharHPCurrent(int charHPCurrent) {
        this.charHPCurrent = charHPCurrent;
    }

    public int getCharAttackModifier() {
        return charAttackModifier;
    }

    public void setCharAttackModifier(int charAttackModifier) {
        this.charAttackModifier = charAttackModifier;
    }

    public int getCharACModifier() {
        return charACModifier;
    }

    public void setCharACModifier(int charACModifier) {
        this.charACModifier = charACModifier;
    }

    public int getCharOngoingDamage() {
        return charOngoingDamage;
    }

    public void setCharOngoingDamage(int charOngoingDamage) {
	        this.charOngoingDamage = charOngoingDamage;
	    }
}
