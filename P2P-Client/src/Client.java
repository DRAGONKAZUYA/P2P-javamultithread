public class Client
{
        public static String[] IPArray=new String[20];
        public static void main(String[] args)
        {
            System.out.println("与tracker建立连接");
            new ClientStartThread("ClientStart").start();
            new GetFileFromClient("GetFile").start();
            new SendFileToClient("SendFile").start();
            //当收到其他用户pull请求时给其他用户发送文件夹信息文档
            //获取文件夹信息，更新信息文档，获取其他用户信息文档
            //本机IP[192.168.43.105]
        }
}
//端口9999:   tracker的响应端口
//端口8888:   向tracker发送request、从tracker接收IP消息、向其他用户广播自身上线、响应其他用户上线所发送“hello”消息
//端口7777:   接收其他客户端发送的pull请求、向发送来pull请求的客户端发送文件表
//端口6666:   向其他客户端发送pull请求、接收其他用户端的文件表
//端口5555:   接收从其他用户发送过来的文件
//端口4444:   向请求文件的客户端发送文件
