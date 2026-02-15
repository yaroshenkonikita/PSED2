package com.psed2.currencyrateprovider.rpc;

public record JsonRpcError(
        int code,
        String message
) {
}
