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
	
	public void create()
	{
		root = new BTreeNode<T>();
	}
	
	/**
	 * recursive search - calls BTreeSearch
	 * @param root
	 * @param key
	 * @return
	 */
	public BTreeNode<T> search(BTreeNode<T> root,T key)
	{
		return null;//TODO
	}
	
	/**
	 * calls InsertSearch, then runs the insert algo on slides
	 * @param root
	 * @param key
	 */
	public void insert(BTreeNode<T> root, long key) {//Golam: changed the key genetic to long to match with tree object
		if(root.getSize() == maxDeg -1 ) {
			TreeObject obj = new TreeObject(key);//note - A BTreeNode stores one or more TreeObjects, a BTreeNode may have one or more child BTreeNodes.
			BTreeNode<T> s = new BTreeNode<T>();
			s.setIsLeaf(false);
			s.setSize(0);
			s.addChild(root);
			splitChild(s, 1, root);
			InsertNonFull(s,key);
		}else {
			InsertNonFull(root,key);
		}
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
	private void InsertNonFull(BTreeNode<T> target, long key)
	{
		//TODO
	}
	
	/**
	 * see slides page 46
	 * @param x
	 * @param i
	 * @param y
	 */
	public void splitChild(BTreeNode<T> x, int i, BTreeNode<T> y) {
		
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
