package com.shildon.tinymq.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
    private final Map<Integer, Set<Subscriber>> subscribers = new ConcurrentHashMap<>();

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

    public int offer(final byte[] data) {
        final int index = this.queueIndex.getAndUpdate(it -> (it + 1) % this.queueSize);
        // round robin
        this.queues.get(index).offer(data);
        return index;
    }

    public Set<Subscriber> getSubscribers(final int queueIndex) {
        return this.subscribers.get(queueIndex);
    }

    private boolean hasSubscribed(final Subscriber subscriber) {
        return this.subscribers.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .contains(subscriber);
    }

    private Set<Subscriber> getSameGroupSubscribers(final Subscriber subscriber) {
        final Set<Subscriber> sameGroupSubscribers = new HashSet<>();
        sameGroupSubscribers.add(subscriber);
        this.subscribers.values()
                .forEach(it -> {
                    final Iterator<Subscriber> iterator = it.iterator();
                    while (iterator.hasNext()) {
                        final Subscriber current = iterator.next();
                        if (current.getGroup().equals(subscriber.getGroup())) {
                            iterator.remove();
                            sameGroupSubscribers.add(current);
                        }
                    }
                });
        return sameGroupSubscribers;
    }

    public void subscribe(final Subscriber subscriber) {
        if (this.hasSubscribed(subscriber)) {
            return;
        }
        final Set<Subscriber> sameGroupSubscribers = this.getSameGroupSubscribers(subscriber);
        this.reBalance(sameGroupSubscribers);
    }

    private void reBalance(Set<Subscriber> sameGroupSubscribers) {
    }

    public void unsubscribe() {

    }

    public String getName() {
        return this.name;
    }

}
