package com.shildon.tinyMQ.storage;

import com.shildon.tinyMQ.core.MQEntity;
import com.shildon.tinyMQ.util.PropertiesUtil;
import com.shildon.tinyMQ.util.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * Redis存储操作。
 * @author shildon<shildondu@gmail.com>
 * @date May 3, 2016
 */
public class RedisTemplate {
	// redis://:auth@localhost:6380/0~15
//	private String uri;
	private String host;
	private int port;
	private int timeout;
	private int select;

	private JedisPool jedisPool;
	
	public RedisTemplate() {
		init();
	}
	
	private void init() {
		PropertiesUtil.load("redis.properties");
		Object hostValue = PropertiesUtil.getValue("redis.host");
		Object portValue = PropertiesUtil.getValue("redis.port");
		Object timeoutValue = PropertiesUtil.getValue("redis.timeout");
		Object selectValue = PropertiesUtil.getValue("redis.select");

		if (null != hostValue) {
			host = (String) hostValue;
		}
		if (null != portValue) {
			port = Integer.valueOf((String) portValue);
		}
		if (null != timeoutValue) {
			timeout = Integer.valueOf((String) timeoutValue);
		}
		if (null != selectValue) {
			select = Integer.valueOf((String) selectValue);
		}
		
		if (0 != timeout) {
			jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, null, select);
		} else {
			jedisPool = new JedisPool(new JedisPoolConfig(), host, port,
					Protocol.DEFAULT_TIMEOUT, null, select);
		}
	}
	
	public boolean offer(String queueId, Object content) {
		try (Jedis jedis = jedisPool.getResource()) {
			byte[] queueIdByte = queueId.getBytes();
			MQEntity mqEntity = new MQEntity().setClazz(content.getClass()).
					setContent(content);
			byte[] contentByte = SerializeUtil.serialize(mqEntity);
			jedis.rpush(queueIdByte, contentByte);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Object poll(String queueId) {
		MQEntity mqEntity = null;
		try (Jedis jedis = jedisPool.getResource()) {
			byte[] queueIdByte = queueId.getBytes();
			byte[] contentByte = jedis.lpop(queueIdByte);
			mqEntity = SerializeUtil.deserialize(contentByte, MQEntity.class);
			return mqEntity.getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mqEntity.getContent();
	}

}
