package com.shildon.tinymq.core.model.meta;

import java.util.List;

/**
 * @author shildon
 */
public class ServerInfo {
    private String ip;
    private int port;
    private List<TopicInfo> topicInfos;

    public ServerInfo() {

    }

    public ServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", topicInfos=" + topicInfos +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<TopicInfo> getTopicInfos() {
        return topicInfos;
    }

    public void setTopicInfos(List<TopicInfo> topicInfos) {
        this.topicInfos = topicInfos;
    }
}
