package com.param.common.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * FIXED 2026-07-17: regression test for the null-{@code CustomException} crash in
 * {@code ErrorResponse}'s constructors (see
 * docs/DECISION_NEEDED_GlobalExceptionHandlerNullCustomExceptionCrash.md, approved by
 * owner). Both constructors previously called {@code ex.printStackTrace(...)} /
 * {@code ex.getMessage()} unconditionally, with no null-check -- a real, live crash at
 * {@code GlobalExceptionHandler.java:134}, which passes a literal {@code null} (by
 * design, to withhold the raw exception from the client) into the constructor that
 * Java's overload resolution picks for a bare {@code null} argument (the
 * {@code CustomException}-typed one, since it's more specific than {@code Exception}).
 *
 * Now: a null exception leaves {@code summary}/{@code stackTrace}/{@code errors} unset
 * instead of throwing. Non-null behavior is unchanged (asserted below as a regression
 * check, not just the null case).
 */
class ErrorResponseNullExceptionFixTest {

    @Test
    @DisplayName("Exception-typed constructor with null ex: no longer throws; summary/stackTrace stay null")
    void exceptionConstructor_nullEx_doesNotThrow() {
        assertThatCode(() -> new ErrorResponse("HIS-1001", "Something went wrong.", (Exception) null))
                .doesNotThrowAnyException();

        ErrorResponse response = new ErrorResponse("HIS-1001", "Something went wrong.", (Exception) null);
        assertThat(response.getCode()).isEqualTo("HIS-1001");
        assertThat(response.getMessage()).isEqualTo("Something went wrong.");
        assertThat(response.getSummary()).isNull();
        assertThat(response.getStackTrace()).isNull();
    }

    @Test
    @DisplayName("CustomException-typed constructor with null ex: no longer throws; summary/stackTrace/errors stay null")
    void customExceptionConstructor_nullEx_doesNotThrow() {
        assertThatCode(() -> new ErrorResponse("HIS-1001", "Something went wrong.", (CustomException) null))
                .doesNotThrowAnyException();

        ErrorResponse response = new ErrorResponse("HIS-1001", "Something went wrong.", (CustomException) null);
        assertThat(response.getCode()).isEqualTo("HIS-1001");
        assertThat(response.getMessage()).isEqualTo("Something went wrong.");
        assertThat(response.getSummary()).isNull();
        assertThat(response.getStackTrace()).isNull();
        assertThat(response.getErrors()).isNull();
    }

    @Test
    @DisplayName("Reproduces the exact production call shape (GlobalExceptionHandler.java:134): new ErrorResponse(code, message, null) resolves to the CustomException overload and no longer crashes")
    void reproducesProductionCallShape_noLongerCrashes() {
        // Matches `new ErrorResponse("HIS-1001", ErrorCorrelationUtil.clientMessage(correlationId), null)`
        // exactly -- a bare `null` literal, letting Java's own overload resolution pick the
        // most-specific applicable constructor (CustomException), same as production.
        ErrorResponse response = new ErrorResponse("HIS-1001", "Something went wrong. Reference: ERR-12345678", null);

        assertThat(response.getCode()).isEqualTo("HIS-1001");
        assertThat(response.getMessage()).isEqualTo("Something went wrong. Reference: ERR-12345678");
    }

    @Test
    @DisplayName("regression check: Exception-typed constructor with a REAL exception still populates summary/stackTrace exactly as before")
    void exceptionConstructor_nonNullEx_unchanged() {
        Exception ex = new IllegalStateException("boom");

        ErrorResponse response = new ErrorResponse("HIS-1004", "Error while processing data", ex);

        assertThat(response.getSummary()).isEqualTo("boom");
        assertThat(response.getStackTrace()).contains("IllegalStateException").contains("boom");
    }

    @Test
    @DisplayName("regression check: CustomException-typed constructor with a REAL exception still populates summary/stackTrace/errors exactly as before")
    void customExceptionConstructor_nonNullEx_unchanged() throws Exception {
        CustomException ex = new CustomException("boom");

        ErrorResponse response = new ErrorResponse("HIS-1044", "validation failed", ex);

        assertThat(response.getSummary()).isEqualTo("boom");
        assertThat(response.getStackTrace()).contains("CustomException").contains("boom");
        assertThat(response.getErrors()).isEqualTo(ex.getErrors());
    }
}
