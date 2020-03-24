package com.shildon.tinymq.core.exception;

/**
 * 没有该topic时抛出的异常
 *
 * @author shildon
 */
public class NoSuchTopicException extends RuntimeException {

    public NoSuchTopicException() {

    }

    public NoSuchTopicException(String message) {
        super(message);
    }

    public NoSuchTopicException(String message, Throwable cause) {
        super(message, cause);
    }

}
