package com.bulut.attendance.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransactionAccount getTransactionAccountSample1() {
        return new TransactionAccount().id(1L);
    }

    public static TransactionAccount getTransactionAccountSample2() {
        return new TransactionAccount().id(2L);
    }

    public static TransactionAccount getTransactionAccountRandomSampleGenerator() {
        return new TransactionAccount().id(longCount.incrementAndGet());
    }
}
