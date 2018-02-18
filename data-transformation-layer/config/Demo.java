package config;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import config.KafkaConfiguration;
import config.KafkaStreamingLogic;

//kafka-streams-application-reset.sh --application-id netflix-analysis --input-topics input_cs502,movie_title_year_cs502 --intermediate-topics rekeyed-topic --bootstrap-servers node1:9092,node2:9092,node3:9092 --zookeeper node1:2181,node2:2181,node3:2181/kafka


public class Demo {

	public static void runDemo(String intputTopic, String intputTopic2, String outputTopic) throws InterruptedException {
		// Stream Analysis DEMO

		// Stream processing is not easy if you choose to

		// (1) DIY:
		// while(consumerisRunning){
		// message = consumer.poll();

		// DIY your analysis here:

		// producer.sent(message);
		// }
		// How do you Ordering the messages if you get them from different
		// topics?
		// How do you Partitioning the messages and Scale out your processing?
		// How do you handle fault tolerance&re-processing the data
		// How do you manage the state of your windowing analysis to achieve
		// exactly-one analysis

		// (2) reply on full-fledged stream processing system:
		// Storm, Spark, Samza
		//

		// (3) Streams API
		// A unique feature of the Kafka Streams API is that the applications
		// you build with it are normal Java applications.
		// These applications can be packaged, deployed, and monitored like any
		// other Java application –
		// there is no need to install separate processing clusters or similar
		// special-purpose and expensive infrastructure!

		Properties props = new Properties();
		props.put("metrics.recording.level", "DEBUG");
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "netflix-analysis");
		props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once");
		props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, KafkaStreamingLogic.precessing_interval);// The frequency with which to save the position of the processor.
		props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1024 * 1024L);
		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 10);
		props.put(StreamsConfig.POLL_MS_CONFIG, KafkaStreamingLogic.precessing_interval);// The amount of time in milliseconds to block waiting for input.
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.BrokerURL);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		// use 1 stream client with 10 threads to analysis
		KStreamBuilder LogicBuilder = KafkaStreamingLogic.TputByMarket_LogicBuilder_Windowing(intputTopic, intputTopic2, outputTopic);
		KafkaStreams streamsApp = new KafkaStreams(LogicBuilder, props);
		streamsApp.cleanUp();
		streamsApp.start();
		Runtime.getRuntime().addShutdownHook(new Thread(streamsApp::close));
		Runtime.getRuntime().addShutdownHook(new Thread(streamsApp::cleanUp));
	}

	public static void main(String args[]) throws InterruptedException {
		runDemo("input_cs502", "movie_title_year_cs502", "output_cs502");
	}
}
