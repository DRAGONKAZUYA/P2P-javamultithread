import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class SendFile extends Thread
{
    private DatagramSocket sender;     //发送的socket端
    private InetAddress address;     //目标地址
    private String filename;
    private int port;
    public SendFile(DatagramSocket sender,InetAddress address,String filename,int port)
    {
        this.sender=sender;
        this.address=address;
        this.filename=filename;
        this.port=port;
    }
    public void run()
    {
        try{
            System.out.println("开始传输文件"+filename);
            File source=new File("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\"+filename);       //要传输的文件的路径
            InputStream inputStream=new FileInputStream(source);
            byte[] length=Integer.toString((int) source.length()).getBytes(StandardCharsets.UTF_8);
            DatagramPacket LengthPacket=new DatagramPacket(length,length.length,address,port);
            sender.send(LengthPacket);
            byte[] data=new byte[1024];
            while (inputStream.read(data)!=-1) {
                DatagramPacket packet = new DatagramPacket(data, data.length, address,port);
                sender.send(packet);
            }
            /*byte[] end="end".getBytes(StandardCharsets.UTF_8);
            DatagramPacket EndPacket=new DatagramPacket(end,end.length,address,6666);
            sender.send(EndPacket);*/
            sender.close();
            System.out.println("成功向地址："+address+"发送文件"+filename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}