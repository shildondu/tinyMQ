package com.shildon.tinymq.nameserver.model;

import java.util.Map;

/**
 * @author shildon
 */
public class TopicInfo {
    private String name;
    private Map<String, QueueInfo> queueInfoMap;
    private Map<String, GroupInfo> groupInfoMap;

    @Override
    public String toString() {
        return "TopicInfo{" +
                "name='" + name + '\'' +
                ", queueInfoMap=" + queueInfoMap +
                ", groupInfoMap=" + groupInfoMap +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, QueueInfo> getQueueInfoMap() {
        return queueInfoMap;
    }

    public void setQueueInfoMap(Map<String, QueueInfo> queueInfoMap) {
        this.queueInfoMap = queueInfoMap;
    }

    public Map<String, GroupInfo> getGroupInfoMap() {
        return groupInfoMap;
    }

    public void setGroupInfoMap(Map<String, GroupInfo> groupInfoMap) {
        this.groupInfoMap = groupInfoMap;
    }
}
