package org.zinaliev.transfermanager.service;

public interface TransferService {

    void transfer(String sourceWalletId, String targetWalletTid, double moneyAmount);
}
