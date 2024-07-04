package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccountingProcedureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccountingProcedure getAccountingProcedureSample1() {
        return new AccountingProcedure().id(1L).title("title1");
    }

    public static AccountingProcedure getAccountingProcedureSample2() {
        return new AccountingProcedure().id(2L).title("title2");
    }

    public static AccountingProcedure getAccountingProcedureRandomSampleGenerator() {
        return new AccountingProcedure().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
