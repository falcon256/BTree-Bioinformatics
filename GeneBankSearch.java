import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * 
 * 
 *
 */
public class GeneBankSearch {
	
	private boolean setupFailed = false;
	private RandomAccessFile bTreeFile = null;
	private BufferedReader qFileReader = null;
	private int degree = 1;
	private int sequenceLength = 31;
	private int totalRead = 0;
	private boolean verbose = false;
	/**
	 * 
	 * @param Args[0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]
	 */
	public static void main(String[] args) {
		GeneBankSearch main = new GeneBankSearch(args);
		
		try {
			main.Go();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public GeneBankSearch(String[] args)
	{
		if(args.length<4)
		{
			System.err.println("Insufficient number of arguements.");
			System.out.println("Args: [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]");
			return;
		}
		try {
			bTreeFile = new RandomAccessFile(args[1], "r");
			qFileReader = new BufferedReader(new FileReader(new File(args[2])));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(bTreeFile==null)
		{
			System.err.println("Bad file path given in paremeter 1.");
			System.out.println("Args: [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]");
			setupFailed = true;
			return;
		}
		
		if(qFileReader==null)
		{
			System.err.println("Bad file path given in paremeter 2.");
			System.out.println("Args: [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]");
			setupFailed = true;
			return;
		}
		
		if(args.length>3)
		{
			verbose = Integer.parseInt(args[4])==1?true:false;
		}
		
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void Go() throws IOException
	{
		if(setupFailed)
			return;
		
		//TODO read in our metadata and read in the root of the btree.
		degree = bTreeFile.readInt();
		sequenceLength = bTreeFile.readInt();
		long startPoint = bTreeFile.readLong();
		System.out.println("Root at: "+startPoint);
		System.out.println(""+bTreeFile.readBoolean() + TreeObject.decode(bTreeFile.readLong())+" "+TreeObject.decode(bTreeFile.readLong())+" "+TreeObject.decode(bTreeFile.readLong()));
		//TODO iterate over the query and get our output data.
		boolean done = false;
		while(!done)
		{			
			String queryString = qFileReader.readLine();
			if(queryString==null)
				break;
			System.out.println(queryString);
			long queryKey = TreeObject.encode(queryString);
			int count = query(queryKey, startPoint);
			
			System.out.println(count);
			
			System.out.println(totalRead+" keys checked.");
		}
		
	}
	
	
	private int query(long key, long offset) throws IOException
	{
		bTreeFile.seek(offset);
		int count = 0;
		long[] keys = new long[degree];
		boolean[] hasSubTrees = new boolean[degree+1];
		long[] offsets = new long[degree+1];
		
		for(int i = 0; i < degree; i++)
		{
			keys[i]=bTreeFile.readLong();
			totalRead++;
			if(keys[i]==key)
				count++;
			if(verbose&&keys[i]!=0l&&keys[i]!=-1l)
				System.out.println(TreeObject.decode(keys[i]));
		}
		for(int i = 0; i <= degree; i++)
		{
			hasSubTrees[i]=bTreeFile.readBoolean();
			offsets[i]=bTreeFile.readLong();
			if(verbose)
				System.out.println(hasSubTrees[i]+" "+offsets[i]);
		}
		System.out.println("Alignment sanity check 256="+bTreeFile.readInt());
		System.out.println(bTreeFile.getFilePointer());
		int test = 0;
		while(test<degree&&keys[test]>key)
			test++;
		if(offsets[test]==offset)
		{
			System.err.println("This node is referencing itself as a child!");
			System.err.println("!!!ABORTING STACK OVERFLOW!!!");
			return count;
		}
		if(offsets[test]<=0&&hasSubTrees[test]||offsets[test+1]<=0&&hasSubTrees[test+1])
			System.err.println("A NEGATIVE offset value? That's problematic.");
		if(hasSubTrees[test])
			count+= query(key, offsets[test]);
		if(hasSubTrees[test+1])
			count+= query(key, offsets[test+1]);
		return count;
	}

}
