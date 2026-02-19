package no.hvl.dat110.messaging;

import java.util.Arrays;

public class MessageUtils {

	public static final int SEGMENTSIZE = 128;

	public static int MESSAGINGPORT = 8080;
	public static String MESSAGINGHOST = "localhost";

	public static byte[] encapsulate(Message message) {

		if (message == null) {
			throw new IllegalArgumentException("message cannot be null");
		}

		byte[] data = message.getData();
		if (data == null) {
			throw new IllegalArgumentException("message data cannot be null");
		}
		if (data.length > (SEGMENTSIZE - 1)) { // max 127
			throw new IllegalArgumentException("message data exceeds 127 bytes");
		}

		byte[] segment = new byte[SEGMENTSIZE];
		segment[0] = (byte) data.length;                // length byte
		System.arraycopy(data, 0, segment, 1, data.length); // payload copy


		return segment;
	}

	public static Message decapsulate(byte[] segment) {

		if (segment == null) {
			throw new IllegalArgumentException("segment cannot be null");
		}
		if (segment.length != SEGMENTSIZE) {
			throw new IllegalArgumentException("segment must be exactly " + SEGMENTSIZE + " bytes");
		}

		int length = segment[0] & 0xFF; // unsigned
		if (length > (SEGMENTSIZE - 1)) {
			throw new IllegalArgumentException("invalid payload length in segment: " + length);
		}

		byte[] data = Arrays.copyOfRange(segment, 1, 1 + length);
		return new Message(data);
	}
}
