package com.shildon.tinymq.nameserver.model;

import java.util.List;

/**
 * @author shildon
 */
public class GroupInfo {
    private String name;
    private List<SubscriberInfo> subscriberInfos;

    @Override
    public String toString() {
        return "GroupInfo{" +
                "name='" + name + '\'' +
                ", subscriberInfos=" + subscriberInfos +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubscriberInfo> getSubscriberInfos() {
        return subscriberInfos;
    }

    public void setSubscriberInfos(List<SubscriberInfo> subscriberInfos) {
        this.subscriberInfos = subscriberInfos;
    }
}
