import javax.swing.*;
import java.io.*;
import java.util.Set;

public class Util {

    //根据输入的IP地址范围确定具体的IP地址
    public static void scanIp(String startIp, String endIp, Set<String> ipSet) {
        String base = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2];
        int start = Integer.valueOf(startIp.split("\\.")[3]);
        int end = Integer.valueOf(endIp.split("\\.")[3]);
        for (int i = start; i <= end; i++) {
            ipSet.add(base + "." + i);
        }
    }

    //通过本地文件查找端口对应的服务和类型
    public static String findServiceByPort(int port) {
        String str = null;
        File f = new File("PortData.txt");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(f);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            while ((str = br.readLine()) != null) {
                String b = str.split(" ")[1].split("=")[0];//端口号
                int p = Integer.valueOf(b);
                if (p == port) {
                    return str;
                }
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //向面板输入数据，保证线程同步
    public static synchronized void write(JTextArea content, String s) {
        content.append(s);
    }

}
