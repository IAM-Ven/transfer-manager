package org.zinaliev.transfermanager.api;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.zinaliev.transfermanager.ApplicationConfig;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.exception.ApplicationException;
import org.zinaliev.transfermanager.exception.WalletException;
import org.zinaliev.transfermanager.service.Wallet;

import java.util.HashSet;
import java.util.Set;

import static org.zinaliev.transfermanager.exception.StatusCode.INVALID_AMOUNT;
import static org.zinaliev.transfermanager.exception.StatusCode.INVALID_CURRENCY;

@Singleton
public class ModelMapper {

    private final Set<String> currencies = new HashSet<>();

    @Inject
    public ModelMapper(ApplicationConfig appConfig) {
        for (String currency : appConfig.getSupportedCurrencies())
            currencies.add(currency.toUpperCase());
    }

    /**
     * Converts {@link WalletModel} received from HTTP endpoint to {@link Wallet} being used internally in the service
     *
     * @throws IllegalArgumentException null or empty input args
     * @throws WalletException client requested currency is not supported by the service, source model hsa negative amount value
     */
    public Wallet convert(String walletId, WalletModel model) {

        if (Strings.isNullOrEmpty(walletId))
            throw new IllegalArgumentException("Wallet id must be specified");

        if (model == null)
            throw new IllegalArgumentException("Wallet model must be specified");

        if (Strings.isNullOrEmpty(model.getCurrencyCode()))
            throw new IllegalArgumentException("Model currency code must be specified");

        if(model.getAmount() < 0)
            throw new WalletException(INVALID_AMOUNT, "Wallet money amount can not be negative");

        if (!currencies.contains(model.getCurrencyCode().toUpperCase()))
            throw new WalletException(INVALID_CURRENCY, "Unsupported currency " + model.getCurrencyCode());

        Wallet result = new Wallet();

        result.setId(walletId);
        result.setMoney(Money.of(CurrencyUnit.of(model.getCurrencyCode().toUpperCase()), model.getAmount()));

        return result;
    }

    /**
     * Converts {@link Wallet} used internally in the service to {@link WalletModel} used by client
     *
     * NOTE: throws different exception than {@link #convert(String, WalletModel)}
     * because in this case it should be handled as INTERNAL_SERVER_ERROR
     *
     * @throws ApplicationException invalid input arguments
     */
    public WalletModel convert(Wallet wallet) {

        if (wallet == null)
            throw new ApplicationException("Wallet must be specified");

        if (wallet.getMoney() == null)
            throw new ApplicationException("Wallet money must be specified");

        return new WalletModel(
                wallet.getMoney().getCurrencyUnit().getCode(),
                wallet.getMoney().getAmount().doubleValue()
        );
    }
}
