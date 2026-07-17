package com.param.global.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.entity.model.master.CircuitBreakerState;
import com.param.global.common.Response;
import com.param.global.dao.ICircuitBreakerStateDao;

/**
 * Tier 0 circuit-breaker state service -- thin delegation to the DAO, same
 * shape as this project's other service-layer tests. See
 * docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CircuitBreakerStateServiceImpl delegates to the DAO and passes through its Response")
class CircuitBreakerStateServiceImplTest {

    @Mock
    private ICircuitBreakerStateDao circuitBreakerStateDao;

    private CircuitBreakerStateServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CircuitBreakerStateServiceImpl();
        service.iCircuitBreakerStateDao = circuitBreakerStateDao;
    }

    @Test
    void getState_returnsWhateverTheDaoReturns() throws Exception {
        CircuitBreakerState state = new CircuitBreakerState();
        state.setState(CircuitBreakerState.STATE_NORMAL);
        Response<?, ?> daoResponse = new Response<>("success", "200", null, null, state);
        when(circuitBreakerStateDao.getState()).thenReturn(daoResponse);

        Response result = service.getState();

        assertThat(result).isSameAs(daoResponse);
    }

    @Test
    void flipToLegacy_passesReasonAndUpdatedByThroughToDao() throws Exception {
        Response<?, ?> daoResponse = new Response<>("success", "200", "Updated Successfully.", null, null);
        when(circuitBreakerStateDao.flipToLegacy("threshold exceeded", 42)).thenReturn(daoResponse);

        Response result = service.flipToLegacy("threshold exceeded", 42);

        assertThat(result).isSameAs(daoResponse);
        verify(circuitBreakerStateDao).flipToLegacy("threshold exceeded", 42);
    }

    @Test
    void resetToNormal_passesUpdatedByThroughToDao() throws Exception {
        Response<?, ?> daoResponse = new Response<>("success", "200", "Updated Successfully.", null, null);
        when(circuitBreakerStateDao.resetToNormal(7)).thenReturn(daoResponse);

        Response result = service.resetToNormal(7);

        assertThat(result).isSameAs(daoResponse);
        verify(circuitBreakerStateDao).resetToNormal(7);
    }
}
