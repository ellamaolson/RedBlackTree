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
		a.printTree();
	}

	//RBT Node Class
	public static class Node<Key extends Comparable<Key>> { //changed to static 

		Key key;  		  
		Node<String> parent;
		Node<String> leftChild;
		Node<String> rightChild;
		boolean isRed;
		int color;

		public Node(Key data){
			this.key = data;
			leftChild = null;
			rightChild = null;
		}		

		public int compareTo(Node<Key> n){ 	//this < that  <0
			return key.compareTo(n.key);  	//this > that  >0
		}

		public boolean isLeaf(){
			if (this.equals(root) && this.leftChild == null && this.rightChild == null) return true;
			if (this.equals(root)) return false;
			if (this.leftChild == null && this.rightChild == null){
				return true;
			}
			return false;
		}
	}

	public boolean isLeaf(RedBlackTree.Node<String> n){
		if (n.equals(root) && n.leftChild == null && n.rightChild == null) return true;
		if (n.equals(root)) return false;
		if (n.leftChild == null && n.rightChild == null){
			return true;
		}
		return false;
	}

	public interface Visitor<Key extends Comparable<Key>> {
		/**
			This method is called at each node.
			@param n the visited node
		 */
		void visit(Node<Key> n);  
	}

	public void visit(Node<Key> n){
		System.out.println(n.key);
	}

	public void printTree(){  //preorder: visit, go left, go right
		RedBlackTree.Node<String> currentNode = root;	
		printTree("", currentNode, true, "");
	}

	private void printTree(String prefix, Node node, boolean isTail, String child) {
		if(node == null) {
			return;
		}

		System.out.println(prefix + (isTail ? "└── " : "├── ") + node.key + " " + child + (node.isRed ? " (red)" : ""));

		if(node.leftChild != null) {
			printTree(prefix + (isTail ? "    " : "│   "), node.leftChild, false, "     = (L)");
		}

		if(node.rightChild != null) {
			printTree(prefix + (isTail ? "    " : "│   "), node.rightChild, false, "     = (R)");
		}

		if(node.isLeaf()) {
			isTail = true;
		}
	}

	// place a new node in the RB tree with data the parameter and color it red. 
	public void addNode(String data){  	//this < that  <0.  this > that  >0
		if (root == null) {
			root = new Node(data);
			//System.out.println(root.key + " ("+ ifIsRed(root) + ") = root");
		}

		Node current = root;
		while (true) {
			int comparisonResult = current.compareTo(new Node(data));
			//common use case: assigning data into root
			if (comparisonResult == 0) {
				current.key = data;
				return;
			} 
			//current > data
			else if (comparisonResult > 0){
				if (current.leftChild == null) {
					current.leftChild = new Node(data);
					fixTree(current.leftChild);
					//System.out.println(current.leftChild.key + " ("+ ifIsRed(current.leftChild) + ") = leftChild of " + current.key);
					break;
				}
				current = current.leftChild;
			}
			//current < data
			else if (comparisonResult < 0) {
				if (current.rightChild == null) {
					current.rightChild = new Node(data);
					fixTree(current.rightChild);
					//System.out.println(current.rightChild.key + " ("+ ifIsRed(current.rightChild) + ") = rightChild of " + current.key);
					break;
				}
				current = current.rightChild;
			} 

		}
	}	

	public void insert(String data){
		addNode(data);	
	}

	public String ifIsRed(Node node) {
		if(node.isRed) {
			return "red";
		}
		return "black";
	}

	public RedBlackTree.Node<String> lookup(String k){
		return null; 
		//fill
	}


	public RedBlackTree.Node<String> getSibling(RedBlackTree.Node<String> n){
		if(n.parent != null && n.parent.leftChild != null && n.parent.rightChild != null) {
			if(n == n.parent.leftChild) {
				return n.parent.rightChild;
			} else {
				return n.parent.rightChild;
			}
		}
		return null;
	}


	public RedBlackTree.Node<String> getAunt(RedBlackTree.Node<String> n){
		Node grandparent = getGrandparent(n);
		if(grandparent != null) {
			if(n.parent == grandparent.leftChild) {
				return grandparent.rightChild;
			} else {
				return grandparent.leftChild;
			}
		}
		return null;
	}

	public RedBlackTree.Node<String> getGrandparent(RedBlackTree.Node<String> n){
		if(n.parent != null && n.parent.parent != null) {
			return n.parent.parent;
		}
		return null;
	}

	public void rotateLeft(RedBlackTree.Node<String> n){
		//
	}

	public void rotateRight(RedBlackTree.Node<String> n){
		//
	}

	public void fixTree(RedBlackTree.Node<String> current) {
		Node aunt = getAunt(current);
		Node parent = current.parent;
		Node grandparent = getGrandparent(current);

		//Set color to red
		current.isRed = true;

		//1. If current node is root
		if(current == root) {
			current.isRed = false;
			return;
		}

		//2. If parent is black, the tree is a balanced RedBlackTree. 
		if(parent != null && !parent.isRed) {
			return;
		}

		//3. Correct double red problems
		if(current != null && parent != null && grandparent != null && current != root && parent.isRed) {

			//3.1 if Aunt is black or empty
			if(aunt == null || !aunt.isRed) {

				//3.1.A parent is left child of grandparent and current is right child of parent
				if(parent == grandparent.leftChild
						&& current == parent.rightChild) {
					rotateLeft(parent);
					fixTree(parent);
				} 
				//3.1.B parent is right child of grandparent and current if left child of parent
				else if (parent == grandparent.rightChild 
						&& current == parent.leftChild) {
					rotateRight(parent);
					fixTree(parent);
				} 
				//3.1.C parent is left child of grandparent and current is left child of parent
				else if(parent == grandparent.leftChild 
						&& current == parent.leftChild) {
					parent.isRed = false;
					grandparent.isRed = true;
					rotateRight(grandparent);
					return;
				} 
				//3.1.D parent is right child of grandparent and current is right child of parent
				else if(parent == grandparent.rightChild 
						&& current == parent.rightChild) {
					parent.isRed = false;
					grandparent.isRed = true;
					rotateLeft(grandparent);
					return;
				}
			} 

			//3.2 The aunt is red
			else if(getAunt(current).isRed) {
				parent.isRed = false;
				aunt.isRed = false;
				grandparent.isRed = true;
				fixTree(grandparent);
			}
		}
	}

	public boolean isEmpty(RedBlackTree.Node<String> n){
		if (n.key == null){
			return true;
		}
		return false;
	}

	public boolean isLeftChild(RedBlackTree.Node<String> parent, RedBlackTree.Node<String> child)
	{
		if (child.compareTo(parent) < 0 ) {//child is less than parent
			return true;
		}
		return false;
	}

	public void preOrderVisit(Visitor<String> v) {
		preOrderVisit(root, v);
	}


	private static void preOrderVisit(RedBlackTree.Node<String> n, Visitor<String> v) {
		if (n == null) {
			return;
		}
		v.visit(n);
		preOrderVisit(n.leftChild, v);
		preOrderVisit(n.rightChild, v);
	}	
}
