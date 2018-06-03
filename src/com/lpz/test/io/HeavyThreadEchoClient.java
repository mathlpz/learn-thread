package com.lpz.test.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * 主线程创建10个子线程去请求server:这是个模拟网络拥堵时的客户端socket，每打一个字符就会停1秒。
 * 这样服务端的线程也要等待，这样服务器端的资源浪费的就很多。
 * @author lpz
 *
 */
public class HeavyThreadEchoClient {

	static ExecutorService es= Executors.newCachedThreadPool();
    static Long sleep_time=1000*1000*1000L;
    public static class EchoClient implements Runnable{
        @Override
        public void run() {

            Socket client = null;
            PrintWriter writer = null;
            BufferedReader reader = null;
            try {
                client=new Socket();
                client.connect(new InetSocketAddress("localhost",8000));
                writer=new PrintWriter(client.getOutputStream(),true);
                writer.print("h");
                LockSupport.parkNanos(sleep_time);
                writer.print("e");
                LockSupport.parkNanos(sleep_time);

                writer.print("l");
                LockSupport.parkNanos(sleep_time);

                writer.print("l");
                LockSupport.parkNanos(sleep_time);

                writer.print("o");
                LockSupport.parkNanos(sleep_time);

                writer.print("!");
                LockSupport.parkNanos(sleep_time);

                writer.println();
                writer.flush();
                reader=new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("from server:"+reader.readLine());
            }catch (UnknownHostException ex){
                ex.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } finally {
                if(writer!=null){
                    writer.close();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }

                }
                if(client!=null){
                    try {
                        client.close();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        EchoClient ec=new EchoClient();
        for(int i=0;i<10;i++){
            es.execute(ec);
        }
    }
	
	
}
