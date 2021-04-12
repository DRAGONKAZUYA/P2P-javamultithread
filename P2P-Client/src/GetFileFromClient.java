import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class GetFileFromClient extends Thread
{
    public GetFileFromClient(String GetFile){super(GetFile);}
    String[] IP;
    public void run()
    {
        try {
            sleep(2000);
            IP=Client.IPArray; //获得所有用户的IP地址
            int IPCount=0;    //创建IP计数器
            while (true)
            {
                sleep(1000);
                System.out.println(IP[IPCount]);
                if(IP[IPCount]==null)    //如果对应的IP地址为空则说明将IP地址全遍历了一遍，此时重置再次遍历
                {
                    IPCount=0;
                    break;
                }
                if(IP[IPCount].equals("192.168.43.105"))
                {
                    IPCount++;
                    continue;
                }
                InetAddress inetAddress=InetAddress.getByName(IP[IPCount]);
                int port=7777;
                int fileport=4444;
                String pull="pull";
                byte[] data=pull.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet=new DatagramPacket(data, data.length,inetAddress,port);
                DatagramSocket socket=new DatagramSocket(6666);
                socket.send(packet);
                System.out.println("成功发送pull请求给 "+inetAddress);
                socket.close();
                String[] FileList=new String[20];
                int count=0;    //count计数，其他客户端有几个文件
                //接收来自其他客户端的文件列表并写入FileList数组
                byte[] FileBuffer=new byte[1024];
                DatagramPacket FileListPacket=new DatagramPacket(FileBuffer,FileBuffer.length);
                DatagramSocket FileListSocket=new DatagramSocket(6666);
                FileListSocket.receive(FileListPacket);
                System.out.println("成功接收到消息");
                FileListSocket.close();
                FileOutputStream fileOutputStream=new FileOutputStream("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\FileList.txt");
                fileOutputStream.write(FileListPacket.getData(),0,FileListPacket.getLength());
                fileOutputStream.close();
                FileReader fileReader=new FileReader("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\FileList.txt");
                BufferedReader bufferedReader=new BufferedReader(fileReader);
                String str;
                if((str=bufferedReader.readLine())!=null)
                {
                    FileList[count]=str;
                    count++;
                }
                //进行共享文件夹同步
                String path="C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\share";
                //共享文件夹路径名
                File share=new File(path);
                File[] array=share.listFiles();     //array存储本地文件夹中的所有文件名
                DatagramSocket FileReceiveSocket=new DatagramSocket(5555);
                for(int i=0;i<count;i++)    //循环比对文件名
                {
                    boolean flag=false;     //flag一开始值为false，如果比对成功则变成true
                    for(int j=0;j<array.length;j++)
                    {
                        if(FileList[i].equals(array[j].getName()))
                            flag=true;
                        if(flag==false)     //如果检索完本地所有文件发现没有该文件则说明该文件在本机不存在，需要请求同步
                        {
                            byte[] NeedFile=FileList[i].getBytes(StandardCharsets.UTF_8);
                            DatagramPacket FilePacket=new DatagramPacket(NeedFile,NeedFile.length,inetAddress,fileport);
                            FileReceiveSocket.send(FilePacket);
                            System.out.println("需要文件："+FileList[i]);
                            //new ReceiveFile(FileReceiveSocket,FileList[i]).start();
                            File Receive=new File("C:\\Users\\13995\\Desktop\\2021春\\计算机网络\\实验\\第四、五周-P2P文件传输\\share\\"+FileList[i]);   //接收的文件的存放路径
                            FileOutputStream output=new FileOutputStream(Receive);
                            byte[] size=new byte[1024];
                            byte[] buffer=new byte[2048];
                            DatagramPacket SizePacket=new DatagramPacket(size,size.length);
                            DatagramPacket ReceiveFilePacket=new DatagramPacket(buffer,buffer.length);
                            FileReceiveSocket.receive(SizePacket);
                            String FileSize=new String(SizePacket.getData(),0,SizePacket.getLength());
                            System.out.println("文件的大小为："+FileSize);
                            int len=0;  //数据的长度
                            while(len==0)     //当数据长度为0则开始接收数据
                            {
                                FileReceiveSocket.receive(ReceiveFilePacket);
                                len=ReceiveFilePacket.getLength();
                                if(len>0)   //指定接收数据的长度
                                {
                                    output.write(buffer,0,len);
                                    output.flush();
                                    len=0;   //循环接收
                                }
                            }
                        }
                        flag=false;
                    }
                }
                FileReceiveSocket.close();
                byte[] over="over".getBytes(StandardCharsets.UTF_8);
                DatagramPacket OverPacket=new DatagramPacket(over,over.length,inetAddress,fileport);
                DatagramSocket OverSocket=new DatagramSocket(4444);
                OverSocket.send(OverPacket);
                OverSocket.close();
                /*IPCount++;*/
            }
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
