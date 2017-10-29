package packet;

import data.Data;

public class PacketIn extends OFPacket{
	private final static byte type = Data.OFPT_PACKET_IN;
	private static int length = 32;	//will be changed later, the length should be flexible.
	
	private byte[] bufferId = new byte[4];	//00ffffff
	private byte[] totalLength = new byte[2];	//3,don't know the function of the field.
	private byte[] reason = new byte[1];	//4-oxc
	private byte[] tableId = new byte[1];	//0
	private byte[] cookie = new byte[8];	//1122334455667788
	
	private byte[] matchType = new byte[2];		//1
	private byte[] matchLength = new byte[2];	//000c
	private byte[] OXMField = new byte[12];		//80000004ffffffff00000000
	private byte[] matchPad = new byte[1];		//0000
	private byte[] data;
	private byte[] packetInBytes;
	
	public PacketIn(int xid, String data){
		super(type, length, xid);
		
		bufferId = Data.str2HexBytes("00ffffff");
		
		reason = Data.str2HexBytes("04");
		tableId[0] = 0x00;
		cookie = Data.str2HexBytes("1122334455667788");
		
		matchType = Data.str2HexBytes("0001");
		matchLength = Data.str2HexBytes("000c");
		OXMField = Data.str2HexBytes("80000004ffffffff00000000");
		matchPad = Data.str2HexBytes("0000");
		this.data = data.getBytes();
		//a method used to change the length field.
//		System.out.println(Arrays.toString(super.OFPacketBytes()));
//		String a = Integer.toHexString(1072);
//		if(a.length() == 3){
//			a = "0" + a;
//		}
//		System.out.println(a);
//		byte[] abuf = new byte[2];
//		abuf = Data.str2HexBytes(a);
//		
//		System.out.println(Arrays.toString(abuf));
//		System.arraycopy(abuf, 0, headerBytes, 2, 2);
//		System.out.println(Arrays.toString(headerBytes));
		
	}
	
	public byte[] packetInBytes(){
		int lengthBuf = data.length + 42;
		System.out.println(lengthBuf);
		packetInBytes = new byte[lengthBuf];
		System.out.println("lengthBuf is in 10: " + lengthBuf);
		
		String lengthBufStr = Integer.toHexString(lengthBuf);
		if(lengthBufStr.length() == 2){
			lengthBufStr = "00" + lengthBufStr;
		}
		else if(lengthBufStr.length() == 3){
			lengthBufStr = "0" + lengthBufStr;
		}
		System.out.println("LengthBufStr is str " + lengthBufStr);
		byte[] bufbyte = Data.str2HexBytes(lengthBufStr);
		System.out.println("lengthBufStr is " + bufbyte.length);
		System.out.println(bufbyte[0] + " " + bufbyte[1]);
		System.arraycopy(headerBytes, 0, packetInBytes, 0, 8);
//		packetInBytes[2] = bufbyte[0];
//		packetInBytes[3] = bufbyte[1];
		System.arraycopy(bufbyte, 0, packetInBytes, 2, 2);
		System.arraycopy(bufferId, 0, packetInBytes, 8, 4);
		System.out.println("data.length is " + data.length);
		String totalLengthStr = Integer.toHexString(data.length);
		System.out.println("Total length is : " + totalLengthStr);
		if(totalLengthStr.length() == 1){
			totalLengthStr = "000" + totalLengthStr;
		}
		if(totalLengthStr.length() == 2){
			totalLengthStr = "00" + totalLengthStr;
		}
		else if(totalLengthStr.length() == 3){
			totalLengthStr = "0" + totalLengthStr;
		}
		System.out.println("Total Length is now is : " + totalLengthStr);
		
		totalLength = Data.str2HexBytes(totalLengthStr);
		System.arraycopy(totalLength, 0, packetInBytes, 12, 2);
		System.arraycopy(reason, 0, packetInBytes, 14, 1);
		System.arraycopy(tableId, 0, packetInBytes, 15, 1);
		System.arraycopy(cookie, 0, packetInBytes, 16, 8);
		System.arraycopy(matchType, 0, packetInBytes, 24, 2);
		System.arraycopy(matchLength, 0, packetInBytes, 26, 2);
		System.arraycopy(OXMField, 0, packetInBytes, 28, 12);
		System.arraycopy(matchPad, 0, packetInBytes, 40, 2);
		System.arraycopy(data, 0, packetInBytes, 42, data.length);
		
		
		return packetInBytes;
	}
	public static void main(String args[]){
		PacketIn p = new PacketIn(123455,"sdf");
	}
}
