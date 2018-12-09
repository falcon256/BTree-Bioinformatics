import java.lang.reflect.Array;

/**
 * 
 * 
 *
 * @param <T>
 */
public class BTreeNode<T> {

	private BTreeNode<T> parentNode = null;
	private int parentIndex = 0;
	private boolean isRoot;
	
	// size should probably be max+1 to allow merging then promoting.
	private long[] keys = null;
	private T[] values = null;
	private BTreeNode<T>[] subTrees = null;
	// element 0 is left of values[0]
	// element subTrees[size] is right of values[size-1]
	
	private int numValues = 0;		// current number of values in this node
	private boolean isLeaf;        	// stores if this is a leaf or not
	
	
	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public BTreeNode(int degree)
	{
		parentIndex = -1;
		keys = new long[degree];
		values = (T[]) new Object[degree];
		try {
			subTrees = (BTreeNode<T>[]) Array.newInstance(Class.forName("BTreeNode"),degree+1);
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
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
	
	public long getKeyAtIndex(int i)
	{
		return keys[i];
	}
	
	public T getValueAtIndex(int i)
	{
		return values[i];
	}
	
	public BTreeNode<T> getSubTreeAtIndex(int i)
	{
		return subTrees[i];
	}
	
	public void setKeyAtIndex(long l, int i)
	{
		keys[i] = l;
	}
	
	public void setValueAtIndex(T t, int i)
	{
		values[i] = t;
	}

	
	public void setSubTreeAtIndex(BTreeNode<T> n,int i)
	{
		subTrees[i] = n;
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
	
	//Golam: we need a method here to remove child
	public BTreeNode<T> removeChild() {
		
		return null;
	}
	
	
	/**
	 * 
	 * @return returns the object we remove at index I. It is typical convention to always return an object you remove.
	 */
	public T removeKey(int i)
	{
		return null;//TODO
	}
	/**
	 * 
	 * @return if values[] contains object with key
	 */
	public boolean contains(T key){
		return false;//TODO
	}
	
	@Override
    public String toString(){
        String s = new String();
        s += "keys: ";
        for (int i = 0; i < keys.length; i++)
        {
            s += (keys[i] + " ");
        }
        s += "\nchildren: ";
        for (int i = 0; i < values.length; i++)
        {
            s += (values[i].toString() + " ");
        }
        return s;
    }
}
