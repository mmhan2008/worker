package com.cnstock.utils;

import java.util.HashMap;
import java.util.Map;



public class MapUtil {
	public static Map<String,Object> client = new HashMap();
	
	public static MapUtil getInstance(){
		MapUtil re = new MapUtil();
		return re;
	}
	
	public static void add(String key, Object value) {
		client.put(key, value);
	}
	
	public static void delete(String key) {
		client.remove(key);
	}
	
	public static Object get(String key) {
		return client.get(key);
	}

	
	
	
}
