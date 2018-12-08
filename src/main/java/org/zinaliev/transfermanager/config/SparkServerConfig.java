package org.zinaliev.transfermanager.config;

import lombok.Getter;
import lombok.Setter;

public class SparkServerConfig {

    @Getter
    @Setter
    private int port = 8080;

    @Getter
    @Setter
    private int minThreads = 1;

    @Getter
    @Setter
    private int maxThreads = 5;

    @Getter
    @Setter
    private int idleTimeoutMillis = 1000;

}
