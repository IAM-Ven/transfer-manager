package org.zinaliev.transfermanager.service.storage;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.zinaliev.transfermanager.exception.AlreadyExistsException;
import org.zinaliev.transfermanager.exception.NonEmptyWalletDeletionException;
import org.zinaliev.transfermanager.exception.NotFoundException;
import org.zinaliev.transfermanager.service.Wallet;

import static org.junit.Assert.*;

public class InMemoryWalletStorageTest {

    private final InMemoryWalletStorage storage = new InMemoryWalletStorage();
    private final Wallet wallet = new Wallet();

    @Before
    public void beforeEach() {
        wallet.setMoney(Money.of(CurrencyUnit.of("RUB"), 100));
        wallet.setId("test-wallet-id");
    }

    @Test
    public void testAdd_NewWallet_GetReturnsAddedWallet() {
        storage.add(wallet);

        assertEquals(wallet, storage.get(wallet.getId()));
    }

    @Test(expected = AlreadyExistsException.class)
    public void testAdd_DifferentWalletWithSameId_ThrowsException() {
        storage.add(wallet);

        Wallet second = new Wallet();
        second.setId(wallet.getId());
        second.setMoney(wallet.getMoney().plus(1.5));

        storage.add(second);
    }

    @Test
    public void testAdd_EqualWalletWithSameId_OriginalWalletIsNotReplaced() {
        storage.add(wallet);

        Wallet second = new Wallet();
        second.setId(wallet.getId());
        second.setMoney(wallet.getMoney());

        storage.add(second);
        assertEquals(wallet, storage.get(wallet.getId()));

        // it's important to keep original object since we might be using it's sync Object in a transfer atm
        assertEquals(wallet.getLock(), storage.get(wallet.getId()).getLock());
    }

    @Test(expected = NotFoundException.class)
    public void testGet_NotExistingWallet_ThrowsException() {
        storage.get("not-existing-id");
    }

    @Test
    public void testDelete_ExistingEmptyWallet_GetThrowsNotFoundException() {
        wallet.setMoney(Money.of(CurrencyUnit.of("RUB"), 0));
        storage.add(wallet);
        storage.delete(wallet.getId());

        try {
            storage.get(wallet.getId());
            fail("Should haven't reached this line");
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
        }
    }

    @Test(expected = NotFoundException.class)
    public void testDelete_NotExistingWallet_ThrowsException() {
        storage.delete("not-existing-id");
    }

    @Test(expected = NonEmptyWalletDeletionException.class)
    public void testDelete_ExistingNonEmptyWallet_ThrowsException() {
        storage.add(wallet);
        storage.delete(wallet.getId());
    }
}