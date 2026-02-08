package com.psed2.currencyrateprovider.rpc;

import com.fasterxml.jackson.databind.JsonNode;

public record JsonRpcRequest(
        String jsonrpc,
        String method,
        JsonNode params,
        Object id
) {
}
