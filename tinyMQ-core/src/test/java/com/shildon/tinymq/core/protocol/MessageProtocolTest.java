package com.shildon.tinymq.core.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;

public class MessageProtocolTest {

    @Test
    public void test() {
        MessageProtocol encodeProtocol = new MessageProtocol.Builder()
                .header(
                        new MessageHeader.Builder()
                                .messageType(MessageType.ACK)
                                .build()
                )
                .body(
                        new MessageBody.Builder()
                                .build()
                )
                .build();

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

        encodeProtocol.encode(byteBuf);

        MessageProtocol decodeProtocol = new MessageProtocol.ByteBufBuilder()
                .build(byteBuf);

        System.out.println(decodeProtocol);
        Assert.assertEquals(encodeProtocol.getHeader().getMessageType(), decodeProtocol.getHeader().getMessageType());
    }

}
