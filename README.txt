Entry Point:
/NetflixSPS/src/main/com/netflix/client/Main.java

Output stream (SPS events) goes to:
/NetflixSPS/stats.txt

=========================================================================

4 Components:

1. StreamClient
2. Message Queue
3. StreamProcessor
4. ConnectionManager

=========================================================================

1. StreamClient:
	* Reads the stream and send the messages to a queue.
	Improvements:
		*	In distributed system, there should be a back up server to take over in case of server failure.

2. Message Queue:
	* I used bounded queue, BlockingQueue, not to overflow memory for unexpected spikes on the sps.
	Improvements: 
		* When the queue is full, RejectedExecutionHandler should start writing the messages to disk.
		* By monitoring the frequency of such incidents, number of queues could be increased with load balancer in front. If one queue is only choice, capacity of the queue could be increased with memory increase on the server. 
		* There should be persistency layer for the queue itself not to loose any message in case of server failure.
		* Like Kafka, partitioning the messages would help to improve the latency at StreamProcessor (consumer) side.

3. StreamProcessor:
	* Reads from the queue with 1 sec window to its own private queue (micro batching), then aggregate the messages and out stream the result messages to file.
	* Every 1 sec, one StreamProcessor thread is created to do the consume&processing job. After 1 sec of consuming messages from queue, StreamProcessor thread gets interrupted by the executor, stops consuming and starts processing those events.
	* Aggregation is based on the “time” of the message itself. Late events might appear among the events of the current second. To eliminate the confusion and distinguish the real time of the event, I created the field “timesec”. 
	Improvements:
		* Instead of streaming to a file, another queue could be used for the output messages since inline writing can have some performance costs in high volume applications.  Asyncronously, another Writer thread (or micro service in the real system) should consume those. 
		* In my implementation, since executor interrupts the Processor thread after 1 sec and immediately created another thread, I didn’t see any harm putting consuming and out streaming logic under same service. 
		* Like Elastic Search and Kibana could be used to analyze and visualize the sps data.

4. ConnectionManager:
	* Responsible from keeping the connection to the URL stream healthy. In case of a disconnect, retry mechanism works.
	
	
