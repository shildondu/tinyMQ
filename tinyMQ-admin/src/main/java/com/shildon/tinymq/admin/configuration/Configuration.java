package com.shildon.tinymq.admin.configuration;

/**
 * @author shildon
 */
public final class Configuration {
    private int port;
    private String nameServers;

    public Configuration() {
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "port=" + port +
                ", nameServers='" + nameServers + '\'' +
                '}';
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNameServers() {
        return nameServers;
    }

    public void setNameServers(String nameServers) {
        this.nameServers = nameServers;
    }
}
