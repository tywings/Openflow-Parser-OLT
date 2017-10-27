package packet;

import data.Data;

public class Hello extends OFPacket{
	private final static byte type = Data.OFPT_HELLO;
	private final static int length = 8;
	
	public static void main(String args[]){
		Hello hello = new Hello(123);
	}
	
	public Hello(int xid) {
		super(type, length, xid);
		// TODO 自动生成的构造函数存根
	}
	
}
