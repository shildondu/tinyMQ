package com.shildon.tinymq.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.net.URL;

/**
 * @author shildon
 */
public final class ConfigurationUtils {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper(new YAMLFactory())
                .setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
    }

    public static <T> T load(URL url, Class<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(url, type);
    }

}
