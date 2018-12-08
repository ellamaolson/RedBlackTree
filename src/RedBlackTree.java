/*
 * This program creates a balanced Binary Search Tree through a 
 * Red-Black Tree methodology. It can take in 
 */

public class RedBlackTree<Key extends Comparable<Key>> {	
	private static RedBlackTree.Node<String> root;//changed root to static b/c error in isLeaf()
	
	public static void main(String[] args) {
		RedBlackTree<String> a = new RedBlackTree<String>();
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
		System.out.println("-STRING----------------------------------");
		a.toString();

	}

	//---------------
	// RBT Node Class
	//---------------
	public static class Node<Key extends Comparable<Key>> { //changed to static 

		String key;  		  
		Node<String> parent;
		Node<String> leftChild;
		Node<String> rightChild;
		boolean isRed;

		public Node(String data){
			this.key = data;
			leftChild = null;
			rightChild = null;
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
		n.isRed = true;
		Node<String> parent = null;

		if (root == null) {
			root = n;
			System.out.println(root.key + " ("+ ifIsRed(root) + ") = root");
		} else {
			parent = search(root, data);
			int compare = parent.compareTo(n);
			if(compare < 0) {
				if(parent.rightChild != null)
					parent.rightChild.parent = n;
				parent.rightChild = n;
				System.out.println(parent.rightChild.key + " ("+ ifIsRed(parent.rightChild) + ") = rightChild of " + parent.key);
			} else if(compare > 0) {
				if(parent.leftChild != null)
					parent.leftChild.parent = n;
				parent.leftChild = n;
				System.out.println(parent.leftChild.key + " ("+ ifIsRed(parent.leftChild) + ") = leftChild of " + parent.key);
			}
			n.parent = parent;
		}
		fixTree(n);
	}

	// Searches for a node with key data
	public Node<String> search(Node<String> node, String data) {
		int compare = 0;

		if(node != null) {
			compare = node.compareTo(new Node<String>(data));
			if(compare < 0 && node.rightChild != null)
				return search(node.rightChild, data);
			else if(compare > 0 && node.leftChild != null)
				return search(node.leftChild, data);
			else
				return node;
		}

		return node;
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
}
