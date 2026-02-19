package no.hvl.dat110.system.display;

import no.hvl.dat110.rpc.RPCServer;
import no.hvl.dat110.system.controller.Common;

public class DisplayDevice {

	public static void main(String[] args) {

		System.out.println("Display server starting ...");

		// Create and start the display RPC server on the configured port
		RPCServer rpcserver = new RPCServer(Common.DISPLAYPORT);

		// Register the display write RPC method implementation on the server
		new DisplayImpl((byte) Common.WRITE_RPCID, rpcserver);

		// Run server loop (accept + handle requests until stop is called)
		rpcserver.run();

		System.out.println("Display server stopping ...");
	}
}
