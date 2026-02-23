package no.hvl.dat110.messaging;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class MessagingServer {


	private ServerSocket welcomeSocket;

	public MessagingServer(int port) {
		try {
			this.welcomeSocket = new ServerSocket();
			this.welcomeSocket.setReuseAddress(true);
			this.welcomeSocket.bind(new java.net.InetSocketAddress(port));
		} catch (IOException ex) {
			System.out.println("Messaging server: " + ex.getMessage());
			ex.printStackTrace();
			this.welcomeSocket = null;
		}
	}


	public MessageConnection accept() {

		if (welcomeSocket == null) {
			System.out.println("MessagingServer.accept: welcomeSocket is null (server failed to start)");
			return null;
		}

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
		if (welcomeSocket != null && !welcomeSocket.isClosed()) {
			try {
				welcomeSocket.close();
			} catch (IOException ex) {
				System.out.println("Messaging server: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
