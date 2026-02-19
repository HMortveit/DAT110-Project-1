package no.hvl.dat110.system.controller;

import no.hvl.dat110.rpc.RPCClient;
import no.hvl.dat110.rpc.RPCLocalStub;
import no.hvl.dat110.rpc.RPCUtils;

public class DisplayStub extends RPCLocalStub {

	public DisplayStub(RPCClient rpcclient) {
		super(rpcclient);
	}

	public void write(String message) {

		// 1) marshall parameter
		byte[] param = RPCUtils.marshallString(message);

		// 2) remote call (rpcid MUST be byte)
		byte[] reply = rpcclient.call((byte) Common.WRITE_RPCID, param);

		// 3) unmarshall return value (void)
		RPCUtils.unmarshallVoid(reply);
	}
}
