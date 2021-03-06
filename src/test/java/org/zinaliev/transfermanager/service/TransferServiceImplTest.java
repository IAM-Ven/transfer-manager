package org.zinaliev.transfermanager.service;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.zinaliev.transfermanager.exception.ApplicationException;
import org.zinaliev.transfermanager.exception.TransferException;
import org.zinaliev.transfermanager.service.storage.WalletStorage;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class TransferServiceImplTest {

    private final String sourceId = "test-source-id";
    private final String targetId = "test-target-id";
    private final Wallet source = new Wallet();
    private final Wallet target = new Wallet();

    private final WalletStorage storage = mock(WalletStorage.class);

    private final TransferServiceImpl service = new TransferServiceImpl(storage);

    @Before
    public void beforeEach() {
        source.setId(sourceId);
        target.setId(targetId);

        source.setMoney(Money.of(CurrencyUnit.USD, 200));
        target.setMoney(Money.of(CurrencyUnit.USD, 500));

        when(storage.get(sourceId)).thenReturn(source);
        when(storage.get(targetId)).thenReturn(target);
    }

    @Test(expected = TransferException.class)
    public void testTransfer_SameWalletIds_ThrowsException() {
        service.transfer(sourceId, sourceId, 1);
    }

    @Test(expected = TransferException.class)
    public void testTransfer_NegativeAmount_ThrowsException() {
        service.transfer(sourceId, targetId, -1);
    }

    @Test(expected = TransferException.class)
    public void testTransfer_ZeroAmount_ThrowsException() {
        service.transfer(sourceId, targetId, 0);
    }

    @Test(expected = TransferException.class)
    public void testTransfer_CurrencyMismatch_ThrowsException() {
        target.setMoney(Money.of(CurrencyUnit.AUD, 500));

        service.transfer(sourceId, targetId, 100);
    }

    @Test(expected = TransferException.class)
    public void testTransfer_InsufficientMoneyOnSourceWallet_ThrowsException() {
        service.transfer(sourceId, targetId, 300);
    }

    @Test(expected = ApplicationException.class)
    public void testTransfer_JodaTimeExceptionOnMinusOperation_ThrowsApplicationException() {
        Money sourceMoney = spy(source.getMoney());
        doThrow(mock(RuntimeException.class)).when(sourceMoney).minus(any(Money.class));
        source.setMoney(sourceMoney);

        service.transfer(sourceId, targetId, 100);
    }

    @Test(expected = ApplicationException.class)
    public void testTransfer_JodaTimeExceptionOnPlusOperation_ThrowsApplicationException() {
        Money targetMoney = spy(target.getMoney());
        doThrow(mock(RuntimeException.class)).when(targetMoney).plus(any(Money.class));
        target.setMoney(targetMoney);

        service.transfer(sourceId, targetId, 100);
    }

    @Test
    public void testTransfer_ValidConditions_MovesMoneyFromSourceToTargetWallet() {
        service.transfer(sourceId, targetId, 100);

        assertEquals(100, source.getMoney().getAmount().doubleValue(), 0);
        assertEquals(600, target.getMoney().getAmount().doubleValue(), 0);
    }
}