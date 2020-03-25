package com.shildon.tinymq.nameserver.registry;

import com.shildon.tinymq.nameserver.model.ServerInfo;
import com.shildon.tinymq.nameserver.model.SubscriberInfo;

/**
 * @author shildon
 */
public interface TopicInfoRegistry {

    void register(ServerInfo serverInfo);

    ServerInfo getPublishServerInfo(String topicName);

    ServerInfo getSubscribeServerInfo(String topicName, SubscriberInfo subscriberInfo);

}
