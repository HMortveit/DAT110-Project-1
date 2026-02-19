package no.hvl.dat110.messaging;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class MessagingServer {


	private ServerSocket welcomeSocket;

	public MessagingServer(int port) {
		try {
			this.welcomeSocket = new ServerSocket(port);
		} catch (IOException ex) {
			System.out.println("Messaging server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}


	public MessageConnection accept() {

		try {

			Socket clientSocket = welcomeSocket.accept();


			return new MessageConnection(clientSocket);

		} catch (IOException ex) {
			System.out.println("MessagingServer.accept: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	public void stop() {
		if (welcomeSocket != null) {
			try {
				welcomeSocket.close();
			} catch (IOException ex) {
				System.out.println("Messaging server: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
