package org.zinaliev.transfermanager.api;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.zinaliev.transfermanager.ApplicationConfig;
import org.zinaliev.transfermanager.api.model.WalletModel;
import org.zinaliev.transfermanager.exception.ApplicationException;
import org.zinaliev.transfermanager.exception.UnsupportedCurrencyException;
import org.zinaliev.transfermanager.service.Wallet;

import static org.junit.Assert.assertEquals;

public class ModelMapperTest {

    private final String id = "test-wallet-id";
    private final Wallet wallet = new Wallet();
    private final WalletModel model = new WalletModel();
    private final ApplicationConfig appConfig = new ApplicationConfig();

    private ModelMapper mapper;

    @Before
    public void beforeEach() {
        appConfig.getSupportedCurrencies().add(CurrencyUnit.USD.getCode());
        mapper = new ModelMapper(appConfig);

        model.setAmount(100);
        model.setCurrencyCode(CurrencyUnit.USD.getCode());

        wallet.setMoney(Money.of(CurrencyUnit.USD, 100));
        wallet.setId(id);
    }

    @Test(expected = UnsupportedCurrencyException.class)
    public void testModelToWallet_UnsupportedCurrency_ThrowsException() {
        model.setCurrencyCode("AUD");

        mapper.convert(id, model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModelToWallet_NullWalletId_ThrowsException() {
        mapper.convert(null, model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModelToWallet_NullModel_ThrowsException() {
        mapper.convert(id, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModelToWallet_NullModelCurrencyCode_ThrowsException() {
        model.setCurrencyCode(null);
        mapper.convert(id, model);
    }

    @Test
    public void testModelToWallet_LowerCasedCurrencyCode_NoException() {
        model.setCurrencyCode("usd");

        mapper.convert(id, model);
    }

    @Test
    public void testModelToWallet() {
        Wallet converted = mapper.convert(id, model);

        assertEquals(id, converted.getId());
        assertEquals(model.getCurrencyCode(), converted.getMoney().getCurrencyUnit().getCode());
        assertEquals(model.getAmount(), converted.getMoney().getAmount().doubleValue(), 0.0001);
    }

    @Test
    public void testWalletToModel() {
        WalletModel converted = mapper.convert(wallet);

        assertEquals(wallet.getMoney().getCurrencyUnit().getCode(), converted.getCurrencyCode());
        assertEquals(wallet.getMoney().getAmount().doubleValue(), converted.getAmount(), 0.0001);
    }

    @Test(expected = ApplicationException.class)
    public void testWalletToModel_NullWallet_ThrowsException() {
        mapper.convert(null);
    }

    @Test(expected = ApplicationException.class)
    public void testWalletToModel_NullWalletMoney_ThrowsException() {
        wallet.setMoney(null);
        mapper.convert(wallet);
    }
}