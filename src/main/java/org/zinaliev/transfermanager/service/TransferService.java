package org.zinaliev.transfermanager.service;

public interface TransferService {

    /**
     * Transfer money between wallets
     */
    void transfer(String sourceWalletId, String targetWalletId, double moneyAmount);
}
