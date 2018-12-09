package org.zinaliev.transfermanager.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.zinaliev.transfermanager.service.storage.WalletStorage;

@Slf4j
@Singleton
public class TransferServiceImpl implements TransferService {

    private final WalletStorage storage;

    @Inject
    public TransferServiceImpl(WalletStorage storage) {
        this.storage = storage;
    }

    @Override
    public void transfer(String sourceWalletId, String targetWalletId, double moneyAmount) {
        Wallet source = storage.get(sourceWalletId);
        Wallet target = storage.get(targetWalletId);

        log.info("Successfully transferred {} {} from {} to {}", moneyAmount, source.getMoney().getCurrencyUnit(), source, target);
    }
}
