package com.shildon.tinymq.server.model;

import java.net.InetSocketAddress;
import java.util.Objects;

import io.netty.channel.Channel;

/**
 * @author shildon
 */
public class Subscriber {
    private String topic;
    private String group;
    private Channel channel;
    private String ip;

    public Subscriber(final String topic, final String group, final Channel channel) {
        this.topic = topic;
        this.group = group;
        this.channel = channel;
        this.ip = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Subscriber that = (Subscriber) o;
        return Objects.equals(this.topic, that.topic) &&
                Objects.equals(this.group, that.group) &&
                Objects.equals(this.channel, that.channel) &&
                Objects.equals(this.ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.topic, this.group, this.channel, this.ip);
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "topic='" + this.topic + '\'' +
                ", group='" + this.group + '\'' +
                ", channel=" + this.channel +
                ", ip='" + this.ip + '\'' +
                '}';
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }
}
