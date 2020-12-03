package com.shildon.tinymq.server.model;

import java.util.ArrayList;
import java.util.List;

import com.shildon.tinymq.core.model.Pair;
import io.netty.channel.Channel;

/**
 * @author shildon
 */
public class Subscriber {
    private String topic;
    private String group;
    private Channel channel;
    private String ip;
    // <queue index, offset>
    private final List<Pair<Integer, Long>> queueInfos = new ArrayList<>();

    public Subscriber(final String topic, final String group, final Channel channel) {
        this.topic = topic;
        this.group = group;
        this.channel = channel;
    }

    public Subscriber(final String ip) {
        this.ip = ip;
    }

    public long ip2Long() {
        final String[] ips = this.ip.split("\\.");
        final long segmentA = Byte.parseByte(ips[0]) << 24;
        final long segmentB = Byte.parseByte(ips[1]) << 16;
        final long segmentC = Byte.parseByte(ips[2]) << 8;
        final long segmentD = Byte.parseByte(ips[3]);
        return segmentA + segmentB + segmentC + segmentD;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "ip='" + this.ip + '\'' +
                ", queueInfos=" + this.queueInfos +
                '}';
    }

    public String getIp() {
        return this.ip;
    }

    public Subscriber setIp(final String ip) {
        this.ip = ip;
        return this;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public Subscriber resetQueueInfos() {
        this.queueInfos.clear();
        return this;
    }

    public void addQueueInfo(final Pair<Integer, Long> queueInfo) {
        this.queueInfos.add(queueInfo);
    }
}
