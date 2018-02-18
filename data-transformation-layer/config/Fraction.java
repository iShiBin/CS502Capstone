package config;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

public class Fraction {
	public long count;
	public double numerator;
	public double denominator;

	Fraction(long c, double num, double den) {
		count = c;
		numerator = num;
		denominator = den;
	}
}

class FractionSerializer implements Serializer<Fraction> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
	}

	@Override
	public byte[] serialize(String topic, Fraction data) {
		if (data == null)
			return null;
		ByteBuffer buffer = ByteBuffer.allocate(8 + 8 + 8);
		buffer.putLong(data.count);
		buffer.putDouble(data.numerator);
		buffer.putDouble(data.denominator);
		return buffer.array();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

}

class FractionDeserializer implements Deserializer<Fraction> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
	}

	@Override
	public Fraction deserialize(String topic, byte[] data) {
		if (data == null)
			return null;
		ByteBuffer buffer = ByteBuffer.wrap(data);
		long count = buffer.getLong();
		double numerator = buffer.getDouble();
		double denominator = buffer.getDouble();
		return new Fraction(count, numerator, denominator);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}
}
