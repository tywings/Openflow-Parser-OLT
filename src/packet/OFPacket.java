package packet;

import java.util.Arrays;
import data.Data;

public class OFPacket {
	private byte version = Data.OpenFlow13;
	private byte type;
	public byte[] length = new byte[2];
	private byte[] xid = new byte[4];
	
	public byte[] headerBytes = new byte[8];
	
	public OFPacket(byte type, int length, int xid){
		this.type = type;
		this.length[0] = (byte) ((length >> 8) & 0xff);
		this.length[1] = (byte)(length & 0xff);
		this.xid[0] = (byte) (xid >>> 24);
		this.xid[1] = (byte) ((xid >>> 16) & 0xff);
		this.xid[2] = (byte) ((xid >>> 8) & 0xff);
		this.xid[3] = (byte) (xid & 0xff);
		System.out.println(Arrays.toString(OFPacketBytes()));
	}
	
	public byte[] OFPacketBytes(){
		headerBytes[0] = version;
		headerBytes[1] = type;
		System.arraycopy(length, 0, headerBytes, 2, 2);
		System.arraycopy(xid, 0, headerBytes, 4, 4);
		
		return headerBytes;
		
	}
}
