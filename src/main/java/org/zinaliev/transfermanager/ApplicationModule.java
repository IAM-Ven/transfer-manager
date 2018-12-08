package org.zinaliev.transfermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ApplicationModule extends AbstractModule {

    private final ApplicationConfig appConfig;

    ApplicationModule(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    protected void configure() {
        super.configure();
    }

    @Provides
    @Singleton
    public ApplicationConfig getAppConfig() {
        return appConfig;
    }

    @Provides
    @Singleton
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


}
