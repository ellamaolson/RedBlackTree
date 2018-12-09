import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SpellChecker {
	private static RedBlackTree dictionary;
	private static int mispellings;
	
	public SpellChecker(File dict, File poem) {
		dictionary = insertFileIntoRBT(dict);
		mispellings = spellCheckFile(poem);
	}
	
	//inserts the dictionary file into a RB Tree
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
	
	//Spell checks a file using lookup()
	public int spellCheckFile(File textFile) {
		int mispellings = 0;
		
		Scanner scan; 
		try {
			scan = new Scanner(textFile);
			while (scan.hasNextLine()) { 
				mispellings += spellCheckLine(scan.nextLine());
			}
			scan.close();
		} 
		//e is the exception thrown is file input is invalid
		catch (FileNotFoundException e) {
			System.out.println(e); 
		}
		return mispellings;
	}
	
	public int spellCheckLine(String line) {
		String[] words = parseLineIntoArray(line);
		int mispellings = 0;
		for(String w: words) {
			if(!dictionary.lookup(w))
				mispellings++;
		}
		return mispellings;
	}
	
	//Gets rid of any punctuation and separtes a string into words
	public String[] parseLineIntoArray(String line) {
		String[] splitted = line.split("[\\p{Punct}\\s]+"); 
		return splitted;
	}
	
	public static int getMispellingCount() {
		return mispellings;
	}
}









