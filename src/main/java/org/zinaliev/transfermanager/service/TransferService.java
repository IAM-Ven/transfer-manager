package org.zinaliev.transfermanager.service;

public interface TransferService {

    /**
     * Transfer money between wallets
     *
     * @throws org.zinaliev.transfermanager.exception.TransferException operation can not be performed with the given input data
     */
    void transfer(String sourceWalletId, String targetWalletId, double moneyAmount);
}
