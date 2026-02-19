package no.hvl.dat110.messaging;

import java.io.IOException;
import java.net.Socket;

public class MessagingClient {


	private String server;


	private int port;

	public MessagingClient(String server, int port) {
		this.server = server;
		this.port = port;
	}


	public MessageConnection connect() {

		try {

			Socket clientSocket = new Socket(server, port);


			return new MessageConnection(clientSocket);

		} catch (IOException ex) {
			System.out.println("MessagingClient.connect: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
}
