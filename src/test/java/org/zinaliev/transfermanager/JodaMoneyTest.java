package org.zinaliev.transfermanager;

import org.joda.money.CurrencyUnit;
import org.joda.money.IllegalCurrencyException;
import org.joda.money.Money;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class JodaMoneyTest {

    @Test
    public void test() {
        String currency = "USD";

        Money usd = Money.of(CurrencyUnit.of(currency), 123.56d);
        System.out.println(usd.getCurrencyUnit().getCode());
    }

    @Test(expected = IllegalCurrencyException.class)
    public void testCurrencyUnitCreation_OfNonExistingCode_ThrowsException() {
        CurrencyUnit cu = CurrencyUnit.of("non-existing-code");
    }

    @Test
    public void testRegisteredCurrencyUnits_ReturnsNonEmptyList() {
        assertFalse(CurrencyUnit.registeredCurrencies().isEmpty());

        for (CurrencyUnit cu : CurrencyUnit.registeredCurrencies()) {
            System.out.println("code: " + cu + ", decimals: " + cu.getDecimalPlaces());

        }
    }
}
