package com.shildon.tinymq.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author shildon
 */
public class Topic {

    private final String name;
    private final List<Queue<byte[]>> queues;
    private final int queueSize;
    private final AtomicInteger queueIndex = new AtomicInteger(0);
    // <group name, <ip, queue index>>
    private final Map<String, Group> groupMap = new ConcurrentHashMap<>();

    public Topic(final String name) {
        this(name, 6);
    }

    public Topic(final String name, final int queueSize) {
        this.name = name;
        this.queueSize = queueSize;
        this.queues = new ArrayList<>(queueSize);
        for (int i = 0; i < queueSize; i++) {
            this.queues.add(new LinkedBlockingQueue<>());
        }
    }

    public void offer(final byte[] data) {
        final int index = this.queueIndex.getAndUpdate(it -> (it + 1) % this.queueSize);
        // round robin
        this.queues.get(index).offer(data);
    }

    public void subscribe(final String groupName, final String ip) {
        // think about thread safe
        Group group = this.groupMap.get(groupName);
        if (group == null) {
            group = new Group(groupName);
            this.groupMap.put(groupName, group);
        }
        if (group.getSubscriberMap().containsKey(ip)) {
            return;
        }
        // re allocate queue to subscriber
        group.getSubscriberMap().putIfAbsent(ip, new Subscriber(ip));
        final List<Subscriber> subscribers = group.getSubscriberMap()
                .values()
                .stream()
                .sorted((s1, s2) -> (int) (s1.ip2Long() - s2.ip2Long()))
                .collect(Collectors.toList());
    }

    public void unsubscribe() {

    }

    public String getName() {
        return this.name;
    }

}
