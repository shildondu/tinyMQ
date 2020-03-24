package com.shildon.tinymq.server.model;

import com.shildon.tinymq.core.model.Pair;

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

    private String name;
    private List<Queue<byte[]>> queues;
    private int queueSize;
    private AtomicInteger queueIndex = new AtomicInteger(0);
    // <group name, <ip, queue index>>
    private Map<String, Group> groupMap = new ConcurrentHashMap<>();

    public Topic(String name) {
        this(name, 6);
    }

    public Topic(String name, int queueSize) {
        this.name = name;
        this.queueSize = queueSize;
        this.queues = new ArrayList<>(queueSize);
        for (int i = 0; i < queueSize; i++) {
            this.queues.add(new LinkedBlockingQueue<>());
        }
    }

    public void offer(byte[] data) {
        int index = queueIndex.getAndUpdate(it -> (it + 1) % queueSize);
        // round robin
        this.queues.get(index).offer(data);
    }

    public void subscribe(String groupName, String ip) {
        // think about thread safe
        Group group = groupMap.get(groupName);
        if (group == null) {
            group = new Group(groupName);
            groupMap.put(groupName, group);
        }
        if (group.getSubscriberMap().containsKey(ip)) {
            return;
        }
        // re allocate queue to subscriber
        group.getSubscriberMap().putIfAbsent(ip, new Subscriber(ip));
        List<Subscriber> subscribers = group.getSubscriberMap()
                .values()
                .stream()
                .sorted((s1, s2) -> (int) (s1.ip2Long() - s2.ip2Long()))
                .collect(Collectors.toList());
    }

    public void unsubscribe() {

    }

    public String getName() {
        return name;
    }

}
