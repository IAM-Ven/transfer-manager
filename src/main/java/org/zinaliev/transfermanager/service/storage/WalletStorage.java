package org.zinaliev.transfermanager.service.storage;

import org.zinaliev.transfermanager.exception.WalletException;
import org.zinaliev.transfermanager.service.Wallet;

/**
 * CRUD operations with wallets
 */
public interface WalletStorage {

    /**
     * Save wallet
     * @throws WalletException if a wallet with the same id is already registered
     */
    void add(Wallet wallet);

    /**
     * Retrieve wallet data
     * @throws org.zinaliev.transfermanager.exception.NotFoundException in case no wallet is stored for the given id
     */
    Wallet get(String walletId);

    /**
     * Change wallet money amount without transfer
     * E.g. for cache operations
     *
     * @throws org.zinaliev.transfermanager.exception.NotFoundException in case no wallet is stored for the given id
     * @throws WalletException if a new amount value is negative
     */
    void update(String walletId, double amount);

    /**
     * Delete a wallet by given id
     * @throws org.zinaliev.transfermanager.exception.NotFoundException in case no wallet is stored for the given id
     * @throws WalletException if a wallet has money on it
     */
    void delete(String walletId);
}
