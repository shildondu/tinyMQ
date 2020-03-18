package com.shildon.tinymq.core.model;

import java.util.Arrays;
import java.util.List;

/**
 * 操作类型
 *
 * @author shildon
 */
public enum Operation {

    /**
     * publish
     */
    PUBLISH(1),

    /**
     * subscribe
     */
    SUBSCRIBE(2);

    private final int code;

    Operation(final int code) {
        this.code = code;
    }

    public static Operation find(final int code) {
        final List<Operation> operations = Arrays.asList(Operation.values());
        return operations.stream()
                .filter(it -> it.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("code: [%s] has no operation", code)));
    }

    public int getCode() {
        return this.code;
    }

}
