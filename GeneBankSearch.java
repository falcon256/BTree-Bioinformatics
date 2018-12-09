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
		long startPoint = bTreeFile.getFilePointer();
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
		}
		
	}
	
	private int query(long key, long offset) throws IOException
	{
		bTreeFile.seek(offset);
		
		long[] keys = new long[degree];
		boolean[] hasSubTrees = new boolean[degree+1];
		long[] offsets = new long[degree+1];
		
		for(int i = 0; i < degree; i++)
		{
			
		}
		return -1;//TODO
	}

}
