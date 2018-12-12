package org.zinaliev.transfermanager.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class TransferModel {

    @Getter
    @Setter
    private String targetWallet;

    @Getter
    @Setter
    private double amount;
}
