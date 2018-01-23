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
	
	
Deliverable:
	
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
		
		Bin Shi: Data ingestion
		Week 1: Produce Data in Kafka.
		Week 2: 
		Week 3: 
		Week 4: 
		
		Albert:
		Week 1: 
		Week 2: 
		Week 3: 
		Week 4: 
		
		Anthony: Data visualization 
		Week 1: Determine the requirements to implement, get ready to fulfill the Nodejs module features; Data cleaning.
		Week 2: Setup backend for data visualization; simple front end to display data
		Week 3: Finish kafka consumer using nodejs, backend server and frontend development
		Week 4: Testing and debugging
		
		
		

