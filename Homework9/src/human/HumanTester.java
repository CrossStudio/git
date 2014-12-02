package human;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class HumanTester {

	public static void main(String[] args) throws IOException{
		Human vasia = new Human("Василий", "Теркин", 37, 173);
		Human kolia = new Human("Николай", "Гоголь", 29, 177);
		Human petia = new Human("Петр", "Ручников", 54, 169);
		
		Human newKolay = null;
		try {
			newKolay = (Human) kolia.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}	
		
		System.out.println(vasia);
		System.out.println(kolia);
		System.out.println(petia);
		System.out.println(newKolay);
		System.out.println(newKolay.equals(kolia));
		
		//Тест сериализации
		
		//Запись объектов Human
		FileOutputStream fos = new FileOutputStream("human.obj");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		try {
			oos.writeObject(vasia);
			oos.writeObject(newKolay);
		}
		finally {
			oos.close();
		}
		//Чтение объектов Human
		Human loadedVasia;
		Human loadedNewKolay;
		FileInputStream fis = new FileInputStream("human.obj");
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		try {
			loadedVasia = (Human) ois.readObject();
			loadedNewKolay = (Human) ois.readObject();
			System.out.println("--Loaded humans!Caution!--");
			System.out.println(loadedVasia);
			System.out.println(loadedNewKolay);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
