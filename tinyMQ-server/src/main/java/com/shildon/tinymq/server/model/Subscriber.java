package com.shildon.tinymq.server.model;

import com.shildon.tinymq.core.model.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shildon
 */
public class Subscriber {
    private String ip;
    // <queue index, offset>
    private List<Pair<Integer, Long>> queueInfos = new ArrayList<>();

    public Subscriber() {

    }

    public Subscriber(String ip) {
        this.ip = ip;
    }

    public long ip2Long() {
        String[] ips = ip.split("\\.");
        long segmentA = Byte.parseByte(ips[0]) << 24;
        long segmentB = Byte.parseByte(ips[1]) << 16;
        long segmentC = Byte.parseByte(ips[2]) << 8;
        long segmentD = Byte.parseByte(ips[3]);
        return segmentA + segmentB + segmentC + segmentD;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "ip='" + ip + '\'' +
                ", queueInfos=" + queueInfos +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public Subscriber setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Subscriber resetQueueInfos() {
        this.queueInfos.clear();
        return this;
    }

    public void addQueueInfo(Pair<Integer, Long> queueInfo) {
        this.queueInfos.add(queueInfo);
    }
}
