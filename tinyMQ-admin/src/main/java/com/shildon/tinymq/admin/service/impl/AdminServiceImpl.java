package com.shildon.tinymq.admin.service.impl;

import com.shildon.tinymq.admin.service.AdminService;

/**
 * @author shildon
 */
public class AdminServiceImpl implements AdminService {

    public void createTopic(String topicName, int queueSize) {
        // call name server to get server infos
        // allocate queue to server
        // call message server to create corresponding queue
    }

}
