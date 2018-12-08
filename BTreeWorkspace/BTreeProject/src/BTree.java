//import BTree.BTreeNode;//Dan - this is implied if they are both in the default package, otherwise lets move everything into a btree package.

/**
 * 
 * 
 *
 * @param <T>
 */
public class BTree<T> {
	private BTreeNode<T> root = null;
	private int minDeg;
	private int maxDeg;
	
	public BTree(int degree){
		//minDeg = 
	}
	
	public void create(){
		BTreeNode<T> x = new BTreeNode<T>();
		x.setIsLeaf(true);
		x.setSize(0);
		//DiskWrite(x);
		root = x;
		
	}
	
	/**
	 * recursive search - calls BTreeSearch
	 * @param root
	 * @param key
	 * @return //Golam it will return the index of the found key. if not found it will return the -1
	 */
	public int search(BTreeNode<TreeObject> x,long key){
		int i = 0;
		// Find the first key greater than or equal to key 
		while( i<x.getSize() && key > x.getKey(i).getData() ) {
			i++;
		}
		// If the found key is equal to k, return this node 
		if(i <= x.getSize() && key == x.getKey(i).getData()) {
			return i;
		}
		if(x.getIsleaf()) {
			return -1;
		}
		return search(x.getChild(i),key);
	}
	
	/**
	 * calls InsertSearch, then runs the insert algo on slides
	 * @param root
	 * @param key
	 */
	public void insert(BTreeNode<TreeObject> root, long key) {//Golam: changed the key genetic to long to match with tree object
		if(root.getSize() == maxDeg -1 ) {
			//Created this object into the insertnonfull method to match with slide algorithm
			//TreeObject obj = new TreeObject(key);//note - A BTreeNode stores one or more TreeObjects, a BTreeNode may have one or more child BTreeNodes.
			//Allocate  memory for new root
			BTreeNode<TreeObject> s = new BTreeNode<>();
			//New root isn't a leaf
			s.setIsLeaf(false);
			//Set the new root size zero for now
			s.setSize(0);
			//Set the previous root as the children of the new root
			s.addChild(root);
			//Split the old root and move one key to the new root
			splitChild(s, 1, root);
			InsertNonFull(s,key);
		}else {
			InsertNonFull(root,key);
		}
	}
	
	/**
	 * 
	 */
	public void delete(BTreeNode<T> x,T key) {
		//TODO
		if(x.contains(key)) {
			RemoveKey(x,key);
			if(!x.getIsleaf()) {//Initial node
				BTreeNode<T> y = findPredecessor(x);
				BTreeNode<T> z = findSuccessor(x);
				if(y.getSize() > minDeg - 1) {
					T tempKey = y.maxKey();
					MoveKey(tempKey,y,x);
				}else if( z.getSize() > minDeg-1) {
					T tempKey = x.maxKey();
					MoveKey(tempKey,z,x);
				}else {
					mergeTwoNodes(y,z);
					RemoveChild(x,z);
					if(x.getIsRoot() && x.getSize() == 0) {
						MakeRoot(y);
					}
				}
			}
		}else {//key not found
			BTreeNode<T> c = FindChild(x,key);
			if(c.getSize() == minDeg-1) {
				BTreeNode<T> b = getLeftSibling(c);
				BTreeNode<T> d = getRightSibling(c);
				if(b.getSize() > minDeg - 1 ) {
					T parentKey = getChildKey(x,b,c);
					MoveKey(parentKey,x,c);
					T tempKey = b.maxKey();
					MoveKey(tempKey,b,x);
				}else if(d.getSize() > minDeg - 1) {
					T parentKey = getChildKey(x,c,d);
					MoveKey(parentKey,x,c);
					T tempKey = d.minKey();
					MoveKey(tempKey,d,x);
					
				}else {
					mergeTwoNodes(c,d);
					RemoveChild(x,d);
					if(x.getIsRoot() && x.getSize() == 0) {
						MakeRoot(c);
					}
				}
			}
			delete(c,key);
		}
	}
	
	private BTreeNode<T> getLeftSibling(BTreeNode<T> x) {
		return null;
	}
	private BTreeNode<T> getRightSibling(BTreeNode<T> x) {
		return null;
	}

	/**
	 * actual recursive algo - see slide 23
	 */
	//private BTreeNode<T> BTreeSearch(x,k)//look up param types when implementing, didn't list them in the document.
	//{
	//	return null;//TODO
	//}
	
