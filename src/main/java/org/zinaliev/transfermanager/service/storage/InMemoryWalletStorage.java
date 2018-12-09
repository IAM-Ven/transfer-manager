package org.zinaliev.transfermanager.service.storage;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Singleton;
import org.zinaliev.transfermanager.exception.AlreadyExistsException;
import org.zinaliev.transfermanager.exception.NotFoundException;
import org.zinaliev.transfermanager.service.Wallet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class InMemoryWalletStorage implements WalletStorage {

    private final ConcurrentMap<String, Wallet> wallets = new ConcurrentHashMap<>();

    @Override
    public void add(Wallet wallet) {
        Wallet existing = wallets.putIfAbsent(wallet.getId(), wallet);

        if (existing != null && !existing.equals(wallet))
            throw new AlreadyExistsException("There is an already existing wallet associated with id " + wallet.getId());
    }

    @Override
    public void delete(String id) {

        if (wallets.remove(id) == null)
            throw new NotFoundException("Wallet " + id + " is not found");

    }

    @Override
    public Wallet get(String id) {
        Wallet result = wallets.get(id);

        if (result == null)
            throw new NotFoundException("Wallet " + id + " is not found");

        return result;
    }

    @VisibleForTesting
    public void reset() {
        wallets.clear();
    }
}
