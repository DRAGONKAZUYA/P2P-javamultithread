import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveFile extends Thread
{
    private static final int Max_Receive_Buffer=1024;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private String filename;
    byte[] buffer=new byte[Max_Receive_Buffer];
    public ReceiveFile(DatagramSocket socket,String filename)
    {
        this.socket=socket;
        this.filename=filename;
        packet=new DatagramPacket(buffer, buffer.length);
    }
    public void run()
    {
        try {
            File Receive=new File("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\share\\"+filename);   //接收的文件的存放路径
            FileOutputStream output=new FileOutputStream(Receive);
            byte[] size=new byte[Max_Receive_Buffer];
            DatagramPacket SizePacket=new DatagramPacket(size,size.length);
            socket.receive(SizePacket);
            System.out.println("文件的大小为："+SizePacket.getData().toString());
            int len=0;  //数据的长度
            while(len==0)     //当数据长度为0则开始接收数据
            {
                socket.receive(packet);
                len=packet.getLength();
                if(len>0)   //指定接收数据的长度
                {
                    output.write(buffer,0,len);
                    output.flush();
                    len=0;   //循环接收
                }
            }
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
