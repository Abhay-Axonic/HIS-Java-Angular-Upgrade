package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.param.entity.model.base.CommonEntity;

/**
 * RULES.md circuit-breaker Tier 0: single-row, system-wide trip state.
 * Deliberately extends CommonEntity, not BaseEntity -- a circuit-breaker
 * trip is about the shared codebase's health, not a per-org/per-unit
 * business setting. See docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md.
 */
@Entity(name = "CircuitBreakerState")
@Table(name = "m_circuit_breaker_state", schema = "public")
public class CircuitBreakerState extends CommonEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	public static final String STATE_NORMAL = "NORMAL";
	public static final String STATE_LEGACY = "LEGACY";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "state", length = 20, nullable = false)
	private String state;

	@Column(name = "trip_reason")
	private String tripReason;

	public CircuitBreakerState() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTripReason() {
		return tripReason;
	}

	public void setTripReason(String tripReason) {
		this.tripReason = tripReason;
	}
}
