import java.util.NoSuchElementException;


/**
 * @author Daniel Lambert
 * This class simulates a level of cache in a processor. Except a processor would use a specific number of bytes depending on address bus width.
 * @param <T> - generic type.
 */
public class Cache<T> implements ICache<T>{

	
	private ListDoubleLinked<T> internalList = null;
	private int capacity = 100;
	private int accesses = 0;
	private int hits = 0;
	
	/**
	 * Default constructor. Makes a Cache of size 100.
	 */
	public Cache()
	{
		internalList = new ListDoubleLinked<T>();		
	}
	
	/**
	 * @param cap - the capacity of the cache.
	 */
	public Cache(int cap)
	{
		this();
		if(cap>=0)
			capacity = cap;	
	}
	
	//See ICache for use.
	@Override
	public T get(T target) {
		accesses++;
		if(internalList.contains(target))
		{
			hits++;			
			moveToFront(target);
			return target;
		}
		else
		{
			add(target);
		}
		return null;
	}

	//See ICache for use.
	@Override
	public void clear() {
		internalList.clear();
	}

	//See ICache for use.
	@Override
	public void add(T data) {
		if(internalList.contains(data))
			moveToFront(data);
		else
			internalList.add(data);
		
		if(internalList.size()>capacity)
		{
			internalList.removeLast();
		}		
	}

	//See ICache for use.
	@Override
	public void removeLast() {
		if(internalList.size()>0)
			internalList.removeLast();
		else
			throw new IllegalStateException();		
	}

	//See ICache for use.
	@Override
	public void remove(T target) {
		if(!internalList.contains(target))
			throw new NoSuchElementException();
		internalList.remove(target);		
	}

	//See ICache for use.
	@Override
	public void write(T data) {
		if(internalList.contains(data))
			moveToFront(data);
		else
			throw new NoSuchElementException();
		
	}

	//See ICache for use.
	@Override
	public double getHitRate() {
		if(accesses>0)
			return hits/(double)accesses;
		return 0;
	}

	//See ICache for use.
	@Override
	public double getMissRate() {
		if(accesses>0)
			return 1.0-(hits/(double)accesses);
		return 1.0;
	}

	/**
	 * @return - returns the number of accesses this cache has experienced.
	 */
	public int getNumAccesses()
	{
		return accesses;
	}
	
	/**
	 * @return - returns the number of hits this cache has experienced.
	 */
	public int getNumHits()
	{
		return hits;
	}

	//See ICache for use.
	@Override
	public boolean isEmpty() {
		return internalList.isEmpty();
	}

	/**
	 * @param element - element to move to the front of our cache data structure.
	 */
	private void moveToFront(T element)
	{
		internalList.remove(element);
		internalList.add(element);
	}
	

/**
 * @author Daniel Lambert
 * Acts as our data storage system for our Cache objects in this project.
 * Normally I wouldn't nest a class like this, but the p1 description prevented making it external.
 * @param <T> - generic type
 */
	private class ListDoubleLinked<T> {
	
		private DLLNode<T> head = null;
		private DLLNode<T> tail = null;
		private int count = 0;
		
		/**
		 * @param element - the element to be added to the front of the list.
		 */
		public void add(T element)
		{
			if(element==null)
				return;
			DLLNode<T> newNode = new DLLNode<T>(element);
			//if the list is empty
			if(head==null)
			{
				head=tail=newNode;
			}
			else//not empty
			{
				head.setPrevious(newNode);
				newNode.setNext(head);
				head = newNode;
			}
			count++;
		}
		
		/**
		 * @param element - element to add to the end of the list.
		 */
		public void addLast(T element)
		{
			if(element==null)
				return;
			DLLNode<T> newNode = new DLLNode<T>(element);
			//if the list is empty
			if(tail==null)
			{
				tail=head=newNode;
			}
			else//not empty
			{
				tail.setNext(newNode);
				newNode.setPrevious(tail);
				tail = newNode;
			}
			count++;		
		}
		
		/**
		 * @return - removes and returns the first item in the list.
		 */
		public T removeFirst()
		{
			if(head==null)
				return null;
			DLLNode<T> oldHead = head;
			head = head.getNext();
			count--;
			
			//clean up if we just became empty
			if(head==null)
			{
				tail = null;
				count = 0;
			}
			else
			{
				head.setPrevious(null);
			}
			
			
			return (T)oldHead.getElement();
		}
		
		/**
		 * @return - removes and returns the last element in the list.
		 */
		public T removeLast()
		{
			if(tail==null)
				throw new IllegalStateException();
			DLLNode<T> oldTail = tail;
			tail = tail.getPrevious();
			count--;
			
			//empty clean up
			if(tail==null)
			{
				head = null;
				count = 0;
			}
			else
			{
				tail.setNext(null);
			}
			
			return (T)oldTail.getElement();
		}
		
		/**
		 * @return - is the list empty 
		 */
		public boolean isEmpty()
		{
			if(head==null)
				return true;
			return false;
		}
		
		/**
		 * @param element - the generic object to be compared via .equals to all other objects in the list.
		 * @return - does the element exist in the list or not.
		 */
		public boolean contains(T element)
		{
			DLLNode<T> current = head;
			while(current!=null)
			{
				if(current.getElement().equals(element))
					return true;
				current = current.getNext();
			}
			return false;
		}
		
		/**
		 * @param element - element to remove. Uses .equals to test and removes the first instance found.
		 * @return - returns the remove element.
		 */
		public T remove(T element)
		{
			if(head==null||element==null)
				throw new NoSuchElementException();
			DLLNode<T> current = head;
			while(current!=null)
			{
				if(current.getElement().equals(element))
				{
					if(current==head)
						return removeFirst();
					else if(current==tail)
						return removeLast();
					else// not front or back, so lets take it out of the middle
					{
						DLLNode<T> prev = current.getPrevious();
						DLLNode<T> next = current.getNext();
						prev.setNext(next);
						next.setPrevious(prev);
						count--;
						return (T)current.getElement();
					}
				}
				current = current.getNext();
			}
			throw new NoSuchElementException();
		}
		
		/**
		 * Annihilates all contents of the list and sets the size to zero.
		 */
		public void clear()
		{
			//Chop the head and let the garbage collector do the rest.
			head=tail=null;
			count=0;
		}
		
		/**
		 * @return - returns the number of elements in the list.
		 */
		public int size()
		{
			return count;
		}
	}
	
	
	
}
