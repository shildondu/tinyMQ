package com.shildon.tinymq.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取Properties文件工具类。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 2, 2016
 */
public final class PropertiesUtils {

	private static Properties properties;

	private PropertiesUtils() {}

	public static void load(final String fileName) {
		properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().
					getResourceAsStream(fileName));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void load(final InputStream inputStream) {
		properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static Object getValue(final Object key) {
		return properties.get(key);
	}

}
