package com.shildon.tinymq.core.exchange;

import com.shildon.tinymq.core.protocol.MessageProtocol;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shildon
 */
public class MessageFuture {

    private Lock lock = new ReentrantLock();
    private Condition done = lock.newCondition();

    private MessageProtocol response;

    private MessageProtocol get() throws InterruptedException {
        lock.lock();
        try {
            while (response == null) {
                done.await();
            }
            return response;
        } finally {
            lock.unlock();
        }
    }

    private void set(MessageProtocol response) {
        lock.lock();
        try {
            this.response = response;
            if (done != null) {
                done.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

}
