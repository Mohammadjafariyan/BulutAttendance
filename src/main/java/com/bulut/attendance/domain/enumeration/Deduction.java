package com.bulut.attendance.domain.enumeration;

/**
 * The Deduction enumeration.
 */
public enum Deduction {
    Other("سایر"),
    Insurance_Labor("سهم بیمه کارگر 7 درصد"),
    Tax("مالیات"),
    Judicial("کسور قضایی"),
    Prepaid("مساعده"),
    Loan("اقساط وام"),
    LastMonthOverPaid("اضافه پرداخت ماه قبل"),
    Lack_of_work_and_absence("کسری کار و غیبت"),
    Supplementary_insurance("بیمه تکمیلی"),
    Total_Deduction("جمع کسور");

    private final String value;

    Deduction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
