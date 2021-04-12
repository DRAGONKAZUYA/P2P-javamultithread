import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Report extends Thread
{
    public Report(String Report){super(Report);}
    public void run()
    {
        String Filename="C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\IP.txt";
        File ReadFile=new File(Filename);
        String[] IPBuffer=new String[10];
        String[] Address=new String[10];
        int[] Port=new int[10];
        try {
            sleep(100);
            BufferedReader in=new BufferedReader(new FileReader(ReadFile));
            String str;
            int i=0;
            while ((str=in.readLine())!=null)   //读取从tracker所得到的IP文件
            {
                IPBuffer[i]=str;
                i++;
            }
            in.close();
            int MessageNum=0;
            for (String s : IPBuffer) {
                if (s != null) {
                    MessageNum++;   //计算出IPBuffer中元素的个数，即从tracker中get到的地址个数
                }
            }
            MessageNum--;
            System.out.println("已加入P2P网络的客户端");
            for (int k=0;k<MessageNum;k++)
            {
                System.out.println(IPBuffer[k]);
            }
            for (int k=0;k<MessageNum;k++)  //将IPBuffer中的地址集合分别存储到Address和Port
            {
                String message=IPBuffer[k];
                int comma,bracket,anti_bracket;
                comma=message.indexOf(',');
                bracket=message.indexOf('[');
                anti_bracket=message.indexOf(']');
                Address[k]=message.substring(bracket+2,comma-1);
                Client.IPArray[k]=Address[k];
                String port=message.substring(comma+2,anti_bracket);    //字符串截取地址
                Port[k]=Integer.parseInt(port);     //端口转换为int类型
                //System.out.println(Address[k]);
                //System.out.println(Port[k]);
            }
            /*System.out.println("IPArray 中存储的地址如下");
            for(i=0;i<Client.IPArray.length;i++)
            {
                System.out.println(Client.IPArray[i]);
                if (Client.IPArray[i]==null)
                    break;
            }*/
            System.out.println("Report Start~");
            for (int k=0;k<MessageNum;k++)  //开始report
            {
                InetAddress address=InetAddress.getByName(Address[k]);
                //InetAddress address=InetAddress.getByName("192.168.43.105");
                int port=Port[k];
                String hello="hello";
                byte[] data=hello.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet=new DatagramPacket(data,data.length,address,port);
                DatagramSocket socket=new DatagramSocket(8888);
                socket.send(packet);
                socket.close();
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
