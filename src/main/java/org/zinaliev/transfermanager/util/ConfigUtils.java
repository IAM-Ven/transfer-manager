package org.zinaliev.transfermanager.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Strings;
import org.joda.money.CurrencyUnit;
import org.joda.money.IllegalCurrencyException;
import org.slf4j.LoggerFactory;
import org.zinaliev.transfermanager.ApplicationConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ConfigUtils {

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    private ConfigUtils() {
    }

    public static ApplicationConfig readAppConfig(String fileName) {

        if (Strings.isNullOrEmpty(fileName))
            throw new IllegalArgumentException("Config file name can not be empty");

        File configFile = new File(fileName);
        if (!configFile.exists())
            throw new IllegalArgumentException("Config file does not exist - " + fileName);

        ApplicationConfig result;

        try {
            result = YAML_MAPPER.readValue(configFile, ApplicationConfig.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not parse config file", e);
        }

        validate(result);

        return result;
    }

    public static void setupLogging(ApplicationConfig appConfig) {

        if (Strings.isNullOrEmpty(appConfig.getLogConfigFile()))
            return;

        if (!Paths.get(appConfig.getLogConfigFile()).toFile().exists())
            return;

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();

            configurator.doConfigure(appConfig.getLogConfigFile());
        } catch (JoranException e) {
            // StatusPrinter will handle this
        }

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }

    private static void validate(ApplicationConfig appConfig) {

        for (String currencyCode : appConfig.getSupportedCurrencies()) {
            try {
                CurrencyUnit.of(currencyCode);
            } catch (IllegalCurrencyException e) {
                throw new IllegalArgumentException("Invalid application configuration provided", e);
            }
        }
    }
}
