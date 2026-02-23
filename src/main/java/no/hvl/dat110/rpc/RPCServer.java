package no.hvl.dat110.rpc;

import java.util.HashMap;

import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessageConnection;
import no.hvl.dat110.messaging.MessagingServer;

public class RPCServer {

	private MessagingServer msgserver;
	private MessageConnection connection;
	private HashMap<Byte, RPCRemoteImpl> services;

	public RPCServer(int port) {
		this.msgserver = new MessagingServer(port);
		this.services = new HashMap<>();
	}

	public void run() {

		try {

			RPCRemoteImpl rpcstop = new RPCServerStopImpl(RPCCommon.RPIDSTOP, this);
			register(RPCCommon.RPIDSTOP, rpcstop);

			System.out.println("RPC SERVER RUN - Services: " + services.size());

			connection = msgserver.accept();

			if (connection == null) {
				System.out.println("RPCServer.run - connection is null (accept failed)");
				return;
			}

			System.out.println("RPC SERVER ACCEPTED");

			boolean stop = false;

			while (!stop) {

				Message requestmsg = connection.receive();

				if (requestmsg == null) {
					break;
				}

				byte[] rpcreq = requestmsg.getData();
				if (rpcreq == null || rpcreq.length == 0) {
					continue;
				}

				byte rpcid = rpcreq[0];
				byte[] param = RPCUtils.decapsulate(rpcreq);

				RPCRemoteImpl method = services.get(rpcid);

				if (method == null) {
					System.out.println("RPCServer.run - no method registered for rpcid=" + rpcid);
					continue;
				}

				byte[] returnval = method.invoke(param);
				byte[] rpcreply = RPCUtils.encapsulate(rpcid, returnval);

				Message replymsg = new Message(rpcreply);
				connection.send(replymsg);

				if (rpcid == RPCCommon.RPIDSTOP) {
					stop = true;
				}
			}

		} finally {
			stop();
		}
	}

	public void register(byte rpcid, RPCRemoteImpl impl) {
		services.put(rpcid, impl);
	}

	public void stop() {

		if (connection != null) {
			connection.close();
			connection = null;
		}

		if (msgserver != null) {
			msgserver.stop();
			msgserver = null;
		}
	}
}