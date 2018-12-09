import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

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
	private int itemsWritten = 0;
	private int nodeSize = 81;//8*maxDegree + 9*(maxDegree+1) + 4;//8 bytes per key, 9 bytes per possible link. +4 alignment sanity check for testing
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
			System.out.println("Total items"+countItems(root, 0));
			printTree(root,0);
			return;
		}
		/*
		if(verbose)//debug output
		{
			System.out.println(spot.toString());		
		}
		*/

		//going to need to shift right.
		
		shiftElementsRight(spot,offset);
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
		shiftElementsLeft(spot);
		System.out.println("Total items"+countItems(root, 0));
		checkForSplit(spot);
		printTree(root,0);
		int debug = 0;
	}
	
	private void checkForSplit(BTreeNode<T> spot)
	{
		if(spot.getSize()==maxDegree)
		{
			int oldTreeCount = countSubtrees(this.root,0);
			if(verbose)
			{
				//System.out.println("Need to split node.\n"+spot.toString());					
			}
			splitNode(spot);
			if(verbose)
			{
				//System.out.println("Split.\n"+spot.toString());					
			}
			int newTreeCount = countSubtrees(this.root,0);
			System.out.println(""+newTreeCount+" "+oldTreeCount);
			if(newTreeCount<=oldTreeCount)
				System.err.println("Split isn't working correctly, didn't add a new tree.");
		}
	}
	
	private void shiftElementsRight(BTreeNode<T> node, int offset)
	{
		
		boolean done = false;
		
		//debug code
		int subtreeCount = 0;
		int valueCount = 0;
		int keyCount = 0;
		for(int i = 0; i < maxDegree; i++)
		{
			if(node.getKeyAtIndex(i)!=0l&&node.getKeyAtIndex(i)!=-1l)
				keyCount++;
			if(node.getValueAtIndex(i)!=null)
				valueCount++;
		}
		for(int i = 0; i <= maxDegree; i++)
		{
			if(node.getSubTreeAtIndex(i)!=null)
				subtreeCount++;
		}
		
		//iterative shift to make sure we don't overwrite anything.
		while(!done)
		for(int i = maxDegree-2; i >= offset&&!done; i--)
		{
			if(node.getValueAtIndex(offset)==null)
			{
				done=true;
				break;
			}
			if(node.getValueAtIndex(i+1)==null)
			{
				node.setKeyAtIndex(node.getKeyAtIndex(i), i+1);
				node.setValueAtIndex(node.getValueAtIndex(i), i+1);				
				node.setKeyAtIndex(-1l, i);
				node.setValueAtIndex(null, i);
				
				if(i+2<=maxDegree+1&&node.getSubTreeAtIndex(i+2)!=null&&node.getSubTreeAtIndex(i+1)!=null)
					System.err.println("We can't move a node, so it is going to be out of order.");//can't do anything in this case.
				if(i+2<=maxDegree+1&&node.getSubTreeAtIndex(i+2)==null&&node.getSubTreeAtIndex(i+1)!=null)
				{
					node.setSubTreeAtIndex(node.getSubTreeAtIndex(i+1), i+2);
					node.setSubTreeAtIndex(null, i+1);
				}
				if(node.getSubTreeAtIndex(i+1)==null&&node.getSubTreeAtIndex(i)!=null)
				{
					node.setSubTreeAtIndex(node.getSubTreeAtIndex(i), i+1);
					node.setSubTreeAtIndex(null, i);
				}
			}
		}
		
		//debug code
		int newsubtreeCount = 0;
		int newvalueCount = 0;
		int newkeyCount = 0;
		for(int i = 0; i < maxDegree; i++)
		{
			if(node.getKeyAtIndex(i)!=0l&&node.getKeyAtIndex(i)!=-1l)
				newkeyCount++;
			if(node.getValueAtIndex(i)!=null)
				newvalueCount++;
		}
		for(int i = 0; i <= maxDegree; i++)
		{
			if(node.getSubTreeAtIndex(i)!=null)
				newsubtreeCount++;
		}
		System.out.println(""+keyCount+" "+newkeyCount);
		System.out.println(""+valueCount+" "+newvalueCount);
		System.out.println(""+subtreeCount+" "+newsubtreeCount);
		if(keyCount!=newkeyCount||valueCount!=newvalueCount||subtreeCount!=newsubtreeCount)
			System.err.println("shiftElementsLeft is destroying data.");
		
	}
	
	private void shiftElementsLeft(BTreeNode<T> node)
	{
		if(node.getSize()==maxDegree)
			return;//this won't work if it is full
		boolean done = false;
		
		
		//debug code
		int subtreeCount = 0;
		int valueCount = 0;
		int keyCount = 0;
		for(int i = 0; i < maxDegree; i++)
		{
			if(node.getKeyAtIndex(i)!=0l&&node.getKeyAtIndex(i)!=-1l)
				keyCount++;
			if(node.getValueAtIndex(i)!=null)
				valueCount++;
		}
		for(int i = 0; i <= maxDegree; i++)
		{
			if(node.getSubTreeAtIndex(i)!=null)
				subtreeCount++;
		}
		
		//iterative shift to make sure we don't overwrite anything.
		while(!done)
		for(int i = maxDegree-1; i>0 &&!done; i--)
		{			
			if(node.getValueAtIndex(i-1)==null)
			{
				//if(node.getSubTreeAtIndex(i)!=null)
				//	System.err.println("We are overwriting stuff.");
				node.setKeyAtIndex(node.getKeyAtIndex(i), i-1);
				node.setValueAtIndex(node.getValueAtIndex(i), i-1);				
				node.setKeyAtIndex(-1l, i);
				node.setValueAtIndex(null, i);
				
				if(node.getSubTreeAtIndex(i-1)!=null&&node.getSubTreeAtIndex(i)!=null)
					System.err.println("We can't move a node, so it is going to be out of order.");//can't do anything in this case.
				if(node.getSubTreeAtIndex(i-1)==null&&node.getSubTreeAtIndex(i)!=null)
				{
					node.setSubTreeAtIndex(node.getSubTreeAtIndex(i), i-1);
					node.setSubTreeAtIndex(null, i);
				}
				if(node.getSubTreeAtIndex(i)==null&&node.getSubTreeAtIndex(i+1)!=null)
				{
					node.setSubTreeAtIndex(node.getSubTreeAtIndex(i+1), i);
					node.setSubTreeAtIndex(null, i+1);
				}
			}
			if(node.getValueAtIndex(maxDegree-1)==null)
			{
				done=true;
				break;
			}
		}
		
		//debug code
		int newsubtreeCount = 0;
		int newvalueCount = 0;
		int newkeyCount = 0;
		for(int i = 0; i < maxDegree; i++)
		{
			if(node.getKeyAtIndex(i)!=0l&&node.getKeyAtIndex(i)!=-1l)
				newkeyCount++;
			if(node.getValueAtIndex(i)!=null)
				newvalueCount++;
		}
		for(int i = 0; i <= maxDegree; i++)
		{
			if(node.getSubTreeAtIndex(i)!=null)
				newsubtreeCount++;
		}
		System.out.println(""+keyCount+" "+newkeyCount);
		System.out.println(""+valueCount+" "+newvalueCount);
		System.out.println(""+subtreeCount+" "+newsubtreeCount);
		if(keyCount!=newkeyCount||valueCount!=newvalueCount||subtreeCount!=newsubtreeCount)
			System.err.println("shiftElementsLeft is destroying data.");
		
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
		//debug code
		int oldTreeCount = countSubtrees(this.root,0);
		if(oldTreeCount==9)//an error is happening here, somewhere in this method.
		{
			int debug = 0;//breakpoint
		}
		
		
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
			System.out.println("New Node: "+newNode);
			System.out.println("New Node: "+node);
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
			System.out.println("New Node: "+newNode);
			System.out.println("New Node: "+node);
			for(int i = minDegree+1; i <= maxDegree; i++)
			{
				newNode.setSubTreeAtIndex(node.getSubTreeAtIndex(i), i-minDegree-1);
				node.setSubTreeAtIndex(null, i);
			}
			newRoot.setIsRoot(true);
			
			newRoot.setIsLeaf(false);
			node.setIsRoot(false);
			if(node.getIsleaf())
			{
				newNode.setIsLeaf(true);
			}
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
			
			//debug code
			int newTreeCount = countSubtrees(this.root,0);
			System.out.println(""+newTreeCount+" "+oldTreeCount);
			if(newTreeCount<=oldTreeCount)
				System.err.println("Split failing at root.");
			
			shiftElementsLeft(newRoot);
			shiftElementsLeft(node);
			shiftElementsLeft(newNode);
			
			//debug code
			newTreeCount = countSubtrees(this.root,0);
			System.out.println(""+newTreeCount+" "+oldTreeCount);
			if(newTreeCount<=oldTreeCount)
				System.err.println("Split failing at root after shiftelementsleft.");
			return;
		}
		BTreeNode<T> newNode = new BTreeNode<T>(maxDegree);
		if(node.getIsleaf()&&verbose)
		{
				System.out.println("Splitnode splitting leaf");
				newNode.setIsLeaf(true);
		}
		if(!node.getIsleaf()&&verbose)
			System.out.println("Splitnode splitting non-root non-leaf node");
		
		
		long midKey = node.getKeyAtIndex(minDegree);
		int offset = 0;
		BTreeNode<T> parent = node.getParentNode();
		shiftElementsLeft(parent);
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
			//node.setSubTreeAtIndex(null, minDegree);//make sure this is right.
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
			System.out.println("New Node: "+newNode);
			System.out.println("New Node: "+node);
			for(int i = minDegree+1; i <= maxDegree; i++)
			{
				newNode.setSubTreeAtIndex(node.getSubTreeAtIndex(i), i-minDegree-1);
				node.setSubTreeAtIndex(null, i);
			}
			
			//debug code
			int newTreeCount = countSubtrees(this.root,0);
			System.out.println(""+newTreeCount+" "+oldTreeCount);
			if(newTreeCount<=oldTreeCount)
				System.err.println("Split failing non-root with no shift.");
			
			
			shiftElementsLeft(node);
			shiftElementsLeft(newNode);	
			shiftElementsLeft(parent);
			
			//debug code
			newTreeCount = countSubtrees(this.root,0);
			System.out.println(""+newTreeCount+" "+oldTreeCount);
			if(newTreeCount<=oldTreeCount)
				System.err.println("Split failing non-root with no shift after shiftelelemtsleft.");
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
			shiftElementsRight(parent,offset);
			if(verbose)
				System.out.println("\nP"+parent);
			if(verbose)
				System.out.println(node);
			parent.setKeyAtIndex(midKey, offset);
			parent.setValueAtIndex(node.getValueAtIndex(minDegree), offset);
			if(parent.getSubTreeAtIndex(offset)!=null)
				System.out.println("Parent doesn't have open position for new node.");
			parent.setSubTreeAtIndex(newNode, offset);
			newNode.setParentNode(parent);
			newNode.setParentIndex(offset);
			node.setKeyAtIndex(-1l, minDegree);
			node.setValueAtIndex(null, minDegree);
			//node.setSubTreeAtIndex(null, minDegree);//TODO make sure this is right
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
				if(newNode.getSubTreeAtIndex(i-minDegree-1)!=null)
					System.err.println("Node Overwritten.");
				newNode.setSubTreeAtIndex(node.getSubTreeAtIndex(i), i-minDegree-1);
				node.setSubTreeAtIndex(null, i);
			}
			
			//debug code
			int newTreeCount = countSubtrees(this.root,0);
			System.out.println(""+newTreeCount+" "+oldTreeCount);
			if(newTreeCount<=oldTreeCount)
				System.err.println("Split failing at non-root with shift.");
			shiftElementsLeft(node);
			shiftElementsLeft(newNode);	
			shiftElementsLeft(parent);
			
			//debug code
			newTreeCount = countSubtrees(this.root,0);
			System.out.println(""+newTreeCount+" "+oldTreeCount);
			if(newTreeCount<=oldTreeCount)
				System.err.println("Split failing non-root with shift after shiftelelemtsleft.");
			
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
		itemsWritten=0;
		RandomAccessFile bTreeFile = new RandomAccessFile(new File(path),"rw");
		//FileOutputStream file = new FileOutputStream(path);
		//BufferedOutputStream bos = new BufferedOutputStream(file);
		//DataOutputStream dos = new DataOutputStream(bos);
		//dos.writeInt(maxDegree);
		//dos.writeInt(seql);
		bTreeFile.writeInt(maxDegree);
		bTreeFile.writeInt(seql);
		bTreeFile.writeLong(16+this.root.getUID()*nodeSize);
		//writeTreeToDisk(dos, this.root, 8);
		writeTreeToDisk(bTreeFile, this.root, 16);
		//dos.flush();
		//dos.close();
	}
	
	public int countItems(BTreeNode<T> n, int count)
	{
		for(int i = 0; i < maxDegree; i++)
		{
			if(n.getValueAtIndex(i)!=null)
				count++;
		}
		for(int i = 0; i <= maxDegree; i++)
		{
			if(n.getSubTreeAtIndex(i)!=null)
				count+=countItems(n.getSubTreeAtIndex(i),count);
		}
		return count;
	}
	
	public int countSubtrees(BTreeNode<T> n, int count)
	{
		for(int i = 0; i <= maxDegree; i++)
		{
			if(n.getSubTreeAtIndex(i)!=null)
			{
				count++;
				count+=countSubtrees(n.getSubTreeAtIndex(i),0);
			}
		}
		return count;
	}
	
	public void printTree(BTreeNode<T> n, int count)
	{
		System.out.println("\nUID: "+n.getUID()+" Level: "+count+" Size: "+n.getSize()+" isRoot: "+n.getIsRoot()+" isLeaf: "+n.getIsleaf()+" Parent: "+(n.getParentNode()!=null?n.getParentNode().getUID():"none"));
		for(int i = 0; i < maxDegree; i++)
		{
			System.out.print(n.getValueAtIndex(i)!=null?n.getValueAtIndex(i)+" ":". ");
		}
		System.out.println();
		for(int i = 0; i <= maxDegree; i++)
		{
			System.out.print(n.getSubTreeAtIndex(i)!=null?n.getSubTreeAtIndex(i).getUID():". ");
		}

		for(int i = 0; i <= maxDegree; i++)
		{
			if(n.getSubTreeAtIndex(i)!=null)
				printTree(n.getSubTreeAtIndex(i),count+1);
		}
		return;
	}
	
	public void writeTreeToDisk(RandomAccessFile dos, BTreeNode<T> node, long offset) throws IOException
	{		
		int size = nodeSize;
		dos.seek(offset+size*node.getUID());
		if(node!=null)
		{			
			for(int i = 0; i < maxDegree; i++)
			{
				dos.writeLong(node.getKeyAtIndex(i));//writes 8 bytes, keys and values are the same for this system.
				if(verbose&&node.getValueAtIndex(i)!=null)
				{
					System.out.println(TreeObject.decode(node.getKeyAtIndex(i))+" written at :"+(offset+size*node.getUID())+". "+ ++itemsWritten+" total");
				}
			}
			for(int i = 0; i <= maxDegree; i++)
			{
				if(node.getSubTreeAtIndex(i)!=null)
				{
					dos.writeBoolean(true);//writes one byte, true if it has a child here.
					dos.writeLong(offset+size*node.getSubTreeAtIndex(i).getUID());//the offset of our next child node object.
				}
				else
				{
					dos.writeBoolean(false);
					dos.writeLong(-1);
				}
			}
			dos.writeInt(256);
			System.out.println(dos.getFilePointer());
			if(node.getIsRoot())
				if(verbose)
					System.out.println("***Root written at "+(offset+size*node.getUID())+"***");
			
			
			
			for(int i = 0; i <= maxDegree; i++)
			{
				if(node.getSubTreeAtIndex(i)!=null)
					writeTreeToDisk(dos,node.getSubTreeAtIndex(i),offset);
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
