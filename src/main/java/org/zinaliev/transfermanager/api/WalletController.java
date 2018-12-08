package org.zinaliev.transfermanager.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.zinaliev.transfermanager.api.model.ResponseModel;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.util.JsonMapper;
import spark.Request;
import spark.Response;

@Singleton
public class WalletController {

    private final JsonMapper jsonMapper;

    @Inject
    public WalletController(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public String createWallet(Request request, Response response) {
        return jsonMapper.toJson(new ResponseModel<>());
    }

    public String getWallet(Request request, Response response) {
        String walletId = request.params(ApiPaths.VAR_WALLET_ID);

        WalletModel result = new WalletModel("USD", 123.45);

        return jsonMapper.toJson(ResponseModel.ok(result));
    }

    public String deleteWallet(Request request, Response response) {
        return jsonMapper.toJson(new ResponseModel<>());
    }

    public String transfer(Request request, Response response) {
        return jsonMapper.toJson(new ResponseModel<>());
    }
}
