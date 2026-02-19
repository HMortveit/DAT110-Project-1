package no.hvl.dat110.messaging;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MessageConnection {

	private DataOutputStream outStream; // for writing bytes to the underlying TCP connection
	private DataInputStream inStream;   // for reading bytes from the underlying TCP connection
	private Socket socket;              // socket for the underlying TCP connection

	public MessageConnection(Socket socket) {
		try {
			this.socket = socket;
			outStream = new DataOutputStream(socket.getOutputStream());
			inStream = new DataInputStream(socket.getInputStream());
		} catch (IOException ex) {
			System.out.println("Connection: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void send(Message message) {
		try {
			byte[] segment = MessageUtils.encapsulate(message); // 128 bytes
			outStream.write(segment);
			outStream.flush();
		} catch (IOException ex) {
			System.out.println("MessageConnection.send: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public Message receive() {
		try {
			byte[] segment = new byte[MessageUtils.SEGMENTSIZE]; // 128
			inStream.readFully(segment);                         // read exactly 128 bytes
			return MessageUtils.decapsulate(segment);
		} catch (IOException ex) {
			System.out.println("MessageConnection.receive: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}


	public void close() {
		try {
			outStream.close();
			inStream.close();
			socket.close();
		} catch (IOException ex) {
			System.out.println("Connection: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
