import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class SharedMemory extends Thread {

	static String[] dataArray;
	static int threadCount;
	public SharedMemory(String[] array, int length) {
		// Shared memory constructor stub
			dataArray=new String[length];
			dataArray=array;
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	//thread run method is called
	public void run(){
		
		MergeSort(dataArray,dataArray.length);
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String sourceFilePath="/mnt/raid/input10GB.txt";
		String outputFilePath="/mnt/raid/output10GB.txt";
		
		threadCount=Integer.parseInt(args[0]);
		// Start time of the program	
		long startTime=System.currentTimeMillis();
		
		ArrayList<String> tempFilesPath=new ArrayList<>();
	
		tempFilesPath=inputFilePartion(sourceFilePath);		//Call the file partition function and stored the temporary file 
									
 		outputTempFilesMerging(tempFilesPath,outputFilePath);	//call the file joining function which will joint all temporary 									//sorted file in the output file	
		
		//end time of the program
		long endTime=System.currentTimeMillis();
			
		System.out.println("thread "+threadCount+"  "+(endTime-startTime));	//gives the total time for whole operation with  											   	//the particular threads 
	}

	
/*----------------------sort and merge the temporary file in the output file-------------------------------*/
	
	
	private static void outputTempFilesMerging(ArrayList<String> tempFilesPath,String outputFilePath) throws IOException {
		// TODO Auto-generated method stub
		int numTempFiles=tempFilesPath.size();
		BufferedReader[] fileReader=new BufferedReader[numTempFiles];
		HashMap<String,String> mapper=new HashMap<>();
		
	/*-------------------------get the temporary files path----------------------------------*/	
		for(int i=0,index=0; i<numTempFiles; i++,index++) {
			fileReader[i] = new BufferedReader(new FileReader(tempFilesPath.get(i)));
			String line = fileReader[i].readLine();
			if(line != null){
				String key = line.substring(0,10);			//get the key 
				String val = index+"|"+line.substring(10);		//get the values
				mapper.put(key, val);					//store the key value in the hash map
			}
		}
	/*-----------------------------------------------------------------------------------------*/
		
		String[] tempKeys=mapper.keySet().toArray(new String[0]);
		
		//Merge Sort 
		MergeSort(tempKeys,tempKeys.length);
		tempKeys=dataArray;
		
		ArrayList<String> keyList=new ArrayList<>();

		for(int j=0;j<tempKeys.length;j++){
			keyList.add(tempKeys[j]);
		}

		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFilePath));
		
		for(;!keyList.isEmpty()&& keyList.size()!=0;){
			String singleKey = keyList.get(0);
			String val = mapper.get(singleKey);
			StringTokenizer fileToken =new StringTokenizer(mapper.get(singleKey),"|");

			int filenum = Integer.parseInt(fileToken.nextToken());
			mapper.remove(singleKey);
			keyList.remove(0);
			
			fileWriter.write(singleKey+fileToken.nextToken());	//single line of sorted data is written in output file
			fileWriter.write("\n");			
			fileWriter.flush();
			singleKey = fileReader[filenum].readLine();
			
			if(singleKey != null ) {				
				String key = singleKey.substring(0,10);
				val = filenum+"|"+singleKey.substring(10);
				mapper.put(key, val);
				keyList.add(0,key);
				String[] tempString=keyList.toArray(new String[0]);	
				MergeSort(tempString,keyList.size());				//calls the merge sort on the data
				tempString=dataArray;
				for(int i1=0;i1<tempString.length;i1++){
					keyList.add(tempString[i1]);
				}
			}	
		}
		fileWriter.close();
		for(int i=0; i<fileReader.length; i++) {
			fileReader[i].close();		
		}
	}

