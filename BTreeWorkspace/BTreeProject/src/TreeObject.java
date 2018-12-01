/**
 * 
 * 
 *
 */
public class TreeObject {

	
	private long data;//the 1-31 As Cs Gs or Ts
	
	/**
	 * 
	 */
	public void setData(long d)
	{
		data = d;
	}
	
	/**
	 * 
	 */
	public long getData()
	{
		return data;
	}

	/**
	 * 
	 */
	public long encode(String s)
	{
		return -1;//TODO
	}

	/**
	 * 
	 */
	public String decode()
	{
		return "Unimplimented";//TODO
	}

	/**
	 * 
	 */
	public boolean equals(TreeObject t)
	{
		return false;//TODO
	}

	/**
	 * 
	 */
	public int compareTo(TreeObject t)
	{
		return -1;//TODO
	}

	/**
	 * 
	 */
	/*
	 * Turns out this won't work.
	public long hashCode() // pass our long data out through this
	{
		return -1;//TODO
	}
	*/
	
	/**
	 * 
	 */
	private long placeCharAtPosition(long modifiedLong, int code, int offset)
	{
		return -1;//TODO
	}

	/**
	 * gets the two bit code for ACGT, throws an exception if bad input. Case insensitive.
	 */
	private static int charToBitcode(char c)
	{
		return -1;//TODO
	}



	
	
}
