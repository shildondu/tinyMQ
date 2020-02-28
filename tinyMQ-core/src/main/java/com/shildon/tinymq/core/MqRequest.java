package com.shildon.tinymq.core;

/**
 * 请求类。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 3, 2016
 */
public class MqRequest {

	private MqTransferType mqTransferType;
	private String queueId;
	private Object content;

	public MqTransferType getMqTransferType() {
		return this.mqTransferType;
	}

	public MqRequest setMqTransferType(final MqTransferType mqTransferType) {
		this.mqTransferType = mqTransferType;
		return this;
	}

	public String getQueueId() {
		return this.queueId;
	}

	public MqRequest setQueueId(final String queueId) {
		this.queueId = queueId;
		return this;
	}

	public Object getContent() {
		return this.content;
	}

	public MqRequest setContent(final Object content) {
		this.content = content;
		return this;
	}

}
