package com.param.adt.admission.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

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

import com.param.adt.admission.dao.IBedAllocationDao;
import com.param.adt.common.Response;
import com.param.global.billing.service.IBillingService;
import com.param.global.dto.AdmissionDto;

/**
 * Verifies fix 1b of {@code docs/DECISION_NEEDED_ADTFinancialCallsCrashBugs.md}:
 * {@code BedAllocationServiceImpl.acceptNursingAcceptanceRequet}'s previously-empty
 * {@code catch (Exception e) { }} block (a direct instance of the CLAUDE.md "no error
 * may be silently swallowed" rule) now logs the exception via a proper {@code Logger}
 * instead of discarding it silently. The caller-visible return behavior (the generic
 * {@code COMMON_ERROR_MESSAGE} fallback response) is confirmed UNCHANGED -- this fix is
 * about making the failure visible, not about what the caller receives.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BedAllocationServiceImpl.acceptNursingAcceptanceRequet -- logging fix verification")
class BedAllocationServiceImplAcceptNursingAcceptanceRequetLoggingFixTest {

    @Mock private IBedAllocationDao iBedAllocationDao;
    @Mock private IBillingService globalBillingService;

    private BedAllocationServiceImpl service;
    private CapturingAppender appender;

    @BeforeEach
    void setUp() {
        service = new BedAllocationServiceImpl();
        setPrivate(service, "iBedAllocationDao", iBedAllocationDao);
        setPrivate(service, "globalBillingService", globalBillingService);

        appender = new CapturingAppender();
        Logger.getLogger(BedAllocationServiceImpl.class).addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        Logger.getLogger(BedAllocationServiceImpl.class).removeAppender(appender);
    }

    private static void setPrivate(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
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
    @DisplayName("convertServicesFromOPToIP throws -> exception is now logged (was: silently swallowed), caller still gets the same generic fallback response")
    void conversionThrows_nowLoggedInsteadOfSilentlySwallowed() throws Exception {
        AdmissionDto admission = new AdmissionDto();
        admission.setOrderDetailsIds(Arrays.asList(7001));
        admission.setEncounterId(300);
        admission.setAdmissionId(400);
        admission.setUpdatedBy(99);
        admission.setVisitTypeId(1);
        admission.setPatientId(500);
        admission.setOrganizationId(1);
        admission.setUnitId(0);

        when(iBedAllocationDao.acceptNursingAcceptanceRequet(admission))
                .thenReturn(new Response<>("success", null, null, null, null));
        when(globalBillingService.convertServicesFromOPToIP(any()))
                .thenThrow(new RuntimeException("simulated downstream failure"));

        Response result = service.acceptNursingAcceptanceRequet(admission);

        // Caller-visible behavior unchanged: same generic fallback response as before this fix.
        assertThat(result.getStatus()).isEqualTo("error");
        assertThat(result.getMessage()).isEqualTo("Something went wrong !!");

        // NEW behavior: the exception is now actually logged, not silently discarded.
        assertThat(appender.events).hasSize(1);
        assertThat(appender.events.get(0).getLevel()).isEqualTo(Level.ERROR);
        assertThat(appender.events.get(0).getRenderedMessage()).isEqualTo("acceptNursingAcceptanceRequet failed");
    }
}
