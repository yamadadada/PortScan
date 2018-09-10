public class Data {

    private String ip;

    private int port;

    private String service;

    private String type;

    public Data(String ip, int port, String service, String type) {
        this.ip = ip;
        this.port = port;
        this.service = service;
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Data{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", service='" + service + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
