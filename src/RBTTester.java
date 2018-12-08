
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;


public class RBTTester {
	File file;

	//	@Before
	//	public void setUp() {
	//		file = new File("src/dictionary.txt");
	//		Main main = new Main(file);
	//	}
	//
	@Test
	//Test the Red Black Tree
	public void test() {
		RedBlackTree rbt = new RedBlackTree();
		rbt.insert("D");
		rbt.insert("B");
		rbt.insert("A");
		rbt.insert("C");
		rbt.insert("F");
		rbt.insert("E");
		rbt.insert("H");
		rbt.insert("G");
		rbt.insert("I");
		rbt.insert("J");
		assertEquals("DBACFEHGIJ", makeString(rbt));
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
		assertEquals(str, makeStringDetails(rbt));
	}

	@Test
	//Test Red Black Tree insert() and fixTree()
	public void testInsert() {
		RedBlackTree a = new RedBlackTree();
		a.insert("Apple");
		a.insert("Arizona");
		a.insert("Aaron");
		a.insert("Banana");
		a.insert("Carrot");
		a.insert("Dog");
		a.insert("Beaver");
		a.insert("Armadillo");
		a.insert("Gorilla");
		System.out.println("-----------------------------------");
		a.printTree();
		assertEquals("BananaAppleAaronArizonaArmadilloCarrotBeaverDogGorilla", 
				makeString(a));
	}
	
	@Test
	public void testSpellChecker() {
		 double start = System.currentTimeMillis();
		 double end =  System.currentTimeMillis();
		 
		 double time = end - start;
		 System.out.println("Program took: " + time);
	}

	//add tester for spell checker

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
