package com.shildon.tinymq.nameserver.model;

/**
 * @author shildon
 */
public class QueueInfo {
    private String name;

    @Override
    public String toString() {
        return "QueueInfo{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
