package org.zinaliev.transfermanager;

import com.google.common.io.Resources;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zinaliev.transfermanager.api.ApiPaths;
import org.zinaliev.transfermanager.api.model.ResponseModel;
import org.zinaliev.transfermanager.api.model.TransferModel;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.service.storage.InMemoryWalletStorage;
import org.zinaliev.transfermanager.util.ConfigUtils;
import org.zinaliev.transfermanager.util.JsonMapper;
import org.zinaliev.transfermanager.util.UnirestUtils;
import unirest.HttpResponse;
import unirest.Unirest;

import static org.junit.Assert.*;

public class ApplicationIT {

    private static final String CONFIG_FILE = "config-test.yaml";
    private static JsonMapper jsonMapper;
    private static String urlBase;
    private static InMemoryWalletStorage storage;

    @BeforeClass
    public static void beforeAll() {
        String configFile = Resources.getResource(CONFIG_FILE).getPath();
        Application.main(new String[]{configFile});
        jsonMapper = Application.injector.getInstance(JsonMapper.class);
        storage = Application.injector.getInstance(InMemoryWalletStorage.class);

        ApplicationConfig appConfig = ConfigUtils.readAppConfig(configFile);
        urlBase = "http://localhost:" + appConfig.getServerConfig().getPort();

        UnirestUtils.setupSerializer();
    }

    @AfterClass
    public static void afterAll() {
        Unirest.shutDown();
    }

    @After
    public void afterEach() {
       storage.reset();
    }

    @Test
    public void testWalletCreation_NewWallet_TheWalletCanBeObtainedViaApi() {
        HttpResponse<ResponseModel> createResp = createWallet("123", "USD", 123.45);

        assertEquals(200, createResp.getStatus());
        assertNotNull(createResp.getBody());
        assertEquals(200, createResp.getBody().getCodeEx());
        assertEquals("OK", createResp.getBody().getMessage());


        HttpResponse<WalletResponseModel> getResp = getWallet("123");
        assertEquals(200, getResp.getStatus());
        WalletResponseModel body = getResp.getBody();

        assertNotNull(body);
        assertEquals(200, body.getCodeEx());
        assertEquals("OK", body.getMessage());
        assertEquals("USD", body.getData().getCurrencyCode());
        assertEquals(123.45, body.getData().getAmount(), 0.001);
    }

    @Test
    public void testGettingWallet_NonExistingWallet_RespondsWithNotFound() {
        HttpResponse<WalletResponseModel> response = getWallet("123");

        assertEquals(404, response.getStatus());
        WalletResponseModel body = response.getBody();

        assertNotNull(body);
        assertEquals(404, body.getCodeEx());
        assertNotEquals("OK", body.getMessage());
    }

    @Test
    public void testWalletDeletion_NonExistingWallet_RespondsWithNotFound() {
        HttpResponse<ResponseModel> response = deleteWallet("123");

        assertEquals(404, response.getStatus());

        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getCodeEx());
        assertNotEquals("OK", response.getBody().getMessage());
    }

    @Test
    public void testTransfer_Dummy_PositiveResponse() {
        createWallet("123", "USD", 1000);
        createWallet("456", "USD", 1000);

        HttpResponse<ResponseModel> response = transfer("123", "456", 78.90);

        assertEquals(200, response.getStatus());

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCodeEx());
        assertEquals("OK", response.getBody().getMessage());
    }

    private HttpResponse<ResponseModel> createWallet(String id, String currencyCode, double amount) {
        WalletModel wallet = new WalletModel(currencyCode, amount);

        return Unirest.post(urlBase + ApiPaths.WALLET + id)
                .body(jsonMapper.toJson(wallet))
                .asObject(ResponseModel.class);
    }

    private HttpResponse<WalletResponseModel> getWallet(String id) {
        return Unirest.get(urlBase + ApiPaths.WALLET + id)
                .asObject(WalletResponseModel.class);
    }

    private HttpResponse<ResponseModel> deleteWallet(String id) {
        return Unirest.delete(urlBase + ApiPaths.WALLET + id)
                .asObject(ResponseModel.class);
    }

    private HttpResponse<ResponseModel> transfer(String sourceWalletId, String targetWalletId, double amount) {
        TransferModel args = new TransferModel();
        args.setTargetWallet(targetWalletId);
        args.setAmount(amount);

        return Unirest.post(urlBase + ApiPaths.WALLET + sourceWalletId + ApiPaths.TRANSFER)
                .body(jsonMapper.toJson(args))
                .asObject(ResponseModel.class);
    }

    private static class WalletResponseModel extends ResponseModel<WalletModel> {
    }
}
