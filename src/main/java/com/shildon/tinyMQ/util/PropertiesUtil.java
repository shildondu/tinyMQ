package com.shildon.tinyMQ.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author shildon<shildondu@gmail.com>
 * @date May 2, 2016
 */
public class PropertiesUtil {

	private static Properties properties;
	
	public static void load(String fileName) {
		properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().
					getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void load(InputStream inputStream) {
		properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object getValue(Object key) {
		return properties.get(key);
	}

}
