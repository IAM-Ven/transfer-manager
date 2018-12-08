package org.zinaliev.transfermanager;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.zinaliev.transfermanager.config.SparkServerConfig;

public class ApplicationConfig {

    @Getter
    @Setter
    @JsonProperty("server")
    @NonNull
    private SparkServerConfig serverConfig = new SparkServerConfig();

    @Getter
    @Setter
    @JsonProperty("logConfig")
    private String logConfigFile;

}
