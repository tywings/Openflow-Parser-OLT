package packet;

import data.Data;

public class EchoRequest extends OFPacket{
	private final static byte type = Data.OFPT_ECHO_REQUEST;
	private final static int length = 8;
	
	public EchoRequest(int xid){
		super(type, length, xid);
	}
}
