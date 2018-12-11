package org.zinaliev.transfermanager.util;

import com.google.common.io.Resources;
import org.junit.Test;
import org.zinaliev.transfermanager.ApplicationConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigUtilsTest {

    @Test
    public void testReadAppConfig_ValidFile_ReturnsValidData() {
        ApplicationConfig appConfig = ConfigUtils.readAppConfig(
                Resources.getResource("util/config-valid.yaml").getPath()
        );

        assertNotNull(appConfig);
        assertNotNull(appConfig.getServerConfig());
        assertNotNull(appConfig.getSupportedCurrencies());

        assertEquals(1, appConfig.getSupportedCurrencies().size());
        assertEquals("RUB", appConfig.getSupportedCurrencies().get(0));

        assertEquals("logback.xml", appConfig.getLogConfigFile());
        assertEquals(4568, appConfig.getServerConfig().getPort());
        assertEquals(2, appConfig.getServerConfig().getMinThreads());
        assertEquals(10, appConfig.getServerConfig().getMaxThreads());
        assertEquals(1000, appConfig.getServerConfig().getIdleTimeoutMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAppConfig_NullFile_ThrowsException() {
        ConfigUtils.readAppConfig(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAppConfig_NonExistingFile_ThrowsException() {
        ConfigUtils.readAppConfig("non-existing-config.yaml");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadAppConfig_InvalidCurrencyFile_ThrowsException() {
        ConfigUtils.readAppConfig(Resources.getResource("util/config-invalid-currency.yaml").getPath());
    }
}