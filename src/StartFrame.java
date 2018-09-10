import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

//GUI界面
public class StartFrame{

    //用户输入的主机号形式，分别为ip和url
    String status;

    public StartFrame() {
        JFrame f = new JFrame("端口扫描器");
        f.setSize(1000, 750);
        f.setLocation(300, 300);
        f.setLayout(new FlowLayout());

        JLabel l1 = new JLabel("IP地址：");
        JTextField t1 = new JTextField();
        t1.setPreferredSize(new Dimension(110, 30));
        JLabel l2 = new JLabel("-");
        JTextField t2 = new JTextField();
        t2.setPreferredSize(new Dimension(110, 30));
        JLabel l3 = new JLabel("端口号：");
        JTextField t3 = new JTextField();
        t3.setPreferredSize(new Dimension(80, 30));
        JLabel l4 = new JLabel("-");
        JTextField t4 = new JTextField();
        t4.setPreferredSize(new Dimension(80, 30));
        JButton b = new JButton("开始扫描");
        JButton clearButton = new JButton("清除");
        JTextArea content = new JTextArea();
        content.setLineWrap(true);
        Font font = new Font("宋体",Font.PLAIN,20);//设置字体
        content.setFont(font);
        content.setWrapStyleWord(true);
        JScrollPane jsp = new JScrollPane(content);
        jsp.setPreferredSize(new Dimension(950, 600));
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//设置滚动条自动出现
        JProgressBar pb = new JProgressBar();//显示进度的进度条
        pb.setStringPainted(true);
        pb.setValue(0);
        JButton clearTextButton = new JButton("清空内容");
        JButton sortButton = new JButton("排序");

        //清空输入框的监听事件
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");
            }
        });

        //开始扫描按钮的监听事件
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String formatResult = FormatTest.test(t1.getText(), t2.getText(), t3.getText(), t4.getText());
                if (formatResult.equals("ip") || formatResult.equals("url")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            content.setText("");
                            status = formatResult;
                            String startIp = t1.getText();
                            String endIp = t2.getText();
                            int startPort = Integer.parseInt(t3.getText());
                            int endPort = Integer.parseInt(t4.getText());
                            Set<String> ipSet = new HashSet<>();//IP的集合
                            //如果用户输入的是一个IP范围，找出所有符合的IP地址
                            if (status.equals("ip")) {
                                Util.scanIp(startIp, endIp, ipSet);
                            } else {
                                ipSet.add(startIp);
                            }
                            ThreadGroup tg = new ThreadGroup("PortScan");
                            int n = 0;//当前已扫描的端口数
                            int sum = ipSet.size() * (endPort - startPort + 1);//需要扫描的总端口数
                            for (String oneIp : ipSet) {
                                for (int port = startPort; port <= endPort; port++) {
                                    //保证运行的线程数不大于5000
                                    while (tg.activeCount() > 5000) {
                                        try {
                                            Thread.currentThread().sleep(1000);
                                        }catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    n = n + 1;
                                    pb.setValue((n * 100)/ sum);//进度条
                                    Thread t = new Thread(tg, new Scan(oneIp, port, content));
                                    t.start();
                                }
                            }
                            //检查扫描是否完成
                            while (tg.activeCount() > 0) {
                                try {
                                    Thread.currentThread().sleep(1000);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            pb.setValue(100);
                            JOptionPane.showMessageDialog(f, "端口扫描完成！");
                        }
                    }).start();
                } else {
                    JOptionPane.showMessageDialog(f, formatResult);
                }
            }
        });

        //清空内容的监听事件
        clearTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                content.setText("");
            }
        });

        //对内容进行排序
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = content.getText();
                String[] rows = s.split("\n");
                List<Data> datas = new ArrayList<>();
                for (String row : rows) {
                    String ip = row.split("主机号：")[1].split("   端口：")[0];
                    String port = row.split("   端口：")[1].split("   服务：")[0];
                    String service = row.split("   服务：")[1].split("   端口类型：")[0];
                    String type = row.split("   端口类型：")[1];
                    datas.add(new Data(ip, Integer.valueOf(port), service, type));
                }
                //使用排序算法
                Collections.sort(datas, new Comparator<Data>() {
                    @Override
                    public int compare(Data o1, Data o2) {
                        if (status.equals("ip")) {
                            int i1 = Integer.valueOf(o1.getIp().split("\\.")[3]);
                            int i2 = Integer.valueOf(o2.getIp().split("\\.")[3]);
                            if (i1 < i2) {
                                return -1;
                            }
                            if (i1 == i2) {
                                if (o1.getPort() < o2.getPort()) {
                                    return -1;
                                }
                                if (o1.getPort() == o2.getPort()) {
                                    return 0;
                                }
                                return 1;
                            }
                            return 1;
                        } else {
                            int i1 = o1.getPort();
                            int i2 = o2.getPort();
                            if (i1 < i2) {
                                return -1;
                            }
                            if (i1 == i2) {
                                return 0;
                            }
                            return 1;
                        }
                    }
                });
                content.setText("");
                for (Data data : datas) {
                    content.append("主机号：" + data.getIp() + "   端口：" + data.getPort() + "   服务：" + data.getService() + "   端口类型：" + data.getType() + "\n");
                }
            }
        });

        f.add(l1);
        f.add(t1);
        f.add(l2);
        f.add(t2);
        f.add(l3);
        f.add(t3);
        f.add(l4);
        f.add(t4);
        f.add(b);
        f.add(clearButton);
        f.add(jsp);
        f.add(pb);
        f.add(clearTextButton);
        f.add(sortButton);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

}
