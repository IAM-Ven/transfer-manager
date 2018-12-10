package org.zinaliev.transfermanager.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.zinaliev.transfermanager.ApplicationConfig;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.exception.UnsupportedCurrencyException;
import org.zinaliev.transfermanager.service.Wallet;

import java.util.HashSet;
import java.util.Set;

@Singleton
public class ModelMapper {

    private final Set<String> currencies = new HashSet<>();

    @Inject
    public ModelMapper(ApplicationConfig appConfig) {
        for(String currency : appConfig.getSupportedCurrencies())
            currencies.add(currency.toUpperCase());
    }

    public Wallet convert(String walletId, WalletModel model) {

        if(!currencies.contains(model.getCurrencyCode().toUpperCase()))
            throw new UnsupportedCurrencyException("Unsupported currency " + model.getCurrencyCode());

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
