package connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import packet.FeatureReply;
import packet.Hello;
import packet.MultipartReply;
import packet.OFPacket;

public class RYUHandshake {
	private DataInputStream in;
	private OutputStream out;
	private int xidInt;
	
	public RYUHandshake(InputStream in, OutputStream out) throws IOException{
		this.in = new DataInputStream(in);
		this.out = out;
		
		HandshakeMethod();
	}
	
	public void HandshakeMethod() throws IOException{
		HandshakeRecv();	//hello
		HandshakeSend(new Hello(xidInt));	//hello
		
		HandshakeRecv();
		HandshakeSend(new FeatureReply(xidInt));
		
		HandshakeRecv();
		HandshakeSend(new MultipartReply(xidInt));
		System.out.println("Handshake with RYU success!");
		System.out.println(in.read(new byte[1024]));
	}
	
	public void HandshakeRecv() throws IOException{
		int version = in.readUnsignedByte();
		System.out.println("version is : " + version);
		int type = in.readUnsignedByte();
		System.out.println("type is : " + type);
		short length = in.readShort();
		System.out.println("length is : " + length);
		byte[] xid = new byte[4];
		int xidInt = in.readInt();
		this.xidInt = xidInt;
		System.out.println("xid is : " + Integer.toHexString(xidInt));
	}
	
	public void HandshakeSend(OFPacket packet) throws IOException{
		if(packet instanceof Hello){
			Hello hello =(Hello) packet;
			out.write(hello.OFPacketBytes());
			out.flush();
		}
		else if(packet instanceof FeatureReply){
			FeatureReply featureReply = (FeatureReply) packet;
			out.write(featureReply.featureReplyBytes());
			out.flush();
		}
		else if(packet instanceof MultipartReply){
			MultipartReply multipartReply = (MultipartReply) packet;
			out.write(multipartReply.multipartReplyBytes());
			out.flush();
		}
	}
}
