package com.lpz.test.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * 在计算机系统中，最不可靠的就是网络请求，我们通过服务器端给客户端echo信息（客户端请求什么信息服务端就返回给客户端什么信息）。比较两种socket io的优劣。
 * 
 * 主线程负责不断地请求echoServer.accept()，如果没有客户端请求主线程会阻塞，当有客户端请求服务器端时，主线程会用线程池新创建一个线程执行。
 * 也就是说一个线程负责一个客户端socket，当一个客户端socket因为网络延迟时，服务器端负责这个客户端的线程就会等待，浪费资源。
 * @author lpz
 *
 */
public class MultiThreadEchoServer {
	
	private static ExecutorService tp = Executors.newCachedThreadPool();
    static class HandleMsg implements Runnable{
        Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            BufferedReader is = null;
            PrintWriter os = null;
            try {
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintWriter(clientSocket.getOutputStream(),true);
                //从InputStream当中读取客户端所发送的数据
                String inputLine=null;
                long b=System.currentTimeMillis();
                while ((inputLine=is.readLine())!=null){
                    os.println(inputLine);
                }
                long e=System.currentTimeMillis();
                System.out.println("spend:"+(e-b)+"ms");
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    if(is!=null) is.close();
                    if(os!=null) os.close();
                    clientSocket.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

	    public static void main(String[] args) {
	        ServerSocket echoServer=null;
	        Socket clientSocket=null;
	        try {
	            echoServer=new ServerSocket(8000);
	        }catch (IOException e){
	            System.out.println(e);
	        }
	        while (true){
	            try {
	                clientSocket =echoServer.accept();//阻塞
	                System.out.println(clientSocket.getRemoteSocketAddress()+" connect!"+System.currentTimeMillis());
	                tp.execute(new HandleMsg(clientSocket));  
	            }catch (IOException e){  
	                System.out.println(e);  
	            }  
	        }  
	    }  
	
	
}

