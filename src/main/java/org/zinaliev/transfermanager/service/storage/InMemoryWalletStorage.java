package org.zinaliev.transfermanager.service.storage;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.zinaliev.transfermanager.exception.NotFoundException;
import org.zinaliev.transfermanager.exception.WalletException;
import org.zinaliev.transfermanager.service.Wallet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.zinaliev.transfermanager.exception.StatusCode.*;

@Slf4j
@Singleton
public class InMemoryWalletStorage implements WalletStorage {

    private final ConcurrentMap<String, Wallet> wallets = new ConcurrentHashMap<>();

    @Override
    public void add(Wallet wallet) {
        Wallet existing = wallets.putIfAbsent(wallet.getId(), wallet);

        if (existing != null && !existing.equals(wallet))
            throw new WalletException(WALLET_ALREADY_EXISTS, "There is an already existing wallet associated with id " + wallet.getId());

        log.info("Added new wallet {}", wallet);
    }

    @Override
    public Wallet get(String id) {
        Wallet result = getInternal(id);

        log.info("Retrieved wallet {}", result);
        return result;
    }

    @Override
    public void update(String id, double amount) {
        if (amount < 0)
            throw new WalletException(INVALID_AMOUNT, "Money amount can not be negative");

        Wallet wallet = getInternal(id);
        Money money = Money.of(wallet.getMoney().getCurrencyUnit(), amount);

        // prevent a wallet to be updated while being used in a transaction
        synchronized (wallet.getLock()) {
            wallet.setMoney(money);
        }

        log.info("Updated wallet {}", wallet);
    }

    @Override
    public void delete(String id) {
        Wallet wallet = getInternal(id);

        if (wallet.getMoney().isPositive())
            throw new WalletException(NON_EMPTY_WALLET_DELETION, "Wallet with money can't be deleted");

        // prevent a wallet to be deleted while being used in a transaction
        synchronized (wallet.getLock()) {
            wallets.remove(id);
        }

        log.info("Deleted wallet with id {}", id);
    }

    private Wallet getInternal(String id) {
        Wallet wallet = wallets.get(id);

        if (wallet == null)
            throw new NotFoundException("Wallet " + id + " is not found");

        return wallet;
    }

    @VisibleForTesting
    public void reset() {
        wallets.clear();
    }
}
