package org.zinaliev.transfermanager.service.storage;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.zinaliev.transfermanager.exception.AlreadyExistsException;
import org.zinaliev.transfermanager.exception.NotFoundException;
import org.zinaliev.transfermanager.service.Wallet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Singleton
public class InMemoryWalletStorage implements WalletStorage {

    private final ConcurrentMap<String, Wallet> wallets = new ConcurrentHashMap<>();

    @Override
    public void add(Wallet wallet) {
        Wallet existing = wallets.putIfAbsent(wallet.getId(), wallet);

        if (existing != null && !existing.equals(wallet))
            throw new AlreadyExistsException("There is an already existing wallet associated with id " + wallet.getId());

        log.info("Added new wallet {}", wallet);
    }

    @Override
    public void delete(String id) {
        Wallet wallet = wallets.get(id);

        if (wallet == null)
            throw new NotFoundException("Wallet " + id + " is not found");

        // prevent a wallet to be deleted while being used in a transaction
        synchronized (wallet.getLock()) {
            wallets.remove(id);
        }

        log.info("Deleted wallet with id {}", id);
    }

    @Override
    public Wallet get(String id) {
        Wallet result = wallets.get(id);

        if (result == null)
            throw new NotFoundException("Wallet " + id + " is not found");

        log.info("Retrieved wallet {}", result);
        return result;
    }

    @VisibleForTesting
    public void reset() {
        wallets.clear();
    }
}