	/**
	 * equivalent to Disk-Read in the slides. // Dan
	 * @param offset
	 * @return
	 */
	private long readAtOffset(int offset)
	{
		return 0;//TODO
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	// A function to get predecessor of node
	
	private BTreeNode<T> findPredecessor(BTreeNode<T> node){
		 // Keep moving to the right most node until we reach a leaf 
		while(!node.getIsleaf()) {
			node = node.getChild(node.getSize());
		}
		// Return the last key of the leaf 
		return node;
	}
	
	/**
	 * 
	 * @return
	 */
	private BTreeNode<T> findSuccessor(BTreeNode<T> node){
		 // Keep moving the left most node starting from node until we reach a leaf
		while(!node.getIsleaf()) {
			node = node.getChild(0);
		}
		// Return the first key of the leaf 
		return node;
	}
	
	/**
	 * 
	 * @param node
	 */
	private void promoteNode(BTreeNode<T> node){
		//TODO
	}
	
	/**
	 * 
	 * @param parent
	 * @param child1
	 * @param child2
	 */
	private void mergeTwoNodes(BTreeNode<T> parent, BTreeNode<T> child1, BTreeNode<T> child2)
	{
		//TODO
	}
	
	/**
	 * 
	 * @param node1
	 * @param node2
	 */
	private void mergeTwoNodes(BTreeNode<T> node1, BTreeNode<T> node2)
	{
		//TODO
	}
	
	/**
	 * Dan - this might interface with a method in GeneBankSearch for actual disk access or might totally be relocated there.
	 * @param offset
	 */
	private long diskRead(int offset)
	{
		return 0;//TODO
	}
	
	/**
	 * 
	 * @param root
	 * @param key
	 * @return
	 */
	private BTreeNode<T> InsertSearch(BTreeNode<T> root, T key)
	{
		return null;//TODO
	}

	/**
	 * 
	 * @param x
	 * @param key
	 */
	private void InsertNonFull(BTreeNode<TreeObject> x, long key){
		// Initialize index as index of rightmost element 
		int i = x.getSize()-1;
		// If this is a leaf node 
		if(x.getIsleaf()) {
			// The following loop does two things 
	        // a) Finds the location of new key to be inserted 
	        // b) Moves all greater keys to one place ahead 
			while(i >= 0 && x.getKey(i).getData() > key) {
				x.addKey(x.getKey(i),i+1);
				i--;
			}
			// Insert the new key at found location 
			TreeObject nTreeObject = new TreeObject(key);
			x.addKey(nTreeObject,i+1);
			x.setSize(x.getSize()+1);
		}else {// If this node is not leaf 
			 // Find the child which is going to have the new key 
			while(i>=0 && x.getKey(i).getData() > key) {
				i--;
			}
			 // See if the found child is full 
			if(x.getChild(i+1).getSize() == maxDeg -1) {
				// If the child is full, then split it 
				splitChild(x,i+1,x.getChild(i+1));
				// After split, the middle key of x.child(i) goes up and splitted into two.  See which of the two is going to have the new key 
				if(x.getKey(i+1).getData() < key) {
					i++;
				}
			}
			InsertNonFull(x.getChild(i),key);
		}
	}
	
	/**
	 * see slides page 46
	 * @param x
	 * @param i
	 * @param y
	 */
	public void splitChild(BTreeNode<TreeObject> x, int i, BTreeNode<TreeObject> y) {
		BTreeNode<TreeObject> z = new BTreeNode<>();
		z.setIsLeaf(y.getIsleaf());
		z.setSize(minDeg-1);
		 // Copy the last (t-1) keys of y to z
		for(int j = 0; j < minDeg-1; j++) {
			z.addKey(y.getKey(j+minDeg));
		}
		// Copy the last t children of y to z 
		if(!y.getIsleaf()) {
			for(int j = 0; j<minDeg;j++) {
				z.addChild(y.getChild(j+minDeg));
			}
		}
		// Reduce the number of keys in y 
		y.setSize(minDeg-1);
		 // Since this node is going to have a new child, 
	    // create space of new child 
		for(int j = x.getSize(); j>=i+1;j--) {
			x.addChild(x.getChild(j),j+1);
		}
		// Link the new child to this node 
		x.addChild(z,i+1);
		// A key of y will move to this node. Find location of 
	    // new key and move all greater keys one space ahead
		for(int j = x.getSize()-1;j>=i;j--) {
			x.addKey(x.getKey(j), j+1);
		}
		x.addKey(y.getKey(minDeg-1), i);
		x.setSize(x.getSize()+1);
    }
	
	/**
	 * 
	 * @param x
	 * @param k
	 */
	private void RemoveKey(BTreeNode<T> x, T k)
	{
		//TODO
	}
	
	/**
	 * 
	 * @param x
	 * @param d
	 */
	private void RemoveChild(BTreeNode<T> x, BTreeNode<T> d)
	{
		//TODO
	}
	
	/**
	 * 
	 * @param tempKey
	 * @param y
	 */
	private void MoveKey(T tempKey, BTreeNode<T> y, BTreeNode<T> x)
	{
		//TODO
	}
	
	/**
	 * 
	 * @param newRoot
	 */
	private void MakeRoot(BTreeNode<T> newRoot)
	{
		//TODO
	}
	
	/**
	 * 
	 * @param root
	 * @param key
	 * @return
	 */
	private BTreeNode<T> FindChild(BTreeNode<T> root,T key)
	{
		return null;//TODO
	}

}
