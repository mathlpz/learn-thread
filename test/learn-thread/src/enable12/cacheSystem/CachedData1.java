package enable12.cacheSystem;

import java.util.HashMap;
import java.util.Map;

public class CachedData1 {

	private Map<String, Object> cachedMap = new HashMap<>();

	//此方法有问题
	public Object get(String key) {
		Object value = cachedMap.get(key);
		if (null == value) {
			value = this.queryDB(key);
			cachedMap.put(key, value);
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