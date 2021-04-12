import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Test extends Thread
{
    public Test(String Test){super(Test);}
    public void run()
    {
        try {
            sleep(1000);
            File file=new File("C:\\Users\\13995\\Desktop\\fuck\\IP.txt");
            FileInputStream send=new FileInputStream(file);
            //byte[] data=new byte[1024];
            byte[] data = new byte[(int) file.length()];
            send.read(data);
            System.out.println(data);
            String string=new String(data,0,data.length);
            System.out.println(string);
            DatagramPacket packet=new DatagramPacket(data,data.length,InetAddress.getByName("10.63.246.106"),7777);
            DatagramSocket socket=new DatagramSocket(5555);
            socket.send(packet);
            System.out.println("File has been send");
            socket.close();
            send.close();
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
