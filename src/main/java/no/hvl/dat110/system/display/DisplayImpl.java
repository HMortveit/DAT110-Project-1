package no.hvl.dat110.system.display;

import no.hvl.dat110.rpc.RPCRemoteImpl;
import no.hvl.dat110.rpc.RPCUtils;
import no.hvl.dat110.rpc.RPCServer;

public class DisplayImpl extends RPCRemoteImpl {

	public DisplayImpl(byte rpcid, RPCServer rpcserver) {
		super(rpcid, rpcserver);
	}

	public void write(String message) {
		System.out.println("DISPLAY:" + message);
	}

	@Override
	public byte[] invoke(byte[] param) {

		// 1) unmarshall parameter (String)
		String message = RPCUtils.unmarshallString(param);

		// 2) call the actual method
		write(message);

		// 3) marshall return value (void)
		return RPCUtils.marshallVoid();
	}
}
