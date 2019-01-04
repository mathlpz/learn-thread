package com.lpz.zk.zkdist;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模拟客户端连接ZooKeeper
 * @author lpz
 *
 */
public class DistributedClient {
	
	private final static Logger logger = LoggerFactory.getLogger(DistributedClient.class);

	private static final String connectString = "172.28.16.45:2181,172.28.16.53:2181,172.28.16.62:2181";
	private static final int sessionTimeout = 2000;
	private static final String parentNode = "/servers";
	
	// 注意:加volatile的意义何在？
	private volatile List<String> serverList;
	// 定义ZooKeeper客户端连接服务器
	private ZooKeeper zk = null;

	/**
	 * 创建到zk的客户端连接
	 * 
	 * @throws Exception
	 */
	public void getConnect() throws Exception {

		zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("WatchedEvent:" + event.getType() + "---" + event.getPath());
				logger.info("WatchedEvent:" + event.getType() + "---" + event.getPath());
				// 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
				try {
					//重新更新服务器列表，并且注册了监听
					getServerList();

				} catch (Exception e) {
				}
			}
		});

	}

	/**
	 * 获取服务器信息列表
	 * 
	 * @throws Exception
	 */
	public void getServerList() throws Exception {

		// 获取服务器子节点信息，并且对父节点进行监听
		List<String> children = zk.getChildren(parentNode, true);

		// 先创建一个局部的list来存服务器信息
		List<String> servers = new ArrayList<String>();
		for (String child : children) {
			// child只是子节点的节点名
			byte[] data = zk.getData(parentNode + "/" + child, false, null);
			servers.add(new String(data));
		}
		// 把servers赋值给成员变量serverList，已提供给各业务线程使用
		serverList = servers;
		
		//打印服务器列表
		System.out.println(serverList.isEmpty() ? "" :String.join(",", serverList));
		logger.info(serverList.isEmpty() ? "" :String.join(",", serverList));
	}

	/**
	 * 业务功能
	 * 
	 * @throws InterruptedException
	 */
	public void handleBussiness() {
		try {
			System.out.println("client start working.....");
			logger.info("client start working.....");
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void main(String[] args) throws Exception {

		// 获取zk连接
		DistributedClient client = new DistributedClient();
		client.getConnect();
		// 获取servers的子节点信息（并监听），从中获取服务器信息列表
		client.getServerList();

		// 业务线程启动
		client.handleBussiness();
		
	}

}
