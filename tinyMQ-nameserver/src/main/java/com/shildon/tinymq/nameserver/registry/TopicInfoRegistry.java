package com.shildon.tinymq.nameserver.registry;

import com.shildon.tinymq.core.model.meta.ServerInfo;
import com.shildon.tinymq.core.model.meta.SubscriberInfo;

/**
 * @author shildon
 */
public interface TopicInfoRegistry {

    void register(ServerInfo serverInfo);

    ServerInfo getPublishServerInfo(String topicName);

    ServerInfo getSubscribeServerInfo(String topicName, SubscriberInfo subscriberInfo);

}
