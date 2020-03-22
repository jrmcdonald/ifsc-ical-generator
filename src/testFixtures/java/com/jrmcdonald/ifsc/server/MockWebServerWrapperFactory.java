package com.jrmcdonald.ifsc.server;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockWebServerWrapperFactory {
    private static MockWebServerWrapper wrapper;

    public static MockWebServerWrapper getInstance() {
        if (wrapper == null) {
            wrapper = new MockWebServerWrapper();
        }
        return wrapper;
    }
}
