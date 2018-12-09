package org.zinaliev.transfermanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.zinaliev.transfermanager.service.TransferService;
import org.zinaliev.transfermanager.service.TransferServiceImpl;
import org.zinaliev.transfermanager.service.storage.InMemoryWalletStorage;
import org.zinaliev.transfermanager.service.storage.WalletStorage;

public class ApplicationModule extends AbstractModule {

    private final ApplicationConfig appConfig;

    ApplicationModule(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    protected void configure() {
        super.configure();

        bind(WalletStorage.class).to(InMemoryWalletStorage.class);
        bind(TransferService.class).to(TransferServiceImpl.class);
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
