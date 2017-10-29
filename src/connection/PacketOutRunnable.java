package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import packet.PacketIn;

public class PacketOutRunnable implements Runnable{
	private boolean isSuspend = false;
	private DataOutputStream out;
	private DataInputStream in;
	
	public PacketOutRunnable(DataOutputStream out, DataInputStream in){
		this.out = out;
		this.in = in;
	}
	
	@Override
	public void run() {
		try {
			PacketOutMsgDealer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void PacketOutMsgDealer() throws IOException{
		while(isSuspend){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		synchronized(this){
			int packetOutDataLength = in.readShort()-40;
			System.out.println("Length is : " + packetOutDataLength);
			int xid = in.readInt();
			System.out.println("xid is : " + Integer.toHexString(xid));
			for(int i=0 ; i<32; i++){
				in.readUnsignedByte();
			}
			byte buf[] = new byte[packetOutDataLength];
			in.read(buf);
			String strBuf = new String(buf).trim() + "\n";
			System.out.println("plain data is : " + Arrays.toString(buf));
			System.out.println("Data is : " + strBuf + " length is : " + strBuf.length());
			
			//Telnet part
			String returnBuf;
			System.out.println("return Buf: " + (returnBuf = TelnetCreator.getTelnetCreator().sendCommand(strBuf)));
			
			//Packet in part
			System.out.println("SHOW THE RETURN BUF: " + returnBuf);
			PacketIn packetIn = new PacketIn(xid, returnBuf);
			out.write(packetIn.packetInBytes());
			out.flush();
		}
	}
	public void PacketOutMsgSuspend(){
		isSuspend = true;
	}
	
	public void PacketOutMsgRestart(){
		isSuspend = false;
		notify();
	}
}
