package filesystem;

public class File {
	
	private String name;
	
	private String path;
	
	private String fullPath;
	
	public File (String fileName, String path){
		this.name = fileName;
		this.path = path;
		this.fullPath = this.path + this.name;
	}

	/**
	 * @return the fileName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setName(String fileName) {
		this.name = fileName;
	}
	
	/**
	 * @return the file full path
	 */
	public String getFullPath(){
		return fullPath;
	}
	
	/**
	 * @return the file path
	 */
	public String getPath(){
		return path;
	}
	/*
	public static void createNewFile(File file){
		if (Directory.checkPath(file.getPath())){
			if (File.checkName(file.getFullPath())){
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
	*/
	
	private static boolean checkName(String gullPath){
		return false;
	}
}
