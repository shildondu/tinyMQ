package com.shildon.tinymq.server.codec;

import com.shildon.tinymq.constant.Constant;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author shildon
 */
public class MessageFrameEncoder extends LengthFieldPrepender {

    public MessageFrameEncoder() {
        super(Constant.LENGTH_FIELD_LENGTH);
    }

}
