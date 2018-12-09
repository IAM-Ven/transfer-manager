package org.zinaliev.transfermanager.service.storage;

import org.zinaliev.transfermanager.service.Wallet;

public interface WalletStorage {

    /**
     * Save wallet
     * @throws org.zinaliev.transfermanager.exception.AlreadyExistsException if a wallet with the same id is already registered
     */
    void add(Wallet wallet);

    /**
     * Delete a wallet by given id
     * @throws org.zinaliev.transfermanager.exception.NotFoundException in case no wallet is stored for the given id
     */
    void delete(String walletId);

    /**
     * Retrieve wallet data
     * @throws org.zinaliev.transfermanager.exception.NotFoundException in case no wallet is stored for the given id
     */
    Wallet get(String walletId);
}
