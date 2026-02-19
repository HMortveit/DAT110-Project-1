package no.hvl.dat110.rpc;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RPCUtils {


	public static byte[] encapsulate(byte rpcid, byte[] payload) {

		if (payload == null) {
			payload = new byte[0];
		}

		// Max Message payload is 127, rpcmsg uses 1 byte for rpcid => max payload is 126
		if (payload.length > 126) {
			throw new IllegalArgumentException("RPC payload too large: " + payload.length + " (max 126 bytes)");
		}

		byte[] rpcmsg = new byte[1 + payload.length];
		rpcmsg[0] = rpcid;
		System.arraycopy(payload, 0, rpcmsg, 1, payload.length);

		return rpcmsg;
	}

	public static byte[] decapsulate(byte[] rpcmsg) {

		if (rpcmsg == null || rpcmsg.length == 0) {
			throw new IllegalArgumentException("rpcmsg is null or empty");
		}

		// payload is everything after the rpcid byte
		return Arrays.copyOfRange(rpcmsg, 1, rpcmsg.length);
	}

	// convert String to byte array (UTF-8)
	public static byte[] marshallString(String str) {

		if (str == null) {
			str = "";
		}

		return str.getBytes(StandardCharsets.UTF_8);
	}

	// convert byte array to a String (UTF-8)
	public static String unmarshallString(byte[] data) {

		if (data == null) {
			return "";
		}

		return new String(data, StandardCharsets.UTF_8);
	}

	// void -> empty payload
	public static byte[] marshallVoid() {
		return new byte[0];
	}

	// void <- empty payload
	public static void unmarshallVoid(byte[] data) {
		if (data != null && data.length != 0) {
			throw new IllegalArgumentException("Expected void payload (empty), got length=" + data.length);
		}
	}

	// convert boolean to a byte array representation
	public static byte[] marshallBoolean(boolean b) {

		byte[] encoded = new byte[1];
		encoded[0] = (byte) (b ? 1 : 0);
		return encoded;
	}

	// convert byte array to a boolean representation
	public static boolean unmarshallBoolean(byte[] data) {

		if (data == null || data.length < 1) {
			throw new IllegalArgumentException("Expected at least 1 byte for boolean");
		}

		return (data[0] > 0);
	}

	// integer to byte array representation (4 bytes, big-endian)
	public static byte[] marshallInteger(int x) {

		return ByteBuffer.allocate(Integer.BYTES).putInt(x).array();
	}

	// byte array representation to integer (expects exactly 4 bytes)
	public static int unmarshallInteger(byte[] data) {

		if (data == null || data.length != Integer.BYTES) {
			throw new IllegalArgumentException("Expected 4 bytes for int, got " + (data == null ? "null" : data.length));
		}

		return ByteBuffer.wrap(data).getInt();
	}
}
