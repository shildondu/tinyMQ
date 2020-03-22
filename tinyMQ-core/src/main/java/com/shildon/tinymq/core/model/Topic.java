package com.shildon.tinymq.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shildon
 */
public class Topic {

    private List<Queue<byte[]>> queues;
    private int queueSize;
    private AtomicInteger queueIndex = new AtomicInteger(0);

    public Topic(int queueSize) {
        this.queueSize = queueSize;
        this.queues = new ArrayList<>(queueSize);
        for (int i = 0; i < queueSize; i++) {
            this.queues.add(new LinkedBlockingQueue<>());
        }
    }

    public void offer(byte[] data) {
        // round robin
        this.queues.get(this.queueIndex.getAndIncrement()).offer(data);
    }

}
