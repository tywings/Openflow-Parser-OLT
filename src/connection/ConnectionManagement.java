package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import data.Data;

public class ConnectionManagement extends Thread{
	private String ip;
	private int port;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private EchoRunnable echoRunnable;
	private Thread echoThread;
	private PacketOutRunnable packetOutRunnable;
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		ConnectionManagement connectionManagement = new ConnectionManagement("10.108.90.211", 6653);
	}
	
	public ConnectionManagement(String ip, int port) throws UnknownHostException, IOException{
		this.ip = ip;
		this.port = port;
		TelnetCreator.getTelnetCreator().TelnetCreatorInit();
		ConnectionCreate();
	}
	
	public void ConnectionCreate() throws UnknownHostException, IOException{
		Socket client = new Socket(ip, port);
		InputStream inputStream = client.getInputStream();
		OutputStream outputStream = client.getOutputStream();
		RYUHandshake ryuHandshake = new RYUHandshake(inputStream, outputStream);
		dataInputStream = new DataInputStream(inputStream);
		dataOutputStream = new DataOutputStream(outputStream);
		
		echoRunnable = new EchoRunnable(dataOutputStream, dataInputStream);
		echoThread = new Thread(echoRunnable);
		packetOutRunnable = new PacketOutRunnable(dataOutputStream, dataInputStream);
		
		echoThread.start();
		ConnectionDistribution();
	}
	
	public void ConnectionDistribution() throws IOException{
		while(true){
			int OpenflowVersion = dataInputStream.readUnsignedByte();
			if(OpenflowVersion == Data.OpenFlow13){
				System.out.println("The Openflow version is right.");
				int packetType = dataInputStream.readUnsignedByte();
				System.out.println("The packetTYpe is : " + packetType);
				if(packetType == Data.OFPT_PACKET_OUT){
					System.out.println("A packet out message came in.");
					try{
						synchronized(this){
							echoRunnable.EchoMsgSuspend();
						}
						
						Thread packetOutThread = new Thread(packetOutRunnable);
						packetOutThread.start();
						System.out.println("start");
						packetOutThread.join();
						System.out.println("end");
						synchronized(this){
							echoRunnable.EchoMsgRestart();
						}						
					}
					catch(Exception e){
						e.printStackTrace();
						System.out.println("An exception occurs.");
					}
					
					continue;
				}
				else if(packetType == Data.OFPT_ECHO_REPLY){
					System.out.println("A Echo Request message came in.");
					echoRunnable.EchoRunnableReader();
					continue ;
				}
				else{
					System.out.println("A wrong packet came in.");
					System.exit(0);
				}
			}
			else{
				System.out.println("The Openflow version is wrong." + OpenflowVersion);
				System.exit(0);
			}
		}
	}
	//通过读取头部分配进入的包的种类
}
