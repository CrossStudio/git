package filesystem;

import java.util.ArrayList;
import java.util.Arrays;

public class FileSystem {

	static ArrayList<Directory> currentSystem = new ArrayList<Directory>();

	/**
	 * 
	 * @return all file system at the moment
	 */
	public static void getWholeFileSystem(){
		//sortSystem();
		System.out.println("File system:");
		if (currentSystem.size() == 1){
			System.out.println(currentSystem.get(0));
		}
		else {
			for (Directory dir : currentSystem){
				if (dir.getFullPath() == "c:\\"){
					continue;
				}
				else {
					System.out.println(dir);
				}
			}
		}
	}
	
	/*
	private static void sortSystem(){
		String[] tempArray =  currentSystem.toArray(new String[currentSystem.size()]);
		Arrays.sort(tempArray);
		currentSystem = new ArrayList<String>(Arrays.asList(tempArray));
	}
	*/
	
}
