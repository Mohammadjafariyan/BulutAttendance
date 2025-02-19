package com.bulut.attendance.domain;

import static com.bulut.attendance.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class WorkAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkAllPropertiesEquals(Work expected, Work actual) {
        assertWorkAutoGeneratedPropertiesEquals(expected, actual);
        assertWorkAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkAllUpdatablePropertiesEquals(Work expected, Work actual) {
        assertWorkUpdatableFieldsEquals(expected, actual);
        assertWorkUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkAutoGeneratedPropertiesEquals(Work expected, Work actual) {
        assertThat(expected)
            .as("Verify Work auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkUpdatableFieldsEquals(Work expected, Work actual) {
        assertThat(expected)
            .as("Verify Work relevant properties")
            .satisfies(
                e ->
                    assertThat(e.getIssueDate())
                        .as("check issueDate")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getIssueDate())
            )
            .satisfies(e -> assertThat(e.getDesc()).as("check desc").isEqualTo(actual.getDesc()))
            .satisfies(e -> assertThat(e.getYear()).as("check year").isEqualTo(actual.getYear()))
            .satisfies(e -> assertThat(e.getMonth()).as("check month").isEqualTo(actual.getMonth()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWorkUpdatableRelationshipsEquals(Work expected, Work actual) {
        assertThat(expected)
            .as("Verify Work relationships")
            .satisfies(e -> assertThat(e.getInternalUser()).as("check internalUser").isEqualTo(actual.getInternalUser()))
            .satisfies(e -> assertThat(e.getCompany()).as("check company").isEqualTo(actual.getCompany()));
    }
}
