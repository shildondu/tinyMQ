package com.shildon.tinymq.storage;

import com.shildon.tinymq.core.MqEntity;
import com.shildon.tinymq.util.PropertiesUtils;
import com.shildon.tinymq.util.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	//	redis://:auth@localhost:6380/0~15
	//	private String uri;
	private String host;
	private int port;
	private int timeout;
	private int select;

	private JedisPool jedisPool;

	// 饿汉式单例模式
	private static final RedisTemplate INSTANCE = new RedisTemplate();

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisTemplate.class);

	public static RedisTemplate getInstance() {
		return INSTANCE;
	}

	private RedisTemplate() {
		this.init();
	}
	
	private void init() {
		PropertiesUtils.load("redis.properties");
		final Object hostValue = PropertiesUtils.getValue("redis.host");
		final Object portValue = PropertiesUtils.getValue("redis.port");
		final Object timeoutValue = PropertiesUtils.getValue("redis.timeout");
		final Object selectValue = PropertiesUtils.getValue("redis.select");

		if (null != hostValue) {
			this.host = (String) hostValue;
		}
		if (null != portValue) {
			this.port = Integer.parseInt((String) portValue);
		}
		if (null != timeoutValue) {
			this.timeout = Integer.parseInt((String) timeoutValue);
		}
		if (null != selectValue) {
			this.select = Integer.parseInt((String) selectValue);
		}

		if (0 != this.timeout) {
			this.jedisPool = new JedisPool(new JedisPoolConfig(), this.host, this.port, this.timeout, null, this.select);
		} else {
			this.jedisPool = new JedisPool(new JedisPoolConfig(), this.host, this.port,
					Protocol.DEFAULT_TIMEOUT, null, this.select);
		}
	}

	public boolean offer(final String queueId, final Object content) {
		try (final Jedis jedis = this.jedisPool.getResource()) {
			final byte[] queueIdByte = queueId.getBytes();
			final MqEntity mqEntity = new MqEntity().setClazz(content.getClass()).
					setContent(content);
			final byte[] contentByte = SerializeUtils.serialize(mqEntity);
			jedis.rpush(queueIdByte, contentByte);
		} catch (final Exception e) {
			LOGGER.error("Offer to MQ fail!", e);
			return false;
		}
		return true;
	}

	public Object poll(final String queueId) {
		MqEntity mqEntity = null;
		try (final Jedis jedis = this.jedisPool.getResource()) {
			final byte[] queueIdByte = queueId.getBytes();
			final byte[] contentByte = jedis.lpop(queueIdByte);
			if (null != contentByte) {
				mqEntity = SerializeUtils.deserialize(contentByte, MqEntity.class);
				return mqEntity.getContent();
			} else {
				return null;
			}
		} catch (final Exception e) {
			LOGGER.error("Poll from MQ fail!", e);
		}
		return mqEntity.getContent();
	}

}
