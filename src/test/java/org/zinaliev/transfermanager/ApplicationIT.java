package org.zinaliev.transfermanager;

import com.google.common.io.Resources;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zinaliev.transfermanager.api.ApiPaths;
import org.zinaliev.transfermanager.api.model.ResponseModel;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.util.ConfigUtils;
import org.zinaliev.transfermanager.util.UnirestUtils;
import unirest.HttpResponse;
import unirest.Unirest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ApplicationIT {

    private static final String CONFIG_FILE = "config-test.yaml";
    private static String urlBase;

    @BeforeClass
    public static void beforeAll() {
        String configFile = Resources.getResource(CONFIG_FILE).getPath();
        Application.main(new String[]{configFile});

        ApplicationConfig appConfig = ConfigUtils.readAppConfig(configFile);
        urlBase = "http://localhost:" + appConfig.getServerConfig().getPort();

        UnirestUtils.setupSerializer();
    }

    @AfterClass
    public static void afterAll(){
        Unirest.shutDown();
    }

    @Test
    public void testGetWallet_Dummy_PositiveResponse() {
        HttpResponse<WalletResponseModel> response = Unirest.get(urlBase + ApiPaths.URL_WALLET + "123")
                .asObject(WalletResponseModel.class);

        assertEquals(200, response.getStatus());
        WalletResponseModel body = response.getBody();

        assertNotNull(body);
        assertEquals(200, body.getCodeEx());
        assertEquals("OK", body.getMessage());
        assertEquals("USD", body.getData().getCurrencyCode());
        assertEquals(123.45, body.getData().getAmount(), 0.001);
    }

    @Test
    public void testDeleteWallet_Dummy_PositiveResponse() {
        HttpResponse<ResponseModel> response = Unirest.delete(urlBase + ApiPaths.URL_WALLET + "123")
                .asObject(ResponseModel.class);

        assertEquals(200, response.getStatus());

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCodeEx());
        assertEquals("OK", response.getBody().getMessage());
    }

    @Test
    public void testCreateWallet_Dummy_PositiveResponse() {
        HttpResponse<ResponseModel> response = Unirest.post(urlBase + ApiPaths.URL_WALLET + "123")
                .asObject(ResponseModel.class);

        assertEquals(200, response.getStatus());

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCodeEx());
        assertEquals("OK", response.getBody().getMessage());
    }

    @Test
    public void testTransfer_Dummy_PositiveResponse() {
        HttpResponse<ResponseModel> response = Unirest.post(urlBase + ApiPaths.URL_WALLET + "123" + ApiPaths.TRANSFER)
                .asObject(ResponseModel.class);

        assertEquals(200, response.getStatus());

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCodeEx());
        assertEquals("OK", response.getBody().getMessage());
    }

    private static class WalletResponseModel extends ResponseModel<WalletModel>{
    }
}
