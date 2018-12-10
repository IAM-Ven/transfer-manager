package org.zinaliev.transfermanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.zinaliev.transfermanager.config.SparkServerConfig;

import java.util.List;

public class ApplicationConfig {

    @Getter
    @Setter
    @NonNull
    @JsonProperty("server")
    private SparkServerConfig serverConfig = new SparkServerConfig();

    @Getter
    @Setter
    @NonNull
    @JsonProperty("currencies")
    private List<String> supportedCurrencies = Lists.newArrayList();

    @Getter
    @Setter
    @JsonProperty("logConfig")
    private String logConfigFile;

}
