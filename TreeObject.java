/**
 * 
 * 
 *
 */
public class TreeObject {

	
private long data;//the 1-31 As Cs Gs or Ts
	
	/**
	 * 
	 * @param key - the key and the value of this object.
	 */
	public TreeObject(long key)
	{
		data = key;
	}

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
	public static long encode(String s)
	{
		long l = 0;
		for(int i = 0; i < s.length() && i<32; i++)
		{
			l = placeCharAtPosition(l, charToBitcode(s.charAt(i)), i);
		}
		return l;
	}

	/**
	 * 
	 */
	public static String decode(long l)
	{
		String s = "";
		for(long i = 0; i < 31; i++)
		{
			s+= bitcodeToChar((int)((l>>i*2l)&3l));//lowest two bits as we scroll through.
		}
		return s;
	}

	/**
	 * 
	 */
	public boolean equals(TreeObject t)
	{
		return (data-t.getData()==0);
	}

	/**
	 * 
	 */
	/*
	public int compareTo(TreeObject t)
	{
		return (int)(data-t.getData());//TODO - might not want to use this since compareTo uses ints.
	}
	*/
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
	private static long placeCharAtPosition(long modifiedLong, long code, long offset)
	{
		
		return modifiedLong | (code<<(offset*2l));
	}

	/**
	 * gets the two bit code for ACGT, throws an exception if bad input. Case insensitive.
	 */
	private static int charToBitcode(char c)
	{
		switch(c)
		{
		case('A'):
		case('a'):
			return 0;
		case('C'):
		case('c'):
			return 1;
		case('G'):
		case('g'):
			return 2;
		case('T'):
		case('t'):
			return 3;
		
		
		
			default:
				return -1;//TODO - what exception should we throw?
		}
		
	}
	
	private static char bitcodeToChar(int i)
	{
		switch(i)
		{
		case(0):
			return 'a';
		case(1):
			return 'c';
		case(2):
			return 'g';
		case(3):
			return 't';
		default:
			return 'F';//TODO - what exception should we throw?
		}
		
	}
	@Override
	public String toString()
	{
		return decode(data);		
	}
	
	
}
