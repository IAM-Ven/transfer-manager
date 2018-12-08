package org.zinaliev.transfermanager.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class WalletModel {

    @Getter
    @Setter
    private String currencyCode;

    @Getter
    @Setter
    private double amount;
}
