import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TeraSort {
	public static class TeraSortMapper extends Mapper<Object, Text, Text, Text>
	{
	      //hadoop supported data types
	      /*private final static IntWritable one = new IntWritable(1);
	      private Text word = new Text();
	     */
			private final Text k=new Text();
			private Text v=new Text();
			
		//map method that performs the tokenizer job and framing the initial key value pairs
	      public void map(Object key, Text value, Context output) throws IOException, InterruptedException{
	            //taking one line at a time and tokenizing the same
	//          String line = value.toString();
	
//	          StringTokenizer tokenizer = new StringTokenizer(line);
	         
	          //iterating through all the words available in that line and forming the key value pair
	              String key1=value.toString().substring(1, 10);
	   	          k.set(key1);
	   	          String value1=value.toString().substring(10);
	   	    	  v.set(value1);
	             //  word.set(tokenizer.nextToken());
	               //sending to output collector which inturn passes the same to reducer
	                 output.write(k,v);
	       }
	}

	public static class TeraSortReducer extends Reducer<Text, Text, Text, Text>
	{
	      //reduce method accepts the Key Value pairs from mappers, do the aggregation based on keys and produce the final out put
	     public void reduce(Text key, Iterable<Text> values, Context output) throws IOException, InterruptedException{
	    	 // private Text key1 = new Text();
	    	  Text value1 = new Text();
	    	  
	            //int sum = 0;
	            /*iterates through all the values available with a key and add them together and give the
	            final result as the key and sum of its values*/
	          for(Text val : values){
	               value1 = val;
	          }
	          output.write(key,value1);
	      }
    }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Tera Sort");
    job.setJarByClass(TeraSort.class);
    job.setMapperClass(TeraSortMapper.class);
    job.setCombinerClass(TeraSortReducer.class);
    job.setReducerClass(TeraSortReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
