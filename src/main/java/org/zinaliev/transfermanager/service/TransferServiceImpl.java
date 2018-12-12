package org.zinaliev.transfermanager.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.zinaliev.transfermanager.exception.ApplicationException;
import org.zinaliev.transfermanager.exception.StatusCode;
import org.zinaliev.transfermanager.exception.TransferException;
import org.zinaliev.transfermanager.service.storage.WalletStorage;

import static org.zinaliev.transfermanager.exception.StatusCode.*;

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
        synchronized (lockedFirst.getLock()) {
            synchronized (lockedAfter.getLock()) {
                transfer(source, target, amount);
            }
        }
    }

    private void transfer(Wallet source, Wallet target, double amount) {
        Money sourceMoney = source.getMoney();
        Money targetMoney = target.getMoney();

        if (!sourceMoney.isSameCurrency(targetMoney))
            throw new TransferException(INVALID_CURRENCY, "Target wallet currency should match to source one");

        Money delta = Money.of(sourceMoney.getCurrencyUnit(), amount);

        if (!delta.isLessThan(sourceMoney))
            throw new TransferException(INSUFFICIENT_MONEY, "Source wallet stores insufficient money for the transfer");

        try {
            sourceMoney = sourceMoney.minus(delta);
            targetMoney = targetMoney.plus(delta);
        } catch (Exception e) {
            log.error("Failed to process transaction, source: " + source + ", target: " + target + ", delta: " + delta, e);
            throw new ApplicationException(TRANSACTION_PROCESSING_ERROR, "Internal transaction processing error");
        }

        source.setMoney(sourceMoney);
        target.setMoney(targetMoney);

        log.info("Transferred {} from wallet {} to wallet {}", delta, source.getId(), target.getId());
    }
}
