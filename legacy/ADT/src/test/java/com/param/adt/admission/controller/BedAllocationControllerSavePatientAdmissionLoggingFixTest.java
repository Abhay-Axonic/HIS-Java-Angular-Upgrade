package com.param.adt.admission.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.adt.admission.service.IBedAllocationService;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;

/**
 * Verifies fix 1b of {@code docs/DECISION_NEEDED_ADTFinancialCallsCrashBugs.md}:
 * {@code BedAllocationController.savePatientAdmission}'s catch block previously called
 * only {@code e.printStackTrace()} (writes to stderr, not captured by application logs
 * or the centralized handler -- functionally the same "silently swallowed" problem
 * CLAUDE.md's rule targets, even though this specific catch wasn't literally an empty
 * block). It now logs via a proper {@code Logger} instead. Caller-visible return
 * behavior (the generic {@code COMMON_ERROR_MESSAGE} fallback response) is confirmed
 * UNCHANGED.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BedAllocationController.savePatientAdmission -- logging fix verification")
class BedAllocationControllerSavePatientAdmissionLoggingFixTest {

    @Mock private IBedAllocationService iBedAllocationService;

    private BedAllocationController controller;
    private CapturingAppender appender;

    @BeforeEach
    void setUp() {
        controller = new BedAllocationController();
        controller.iBedAllocationService = iBedAllocationService;

        appender = new CapturingAppender();
        Logger.getLogger(BedAllocationController.class).addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        Logger.getLogger(BedAllocationController.class).removeAppender(appender);
    }

    private static class CapturingAppender extends AppenderSkeleton {
        final java.util.List<LoggingEvent> events = new java.util.ArrayList<>();

        @Override
        protected void append(LoggingEvent event) {
            events.add(event);
        }

        @Override
        public void close() {
        }

        @Override
        public boolean requiresLayout() {
            return false;
        }
    }

    @Test
    @DisplayName("isBedBookedOrNot throws -> exception is now logged via Logger.error (was: e.printStackTrace() only), caller still gets the same generic fallback response")
    void bedBookingCheckThrows_nowLoggedInsteadOfOnlyPrintStackTrace() {
        AdmissionDto admission = new AdmissionDto();
        // No payment-detail list -> ccAvenue gate is skipped, isBedBookedOrNot is reached directly.
        when(iBedAllocationService.isBedBookedOrNot(admission))
                .thenThrow(new RuntimeException("simulated downstream failure"));

        Response result = controller.savePatientAdmission(admission);

        // Caller-visible behavior unchanged: same generic fallback response as before this fix.
        assertThat(result.getStatus()).isEqualTo("error");
        assertThat(result.getMessage()).isEqualTo("Something went wrong !!");

        // NEW behavior: the exception is now actually logged via a Logger, not just printed to stderr.
        assertThat(appender.events).hasSize(1);
        assertThat(appender.events.get(0).getLevel()).isEqualTo(Level.ERROR);
        assertThat(appender.events.get(0).getRenderedMessage()).isEqualTo("savePatientAdmission failed");
    }
}
