package com.jrmcdonald.ifsc.functional.config;

import com.jrmcdonald.ifsc.server.MockWebServerWrapper;
import com.jrmcdonald.ifsc.server.MockWebServerWrapperFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class FunctionalTestConfiguration {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            MockWebServerWrapper wrapper = MockWebServerWrapperFactory.getInstance();
            TestPropertyValues.of(String.format("external.ifsc.host=http://localhost:%d", wrapper.getServer().getPort())).applyTo(applicationContext);
        }
    }

}
