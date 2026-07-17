package com.param.global.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.master.CircuitBreakerState;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

/**
 * Tier 0 of the circuit-breaker/monitoring proposal
 * (docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md). Single row
 * (id=1, seeded by docs/br099_circuit_breaker_state.sql) -- there is
 * exactly one system-wide breaker, not one per org/unit.
 */
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CircuitBreakerStateDaoImpl extends GenericDao<CircuitBreakerState, Integer>
		implements ICircuitBreakerStateDao, ICommonConstants {

	private static final Integer SINGLETON_ROW_ID = 1;

	final static Logger logger = Logger.getLogger(CircuitBreakerStateDaoImpl.class);

	public CircuitBreakerStateDaoImpl() {
		super(CircuitBreakerState.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getState() throws ApplicationException {
		try {
			CircuitBreakerState state = (CircuitBreakerState) sessionFactory.getCurrentSession()
					.get(CircuitBreakerState.class, SINGLETON_ROW_ID);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, state);
		} catch (HibernateException he) {
			logger.error("getState failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getState failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response flipToLegacy(String tripReason, Integer updatedBy) throws ApplicationException {
		try {
			CircuitBreakerState state = (CircuitBreakerState) sessionFactory.getCurrentSession()
					.get(CircuitBreakerState.class, SINGLETON_ROW_ID);
			state.setState(CircuitBreakerState.STATE_LEGACY);
			state.setTripReason(tripReason);
			state.setUpdatedByUser(updatedBy);
			state.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			sessionFactory.getCurrentSession().update(state);
			logger.error("CIRCUIT_BREAKER_TRIP: flipped to LEGACY. reason=" + tripReason);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, state);
		} catch (HibernateException he) {
			logger.error("flipToLegacy failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("flipToLegacy failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response resetToNormal(Integer updatedBy) throws ApplicationException {
		try {
			CircuitBreakerState state = (CircuitBreakerState) sessionFactory.getCurrentSession()
					.get(CircuitBreakerState.class, SINGLETON_ROW_ID);
			state.setState(CircuitBreakerState.STATE_NORMAL);
			state.setTripReason(null);
			state.setUpdatedByUser(updatedBy);
			state.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			sessionFactory.getCurrentSession().update(state);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, state);
		} catch (HibernateException he) {
			logger.error("resetToNormal failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("resetToNormal failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
