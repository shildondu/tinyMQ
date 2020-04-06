package com.shildon.tinymq.server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.shildon.tinymq.core.model.Pair;
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
public final class ConfigurationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);

    private static final URL PATH = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("configuration.yml"));

    private static Configuration CONFIGURATION = null;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    static {
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        try {
            CONFIGURATION = OBJECT_MAPPER.readValue(PATH, Configuration.class);
            LOGGER.info("" + CONFIGURATION);
        } catch (IOException e) {
            LOGGER.error("parse configuration.yml error!", e);
        }
    }

    public static List<Pair<String, Integer>> getServerInfos() {
        return Arrays.stream(CONFIGURATION.getNameServers().split(","))
                .map(it -> {
                    String[] serverInfo = it.split(":");
                    return new Pair<>(serverInfo[0], Integer.valueOf(serverInfo[1]));
                })
                .collect(Collectors.toList());
    }

    public static int getPort() {
        return CONFIGURATION.getPort();
    }

}
