package filesystem;

public class FileSystemTest {

	public static void main(String[] args) {
		new FileSystem();

		Directory games = new Directory("Games", "c:\\");
		Directory windows = new Directory("Windows", "d:\\");
		Directory wow = new Directory("WorldOfWarcraft", "c:\\Games\\");
		Directory fifa = new Directory("Fifa 15", "c:\\Games\\");
		
		Directory newDir = new Directory("Manga98", "c:\\Games\\");
		
		Directory.createNewDir(games);
		Directory.createNewDir(windows);
		Directory.createNewDir(wow);
		Directory.createNewDir(fifa);
		Directory.createNewDir(newDir);
		
		Directory.removeSubDir(fifa);
		
		fifa.moveTo("c:\\");
		
		//FileSystem.getWholeFileSystem();
		
		for (Directory dir : FileSystem.currentSystem){
			//System.out.println(dir);
			for (Directory subDir : dir.getSubDirectories()){
				System.out.println("Directory \"" + dir + "\" : " + subDir.getFullPath());
			}
		}
	}

}
