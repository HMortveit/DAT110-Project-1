package no.hvl.dat110.rpc;

import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.MessagingClient;

public class RPCClient {


	private MessagingClient msgclient;


	private MessageConnection connection;

	public RPCClient(String server, int port) {
		msgclient = new MessagingClient(server, port);
	}

	public void connect() {

		connection = msgclient.connect();
	}

	public void disconnect() {
		// disconnect by closing the underlying messaging connection
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	/*
	 Make a remote call on the method on the RPC server by sending an RPC request message and receive an RPC reply message

	 rpcid is the identifier on the server side of the method to be called
	 param is the marshalled parameter of the method to be called
	 */
	public byte[] call(byte rpcid, byte[] param) {

		if (connection == null) {
			throw new IllegalStateException("RPCClient is not connected");
		}
		if (param == null) {
			param = new byte[0];
		}

		// 1) encapsulate rpcid + param into rpc request bytes
		byte[] rpcreq = RPCUtils.encapsulate(rpcid, param);

		// 2) send request as a messaging Message
		connection.send(new Message(rpcreq));

		// 3) receive reply message
		Message reply = connection.receive();
		if (reply == null) {
			return null;
		}

		// 4) decapsulate rpc reply: remove rpcid header and return payload (return value)
		byte[] rpcreply = reply.getData();
		return RPCUtils.decapsulate(rpcreply);
	}
}
