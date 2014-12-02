package filesystem;

import java.util.ArrayList;

public class Directory {
	
	private static ArrayList<String> availablePaths = new ArrayList<String>();
	
	static {
		if (!availablePaths.contains("c:\\")){
			availablePaths.add("c:\\");
		}
	}
	
	private String name;
	
	private String path;
	
	private ArrayList<Directory> directories = new ArrayList<Directory>();
	
	private ArrayList<File> files = new ArrayList<File>();
	
	public Directory (String name, String path){
		if (checkPath(path)){
			this.path = path;
		}
		else {
			System.out.println("No such path in the system");
			return;
		}
		if (checkName(name, path)){
			this.name = name;
		}
		else {
			System.out.println("Such directory already exists");
			return;
		}
		availablePaths.add(this.getPath() + this.getName());
		System.out.println("\"" + this + "\" created");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void rename(String name) {
		if (checkName(name, path)){
			this.name = name;
		}
		else {
			System.out.println("Such directory already exists");
			return;
		}
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void moveTo(String path) {
		if (checkPath(path)){
			this.path = path;
		}
		else {
			System.out.println("No such path in the system");
			return;
		}
	}
	
	public String toString(){
		return path + name;
	}
	
	/**
	 * Checks whether such path exists in the file system
	 * @param path - path to be checked for existence
	 * @return true if such path exists, false otherwise
	 */
	private boolean checkPath(String path){
		if (availablePaths.contains(path)){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether such directory already exists
	 * @param name - directory name to be checked
	 * @param path - directory path where new directory is to be created
	 * @return true if there is no directory in such path with such name, false otherwise
	 */
	private boolean checkName(String name, String path){
		if (!availablePaths.contains(path + name)){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return all file system at the moment
	 */
	public static ArrayList<String> getWholeFileSystem(){
		return availablePaths;
	}
}
