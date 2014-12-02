package filesystem;

public class FileSystemTest {

	public static void main(String[] args) {
		Directory games = new Directory("Games", "c:\\");
		
		for (String path : Directory.getWholeFileSystem()){
			System.out.println(path);
		}

	}

}
