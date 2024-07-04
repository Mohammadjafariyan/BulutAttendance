package com.bulut.attendance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccountingProcedureExecutionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccountingProcedureExecution getAccountingProcedureExecutionSample1() {
        return new AccountingProcedureExecution().id(1L).desc("desc1");
    }

    public static AccountingProcedureExecution getAccountingProcedureExecutionSample2() {
        return new AccountingProcedureExecution().id(2L).desc("desc2");
    }

    public static AccountingProcedureExecution getAccountingProcedureExecutionRandomSampleGenerator() {
        return new AccountingProcedureExecution().id(longCount.incrementAndGet()).desc(UUID.randomUUID().toString());
    }
}
