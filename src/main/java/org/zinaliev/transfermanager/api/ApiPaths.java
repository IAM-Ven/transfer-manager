package org.zinaliev.transfermanager.api;

public class ApiPaths {

    public static final String TRANSFER = "/transfer";
    public static final String VAR_WALLET_ID = ":walletId";
    public static final String URL_WALLET = "/wallet/" + VAR_WALLET_ID;
    public static final String URL_TRANSFER = URL_WALLET + TRANSFER;

    private ApiPaths() {
    }
}
