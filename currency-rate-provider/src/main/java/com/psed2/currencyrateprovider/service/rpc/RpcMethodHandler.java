package com.psed2.currencyrateprovider.service.rpc;

import com.psed2.currencyrateprovider.rpc.JsonRpcRequest;
import com.psed2.currencyrateprovider.rpc.JsonRpcResponse;

public interface RpcMethodHandler {
    String methodName();

    JsonRpcResponse handle(JsonRpcRequest request);
}
