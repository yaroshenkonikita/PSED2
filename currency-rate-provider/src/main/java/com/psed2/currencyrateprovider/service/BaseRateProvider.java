package com.psed2.currencyrateprovider.service;

public interface BaseRateProvider {
    double getBaseRate(String normalizedPair);
}
