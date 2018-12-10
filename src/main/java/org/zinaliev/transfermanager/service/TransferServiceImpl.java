package org.zinaliev.transfermanager.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.zinaliev.transfermanager.exception.StatusCode;
import org.zinaliev.transfermanager.exception.TransferException;
import org.zinaliev.transfermanager.service.storage.WalletStorage;

import static org.zinaliev.transfermanager.exception.StatusCode.INSUFFICIENT_MONEY;
import static org.zinaliev.transfermanager.exception.StatusCode.INVALID_CURRENCY;

@Slf4j
@Singleton
public class TransferServiceImpl implements TransferService {

    private final WalletStorage storage;

    @Inject
    public TransferServiceImpl(WalletStorage storage) {
        this.storage = storage;
    }

    @Override
    public void transfer(String sourceId, String targetId, double amount) {

        if (sourceId.equals(targetId))
            throw new TransferException(StatusCode.INVALID_WALLET_ID, "Target wallet id must be different from source one");

        if (amount <= 0)
            throw new TransferException(StatusCode.INVALID_AMOUNT, "Transferred money amount must be positive");

        Wallet source = storage.get(sourceId);
        Wallet target = storage.get(targetId);

        Wallet lockedFirst;
        Wallet lockedAfter;

        if (sourceId.compareTo(targetId) > 0) {
            lockedFirst = source;
            lockedAfter = target;
        } else {
            lockedFirst = target;
            lockedAfter = source;
        }

        // avoids resource-ordering deadlock
        synchronized (lockedFirst.getSync()) {
            synchronized (lockedAfter.getSync()) {
                transfer(source, target, amount);
            }
        }
    }

    private void transfer(Wallet source, Wallet target, double amount) {
        Money sourceMoney = source.getMoney();
        Money targetMoney = target.getMoney();

        if (!sourceMoney.isSameCurrency(targetMoney))
            throw new TransferException(INVALID_CURRENCY, "Target wallet currency should match to source one");

        Money transferred = Money.of(sourceMoney.getCurrencyUnit(), amount);

        if (!transferred.isLessThan(sourceMoney))
            throw new TransferException(INSUFFICIENT_MONEY, "Source wallet stores insufficient money for the transfer");

        source.setMoney(sourceMoney.minus(transferred));
        target.setMoney(targetMoney.plus(transferred));

        log.info("Transferred {} from wallet {} to wallet {}", transferred, source.getId(), target.getId());
    }
}
