package connection;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

import data.Data;
/**
 * 利用apache net 开源包，使用telnet方式获取AIX主机信息
 * 
 * @author zhaoyl
 * @date 20008.7.21
 * @version 1.2
 */
public class TelnetCreator {
    // Telnet对象
	private static TelnetCreator telnetCreator = new TelnetCreator();
    private TelnetClient telnet = new TelnetClient("vt200");
    private InputStream in;
    private PrintStream out;
    // 提示符。具体请telnet到AIX主机查看
    private char prompt = Data.TelnetPrompt;
    // telnet端口
    private String port;
    // 用户
    private String user;
    // 密码
    private String password;
    // IP地址
    private String ip;
    
    public void TelnetCreatorInit() {
        try {
            // AIX主机IP
            this.ip = Data.deviceIP;
            this.password = Data.password;
            this.user = Data.username;
            this.port = "23";
            telnet.connect(ip, Integer.parseInt(port));
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            // 登录
            System.out.println(readUntil("Username:"));
            write(user + "\n");
            System.out.println(readUntil("Password:"));
            write(password + "\n");
            System.out.println("________________________________");
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
     * 向目标发送命令字符串
     * 并获得返回值
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
//            // 通过aix的命令“查找主机名称”获取数据
//            // 命令是 "hostname"
//            // 不熟悉命令的参考<<AIX网络管理手册>>
//            //String result = telnet.sendCommand("conf t");
//            //System.out.println(result);
//            // 最后一定要关闭
//            telnet.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}