import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//import BTree.BTreeNode;//Dan - this is implied if they are both in the default package, otherwise lets move everything into a btree package.

/**
 * 
 * 
 *
 * @param <T>
 */
public class BTree<T> {
	private BTreeNode<T> root = null;
	private int minDegree;
	private int maxDegree;
	private boolean verbose = false;
	
	public BTree(int degree){
		maxDegree = degree;
		minDegree = degree/2;
		create();
	}
	
	public BTree(int degree,boolean verb){
		this(degree);
		verbose = verb;
	}
	
	public void create(){
		if(verbose)System.out.println("Create called.");
		BTreeNode<T> x = new BTreeNode<T>(maxDegree);
		x.setIsRoot(true);
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
	/*
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
	}*/
	
	/**
	 * calls InsertSearch, then runs the insert algo on slides
	 * @param root
	 * @param key
	 */
	public void insert(T obj, long key) {
		BTreeNode<T> spot = InsertSearch(this.root,key);
		//figure out if we need to shift.
		checkForSplit(spot);
		int offset = 0;
		while(offset<maxDegree&&spot.getValueAtIndex(offset)!=null&&spot.getKeyAtIndex(offset)<key)
		{
			offset++;
		}
		if(spot.getValueAtIndex(offset)==null)//it is empty, no need to shift.
		{
			if(verbose)
				System.out.println("Inserting " + key + " without shift. New size: "+(spot.getSize()+1));
			spot.setKeyAtIndex(key, offset);
			spot.setValueAtIndex(obj, offset);
			spot.setSize(spot.getSize()+1);
			checkForSplit(spot);
			return;
		}
		/*
		if(verbose)//debug output
		{
			System.out.println(spot.toString());		
		}
		*/

		//going to need to shift right.
		for(int i = maxDegree-1; i > offset; i--)
		{
			if(spot.getValueAtIndex(i-1)!=null)
			{
				spot.setKeyAtIndex(spot.getKeyAtIndex(i-1), i);
				spot.setValueAtIndex(spot.getValueAtIndex(i-1), i);
				spot.setSubTreeAtIndex(spot.getSubTreeAtIndex(i), i+1);
				spot.setKeyAtIndex(-1l, i-1);
				spot.setValueAtIndex(null, i-1);
				spot.setSubTreeAtIndex(null, i);
			}
		}
		spot.setKeyAtIndex(-1l, offset);
		spot.setValueAtIndex(null, offset);
		spot.setSubTreeAtIndex(null, offset);
		/*
		if(verbose)//debug output
		{
			System.out.println(spot.toString());		
		}
		*/
		if(verbose)
			System.out.println("Inserting " + key + " after shifting.");
		
		spot.setKeyAtIndex(key, offset);
		spot.setValueAtIndex(obj, offset);
		spot.setSize(spot.getSize()+1);
		checkForSplit(spot);
	}
	
	private void checkForSplit(BTreeNode<T> spot)
	{
		if(spot.getSize()==maxDegree)
		{
			if(verbose)
			{
				System.out.println("Need to split node.\n"+spot.toString());					
			}
			splitNode(spot);
			if(verbose)
			{
				System.out.println("Split.\n"+spot.toString());					
			}
		}
	}
	
	/**
	 * 
	 * @param root
	 * @param key
	 * @return
	 */
	private BTreeNode<T> InsertSearch(BTreeNode<T> start, long key)
	{
		if(start.getIsleaf())
		{	
			int offset = 0;
			while(offset<maxDegree-1&&start.getValueAtIndex(offset)!=null&&start.getKeyAtIndex(offset)<key)
			{
				offset++;
			}
			//TODO check if this is needed.
			return start;
		}
		//not leaf
		int offset = 0;
		while(offset<maxDegree&&start.getValueAtIndex(offset)!=null&&start.getKeyAtIndex(offset)<key)
		{
			offset++;
		}
		if(start.getSubTreeAtIndex(offset)!=null)
			return InsertSearch(start.getSubTreeAtIndex(offset), key);
		return start;
	}
	
	public void splitNode(BTreeNode<T> node)
	{
		
		if(node.getIsRoot())
		{
			if(verbose)
				System.out.println("Splitnode splitting root");
			BTreeNode<T> newRoot = new BTreeNode<T>(maxDegree);
			BTreeNode<T> newNode = new BTreeNode<T>(maxDegree);
			long midKey = node.getKeyAtIndex(minDegree);
			newRoot.setKeyAtIndex(midKey, 0);
			newRoot.setValueAtIndex(node.getValueAtIndex(minDegree), 0);
			newRoot.setSubTreeAtIndex(node, 0);
			newRoot.setSubTreeAtIndex(newNode, 1);
			newNode.setParentNode(newRoot);
			newNode.setParentIndex(1);
			node.setParentNode(newRoot);
			node.setParentIndex(0);
			node.setKeyAtIndex(-1l, minDegree);
			node.setValueAtIndex(null, minDegree);
			node.setSize(node.getSize()-1);
			newRoot.setSize(1);
			for(int i = minDegree+1; i < maxDegree; i++)
			{
				newNode.setKeyAtIndex(node.getKeyAtIndex(i),i-minDegree-1);
				newNode.setValueAtIndex(node.getValueAtIndex(i), i-minDegree-1);
				if(node.getValueAtIndex(i)!=null)
				{
					node.setKeyAtIndex(-1l, i);
					node.setValueAtIndex(null, i);
					newNode.setSize(newNode.getSize()+1);
					node.setSize(node.getSize()-1);
				}
			}
			for(int i = minDegree+1; i <= maxDegree; i++)
			{
				newNode.setSubTreeAtIndex(node.getSubTreeAtIndex(i), i-minDegree);
				node.setSubTreeAtIndex(null, i);
			}
			newRoot.setIsRoot(true);
			newRoot.setIsLeaf(false);
			node.setIsRoot(false);
			newNode.setIsRoot(false);
			this.root = newRoot;
			if(verbose)
			{
				System.out.println("New Root: "+newRoot);
				System.out.println("Old Node: "+node);
				System.out.println("New Node: "+newNode);
			}
			if(checkNodeSizing(newRoot))
				System.err.println("Node size/element mismatch");
			if(checkNodeSizing(node))
				System.err.println("Node size/element mismatch");
			if(checkNodeSizing(newNode))
				System.err.println("Node size/element mismatch");

			return;
		}
		if(node.getIsleaf()&&verbose)
				System.out.println("Splitnode splitting leaf");
		if(!node.getIsleaf()&&verbose)
			System.out.println("Splitnode splitting non-root non-leaf node");
		
		BTreeNode<T> newNode = new BTreeNode<T>(maxDegree);
		long midKey = node.getKeyAtIndex(minDegree);
		int offset = 0;
		BTreeNode<T> parent = node.getParentNode();
		while(offset<maxDegree&&parent.getValueAtIndex(offset)!=null&&parent.getKeyAtIndex(offset)<midKey)
		{
			offset++;
		}
		if(parent.getValueAtIndex(offset)==null)//it is empty, no need to shift.
		{
			if(verbose)
				System.out.println("Promoting " + midKey + " without shift. New size: "+(parent.getSize()+1));
			if(verbose)
				System.out.print(parent);
			parent.setKeyAtIndex(midKey, offset);
			parent.setValueAtIndex(node.getValueAtIndex(minDegree), offset);
			parent.setSubTreeAtIndex(newNode, offset+1);
			node.setKeyAtIndex(-1l, minDegree);
			node.setValueAtIndex(null, minDegree);
			node.setSubTreeAtIndex(null, minDegree);
			newNode.setParentNode(parent);
			newNode.setParentIndex(offset);
			parent.setSize(parent.getSize()+1);
			node.setSize(node.getSize()-1);
			if(verbose)
				System.out.print(parent);
			if(checkNodeSizing(node))
				System.err.println("Node size/element mismatch");
			if(checkNodeSizing(newNode))
				System.err.println("Node size/element mismatch");
			checkForSplit(parent);
		}
		else
		{
			if(verbose)
				System.out.println("Promoting " + midKey + " after shifting.");		
			if(verbose)
				System.out.print(parent);
			if(verbose)
				System.out.print(node);
			for(int i = maxDegree-1; i > offset; i--)
			{
				if(parent.getValueAtIndex(i-1)!=null)
				{
					parent.setKeyAtIndex(parent.getKeyAtIndex(i-1), i);
					parent.setValueAtIndex(parent.getValueAtIndex(i-1), i);
					parent.setSubTreeAtIndex(parent.getSubTreeAtIndex(i), i+1);
					parent.setKeyAtIndex(-1,i-1);
					parent.setValueAtIndex(null, i-1);
					parent.setSubTreeAtIndex(null, i);
				}
			}

			
			parent.setKeyAtIndex(-1l, offset);
			parent.setValueAtIndex(null, offset);
			parent.setSubTreeAtIndex(null, offset);

			parent.setKeyAtIndex(midKey, offset);
			parent.setValueAtIndex(node.getValueAtIndex(minDegree), offset);
			node.setKeyAtIndex(-1l, minDegree);
			node.setValueAtIndex(null, minDegree);
			node.setSubTreeAtIndex(null, minDegree);
			parent.setSize(parent.getSize()+1);
			node.setSize(node.getSize()-1);
			
			if(verbose)
				System.out.println(parent);
			if(verbose)
				System.out.println(node);
			
			
			for(int i = minDegree+1; i < maxDegree; i++)
			{
				newNode.setKeyAtIndex(node.getKeyAtIndex(i),i-minDegree-1);
				newNode.setValueAtIndex(node.getValueAtIndex(i), i-minDegree-1);
				if(node.getValueAtIndex(i)!=null)
				{
					node.setKeyAtIndex(-1l, i);
					node.setValueAtIndex(null, i);
					newNode.setSize(newNode.getSize()+1);
					node.setSize(node.getSize()-1);
				}
			}
			for(int i = minDegree+1; i <= maxDegree; i++)
			{
				newNode.setSubTreeAtIndex(node.getSubTreeAtIndex(i), i-minDegree);
				node.setSubTreeAtIndex(null, i);
			}
			if(verbose)
				System.out.println("Moved elements to new node.");
			if(verbose)
				System.out.println(node);
			if(verbose)
				System.out.println(newNode);
			checkForSplit(parent);
			
		}
		
		
	}
	
	private boolean checkNodeSizing(BTreeNode<T> node)
	{
		int count = 0;
		for(int i = 0; i < maxDegree; i++)
			if(node.getValueAtIndex(i)!=null)
				count++;
		return count!=node.getSize();
	}
	
	public void writeToDisk(String path,int seql) throws IOException
	{
		FileOutputStream file = new FileOutputStream(path);
		BufferedOutputStream bos = new BufferedOutputStream(file);
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeInt(maxDegree);
		dos.writeInt(seql);
		writeTreeToDisk(dos, this.root, 8);
		dos.flush();
		dos.close();
	}
	
	public void writeTreeToDisk(DataOutputStream dos, BTreeNode<T> node, long offset) throws IOException
	{
		int size = 8*maxDegree + 9*(maxDegree+1);//8 bytes per key, 9 bytes per possible link.
		if(node!=null)
		{
			
			for(int i = 0; i < maxDegree; i++)
			{
				dos.writeLong(node.getKeyAtIndex(i));//writes 8 bytes, keys and values are the same for this system.
			}
			for(int i = 0; i < maxDegree; i++)
			{
				dos.writeBoolean(node.getValueAtIndex(i)==null?false:true);//writes one byte, true if it has a child here.
				dos.writeLong(offset + i*size);//the offset of our next child node object.
			}
			for(int i = 0; i <= maxDegree; i++)
			{
				if(!node.getIsleaf())
					writeTreeToDisk(dos,node.getSubTreeAtIndex(i),offset+size);
			}
		}
		else
		{
			for(int i = 0; i < maxDegree; i++)
			{
				dos.writeBoolean(false);
				dos.writeLong(0);
				dos.writeLong(0);
			}
		}
		
	}
	
	/**
	 * 
	 */
	/*
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
	*/
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
	/*
	private BTreeNode<T> findPredecessor(BTreeNode<T> node){
		 // Keep moving to the right most node until we reach a leaf 
		while(!node.getIsleaf()) {
			node = node.getChild(node.getSize());
		}
		// Return the last key of the leaf 
		return node;
	}
	*/
	/**
	 * 
	 * @return
	 */
	/*
	private BTreeNode<T> findSuccessor(BTreeNode<T> node){
		 // Keep moving the left most node starting from node until we reach a leaf
		while(!node.getIsleaf()) {
			node = node.getChild(0);
		}
		// Return the first key of the leaf 
		return node;
	}
	*/
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
