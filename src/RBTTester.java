
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;


public class RBTTester {
	File dictionary;
	File poem;
	SpellChecker checker;
	RedBlackTree<String> a, b;

	@Before
	public void setUp() {
		dictionary = new File("src/dictionary.txt");
		poem = new File("src/poem.txt");
		a = new RedBlackTree<String>();
		b = new RedBlackTree<String>();
	}
	//
	@Test
	//Test the Red Black Tree
	public void test() {
		System.out.println("--- Red Black Tree A ---");
		a.insert("D");
		a.insert("B");
		a.insert("A");
		a.insert("C");
		a.insert("F");
		a.insert("E");
		a.insert("H");
		a.insert("G");
		a.insert("I");
		a.insert("J");
		a.printTree();
		assertEquals("DBACFEHGIJ", makeString(a));
		String str=     "Color: 1, Key:D Parent: \n"+
				"Color: 1, Key:B Parent: D\n"+
				"Color: 1, Key:A Parent: B\n"+
				"Color: 1, Key:C Parent: B\n"+
				"Color: 1, Key:F Parent: D\n"+
				"Color: 1, Key:E Parent: F\n"+
				"Color: 0, Key:H Parent: F\n"+
				"Color: 1, Key:G Parent: H\n"+
				"Color: 1, Key:I Parent: H\n"+
				"Color: 0, Key:J Parent: I\n";
		assertEquals(str, makeStringDetails(a));
	}

	@Test
	//Test Red Black Tree insert() and fixTree()
	public void testInsert() {
		System.out.println("\n--- Red Black Tree B ---");
		b.insert("Apple");
		b.insert("Arizona");
		b.insert("Aaron");
		b.insert("Banana");
		b.insert("Carrot");
		b.insert("Dog");
		b.insert("Beaver");
		b.insert("Armadillo");
		b.insert("Gorilla");
		b.printTree();
		assertEquals("BananaAppleAaronArizonaArmadilloCarrotBeaverDogGorilla", 
				makeString(b));
	}

	@Test
	public void testSpellChecker() {
		System.out.println("\n--- Testing Spell Checker ---");
		
		double start = System.currentTimeMillis();
		SpellChecker checker = new SpellChecker(dictionary, poem);
		double end =  System.currentTimeMillis();
		
		double time = end - start;
		System.out.println("Program took: " + time + "ms");
		
		System.out.println("Mispellings: " + checker.getMispellingCount());
		assertEquals(81, checker.getMispellingCount());
		
	}

	public static String makeString(RedBlackTree t) {
		class MyVisitor implements RedBlackTree.Visitor {
			String result = "";
			public void visit(RedBlackTree.Node n)
			{
				result = result + n.key;
			}
		};
		MyVisitor v = new MyVisitor();
		t.preOrderVisit(v);
		return v.result;
	}

	public static String makeStringDetails(RedBlackTree t) {
		class MyVisitor implements RedBlackTree.Visitor {
			String result = "";
			public void visit(RedBlackTree.Node n)
			{
				if(!(n.key).equals("")) {
					result = result + "Color: " + (n.isRed ? "0" : "1") +", Key:" + n.key 
							+ " Parent: " + (n.parent != null ? n.parent.key : "") + "\n";
				}
			}
		}
		MyVisitor v = new MyVisitor();
		t.preOrderVisit(v);
		return v.result;
	}

}
