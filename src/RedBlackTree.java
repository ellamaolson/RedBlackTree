/*
 * This program creates a balanced Binary Search Tree through a 
 * Red-Black Tree methodology. It can take in 
 */

public class RedBlackTree<Key extends Comparable<Key>> {	
	private static RedBlackTree.Node<String> root;//changed root to static b/c error in isLeaf()

	//---------------
	// RBT Node Class
	//---------------
	public static class Node<Key extends Comparable<Key>> { //changed to static 

		String key;  		  
		Node<String> parent;
		Node<String> leftChild;
		Node<String> rightChild;
		boolean isRed;
	    //public int numLeft;// the number of elements to the left of each node
	    //public int numRight; // the number of elements to the right of each node

		public Node(String data){
			this.key = data;
			leftChild = null;
			rightChild = null;
			//numLeft = 0;
			//numRight = 0;
		}		

		public int compareTo(Node<String> n) { 	//this < that  <0
			return key.compareTo(n.key);  	//this > that  >0
		}

		public boolean isLeaf() {
			if (this.equals(root) && this.leftChild == null && this.rightChild == null) return true;
			if (this.equals(root)) return false;
			if (this.leftChild == null && this.rightChild == null){
				return true;
			}
			return false;
		}
	}

	//Constructor
	public RedBlackTree() {
		root = null;
	}

	//---------------
	// Insert Methods 
	//---------------
	
	//Add new data to the tree, search for the node with the data, then fix the tree
	public void insert(String data){  	//this < that  <0.  this > that  >0
		Node<String> n = new Node<String>(data);
		Node<String> y = null;
		Node<String> x = root;
		
		while(x != null) {
			y = x;
			if(n.compareTo(x) < 0)
				x = x.leftChild;
			else
				x = x.rightChild;
		}

		n.parent = y;
		if(y == null)
			root = n;
		else if (n.compareTo(y) < 0)
			y.leftChild = n;
		else 
			y.rightChild = n;

		n.leftChild = null;
		n.rightChild = null;
		n.isRed = true;
		fixTree(n);
	}
	
	// Searches for a node with key data
	public Node<String> search(String data) {
		Node<String> current = root;
		while (current != null) { 
			if (current.key.equals(data))
				return current;
			else if (current.key.compareTo(data) < 0)
				current = current.rightChild;
			else
				current = current.leftChild;
		}
		return null;
	}
	
	//Fix the tree in different cases
	public void fixTree(RedBlackTree.Node<String> current) {
		Node<String> aunt = getAunt(current);
		Node<String> parent = current.parent;
		Node<String> grandparent = getGrandparent(current);

		if(current == root) {//1. If current node is root
			current.isRed = false;
		} else if(parent != null && !parent.isRed) { //2. If parent is black, the tree is a balanced RedBlackTree. 
			//Don't do anything, it is balanced
		} else if(current != null && parent != null && grandparent != null && current != root 
				&& current.isRed && parent.isRed) { //3. Correct double red problems

			if(aunt == null || !aunt.isRed) { //3.1 if Aunt is black or empty
				
				if(parent == grandparent.leftChild
						&& current == parent.rightChild) { //3.1.A 
					rotateLeft(parent);
					fixTree(parent);
				} else if (parent == grandparent.rightChild 
						&& current == parent.leftChild) { //3.1.B
					rotateRight(parent);
					fixTree(parent);
				} else if(parent == grandparent.leftChild 
						&& current == parent.leftChild) { //3.1.C 
					parent.isRed = false;
					grandparent.isRed = true;
					rotateRight(grandparent);
					return;
				} else if(parent == grandparent.rightChild 
						&& current == parent.rightChild) { //3.1.D 
					parent.isRed = false;
					grandparent.isRed = true;
					rotateLeft(grandparent);
					return;
				}
			} else if(getAunt(current).isRed) { //3.2 The aunt is red
				parent.isRed = false;
				aunt.isRed = false;
				grandparent.isRed = true;
				fixTree(grandparent);
			}
		}
	}

	//Helper method to fixTree to rotate left around x
	public void rotateLeft(RedBlackTree.Node<String> x){
		Node<String> y = x.rightChild;
		x.rightChild = y.leftChild;
		if(y.leftChild != null)
			y.leftChild.parent = x;
		
		y.parent = x.parent;
		if(x.parent == null)
			root = y;
		else if(x == x.parent.leftChild)
			x.parent.leftChild = y;
		else
			x.parent.rightChild = y;

		y.leftChild = x;
		x.parent = y;	
	}

