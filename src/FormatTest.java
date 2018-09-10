import java.util.regex.Pattern;

public class FormatTest {

    //对输入的IP和端口号的格式进行检测
    public static String test(String startIp, String endIp, String startPort, String endPort) {
        String result = testIp(startIp, endIp);
        if (result.equals("ip") || result.equals("url")) {
            String result2 = portTest(startPort, endPort);
            if (result2.equals("success")) {
                return result;
            } else {
                return result2;
            }
        } else {
            return result;
        }
    }

    //对输入的IP地址进行检测
    public static String testIp(String startIp, String endIp) {
        //匹配字符串是否为IP地址
        Pattern ipPattern = Pattern.compile("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)");
        //匹配字符串是否为URL
        Pattern urlPattern = Pattern.compile("[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
        if (startIp.trim().equals("") && endIp.trim().equals("")) {
            return "IP地址不能为空！";
        } else if (startIp.trim().equals("")) {
            return "IP地址段开始部分不能为空！";
        } else if (endIp.trim().equals("")) {
            if (urlPattern.matcher(startIp).matches()) {
                return "url";//表示输入的地址为URL地址
            } else {
                return "你输入的IP地址格式不正确！";
            }
        } else {
            if (ipPattern.matcher(startIp).matches() && ipPattern.matcher(endIp).matches()) {
                for (int i = 0; i < 3; i++) {
                    //IP地址前三位不相同
                    if (!(startIp.split("\\.")[i].equals(endIp.split("\\.")[i]))) {
                        return "IP地址前三位不相同";
                    }
                }
                int start = Integer.valueOf(startIp.split("\\.")[3]);
                int end = Integer.valueOf(endIp.split("\\.")[3]);
                if (start > end) {
                    return "IP地址段格式不正确！";
                }
                return "ip";
            } else {
                return "你输入的IP地址格式不正确！";
            }
        }
    }

    //对输入的端口号进行检测
    public static String portTest (String startPort, String endPort) {
        Pattern mathPattern = Pattern.compile("^\\d+$");
        if (startPort.trim().equals("") || endPort.trim().equals("")) {
            return "端口号不能为空！";
        }
        if (mathPattern.matcher(startPort).matches() && mathPattern.matcher(endPort).matches()) {
            int start = Integer.valueOf(startPort);
            int end = Integer.valueOf(endPort);
            if (start < 0 || start > 65535 || end < 0 || end > 65535) {
                return "端口号必须在0到65535的范围内！";
            } else {
                if (start > end) {
                    return "开始端口必须小于等于结束端口";
                }
            }
        } else {
            return "端口格式不正确！";
        }
        return "success";
    }

}
