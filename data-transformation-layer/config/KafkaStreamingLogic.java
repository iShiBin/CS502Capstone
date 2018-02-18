package config;
//kafka-streams-application-reset.sh --application-id netflix-analysis --input-topics input_cs502,movie_title_year_cs502 --intermediate-topics rekeyed-topic --bootstrap-servers node1:9092,node2:9092,node3:9092 --zookeeper node1:2181,node2:2181,node3:2181/kafka
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.kstream.Windowed;

public class KafkaStreamingLogic {
	public static int precessing_interval = 1000;
	private static final DateFormat Date_Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static KStreamBuilder TputByMarket_LogicBuilder(String intputTopic, String outputTopic) {
		final FractionSerializer FractionSerializer = new FractionSerializer();
		final FractionDeserializer FractionDeserializer = new FractionDeserializer();
		final Serde<Fraction> FractionSerde = Serdes.serdeFrom(FractionSerializer, FractionDeserializer);
		KStreamBuilder builder = new KStreamBuilder();
		KStream<String, String> inputRecord = builder.stream(intputTopic);
		KTable<String, String> marketUserTput = inputRecord
				.filter((recordKey,
						recordValue) -> (recordValue.length() > 0 && !recordValue.equals("") && recordValue != null))
				.map(new KeyValueMapper<String, String, KeyValue<String, String>>() {
					@Override
					public KeyValue<String, String> apply(String recordKey, String recordValue) {
						int comma = 0, walker = 0, runner = -1;
						String region = null, market = null;
						while (++runner < recordValue.length()) {
							if (recordValue.charAt(runner) != ',')
								continue;
							comma++;
							if (comma == 5)
								walker = runner + 1;
							else if (comma == 6) {
								region = recordValue.substring(walker, runner);
								walker = runner + 1;
							} else if (comma == 7)
								market = recordValue.substring(walker, runner);
							else if (comma == 12)
								break;
						}
						String newKey = "Date:" + recordKey + "_Region:" + region + "_Market:" + market;
						String newRecord = recordValue.substring(runner + 1, recordValue.length());
						return new KeyValue<String, String>(newKey, newRecord);
					}
				}).groupByKey()
				// .groupBy(new KeyValueMapper<String, String, String>() {//new
				// KeyValueMapper<OldKeyType, OldValueType, NewKeyType>
				// @Override
				// //return NewKey
				// public String apply(String recordKey, String recordValue) {
				// String[] record = recordValue.split(",");
				// String region = record[5];
				// String market = record[6];
				// String newKey =
				// "Date:"+recordKey+"_Region:"+region+"_Market:"+market;
				// return newKey;
				// }
				// })
				.aggregate(new Initializer<Fraction>() {
					@Override
					public Fraction apply() {
						return new Fraction(0, 0.0, 0.0);
					}
				}, new Aggregator<String, String, Fraction>() {
					@Override
					public Fraction apply(String recordKey, String recordValue, Fraction aggregate) {
						String[] record = recordValue.split(",");
						Double EUCELL_DL_TPUT_NUM_KBITS = 0.0;
						Double EUCELL_DL_TPUT_DEN_SECS = 0.0;
						if (record.length != 0) {
							EUCELL_DL_TPUT_NUM_KBITS = Double.parseDouble(record[0]);
							EUCELL_DL_TPUT_DEN_SECS = Double.parseDouble(record[1]);
						}
						aggregate.count++;
						aggregate.numerator += EUCELL_DL_TPUT_NUM_KBITS;
						aggregate.denominator += EUCELL_DL_TPUT_DEN_SECS;
						return aggregate;
					}
				}, FractionSerde).mapValues(new ValueMapper<Fraction, String>() {
					@Override
					public String apply(Fraction aggregate) {
						return "{\"count\":\"" + aggregate.count + "\", \"UTput\":\""
								+ (aggregate.numerator / aggregate.denominator) + "\"}";
						// return "count:" + aggregate.count + " UTput:" +
						// (aggregate.numerator / aggregate.denominator);
					}
				});
		marketUserTput.to(Serdes.String(), Serdes.String(), outputTopic);
		return builder;
	}
	public static KStreamBuilder TputByMarket_LogicBuilder_Windowing(String intputTopic1, String intputTopic2, String outputTopic) {
		final FractionSerializer FractionSerializer = new FractionSerializer();
		final FractionDeserializer FractionDeserializer = new FractionDeserializer();
		final Serde<Fraction> FractionSerde = Serdes.serdeFrom(FractionSerializer, FractionDeserializer);
		KStreamBuilder builder = new KStreamBuilder();
		KStream<String, String> moviesRecord = builder.stream(intputTopic1);
		KTable<String, String> movies_Title_year_table = builder.table(intputTopic2);
		KTable<Windowed<String>, String> WindowTable = moviesRecord
				.filter((recordKey, recordValue) -> (recordValue.length() > 0 && !recordValue.equals("") && recordValue != null))
				.groupByKey()
				.aggregate(()->new Fraction(0, 0.0, 0.0), /* initializer */			
							(recordKey, recordValue, aggregate)->UDFaggregate(recordKey, recordValue, aggregate),
							TimeWindows.of(60*1000),/* time-based window */
							FractionSerde,
						    "rating-stats-store")
				.mapValues(aggregate->
								"\"count\":\"" + aggregate.count + "\", \"rating\":\""
		+ (aggregate.numerator / aggregate.count));
		
		
		//set stream key
		KStream<String, String> windowstream = WindowTable.toStream((recordKey, recordValue) -> recordKey.window().start()+";"+Date_Format.format(new Date(recordKey.window().start()))+";"+recordKey.key())
		//map key value
		.map((recordKey, recordValue) -> UDF_key_value_map(recordKey, recordValue))
		.leftJoin(movies_Title_year_table, (value1, value2) -> {
			String result = "";
			if(value2 == null) result = value1+"\"}";
			else {
				String movie_title = null, genre = null;
				if(value2.indexOf("\",")>0) {
					String[] strings = value2.split("\",");
					movie_title = strings[0].substring(1);
					genre = strings[1];
				}
				else {
					String[] strings = value2.split(",");
					movie_title = strings[0];
					genre = strings[1];
				}
				result = value1 + "\", \"movie_title\":\""+movie_title+ "\", \"genre\":\""+genre + "\"}";
				//System.out.print(genre);
			}
			
			System.out.println(result);
			return result;
		});
		windowstream.to(Serdes.String(), Serdes.String(), outputTopic);
		
		KTable<Windowed<String>, String> CustomerWindowTable = moviesRecord
				.filter((recordKey, recordValue) -> (recordValue.length() > 0 && !recordValue.equals("") && recordValue != null))
				//map value to be customer_id
				.map((recordKey, recordValue) -> UDF_key_value_customer_map(recordKey, recordValue))
				.groupByKey()
				.aggregate(()->new Fraction(0, 0.0, 0.0), /* initializer */			
							(recordKey, recordValue, aggregate)->UDFaggregate_customer(recordKey, recordValue, aggregate),
							TimeWindows.of(60*1000),/* time-based window */
							FractionSerde,
						    "customer-stats-store")
				.mapValues(aggregate->
								"\"count\":\"" + aggregate.count + "\", \"rating\":\""
		+ (aggregate.numerator / aggregate.count)+ "\"}");
		
		CustomerWindowTable.toStream((recordKey, recordValue) -> recordKey.window().start()+";"+Date_Format.format(new Date(recordKey.window().start()))+";"+recordKey.key())
		//map key value
		.map((recordKey, recordValue) -> UDF_key_value_window_stream_map(recordKey, recordValue))
		.to(Serdes.String(), Serdes.String(), "output_customer_cs502");
		
		
		return builder;
	}

