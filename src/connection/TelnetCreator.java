package connection;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

import data.Data;
/**
 * 利用apache.net开源包，使用telnet方式获取olt信息。
 * 
 * @author zhaoty
 * @date 2017.7.21
 * @version 1.2
 */
public class TelnetCreator {
    // Telnet����
	private static TelnetCreator telnetCreator = new TelnetCreator();
    private TelnetClient telnet = new TelnetClient("vt200");
    private InputStream in;
    private PrintStream out;
    private char prompt = Data.TelnetPrompt;
    // telnet对象
    private String port;
    // telnet端口
    private String user;
    // 用户名
    private String password;
    // IP地址ַ
    private String ip;
    
    public void TelnetCreatorInit() {
        try {
            // OLT 管理IP
            this.ip = Data.deviceIP;
            this.password = Data.password;
            this.user = Data.username;
            this.port = "23";
            telnet.connect(ip, Integer.parseInt(port));
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            // 登陆
            System.out.println(readUntil("Username:"));
            write(user + "\n");
            System.out.println(readUntil("Password:"));
            write(password + "\n");
            System.out.println(readUntil(prompt + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static TelnetCreator getTelnetCreator(){
    	return telnetCreator;
    }
    
    /**
     * 读取分析结果
     * 
     * @param pattern
     * @return
     */
    public String readUntil(String pattern) {
    	System.out.println("Pattern is " + pattern);
        try {
        	System.out.println("pattern length " + pattern.length());
            char lastChar = pattern.charAt(pattern.length()-1);//ENTER
            int receiveCount = 0;
            while(receiveCount == 0){
            	receiveCount = in.available();
            	//System.out.println("lastChar " + lastChar +  " in.availabel " + receiveCount);
            }
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            
            while (true) {
            	//System.out.println(++i + sb.toString());
                sb.append(ch);
                if (ch == lastChar) {
                	//System.out.println("ch == lastChar mode: " + ch + " " + lastChar);
                    if (sb.toString().endsWith(pattern)) {
                    	System.out.println("Read finish.");
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 写
     * 
     * @param value
     */
    public void write(String value) {
        try {
            out.print(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 向OLT发送命令字符串
     * 并获得返回值ֵ
     * 
     * @param command
     * @return
     */
    public String sendCommand(String command) {
        try {
            write(command);
            System.out.println("Send command to the device success: " + command);
            return readUntil(prompt + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 关闭连接
     * 
     */
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        try {
//            TelnetCreator telnet = new TelnetCreator();



//            //String result = telnet.sendCommand("conf t");
//            //System.out.println(result);

//            telnet.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}