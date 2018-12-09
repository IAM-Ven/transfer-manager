package org.zinaliev.transfermanager.api.model;

import lombok.Getter;
import lombok.Setter;

public class TransferModel {

    @Getter
    @Setter
    private String targetWallet;

    @Getter
    @Setter
    private double amount;
}