/*-------------divides the input file in the 20000 temporary file and sort each file-----------------------*/	
	
	public static ArrayList inputFilePartion(String sourceFilePath) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader fileReader=new BufferedReader(new FileReader(sourceFilePath));
		String line=fileReader.readLine();
	
		ArrayList<String> tempFilesPath=new ArrayList<>();
		HashMap<String,String> mapper=new HashMap<>();
		
		
		for(int i=0;line!=null;i++){
			String key=line.substring(0,10);
			String value=line.substring(10);
			//String[] keyVal=new String[]{key,value};
			mapper.put(key, value);
			
			if(mapper.size() == 20000){
				
				File tempFile=File.createTempFile("temp", ".txt", new File("/home/dipen/Downloads/assign2/temp/"));
				//adding tempfile path to arraylist
				tempFilesPath.add(tempFile.getAbsolutePath());
				
				FileWriter fWriter=new FileWriter(tempFile.getAbsolutePath());
				
				String[] array=mapper.keySet().toArray(new String[0]);
				
				System.out.println("array length---->"+array.length);
				//Merge sort 
				SharedMemory thread =new SharedMemory(array,array.length);
				
				thread.start();
				//Write in the file
				array=dataArray;
				FileWrite(mapper,array,fWriter);
				
				fWriter.close();
				mapper = new HashMap<String,String>();
		
			}
			line=fileReader.readLine();
		}
		fileReader.close();
		
		if(!mapper.isEmpty()){
			File tempFile=File.createTempFile("temp", ".txt", new File("/home/dipen/Downloads/assign2/temp/"));
			FileWriter fWriter1=new FileWriter(tempFile.getAbsolutePath());
			
			tempFilesPath.add(tempFile.getAbsolutePath());
			String[] array=mapper.keySet().toArray(new String[0]);
			
			//Merge sort 
			MergeSort(array,array.length);
			array=dataArray;
			//Write in the file
			FileWrite(mapper,array,fWriter1);
			fWriter1.close();
		}
		return tempFilesPath;
	}

	
	private static void FileWrite(HashMap<String, String> map, String[] array, java.io.FileWriter fWriter) throws IOException {
		// TODO Auto-generated method stub
	
		for (String k: array){
			//System.out.println(k);
			
			fWriter.write(k+map.get(k).toString());
			fWriter.write('\n');
			fWriter.flush();
		}
	}

	// Utility function to find minimum of two integers
	static int min(int x, int y) { return (x<y)? x:y; }

/*---------------------------- Iterative mergesort function to sort arr[0...n-1]-------------------------------------------------------- */
/*----------------------------referenced from the website:http://www.geeksforgeeks.org/iterative-merge-sort/-----------------------------*/
	
	static void MergeSort(String[] arr, int n)
	{
	int curr_size; // For current size of subarrays to be merged
					// curr_size varies from 1 to n/2
	int left_start; // For picking starting index of left subarray
					// to be merged

	// Merge subarrays in bottom up manner. First merge subarrays of
	// size 1 to create sorted subarrays of size 2, then merge subarrays
	// of size 2 to create sorted subarrays of size 4, and so on.
	for (curr_size=1; curr_size<=n-1; curr_size = 2*curr_size)
	{
		// Pick starting point of different subarrays of current size
		for (left_start=0; left_start<n-1; left_start += 2*curr_size)
		{
			// Find ending point of left subarray. mid+1 is starting 
			// point of right
			int mid = left_start + curr_size - 1;

			int right_end = min(left_start + 2*curr_size - 1, n-1);

			// Merge Subarrays arr[left_start...mid] & arr[mid+1...right_end]
			merge(arr, left_start, mid, right_end);
		}
	}
	}

	/* Function to merge the two haves arr[l..m] and arr[m+1..r] of array arr[] */
	static void merge(String[] arr, int l, int m, int r)
	{
		int i, j, k;
		int n1 = m - l + 1;
		int n2 = r - m;

		System.out.println(n2);
		/* create temp arrays */
		String[] L=new String[n1];
		String[] R=new String[n2];

		/* Copy data to temp arrays L[] and R[] */
		for (i = 0; i < n1; i++)
			L[i] = arr[l + i];
		for (j = 0; j < n2; j++)
			R[j] = arr[m + 1+ j];

		/* Merge the temp arrays back into arr[l..r]*/
		i = 0;
		j = 0;
		k = l;
		while (i < n1 && j < n2)
		{
			if (L[i].compareTo(R[j])<=0)
			{
				arr[k] = L[i];
				i++;
			}
			else
			{
				arr[k] = R[j];
				j++;
			}
			k++;
		}

		/* Copy the remaining elements of L[], if there are any */
		while (i < n1)
		{
			arr[k] = L[i];
			i++;
			k++;
		}

		/* Copy the remaining elements of R[], if there are any */
		while (j < n2)
		{
			arr[k] = R[j];
			j++;
			k++;
		}
	}
}

