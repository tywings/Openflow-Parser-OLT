package packet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import data.Data;

/*
 * The FeatureRequest packet is the same as FeatureReply.
 */
public class FeatureReply extends OFPacket{
	private final static byte type = 6;
	private final static int length = 32;
	
	/*
	 * The datapath-id is a 64 bit field that should be 
	 * thought of as analogous to a Ethernet Switches bridge MAC, 
	 * its a unique identifier for the specific packet processing 
	 * pipeline being managed.
	 * 
	 */
	private byte[] datapathId = new byte[8];
	
	private byte[] nBuffers = new byte[4];		// identifies how many packets the switch can queue for PacketIn
	private byte nTables = 1;					//The number of tables in the switch is captured
	private byte auxilaryId = 0;				//In main connection is always 0.
	private byte[] pad = new byte[2];			//pad means empty, default is 0.
	private byte[] capabilities = new byte[4];	//I think can be all 0.
	private byte[] reserved = new byte[4];
	
	public byte[] featureReplyBytes = new byte[32];
	
	public static void main(String[] args){
		FeatureReply fr = new FeatureReply(123);
	}
	
	public FeatureReply(int xid) {
		super(type, length, xid);
		
        try {
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());

	        //NetworkInterface network = NetworkInterface.getByInetAddress(ip);

	        //byte[] mac = network.getHardwareAddress();
	        byte[] dpid = Data.DatapathId;	//may need to change to input from the start interface 2017.5.19
	        //System.out.println(Arrays.toString(mac));
	        
	        System.arraycopy(dpid, 0, datapathId, 2, 6);
	        
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        nBuffers = Data.str2HexBytes("00000100");
        capabilities = Data.str2HexBytes("0000004f");
    	
	}
	
	public byte[] featureReplyBytes(){
		System.arraycopy(headerBytes, 0, featureReplyBytes, 0, 8);
		System.arraycopy(datapathId, 0, featureReplyBytes, 8, 8);
		System.arraycopy(nBuffers, 0, featureReplyBytes, 16, 4);
		
		featureReplyBytes[20] = nTables;
		System.arraycopy(capabilities, 0, featureReplyBytes, 24, 4);
		//pad, capabilities, and reserved are all 0. So do not need to copy.
		
		return featureReplyBytes;
	}
}
