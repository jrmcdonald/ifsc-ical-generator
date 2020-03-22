package com.jrmcdonald.ifsc.extensions;

import com.jrmcdonald.ifsc.server.MockWebServerWrapper;
import com.jrmcdonald.ifsc.server.MockWebServerWrapperFactory;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class MockWebServerExtension implements BeforeAllCallback, ParameterResolver {

    private static MockWebServerWrapper serverWrapper(ExtensionContext extensionContext) {
        return extensionContext.getRoot()
                .getStore(ExtensionContext.Namespace.GLOBAL)
                .get("serverWrapper", MockWebServerWrapper.class);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        extensionContext.getRoot()
                .getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent("serverWrapper", s -> MockWebServerWrapperFactory.getInstance(), MockWebServerWrapper.class);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().equals(MockWebServerWrapper.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return serverWrapper(extensionContext);
    }
}
