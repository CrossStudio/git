package cross.xam.newsplusplus;

public enum Category {

	GENERAL(0, "General"),
	POLITICS(1, "Politics"),
	SPORTS(2, "Sports"),
	ECONOMY(3, "Economy");
	
	private int numValue;
	
	private String stringValue;
	
	Category (int num, String name){
		numValue = num;
		stringValue = name;
	}
	
	public int getNumValue(){
		return numValue;
	}
	
	public String getStringValue(){
		return stringValue;
	}
	
	public String toString(){
		return stringValue;
	}
	
}
