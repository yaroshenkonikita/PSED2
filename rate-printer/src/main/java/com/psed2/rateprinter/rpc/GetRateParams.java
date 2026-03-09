package com.psed2.rateprinter.rpc;

public record GetRateParams(
        String pair,
        String at
) {
}
