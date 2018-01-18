# CS502Capstone
Dataset:

	Netflix Prize data

Architecture:

	data-ingestion-inferstructure: Kafka, Kafka connect
	data-transformation-layer: Kafka Streaming
	data-storage-layer: Cassandra
	data-visualization-layer: NodeJS
	cloud-inferstructure: AWS

Detailed Schema:

​	
Deliverable:
​	
	Week1: Figure out project architecture, data source, determine the requirements and the functionalities to implement
	Week2: Each team member starts implementing their own module
	Week3: Each team member finishes their own module
	Week4: Testing, Report documenting		
	
	Detail Ownerships:
	
		Tongzhou Zhou: 
		Week 1: Set up Kafka Cluster, Cassandra Cluster in AWS
		Week 2: Starts implement data transformation layer, Set up Kafka Connect to load data from Kafka Cluster to Cassandra Cluster 
		Week 3: Finishes implement data transformation layer: 
			(1) Aggragate the data into formates to support the Cassandra data schemas in (2)
			(2) Configure Cassandra data schema to support:
				a: Top 10 highest rating movie by a given month
				b: Top 10 rated movie by a given month
				c: Top rated month by a movie		
		Week 4: Starts and finishes unit testing
		
<<<<<<< HEAD
		Bin Shi: Data ingestion using kafka
		Week 1: Produce data to kafka
=======
		Bin Shi: Data ingestion
		Week 1: Produce Data in Kafka.
>>>>>>> 9079982b697199f798d1293e815208ecd00a87c7
		Week 2: 
		Week 3: 
		Week 4: 
		
		Albert:
		Week 1: 
		Week 2: 
		Week 3: 
		Week 4: 
		
		Anthony:
		Week 1: 
		Week 2: 
		Week 3: 
		Week 4: 


​		

