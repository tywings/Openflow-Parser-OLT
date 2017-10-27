package data;

public abstract class Data {
	public final static byte OpenFlow13 = 0x04;
	public final static byte OFPT_HELLO = 0;
	public final static byte OFPT_ECHO_REQUEST = 0x02;
	public final static byte OFPT_ECHO_REPLY = 0x03;
	public final static byte OFPT_FEATURE_REQUEST = 5;
	public final static byte OFPT_FEATURE_REPLY = 6;
	public final static byte OFPT_MULTIPART_REQUEST = 18;
	public final static byte OFPT_MULTIPART_REPLY = 19;
	public final static byte OFPT_PACKET_IN = 10;
	public final static byte OFPT_PACKET_OUT = 13;
	
	
	public final static byte[] OFPMP_PORT_DESC = str2HexBytes("000d");	//13
	
	
	public final static byte[] OFPP_LOCAL = str2HexBytes("fffffffe");
	
	public final static byte[] DatapathId = str2HexBytes("121212121212");
	
	public final static String username = "zte";
	public final static String password = "zte";
	public final static String deviceIP = "10.108.90.213";
	public final static char TelnetPrompt = '#';
	
	public static byte[] str2HexBytes(String inputStr) {
		byte[] result = new byte[inputStr.length() / 2];
	    for (int i = 0; i < inputStr.length() / 2; ++i) 
		        result[i] = (byte)(Integer.parseInt(inputStr.substring(i * 2, i * 2 +2), 16) & 0xff);
	    return result;
	}
}
