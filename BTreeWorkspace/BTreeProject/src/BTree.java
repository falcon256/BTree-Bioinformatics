/**
 * 
 * 
 *
 * @param <T>
 */
public class BTree<T> {
	private BTreeNode<T> root = null;
	
	public void create()
	{
		//TODO
	}
	
	/**
	 * recursive search - calls BTreeSearch
	 * @param root
	 * @param key
	 * @return
	 */
	public BTreeNode<T> search(BTreeNode root,T key)
	{
		return null;//TODO
	}
	
	/**
	 * calls InsertSearch, then runs the insert algo on slides
	 * @param root
	 * @param key
	 */
	public void insert(BTreeNode<T> root, T key)
	{
		//TODO
	}
	
	/**
	 * 
	 */
	public void delete()
	{
		//TODO
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
	private BTreeNode<T> findPredecessor(BTreeNode<T> node)
	{
		return null; //TODO
	}
	
	/**
	 * 
	 * @return
	 */
	private BTreeNode<T> findSuccessor(BTreeNode<T> node)
	{
		return null;//TODO
	}
	
	/**
	 * 
	 * @param node
	 */
	private void promoteNode(BTreeNode<T> node)
	{
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
	 * @param target
	 * @param key
	 */
	private void InsertNonFull(BTreeNode<T> target, T key)
	{
		//TODO
	}
	
	/**
	 * see slides page 46
	 * @param x
	 * @param i
	 * @param y
	 */
	private void SplitChild(BTreeNode<T> x, int i, BTreeNode<T> y)
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
	private BTreeNode<T> FindChild(BTreeNode root,T key)
	{
		return null;//TODO
	}

}
