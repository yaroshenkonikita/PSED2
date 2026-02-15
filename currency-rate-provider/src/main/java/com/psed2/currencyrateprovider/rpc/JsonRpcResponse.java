package com.psed2.currencyrateprovider.rpc;

public record JsonRpcResponse(
        String jsonrpc,
        Object result,
        JsonRpcError error,
        Object id
) {
    public static JsonRpcResponse success(Object result, Object id) {
        return new JsonRpcResponse("2.0", result, null, id);
    }

    public static JsonRpcResponse failure(int code, String message, Object id) {
        return new JsonRpcResponse("2.0", null, new JsonRpcError(code, message), id);
    }
}
