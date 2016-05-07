package com.shildon.tinyMQ.core;

/**
 * 
 * @author shildon<shildondu@gmail.com>
 * @date May 3, 2016
 */
public class MQResponse {
	private MQTransferType mqTransferType;
	private Object content;

	public MQTransferType getMqTransferType() {
		return mqTransferType;
	}
	public MQResponse setMqTransferType(MQTransferType mqTransferType) {
		this.mqTransferType = mqTransferType;
		return this;
	}
	public Object getContent() {
		return content;
	}
	public MQResponse setContent(Object content) {
		this.content = content;
		return this;
	}
}
