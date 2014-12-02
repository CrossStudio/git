package humandatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class HumanDatabase {

	public static void main(String[] args){
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		FileOutputStream fos;
		ObjectOutputStream oos;
		
		FileInputStream fis;
		ObjectInputStream ois;
		
		Human [] humans = new Human[10];
		
		File humansFile = new File ("humans.obj");
		
		if (humansFile.length() == 0){
			humans = getInfoFromUser(reader);
			try {
				fos = new FileOutputStream(humansFile);
				oos = new ObjectOutputStream(fos);
				
				for (Human human : humans){
					oos.writeObject(human);
					System.out.println(human);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				int i = 0;
				fis = new FileInputStream(humansFile);
				ois = new ObjectInputStream(fis);
				while (true){
					humans[i] = (Human) ois.readObject();
					i++;
				}
			}
			catch (IOException | ClassNotFoundException ex){
				for (Human human : humans){
					System.out.println(human);
				}
				System.out.println("No more entries to read");
			}
			catch (ArrayIndexOutOfBoundsException ex){
				ex.printStackTrace();
			}
		}
		
	
		

	}

	/**
	 * Get info about 10 people from user through console
	 * @param reader - input variable
	 * @return array of humans
	 */
	private static Human[] getInfoFromUser(BufferedReader reader) {
		String firstName;
		String surname;
		int age;
		int height;
		Human[] humans = new Human[10];
		try {
			for (int i = 0; i < 10; ++i){
				System.out.println("Enter first name:");
				firstName = reader.readLine();
				System.out.println("Enter surname:");
				surname = reader.readLine();
				System.out.println("Enter age (in full years):");
				age = Integer.parseInt(reader.readLine());
				System.out.println("Enter height (in cm):");
				height = Integer.parseInt(reader.readLine());
				humans[i] = new Human(firstName, surname, age, height);
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		return humans;
		
	}

}
