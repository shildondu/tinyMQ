package com.shildon.tinyMQ.core;

/**
 * 请求类。
 * @author shildon<shildondu@gmail.com>
 * @date May 3, 2016
 */
public class MQRequest {
	private MQTransferType mqTransferType;
	private String queueId;
	private Object content;
	
	public MQTransferType getMqTransferType() {
		return mqTransferType;
	}
	public MQRequest setMqTransferType(MQTransferType mqTransferType) {
		this.mqTransferType = mqTransferType;
		return this;
	}
	public String getQueueId() {
		return queueId;
	}
	public MQRequest setQueueId(String queueId) {
		this.queueId = queueId;
		return this;
	}
	public Object getContent() {
		return content;
	}
	public MQRequest setContent(Object content) {
		this.content = content;
		return this;
	}
}