	//Helper method to fixTree to rotate right around y
	public void rotateRight(RedBlackTree.Node<String> y){
		Node<String> x = y.leftChild;
		y.leftChild = x.rightChild;
		if(x.rightChild != null)
			x.rightChild.parent = y;

		x.parent = y.parent;
		if(y.parent == null)
			root = x;
		else if(y == y.parent.rightChild)
			y.parent.rightChild = x;
		else
			y.parent.leftChild = x;
		
		x.rightChild = y;
		y.parent = x;
	}
	
	//---------------
	//Print Methods
	//---------------
	
	//Print in preorder: visit, go left, go right
	public void printTree(){
		RedBlackTree.Node<String> currentNode = root;	
		printTree("", currentNode, true, "");
	}

	//Print in a BST format 
	private void printTree(String prefix, Node<String> node, boolean isTail, String child) {
		if(node == null)
			return;
		System.out.println(prefix + (isTail ? "└── " : "├── ") + node.key + " " + child 
				+ (node.isRed ? " (red) " : ""));

		if(node.leftChild != null)
			printTree(prefix + (isTail ? "    " : "│   "), node.leftChild, false, "- L");

		if(node.rightChild != null)
			printTree(prefix + (isTail ? "    " : "│   "), node.rightChild, false, "- R");

		if(node.isLeaf())
			isTail = true;
	}

	//---------------
	//Helper Methods
	//---------------

	public RedBlackTree.Node<String> getSibling(RedBlackTree.Node<String> n){
		if(n.parent != null && n.parent.leftChild != null && n.parent.rightChild != null) {
			if(n == n.parent.leftChild)
				return n.parent.rightChild;
			else
				return n.parent.rightChild;
		}
		return null;
	}

	public RedBlackTree.Node<String> getAunt(RedBlackTree.Node<String> n){
		Node<String> grandparent = getGrandparent(n);
		if(grandparent != null) {
			if(n.parent == grandparent.leftChild)
				return grandparent.rightChild;
			else
				return grandparent.leftChild;
		}
		return null;
	}

	public RedBlackTree.Node<String> getGrandparent(RedBlackTree.Node<String> n){
		if(n.parent != null && n.parent.parent != null)
			return n.parent.parent;
		return null;
	}
	
	public String ifIsRed(Node<String> node) {
		if(node.isRed) 
			return "red";
		return "black";
	}

	public boolean isLeaf(RedBlackTree.Node<String> n){
		if (n.equals(root) && n.leftChild == null && n.rightChild == null) return true;
		if (n.equals(root)) return false;
		if (n.leftChild == null && n.rightChild == null)
			return true;
		return false;
	}
	
	public Node<String> getRoot() {
		return root;
	}

	//---------------
	//Visit 
	//---------------
	
	public interface Visitor<Key extends Comparable<Key>> {
		/**
			This method is called at each node.
			@param n the visited node
		 */
		void visit(Node<Key> n);  
	}

	public void visit(Node<Key> n){
		if(n == null) {
			System.out.println("Node is null in visit()");
			return;
		}
		System.out.println(n.key);
	}

	public void preOrderVisit(Visitor<String> v) {
		preOrderVisit(root, v);
	}

	private static void preOrderVisit(RedBlackTree.Node<String> n, Visitor<String> v) {
		if (n == null) {
			System.out.println("Node is null in preOrderVisit()");
			return;
		}
		
		v.visit(n);
		
		if(n.leftChild != null) 
			preOrderVisit(n.leftChild, v);

		if(n.rightChild != null)
			preOrderVisit(n.rightChild, v);
	}
	
	// Looks up a value in the RBTree to see if it exists
	public Boolean lookup(String data) {
		Node<String> current = root;
		while (current != null){
			if (current.key.equals(data))// return that node and exit search(int)
				return true;
			else if (current.key.compareTo(data) < 0)// go left or right based on value of current and key
				current = current.rightChild;
			else // go left or right based on value of current and key
				current = current.leftChild;
		}

		return false; // we have not found a node whose key is "key"
	}
	
}
