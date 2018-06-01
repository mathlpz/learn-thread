package enable12.cacheSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CachedData3 {

	private Map<String, Object> cachedMap = new HashMap<>();

	private ReadWriteLock rwl = new ReentrantReadWriteLock();

	public Object get(String key) {
		rwl.readLock().lock();
		Object value = null;
		try {
			value = cachedMap.get(key);
			if (null == value) {
				rwl.readLock().unlock();
				rwl.writeLock().lock();// 此处有其他线程派对
				try {
					if (null == value) {
						value = this.queryDB(key);// 查询数据库
						cachedMap.put(key, value);
					}
				} finally {
					rwl.writeLock().unlock();
				}
				rwl.readLock().lock();
			}
		} finally {
			rwl.readLock().unlock();
		}
		return value;
	}

	private Object queryDB(String id) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Object();
	}
}