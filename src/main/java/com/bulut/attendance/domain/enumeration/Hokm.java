package com.bulut.attendance.domain.enumeration;

/**
 * The Hokm enumeration.
 */
public enum Hokm {
    Other("سایر"),
    BASE("حقوق پایه"),
    CHILD("حق اولاد"),
    LABOR("بن کارگری"),
    FOOD("بن خواربار"),
    RANGE("مزد رتبه"),
    ROLE("مزایای پست"),
    EFFECTIVENESS("فوق العاده کارایی"),
    HOUSE("حق مسکن"),
    TOTAL_HOKM("جمع حکم");

    private final String value;

    Hokm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
