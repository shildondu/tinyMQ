package com.shildon.tinymq.client.configuration;

/**
 * @author shildon
 */
public final class Configuration {
    private String nameServers;
    private String servers;
    private int maxChannelSize;

    @Override
    public String toString() {
        return "Configuration{" +
                "nameServers='" + nameServers + '\'' +
                ", servers='" + servers + '\'' +
                ", maxChannelSize=" + maxChannelSize +
                '}';
    }

    public String getNameServers() {
        return nameServers;
    }

    public void setNameServers(String nameServers) {
        this.nameServers = nameServers;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getMaxChannelSize() {
        return maxChannelSize;
    }

    public void setMaxChannelSize(int maxChannelSize) {
        this.maxChannelSize = maxChannelSize;
    }
}
