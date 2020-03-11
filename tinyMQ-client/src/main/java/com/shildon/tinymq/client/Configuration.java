package com.shildon.tinymq.client;

/**
 * @author shildon
 */
public final class Configuration {
    private String host;
    private int port;

    @Override
    public String toString() {
        return "Configuration{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
