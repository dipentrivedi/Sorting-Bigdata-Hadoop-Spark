TerasortFile = sc.textFile("hdfs://PUBLIC DNS :9000/user/hadooo/input/input_100GB.txt")

TeraSortObj = TerasortFile.flatMap(lambda line:ine.split("\n"))
		.map(lambda dicto:(str(dicto[:10]),str(dicto[10:]))).sortByKey().map(lambda (a,b) : a+b)

TeraSortObj.saveAsTextFile("hdfs://Public DNS :9000/user/hadoop/output_100GB")

