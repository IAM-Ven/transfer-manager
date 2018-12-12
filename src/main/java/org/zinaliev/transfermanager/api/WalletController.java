package org.zinaliev.transfermanager.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.zinaliev.transfermanager.api.model.ResponseModel;
import org.zinaliev.transfermanager.api.model.TransferModel;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.service.TransferService;
import org.zinaliev.transfermanager.service.Wallet;
import org.zinaliev.transfermanager.service.storage.WalletStorage;
import org.zinaliev.transfermanager.util.JsonMapper;
import spark.Request;
import spark.Response;

@Singleton
public class WalletController {

    private final JsonMapper jsonMapper;
    private final ModelMapper modelMapper;
    private final WalletStorage storage;
    private final TransferService transferService;

    @Inject
    public WalletController(JsonMapper jsonMapper, ModelMapper modelMapper, WalletStorage storage, TransferService transferService) {
        this.jsonMapper = jsonMapper;
        this.modelMapper = modelMapper;
        this.storage = storage;
        this.transferService = transferService;
    }

    public String createWallet(Request request, Response response) {
        String id = request.params(ApiPaths.VAR_WALLET_ID);
        WalletModel model = jsonMapper.fromJson(request.body(), WalletModel.class);
        Wallet wallet = modelMapper.convert(id, model);

        storage.add(wallet);

        return jsonMapper.toJson(ResponseModel.ok());
    }

    public String getWallet(Request request, Response response) {
        String id = request.params(ApiPaths.VAR_WALLET_ID);

        Wallet wallet = storage.get(id);
        WalletModel model = modelMapper.convert(wallet);

        return jsonMapper.toJson(ResponseModel.ok(model));
    }

    public String deleteWallet(Request request, Response response) {
        String walletId = request.params(ApiPaths.VAR_WALLET_ID);

        storage.delete(walletId);

        return jsonMapper.toJson(ResponseModel.ok());
    }

    public String transfer(Request request, Response response) {
        String walletId = request.params(ApiPaths.VAR_WALLET_ID);
        TransferModel transferArgs = jsonMapper.fromJson(request.body(), TransferModel.class);

        transferService.transfer(walletId, transferArgs.getTargetWallet(), transferArgs.getAmount());

        return jsonMapper.toJson(ResponseModel.ok());
    }
}
