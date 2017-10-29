package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import packet.EchoRequest;

public class EchoRunnable implements Runnable{
	private boolean isSuspend = false;
	private DataOutputStream out;
	private DataInputStream in;
	
	public EchoRunnable(DataOutputStream out, DataInputStream in){
		this.out = out;
		this.in = in;
	}
	@Override
	public void run() {
		EchoMsgDealer();
	}
	
	public void EchoMsgDealer(){
		while(true){
			while(isSuspend){
				try {
					synchronized(this){
						this.wait();	
					}
				} catch (InterruptedException e) {
					System.out.println("wait() exception occurs.");
					e.printStackTrace();
				}
			}
			synchronized(this){
				EchoRequest echoRequest = new EchoRequest(123456);
				try {
					out.write(echoRequest.OFPacketBytes());
					out.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000*3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void EchoRunnableReader() throws IOException{
		byte[] buf = new byte[6];
		in.read(buf);
		System.out.println("THe EchoRunnable buf is : " + Arrays.toString(buf));
	}
	
	public void EchoMsgSuspend(){
		isSuspend = true;
	}
	
	public synchronized void EchoMsgRestart(){
		isSuspend = false;
		this.notify();
	}
}
