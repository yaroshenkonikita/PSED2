package com.psed2.rateprinter.rpc;

public record JsonRpcRequest(
        String jsonrpc,
        String method,
        Object params,
        Object id
) {
}
