import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * 
 * 
 *
 */
public class GeneBankSearch {
	
	boolean setupFailed = false;
	RandomAccessFile bTreeFile = null;
	RandomAccessFile queryFile = null;
	
	/**
	 * 
	 * @param Args[0 | 1(no/with Cache)] [btree file] [query file] [ | cache size] [ | debug level]
	 */
	public static void main(String[] args) {
		GeneBankSearch main = new GeneBankSearch(args);
		main.Go();
	}
	
	/**
	 * 
	 * @param args
	 */
	public GeneBankSearch(String[] args)
	{
		
		try {
			bTreeFile = new RandomAccessFile(args[1], "r");
			queryFile = new RandomAccessFile(args[1], "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(bTreeFile==null)
		{
			System.err.println("Bad file path given in paremeter 1.");
			System.out.println("Args: [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]");
			setupFailed = true;
			return;
		}
		
		if(queryFile==null)
		{
			System.err.println("Bad file path given in paremeter 2.");
			System.out.println("Args: [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]");
			setupFailed = true;
			return;
		}
		
		//TODO read in our metadata and read in the root of the btree.
		
		
	}
	
	/**
	 * 
	 */
	public void Go()
	{
		if(setupFailed)
			return;
		//TODO iterate over the query and get our output data.
		
	}

}
