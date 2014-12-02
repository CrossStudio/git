package filesystem;

public class File {
	
	private String fileName;
	
	private String fullPath;
	
	public File (String fileName, String fullPath){
		this.setFileName(fileName);
		this.fullPath = fullPath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return the file path
	 */
	public String getFullPath(){
		return fullPath;
	}
	
}
