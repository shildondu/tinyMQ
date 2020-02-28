package com.shildon.tinymq.core;

/**
 * 进行序列化的实体类，之所以要用该类对内容进行包装是为了方便序列化。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 6, 2016
 */
public class MqEntity {

	private Class<?> clazz;
	private Object content;

	public Class<?> getClazz() {
		return this.clazz;
	}

	public MqEntity setClazz(final Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

	public Object getContent() {
		return this.content;
	}

	public MqEntity setContent(final Object content) {
		this.content = content;
		return this;
	}

}
