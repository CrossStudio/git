package human;

import java.io.Serializable;

public class Human implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Age of the human in full years
	 */
	private int age;
	
	/**
	 * Height of the human in cm
	 */
	private int height;
	
	/**
	 * Given name of the human
	 */
	private String firstName;
	
	/**
	 * Family name of the human
	 */
	private String surname;

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	/**
	 * 
	 * @param firstName - given name of the human
	 * @param surname - family name of the human
	 * @param age - age of the human in full years
	 * @param height - height of the human in cm
	 */
	public Human (String firstName, String surname, int age, int height){
		this.firstName = firstName;
		this.surname = surname;
		this.age = age;
		this.height = height;
	}

	/**
	 * Creates a new human with the same field values as the one sent as a parameter
	 * @param o - object of type human
	 * @return - human object
	 */
	public Object clone () throws CloneNotSupportedException{
		Human human = (Human) super.clone();
		return human;
	}

	public String toString(){
		return firstName + " " + surname + ": " + age + " лет, " + height + " см высотой" +
				" HashCode: " + this.hashCode();
	}
	
	public boolean equals(Object o){
		if (this.getClass() == o.getClass()){
			if (this.hashCode() == o.hashCode()){
				return true;
			}
		}
		return false;
		
	}
	
	public int hashCode(){
		int temp;
		temp = (firstName.hashCode() + surname.hashCode()) * age / height;
		return temp;
	}
	
}
