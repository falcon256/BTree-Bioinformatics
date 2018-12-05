import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;



/**
 * 
 *
 *
 */
public class GeneBankCreateBTree {
	boolean setupFailed = false;
	RandomAccessFile file = null;
	/**
	 * note - outputs xyz.gbk.btree.data.k.t
	 * write to buffer and dump to disk
	 * @param Args: [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]
	 */
	public static void main(String[] args) {
		GeneBankCreateBTree main = new GeneBankCreateBTree(args);//set up
		main.Go();//execution
	}
	
	/**
	 * Set up and input checking
	 * @param args
	 */
	public GeneBankCreateBTree(String[] args)
	{
		try {
			file = new RandomAccessFile(args[2], "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(file==null)
		{
			System.err.println("Bad file path given in paremeter 2.");
			System.out.println("Args: [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]");
			setupFailed = true;
			return;
		}
	}
	
	/**
	 * Execution
	 */
	public void Go()
	{
		if(setupFailed)
			return;
		
		//get ready to start writing.
		try {
			file.seek(0);
			//TODO write metadata.
			
			
			long pointer = file.getFilePointer();//get our current location after metadata.
			
			//iterate through all of our TreeObjects and write them
			//for each ... file.write(treeObject.getBytes());

			//and we are done.
			file.close();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	/**
	 * see 15-B-Trees slide 14
	 * @return the optimal degree
	 */
	private int calculateOptimalDegree()
	{
		return -1;//TODO
	}

}
