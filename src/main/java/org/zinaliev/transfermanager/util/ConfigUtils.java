package org.zinaliev.transfermanager.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Strings;
import org.slf4j.LoggerFactory;
import org.zinaliev.transfermanager.ApplicationConfig;
import org.zinaliev.transfermanager.exception.ApplicationStartupException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ConfigUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private ConfigUtils() {
    }

    public static ApplicationConfig readAppConfig(String fileName) {

        if (Strings.isNullOrEmpty(fileName))
            throw new ApplicationStartupException("Config file name can not be empty");

        File configFile = new File(fileName);
        if (!configFile.exists())
            throw new ApplicationStartupException("Config file does not exist - " + fileName);

        try {
            return MAPPER.readValue(configFile, ApplicationConfig.class);
        } catch (IOException e) {
            throw new ApplicationStartupException("Can not read config file", e);
        }
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
}
