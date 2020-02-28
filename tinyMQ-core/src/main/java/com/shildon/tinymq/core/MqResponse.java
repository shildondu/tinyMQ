package com.shildon.tinymq.core;

/**
 * 响应类。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 3, 2016
 */
public class MqResponse {

	private MqTransferType mqTransferType;
	private Object content;

	public MqTransferType getMqTransferType() {
		return this.mqTransferType;
	}

	public MqResponse setMqTransferType(final MqTransferType mqTransferType) {
		this.mqTransferType = mqTransferType;
		return this;
	}

	public Object getContent() {
		return this.content;
	}

	public MqResponse setContent(final Object content) {
		this.content = content;
		return this;
	}

}