	public static KeyValue<String, String> UDF_key_value_map(String recordKey, String recordValue) {
		String[] split = recordKey.split(";");
		String movie_id = split[2];
		recordValue = "{\"movie_id\":\""+movie_id+ "\", " + "\"start_time\":\""+split[0]+"\", " + recordValue;
		return new KeyValue<String, String>(movie_id, recordValue);
	}
	
	public static KeyValue<String, String> UDF_key_value_customer_map(String recordKey, String recordValue) {
		String[] record = recordValue.split(",");
		String customer_id = record[0];
		recordValue = record[1];
		return new KeyValue<String, String>(customer_id, recordValue);
	}
	
	public static KeyValue<String, String> UDF_key_value_window_stream_map(String recordKey, String recordValue) {
		String[] split = recordKey.split(";");
		String customer_id = split[2];
		recordValue = "{\"customer_id\":\""+customer_id+ "\", " + "\"start_time\":\""+split[0]+"\", " + recordValue;
//		System.out.println(Date_Format.format(new Date(Long.valueOf(split[0]))));
//		System.out.println(recordValue);
		return new KeyValue<String, String>(customer_id, recordValue);
	}
	
	
	public static Fraction UDFaggregate(String recordKey, String recordValue, Fraction aggregate) {
		String[] record = recordValue.split(",");
		Double rating = 0.0;
		if (record.length != 0) {
			rating = Double.parseDouble(record[1]);
			aggregate.count++;
		}	
		aggregate.numerator += rating;
		//aggregate.denominator += EUCELL_DL_TPUT_DEN_SECS;
		return aggregate;
	}
	
	public static Fraction UDFaggregate_customer(String recordKey, String recordValue, Fraction aggregate) {
		Double rating = 0.0;
		if (recordValue.length() != 0) {
			rating = Double.parseDouble(recordValue);
			aggregate.count++;
		}	
		aggregate.numerator += rating;
		//aggregate.denominator += EUCELL_DL_TPUT_DEN_SECS;
		return aggregate;
	}
}
