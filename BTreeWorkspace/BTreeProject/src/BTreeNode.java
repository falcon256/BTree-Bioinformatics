/**
 * 
 * 
 *
 * @param <T>
 */
public class BTreeNode<T> {

	//private int maxDegree;	//max elements in node // Golam : I think we don't need this here. We can match the degree in the BTree insert method. as everything else will be handle from there like should we split the tree or not we can't do this from this class
	//private int minDegree;	//min elements in node //Golam: Same as max degree
	private BTreeNode<T> parentNode = null;
	private int parentIndex = 0;
	private boolean isRoot;
	
	// size should probably be max+1 to allow merging then promoting.
	private T [] keys = null;		// this is actually needed since the hashCode method only returns a 32 bit int, not a 64.
	private T [] values = null;		// size set at runtime based on degree, can have null elements.
	// element 0 is left of values[0]
	// element subTrees[size] is right of values[size-1]
	private BTreeNode<T>[] subTrees = null; //slots for subtrees of size = values.length + 1
	private int numValues = 0;		// current number of values in this node
	private boolean isLeaf;        	// stores if this is a leaf or not
	
	
	/**
	 *
	 */
	//Golam: I am removing the parameter for now. we need to check the degree in BTree class.
	public BTreeNode()
	{
		//TODO
		parentIndex = -1;
	}
	
	/**
	 * 
	 * @return if it is a leaf	
	 */
	public boolean getIsleaf()           
	{
		return this.isLeaf;
	}
	
	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	/**
	 * 
	 * @return if it is root	
	 */
	public boolean getIsRoot()
	{
		return isRoot;
	}
	
	/**
	 * 
	 */
	public void setIsRoot(boolean root)
	{
		isRoot = root;
	}
	
	/**
	 * 
	 * @return maximum keyed object	
	 */
	public T maxKey()
	{
		return null;//TODO
	}
	
	/**
	 * 
	 * @return minimum keyed object
	 */
	public T minKey()
	{
		return null;//TODO
	}
	
	/**
	 * 
	 * @return parent of this node
	 */
	public BTreeNode<T> getParentNode()
	{
		return this.parentNode;
	}
	
	/**
	 * 
	 * @return index of parent node
	 */
	public int getParentIndex()
	{
		return this.parentIndex;
	}
	
	/**
	 * 
	 */
	public void setParentNode(BTreeNode<T> pnt)
	{
		parentNode = pnt;
	}
	
	/**
	 * 
	 */
	public void setParentIndex(int pnti)
	{
		parentIndex = pnti;
	}
	
	/**
	 * 
	 */
	public void putObjectAtIndex(T obj)
	{
		//TODO
	}
	
	/**
	 * 
	 * @return number of elements stored in this node	
	 */
	public int getSize()
	{
		return this.numValues;
	}
	public void setSize(int size) {
		this.numValues = size;
	}
	/**
	 * 
	 * @return object at index i in values[]
	 */
	public T getObjectAtIndex(int i)
	{
		return null;//TODO
	}
	//Golam: we need a method here to add child
	public void addChild(BTreeNode<T> child) {
		
	}
	/**
	 * 
	 * @return returns the object we remove at index I. It is typical convention to always return an object you remove.
	 */
	public T removeObjectAtIndex(int i)
	{
		return null;//TODO
	}
	/**
	 * 
	 * @return if values[] contains object with key
	 */
	public boolean contains(T key)
	{
		return false;//TODO
	}
}
