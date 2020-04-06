package com.shildon.tinymq.server.configuration;

import com.shildon.tinymq.core.model.Pair;
import com.shildon.tinymq.core.util.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author shildon
 */
public final class ConfigurationHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationHolder.class);

    private static final URL URL = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("configuration.yml"));

    private static final ConfigurationHolder INSTANCE = new ConfigurationHolder();

    private Configuration configuration;

    private ConfigurationHolder() {
        try {
            this.configuration = ConfigurationUtils.load(URL, Configuration.class);
        } catch (IOException e) {
            LOGGER.error("load configuration failed! url: {}", URL.getPath(), e);
        }
    }

    public static ConfigurationHolder getInstance() {
        return INSTANCE;
    }

    public List<Pair<String, Integer>> getNameServerInfos() {
        return Arrays.stream(this.configuration.getNameServers().split(","))
                .map(it -> {
                    String[] serverInfo = it.split(":");
                    return new Pair<>(serverInfo[0], Integer.valueOf(serverInfo[1]));
                })
                .collect(Collectors.toList());
    }

    public int getPort() {
        return this.configuration.getPort();
    }

}
