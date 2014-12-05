package filesystem;

import java.util.ArrayList;


public class Directory{
	
	private String name;
	
	private String path;
	
	private String fullPath;
	
	private ArrayList<Directory> subDirectories = new ArrayList<Directory>();
	
	private ArrayList<File> files = new ArrayList<File>();
	
	static {
		new Directory();
	}
	
	/**
	 * Initialises file system by creating a root directory "c:\"
	 */
	private Directory(){
		this.name = "";
		this.path = "c:\\";
		this.fullPath = this.path + this.name;
		FileSystem.currentSystem.add(this);
	}
	
	public Directory (String name, String path){
		this.name = name + "\\";
		this.path = path;
		this.fullPath = this.path + this.name;
	}

	/**
	 * Create new directory at a given location
	 * @param dir - intended location of the directory to be created
	 */
	public static void createNewDir(Directory dir){
		
		if (Directory.checkPath(dir.getPath())){
			if (Directory.checkName(dir.getFullPath())){
				addSubDir(dir);
			}
			else {
				System.out.println("Such directory already exists");
				return;
			}
		}
		else {
			System.out.println(dir.getPath());
			System.out.println("No such path \"" + dir.getPath() + "\" in the system");
			return;
		}
		System.out.println("\"" + dir + "\" created");
	}
	/**
	 * Add new sub-Directory to the parent directory
	 * @param subDir - sub-Directory to add
	 */
	private static void addSubDir(Directory subDir){
		for (Directory dir : FileSystem.currentSystem){
			if (dir.getFullPath().equals(subDir.getPath())){
				dir.subDirectories.add(subDir);
				System.out.println("\"" + subDir + "\" sub-directory added");
				FileSystem.currentSystem.add(subDir);
				break;
			}
		}
	}
	
	/**
	 * Remove sub-Directory from its parent
	 * @param subDir - sub-Directory to be removed
	 */
	public static void removeSubDir(Directory subDir){
		for (Directory dir : FileSystem.currentSystem){
			if (dir.getFullPath().equals(subDir.getPath())){
				dir.subDirectories.remove(subDir);
				FileSystem.currentSystem.remove(subDir);
				break;
			}
		}
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
		if (checkName(fullPath)){
			this.name = name;
			this.fullPath = this.path + this.name;
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
	 * 
	 * @return the full path
	 */
	public String getFullPath(){
		return fullPath;
	}
	
	public ArrayList<Directory> getSubDirectories(){
		return subDirectories;
	}
	
	/**
	 * Move the directory
	 * @param path the path to set
	 */
	public void moveTo(String path) {
		if (checkPath(this.fullPath)){
			if (checkPath(path)){
				if (checkName(path + this.name)){
					removeSubDir(this);		
					System.out.println("Directory " + this.fullPath + " moved to " + path);
					this.path = path;
					this.fullPath = this.path + this.name;
					addSubDir(this);
				}
				else{
					System.out.println("Such directory already exists");
				}
			}
			else {
				System.out.println("No such path \"" + this.getPath() + "\" in the system");
				return;
			}
		}
		else {
			System.out.println("No such directory \"" + this.fullPath + "\" to move");
		}
	}
	
	public String toString(){
		return fullPath;
	}
	
	/**
	 * Checks whether such path exists in the file system
	 * @param path - path to be checked for existence
	 * @return true if such path exists, false otherwise
	 */
	protected static boolean checkPath(String path){
		for (Directory dir : FileSystem.currentSystem){
			if (dir.getFullPath().equals(path)){
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether such directory already exists
	 * @param name - directory name to be checked
	 * @param path - directory path where new directory is to be created
	 * @return true if there is no directory in such path with such name, false otherwise
	 */
	protected static boolean checkName(String fullPath){
		for (Directory dir : FileSystem.currentSystem){
			if (dir.getFullPath().equals(fullPath)){
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Object o){
		if (o != null && o.getClass() == this.getClass()){
			if (this.name == ((Directory) (o)).name &&
					this.path == ((Directory) (o)).path){
				return true;
			}
		}
		return false;
	}

}
