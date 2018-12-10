package org.zinaliev.transfermanager;

import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.zinaliev.transfermanager.api.ApiPaths;
import org.zinaliev.transfermanager.api.ExceptionHandlerImpl;
import org.zinaliev.transfermanager.api.WalletController;
import org.zinaliev.transfermanager.util.ConfigUtils;
import spark.Spark;

@Slf4j
@Singleton
public class Application {

    private static final String DEFAULT_CONFIG = "config.yaml";

    static Injector injector;

    private final ApplicationConfig appConfig;
    private final ExceptionHandlerImpl exceptionHandler;
    private final WalletController walletController;

    @Inject
    public Application(ApplicationConfig appConfig, ExceptionHandlerImpl exceptionHandler, WalletController walletController) {
        this.appConfig = appConfig;
        this.exceptionHandler = exceptionHandler;
        this.walletController = walletController;
    }

    public static void main(String[] args) {
        String configFile;

        try {

            if (args.length == 1)
                configFile = args[0];
            else
                configFile = Resources.getResource(DEFAULT_CONFIG).getPath();

            ApplicationConfig appConfig = ConfigUtils.readAppConfig(configFile);
            ConfigUtils.setupLogging(appConfig);
            log.info("Starting application using configuration file {}", configFile);

            injector = Guice.createInjector(new ApplicationModule(appConfig));

            Application application = injector.getInstance(Application.class);
            application.start();

            Runtime.getRuntime().addShutdownHook(new Thread(application::stop));

        } catch (Exception e) {
            log.error("Failed to start application", e);
        }
    }

    private void start() {
        Spark.port(appConfig.getServerConfig().getPort());
        Spark.threadPool(appConfig.getServerConfig().getMaxThreads(),
                appConfig.getServerConfig().getMinThreads(),
                appConfig.getServerConfig().getIdleTimeoutMillis()
        );

        registerApi();
        registerExceptionHandler();

        Spark.awaitInitialization();
    }

    private void stop() {
        Spark.stop();
    }

    private void registerApi() {
        Spark.post(ApiPaths.URL_WALLET, walletController::createWallet);
        Spark.get(ApiPaths.URL_WALLET, walletController::getWallet);
        Spark.delete(ApiPaths.URL_WALLET, walletController::deleteWallet);
        Spark.post(ApiPaths.URL_TRANSFER, walletController::transfer);
    }

    private void registerExceptionHandler() {
        Spark.exception(Exception.class, exceptionHandler);
    }
}
