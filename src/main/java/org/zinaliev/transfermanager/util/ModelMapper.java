package org.zinaliev.transfermanager.util;

import com.google.inject.Singleton;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.service.Wallet;

@Singleton
public class ModelMapper {

    public Wallet convert(String walletId, WalletModel model) {
        Wallet result = new Wallet();

        result.setId(walletId);
        result.setMoney(Money.of(CurrencyUnit.of(model.getCurrencyCode()), model.getAmount()));

        return result;
    }

    public WalletModel convert(Wallet wallet) {
        return new WalletModel(
                wallet.getMoney().getCurrencyUnit().getCode(),
                wallet.getMoney().getAmount().doubleValue()
        );
    }
}
