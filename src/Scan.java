import javax.swing.*;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.Socket;

public class Scan implements Runnable{
    String ip;
    int port;
    JTextArea content;

    public Scan(String ip, int port, JTextArea content) {
        this.ip = ip;
        this.port = port;
        this.content = content;
    }

    @Override
    public void run(){
        Socket socket = null;
        try {
            System.out.println("开始扫描：" + ip + ":" + port);
            //创建套接字对象
            socket = new Socket();

            socket.connect(new InetSocketAddress(ip, port), 10000);//设置连接超时时间为3秒
            String data = Util.findServiceByPort(port);//通过端口号到txt文件查找端口对应的服务
            String service = null;
            String type = null;
            //如果找不到端口对应的服务
            if (data.equals("")){
                service = "无";
                type = "无";
            } else {
                service = data.split("=")[1];
                type = data.split(" ")[0];
            }
            String s = "主机号：" + ip + "   端口：" + port + "   服务：" + service + "   端口类型：" + type + "\n";
            Util.write(content, s);//输出信息
            System.out.println("扫描完成：" + ip + "." + port + "服务：" + service + "端口类型：" + type);
        } catch (IOException exception) {
            //抛出异常表示该端口不开放
            System.out.println("主机号为：" + ip + "的端口号为：" + port + "的服务不开放");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}
