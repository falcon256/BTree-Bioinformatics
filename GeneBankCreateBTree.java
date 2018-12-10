import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;



/**
 * 
 *
 *
 */
public class GeneBankCreateBTree {
	private boolean setupFailed = false;
	private File file = null;
	private BufferedReader fileReader = null;
	private boolean withCache = false;
	private int degree = 2;
	private int sequenceLength = 31;
	private int cacheSize = 1;
	private int verbosity = 0;
	private String filePath = "";
	
	/**
	 * note - outputs xyz.gbk.btree.data.k.t
	 * write to buffer and dump to disk
	 * @param Args: [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]
	 */
	public static void main(String[] args) {
		if(args.length<4)
		{
			System.err.println("Insufficient number of arguements.");
			System.out.println("Args: [0 | 1(no/with Cache)] [degree] [gbk file] [sequence length] [ | cache size] [ | debug level]");
			return;
		}
		GeneBankCreateBTree main = new GeneBankCreateBTree(args);//set up
		main.Go();//execution
	}
	
	/**
	 * Set up and input checking
	 * @param args
	 */
	public GeneBankCreateBTree(String[] args)
	{
		withCache = (Integer.parseInt(args[0])==1)?true:false;
		degree = Integer.parseInt(args[1]);
		try {
			
			filePath=args[2];
			
			file = new File(filePath);
			//bTreeFile = new RandomAccessFile(file,"rw");
			//fileStream = new FileInputStream(file);
			fileReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sequenceLength = Integer.parseInt(args[3]);
		if(sequenceLength<1||sequenceLength>31)
		{
			System.err.println("Bad sequence length.");
			System.out.println("Sequence Length needs to be between 1 and 31 inclusive.");
		}
		if(args.length>4)
		{
			cacheSize = Integer.parseInt(args[4]);
		}
		if(args.length>5)
		{
			verbosity = Integer.parseInt(args[5]);
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
		int count = 0;
		//get ready to start writing.
		try {
			
			ArrayList<Long> testArrayList = new ArrayList<Long>();
			
			
			if(verbosity>0)
				System.out.println("Verbose mode selected\nFile "+filePath+" Loaded\nCache: "+withCache+" degree: "+degree+" Seq Len: "+sequenceLength+" Cache size: "+cacheSize);
			
			BTree<TreeObject> bt = new BTree<TreeObject>(degree,verbosity>0?true:false);
			if(verbosity>0)
				System.out.println("BTree created with degree: "+degree);
			
			//bring in the data.
			String inLine = fileReader.readLine();
			while(inLine!=null)
			{
				
			while(inLine!=null&&!inLine.contains("ORIGIN"))
				inLine = fileReader.readLine();
			
			inLine = fileReader.readLine();//skip the ORIGIN line before processing.
			String batch = "";
			int offset = 0;
			while(inLine!=null&&!inLine.contains("//"))
			{
				
				if(offset>=inLine.length())
				{
					inLine = fileReader.readLine();
					offset=0;
				}				
				
				char c = inLine.charAt(offset);
				
				if(c=='/'||c=='n'||c=='N')
					break;
				
				if(c=='a'||c=='c'||c=='t'||c=='g')
					batch = batch + c;
				
				if(batch.length()>=sequenceLength)
				{
					long key = TreeObject.encode(batch);
					//TreeObject t = new TreeObject(key);
					//bt.insert(t, key);
					testArrayList.add(key);
					if(verbosity>0)
						System.out.println(batch+" Read as key "+key+" which decodes to "+TreeObject.decode(key));
					
					
					count++;
					batch = batch.substring(1);//this is right//temp removed to lower output size for debugging.
					//batch = "";
					
				}
				offset++;
			}
			}
			Collections.sort(testArrayList);
			for(int i = 0; i < testArrayList.size(); i++)
			{
				TreeObject t = new TreeObject(testArrayList.get(i));
				bt.insert(t, testArrayList.get(i));
			}
			
			
			bt.writeToDisk(filePath+"btree.data."+sequenceLength+"."+degree,sequenceLength);
			//iterate through all of our TreeObjects and write them
			//for each ... file.write(treeObject.getBytes());

			//and we are done.
			//file.close();
			
			//if(verbosity>0)
				System.out.println("Number of items loaded: "+count);
			
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
