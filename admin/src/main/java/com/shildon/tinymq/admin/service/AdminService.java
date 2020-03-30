package com.shildon.tinymq.admin.service;

/**
 * @author shildon
 */
public interface AdminService {

    void createTopic(String topicName, int queueSize);

}
