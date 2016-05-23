|---------------------------------------------------------------------------------------------------------|
|------------------------------------------------Hadoop Run-----------------------------------------------|
|---------------------------------------------------------------------------------------------------------|


(1) 10 GB Sorting :
	>launch the c3.large instance.
	>configure it as per the prog2_report(page-10 to 17).
	>hadoop fs -mkdir /user/dipen/input
	>hadoop fs -put /home/ec2-user/64/input10GB.txt /user/dipen/input/	

	/----------------create and run jar file--------------------/
	>hadoop com.sun.tools.javac.Main TeraSort.java	
	>jar -cf ts.jar TeraSort*.class
	>hadoop jar ts.jar TeraSort_hadoop /user/dipen/input /user/dipen/output
	
	/-----------------get the output data and run valsort--------------/

	>hadoop fs -get /user/dipen/output/part-r-00000 /home/ubuntu/64
	>./valsort part-r-00000

(2)100 GB sorting :
	>launch the c3.large instance.
	>configure it as per the prog2_report(page-10 to 26).
	>hadoop fs -mkdir /user/dipen/input
	
	/----------copy the input file in the disk--------------------/	
	>cp 64/input100GB.txt /mnt/raid/
	>hadoop fs -put /mnt/raid/input100GB.txt /user/dipen/input/	

	/----------------create and run jar file--------------------/
	>hadoop com.sun.tools.javac.Main TeraSort.java	
	>jar -cf ts.jar TeraSort*.class
	>hadoop jar ts.jar TeraSort_hadoop /user/dipen/input /user/dipen/output
	
	/-----------------get the output data and run valsort--------------/
	>hadoop fs -get /user/dipen/output/part-r-00000 /mnt/raid/
	>./valsort /mnt/raid/part-r-00000

	

|---------------------------------------------------------------------------------------------------------|
|------------------------------------------------Spark Run------------------------------------------------|
|---------------------------------------------------------------------------------------------------------|

(1) 10GB Sorting :
	>launch the c3.large instance.
	>configure it as per the prog2_report(page-34 to 37).
	
	/---------------------------------Open the  pyspark shell--------------------------------------/

	>cd spark/sbin
	>./pyspark
	
	/---------------------------------run the following program------------------------------------/
	
	>>TerasortFile = sc.textFile("hdfs://PUBLIC DNS :9000/user/hadooo/input/input_10GB.txt")

	>>TeraSortObj = TerasortFile.flatMap(lambda line:ine.split("\n"))
		.map(lambda dicto:(str(dicto[:10]),str(dicto[10:]))).sortByKey().map(lambda (a,b) : a+b)

	>>TeraSortObj.saveAsTextFile("hdfs://Public DNS :9000/user/hadoop/output_10GB")
	


(2) 100GB Sorting :
	>launch the c3.large instance.
	>configure it as per the prog2_report(page-34 to 41).
	
	/---------------------------------Open the  pyspark shell--------------------------------------/

	>cd spark/sbin
	>./pyspark
	
	/---------------------------------run the following program------------------------------------/
	
	>>TerasortFile = sc.textFile("hdfs://PUBLIC DNS :9000/user/hadooo/input/input_100GB.txt")

	>>TeraSortObj = TerasortFile.flatMap(lambda line:ine.split("\n"))
		.map(lambda dicto:(str(dicto[:10]),str(dicto[10:]))).sortByKey().map(lambda (a,b) : a+b)

	>>TeraSortObj.saveAsTextFile("hdfs://Public DNS :9000/user/hadoop/output_100GB")
	





|---------------------------------------------------------------------------------------------------------|
|------------------------------------------SharedMemory Run-----------------------------------------------|
|---------------------------------------------------------------------------------------------------------|

>launch the c3.large instance.
>configure it.

(1)1 thread:

	>javac SharedMemory.java
	>java SharedMemory 1 >output.txt


(2)2 thread:

	>javac SharedMemory.java
	>java SharedMemory 2 >output.txt


(3)4 thread:

	>javac SharedMemory.java
	>java SharedMemory 4 >output.txt


(4)8 thread:

	>javac SharedMemory.java
	>java SharedMemory 8 >output.txt










