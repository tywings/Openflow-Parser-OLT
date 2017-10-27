package packet;

import data.Data;

public class MultipartReply extends OFPacket{
	private final static byte type = Data.OFPT_MULTIPART_REPLY;
	private final static int length = 80;
	
	private byte[] subtype = new byte[2];
	private byte[] flags = new byte[2];
	private byte[] padding = new byte[4];
	private byte[] portNo = new byte[4];
	private byte[] padding2 = new byte[4];
	private byte[] datapathId = new byte[6];
	private byte[] padding3 = new byte[2];
	private byte[] name = new byte[16];
	private byte[] config = new byte[4];
	private byte[] state = new byte[4];
	private byte[] current = new byte[4];
	private byte[] advertised = new byte[4];
	private byte[] supported = new byte[4];
	private byte[] peer = new byte[4];
	private byte[] CurrSpeed = new byte[4];
	private byte[] MaxSpeed = new byte[4];
	
	private byte[] multipartReplyBytes = new byte[80];
	
	public MultipartReply(int xid) {
		super(type, length, xid);
		// TODO 自动生成的构造函数存根
		
		subtype = Data.OFPMP_PORT_DESC;
		portNo = Data.OFPP_LOCAL;
		datapathId = Data.DatapathId;
		String nameStr = "tap:";
		System.arraycopy(nameStr.getBytes(), 0, name, 0, nameStr.length());
		state = Data.str2HexBytes("00000004");
		current = Data.str2HexBytes("00000802");
		CurrSpeed = Data.str2HexBytes("00002800");	//10240
		
	}
	
	public byte[] multipartReplyBytes(){
		System.arraycopy(headerBytes, 0, multipartReplyBytes, 0, 8);
		System.arraycopy(subtype, 0, multipartReplyBytes, 8, 2);
		System.arraycopy(flags, 0, multipartReplyBytes, 10, 2);
		
		System.arraycopy(portNo, 0, multipartReplyBytes, 16, 4);
		System.arraycopy(datapathId, 0, multipartReplyBytes, 24, 6);
		System.arraycopy(name, 0, multipartReplyBytes, 32, 16);
		System.arraycopy(config, 0, multipartReplyBytes, 48, 4);
		System.arraycopy(state, 0, multipartReplyBytes, 52, 4);
		System.arraycopy(current, 0, multipartReplyBytes, 56, 4);
		System.arraycopy(advertised, 0, multipartReplyBytes, 60, 4);
		System.arraycopy(supported, 0, multipartReplyBytes, 64, 4);
		System.arraycopy(peer, 0, multipartReplyBytes, 68, 4);
		System.arraycopy(CurrSpeed, 0, multipartReplyBytes, 72, 4);
		System.arraycopy(MaxSpeed, 0, multipartReplyBytes, 76, 4);
		return multipartReplyBytes;
	}

}
