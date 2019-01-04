package com.lpz.zk.zkdist;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模拟服务端连接ZooKeeper
 * 
 * @author lpz
 *
 */
public class DistributedServer {
	
	private final static Logger logger = LoggerFactory.getLogger(DistributedServer.class);
	
	private static final String connectString = "172.28.16.45:2181,172.28.16.53:2181,172.28.16.62:2181";
	private static final int sessionTimeout = 2000;
	private static final String parentNode = "/servers";

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
				// 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
				System.out.println("WatchedEvent:" + event.getType() + "---" + event.getPath());
				logger.info("WatchedEvent:" + event.getType() + "---" + event.getPath());
				try {
					zk.getChildren("/", true);
				} catch (Exception e) {
				}
			}
		});

	}

	/**
	 * 向zk集群注册服务器信息
	 * 
	 * @param hostname
	 * @throws Exception
	 */
	public void registerServer(String hostname) throws Exception {

		String create = zk.create(parentNode + "/server", hostname.getBytes(), 
				Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println(hostname + " is online..." + create);
		logger.info(hostname + " is online..." + create);

	}

	/**
	 * 业务功能
	 * 
	 * @throws InterruptedException
	 */
	public void handleBussiness(String hostname) {
		try {
			System.out.println(hostname + " start working.....");
			logger.info(hostname + " start working.....");
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) throws Exception {

		// 获取zk连接
		DistributedServer server = new DistributedServer();
		server.getConnect();

		// 利用zk连接注册服务器信息
		server.registerServer(args[0]);

		// 启动业务功能
		server.handleBussiness(args[0]);

	}

}
