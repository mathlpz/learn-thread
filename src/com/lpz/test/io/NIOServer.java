package com.lpz.test.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
/**
 * NIO服务端
 * 
 * 
 * BIO：同步阻塞式IO，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销，当然可以通过线程池机制改善。 
NIO：同步非阻塞式IO，服务器实现模式为一个请求一个线程，即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有I/O请求时才启动一个线程进行处理。 
AIO(NIO.2)：异步非阻塞式IO，服务器实现模式为一个有效请求一个线程，客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理。

 * 各自应用场景 
到这里你也许已经发现，一旦有请求到来(不管是几个同时到还是只有一个到)，都会调用对应IO处理函数处理，所以：
（1）NIO适合处理连接数目特别多，但是连接比较短（轻操作）的场景，Jetty，Mina，ZooKeeper等都是基于Java nio实现。
（2）BIO方式适用于连接数目比较小且固定的场景，这种方式对服务器资源要求比较高，并发局限于应用中。
 * 
 * 
 * 
 * @author lpz
 *
 */
public class NIOServer {    
    //通道管理器    
    private Selector selector;    
    
    /**  
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作  
     * @param port  绑定的端口号  
     * @throws IOException  
     */    
    public void initServer(int port) throws IOException {    
       // 获得一个ServerSocket通道    
        ServerSocketChannel serverChannel = ServerSocketChannel.open();    
        // 设置通道为非阻塞    
       serverChannel.configureBlocking(false);    
       // 将该通道对应的ServerSocket绑定到port端口    
        serverChannel.socket().bind(new InetSocketAddress(port));    
        // 获得一个通道管理器    
       this.selector = Selector.open();    
       //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，    
       //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。    
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);    
    }
    
    
    /**  
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理  
     * @throws IOException  
     */    
    @SuppressWarnings("unchecked")    
    public void listen() throws IOException {    
        System.out.println("服务端启动成功！");    
        // 轮询访问selector    
        while (true) {    
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞    
            selector.select();    
            // 获得selector中选中的项的迭代器，选中的项为注册的事件    
            Iterator ite = this.selector.selectedKeys().iterator();    
            while (ite.hasNext()) {    
                SelectionKey key = (SelectionKey) ite.next();    
                // 删除已选的key,以防重复处理    
                ite.remove();    
                // 客户端请求连接事件    
                if (key.isAcceptable()) {    
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();    
                    // 获得和客户端连接的通道    
                    SocketChannel channel = server.accept();    
                    // 设置成非阻塞    
                    channel.configureBlocking(false);    
    
                    //在这里可以给客户端发送信息哦    
                    channel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes()));    
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。    
                    channel.register(this.selector, SelectionKey.OP_READ);    
                        
                    // 获得了可读的事件    
                } else if (key.isReadable()) {    
                        read(key);    
                }    
    
            }    
    
        }    
    }    
    
    /**  
     * 处理读取客户端发来的信息 的事件  
     * @param key  
     * @throws IOException   
     */    
    public void read(SelectionKey key) throws IOException{    
        // 服务器可读取消息:得到事件发生的Socket通道    
        SocketChannel channel = (SocketChannel) key.channel();    
        // 创建读取的缓冲区    
        ByteBuffer buffer = ByteBuffer.allocate(10);    
        channel.read(buffer);    
        byte[] data = buffer.array();    
        String msg = new String(data).trim();    
        System.out.println("服务端收到信息："+msg);    
        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());    
        channel.write(outBuffer);// 将消息回送给客户端    
    }  
    
    /**  
     * 启动服务端测试  
     * @throws IOException   
      */    
     public static void main(String[] args) throws IOException {    
         NIOServer server = new NIOServer();    
         server.initServer(8000);    
         server.listen();    
     }    
     
    
}