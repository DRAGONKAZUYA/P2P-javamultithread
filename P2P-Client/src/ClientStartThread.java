import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ClientStartThread extends Thread
{
    public ClientStartThread(String ClientStart)
    {
        super(ClientStart);
    }
    public void run()
    {
        try {
            InetAddress address=InetAddress.getByName("192.168.43.246");
            int port=9999;
            System.out.println("ClintStartThread Start~");
                String send="request";
                byte[] data=send.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet=new DatagramPacket(data, data.length,address,port);
                DatagramSocket socket=new DatagramSocket(8888);
                socket.send(packet);
                socket.close();
                //接收IP.txt
                File Receive=new File("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\IP.txt");   //接收的文件的存放路径
                FileWriter fileWriter=new FileWriter(Receive);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
                FileOutputStream output=new FileOutputStream(Receive,true);
                byte[] buffer=new byte[1024];
                DatagramPacket datagramPacket= new DatagramPacket(buffer,0,buffer.length);
                DatagramSocket datagramSocket= new DatagramSocket(8888);
                datagramSocket.receive(datagramPacket);
                System.out.println("Get packet from tracker");
                byte[] IPdata=datagramPacket.getData();
                output.write(IPdata,0,IPdata.length);
                System.out.println("message has been written");
                datagramSocket.close();
                output.close();
                System.out.println("向其他用户发送自身上线消息");
                new Report("Report").start();
                //开始接收来自其他客户端的上线消息
                sleep(1000);
                System.out.println("ResponseThread Start~");
                System.out.println("启动文件夹同步线程");
                while (true)
                {
                    byte[] data1=new byte[1024];
                    DatagramPacket packet1=new DatagramPacket(data1,0,data1.length);
                    DatagramSocket socket1=new DatagramSocket(8888);
                    socket1.receive(packet1);
                    String message=new String(packet1.getData(),0, packet1.getLength());
                    if(message.equals("hello"))
                    {
                        String Filename="C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\IP.txt";
                        FileWriter FW=new FileWriter(Filename,true);
                        String Address=packet1.getAddress().getHostAddress();
                        int p=packet1.getPort();
                        System.out.println("成功收到地址："+Address+" 端口："+p+"的上线消息");
                        for (int i=0;i<Client.IPArray.length;i++)
                        {
                            if (Client.IPArray[i]==null)
                            {
                                Client.IPArray[i]=Address;
                                break;
                            }
                        }
                        FW.write("\n");
                        FW.write("['"+Address+"', "+p+']');   //获得IP和端口并写入
                        FW.close();
                        String OK="ok";
                        byte[] reply=OK.getBytes(StandardCharsets.UTF_8);
                        DatagramPacket ReplyPacket=new DatagramPacket(reply,reply.length,packet.getAddress(),p);
                        socket1.send(ReplyPacket);
                        socket1.close();
                    }
                    if(message.equals("close ReceiveThread"))
                    {
                        socket.close();
                        System.out.println("Stop Receiving");
                        break;
                    }
                }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
