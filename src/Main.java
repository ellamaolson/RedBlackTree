import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		File file = new File("src/dictionary.txt");
		Main main = new Main(file);
	}
	
	public Main(File textFile) {
		RedBlackTree dictionary = insertFileIntoRBT(textFile);
	}
	
	public RedBlackTree insertFileIntoRBT(File textFile) {
		RedBlackTree dictionary = new RedBlackTree();
		
		//Scans the file line-by-line
		Scanner scan; 
		try {
			scan = new Scanner(textFile);
			while (scan.hasNextLine()) { 
				dictionary.insert(scan.nextLine());
			}
			scan.close();
			return dictionary;
		} 
		//e is the exception thrown is file input is invalid
		catch (FileNotFoundException e) {
			System.out.println(e); 
			return null;
		}
	}
}
