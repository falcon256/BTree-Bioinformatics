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
			System.err.println("Args: [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]");
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
			System.err.println("Args: [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]");
			setupFailed = true;
			return;
		}
		
		if(qFileReader==null)
		{
			System.err.println("Bad file path given in paremeter 2.");
			System.err.println("Args: [0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]");
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
		if(verbose)
			System.err.println("Root at: "+startPoint);
		//System.out.println(""+bTreeFile.readBoolean() + TreeObject.decode(bTreeFile.readLong())+" "+TreeObject.decode(bTreeFile.readLong())+" "+TreeObject.decode(bTreeFile.readLong()));
		//TODO iterate over the query and get our output data.
		boolean done = false;
		while(!done)
		{			
			String queryString = qFileReader.readLine();
			if(queryString==null)
				break;
			if(verbose)
				System.err.println("Looking for "+queryString);
			long queryKey = TreeObject.encode(queryString);
			int count = query(queryKey, startPoint);
			
			System.out.println(count);
			if(verbose)
				System.err.println(totalRead+" keys checked.");
			totalRead=0;
		}
		
	}
	
	
	private int query(long key, long offset) throws IOException
	{
		bTreeFile.seek(offset);
		int count = 0;
		boolean[] hasKeys = new boolean[degree];
		long[] keys = new long[degree];
		boolean[] hasSubTrees = new boolean[degree+1];
		long[] offsets = new long[degree+1];
		
		for(int i = 0; i < degree; i++)
		{
			hasKeys[i]=bTreeFile.readBoolean();
			keys[i]=bTreeFile.readLong();
			if(keys[i]!=0l&&keys[i]!=-1l)
			{
				totalRead++;
			//	System.out.println(TreeObject.decode(keys[i]));
			}
			if(keys[i]==key)
			{
				count++;
				//if(verbose&&keys[i]!=0l&&keys[i]!=-1l)
				//	System.out.println(TreeObject.decode(keys[i]));
			}
		}
		for(int i = 0; i <= degree; i++)
		{
			hasSubTrees[i]=bTreeFile.readBoolean();
			offsets[i]=bTreeFile.readLong();
			//if(verbose)
			//	System.out.println(hasSubTrees[i]+" "+offsets[i]);
		}
		int sanity = bTreeFile.readInt();
		//System.out.println("Alignment sanity check 256="+sanity);
		//System.out.println(bTreeFile.getFilePointer());
		int test = 0;
		//System.out.println("target="+key);
		//for(int i = 0; i < degree; i++)
		//	System.out.println(keys[i]+" ");
		//System.out.println(key);		
		while(test<degree&&keys[test]<key&&hasKeys[test])
		{
			//System.out.println(keys[test]/1000 + " < " + key/1000);
			test++;		
		}
		//System.out.println("Selected" + keys[test]);
		
		//this helps visualize the path a lot.
		/*
		if(verbose)
		{
			if(test>0)
				System.out.println(keys[test-1]+ "<" + key + "<" + keys[test]);
			else
				System.out.println(key + "<" + keys[test]);
		}*/
		
		
		if(offsets[test]==offset)
		{
			System.err.println("This node is referencing itself as a child!");
			System.err.println("!!!ABORTING STACK OVERFLOW!!!");
			return count;
		}
		if(offsets[test]<=0&&hasSubTrees[test]||offsets[test+1]<=0&&hasSubTrees[test+1])
			System.err.println("A NEGATIVE offset value? That's problematic.");
		
		/*
		if(test==0&&hasSubTrees[0])
			count+= query(key, offsets[0]);
		else if(test==degree-1&&hasSubTrees[degree])
			count+= query(key, offsets[degree]);
		else if(hasSubTrees[test-1])
			count+= query(key, offsets[test-1]);
		*/
		
		//latest try
		/*
		if(key<=keys[test]||keys[test]==-1l&&hasSubTrees[test])
			count+= query(key, offsets[test]);
		else if(key>=keys[test]&&hasSubTrees[test+1])
			count+= query(key, offsets[test+1]);
		*/
		/*
		if(test>0&&hasSubTrees[test-1])
			count+= query(key, offsets[test-1]);
		
		if(hasSubTrees[test])
			count+= query(key, offsets[test]);
			*/
		/*
		if(hasSubTrees[test+1])
			count+= query(key, offsets[test+1]);
		*/
		
		
		// temp code to make sure things exist in the tree		
		
		for(int i = 0; i <= degree; i++)
		{
			if(hasSubTrees[i])
				count+= query(key, offsets[i]);
		}
		
		
		return count;
	}

}
