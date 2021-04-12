import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class SendFileToClient extends Thread
{
    public SendFileToClient(String SendFileToClient){super(SendFileToClient);}
    public void run()
    {
        try {
            sleep(1000);
            System.out.println("ReceivePullThread Start~");
            while (true)    //循环监听其他客户端发来的pull请求
            {
                byte[] pull=new byte[10];
                DatagramPacket PullPacket=new DatagramPacket(pull,pull.length);
                DatagramSocket PullSocket=new DatagramSocket(7777);
                PullSocket.receive(PullPacket);
                InetAddress address=PullPacket.getAddress();
                String str=new String(PullPacket.getData(),0,PullPacket.getLength());
                PullSocket.close();
                if(str.equals("pull"))      //如果收到pull请求则发送文件表，并监听其他客户端所请求的文件
                {
                    System.out.println("接收到来自地址："+address.getHostAddress()+"的pull请求");
                    String path="C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\share";
                    //共享文件夹路径名
                    File share=new File(path);
                    File[] array=share.listFiles();     //array存储本地文件夹中的所有文件名
                    File FileList=new File("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\List.txt");
                    FileWriter fileWriter=new FileWriter(FileList);
                    if(array!=null)
                    {
                        for (File file : array) {
                            fileWriter.write(file.getName());
                            fileWriter.write("\n");
                        }
                    }
                    fileWriter.close();
                    DatagramSocket ListSocket=new DatagramSocket(7777);
                    String list="List.txt";
                    System.out.println("开始传输文件"+list);
                    File source=new File("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\"+list);       //要传输的文件的路径
                    InputStream inputStream=new FileInputStream(source);
                    byte[] data=new byte[1024];
                    while (inputStream.read(data)!=-1) {
                        DatagramPacket packet = new DatagramPacket(data, data.length, address,6666);
                        ListSocket.send(packet);
                    }
                    ListSocket.close();
                    System.out.println("成功向地址："+address+"发送文件"+list);
                    //new SendFile(ListSocket,address,"List.txt",6666).start();
                    System.out.println("准备发送其他客户端的缺少文件");
                    while (true)
                    {
                        DatagramSocket NameSocket=new DatagramSocket(4444);
                        byte[] FileName=new byte[1024];
                        DatagramPacket NamePacket=new DatagramPacket(FileName, FileName.length);
                        NameSocket.receive(NamePacket);
                        String name=new String(NamePacket.getData(),0,NamePacket.getLength());  //获得请求文件的名称
                        if(name.equals("over"))     //如果收到的文件消息是over则说明客户端请求完毕
                        {
                            System.out.println("同步完成");
                            break;
                        }
                        System.out.println("需要文件:"+name);
                        new SendFile(NameSocket,address,name,5555).start();
                        NameSocket.close();
                    }
                }
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
