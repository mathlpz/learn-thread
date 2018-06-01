package enable12.cacheSystem;

import java.util.HashMap;
import java.util.Map;

public class CachedData2 {

	private Map<String, Object> cachedMap = new HashMap<>();

	//没问题，但是效率不高
	public synchronized Object get(String key) {
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