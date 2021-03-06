package com.jrmcdonald.ifsc.server;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockWebServer;

@Slf4j
@Getter
public final class MockWebServerWrapper implements ExtensionContext.Store.CloseableResource, AutoCloseable {

    private final MockWebServer server;

    public MockWebServerWrapper() {
        server = new MockWebServer();
        try {
            server.start();
        } catch (IOException e) {
            log.error("An exception occurred starting the mock web server", e);
        }
    }

    @Override
    public void close() throws IOException {
        server.shutdown();
    }
}
