package com.shildon.tinymq.core.protocol;

import com.shildon.tinymq.core.util.MessageIdUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

public class MessageProtocolTest {

    @Test
    public void test() {
        MessageHeader header = new MessageHeader();
        header.setMessageId(MessageIdUtils.generate());
        header.setMessageType(MessageType.ACK.getValue());
        header.setTimestamp(System.currentTimeMillis());
        MessageBody body = new MessageBody();
        MessageProtocol encodeProtocol = new MessageProtocol(header, body);

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

        encodeProtocol.encode(byteBuf);

        MessageProtocol decodeProtocol = new MessageProtocol(byteBuf);
        System.out.println(decodeProtocol);
    }

}
