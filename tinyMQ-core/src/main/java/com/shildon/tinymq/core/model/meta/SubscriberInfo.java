package com.shildon.tinymq.core.model.meta;

import java.util.List;

/**
 * @author shildon
 */
public class SubscriberInfo {
    private String name;
    private String ip;
    private List<String> subscribingQueueNames;

    public SubscriberInfo(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "SubscriberInfo{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", subscribingQueueNames=" + subscribingQueueNames +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<String> getSubscribingQueueNames() {
        return subscribingQueueNames;
    }

    public void setSubscribingQueueNames(List<String> subscribingQueueNames) {
        this.subscribingQueueNames = subscribingQueueNames;
    }
}
