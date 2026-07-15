package com.param.global.master.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "DIET_TIMING_MASTER.findAll", query = "SELECT dietTimingMaster.timingId as timingId, dietTimingMaster.timingType as timingType"
				+ " from DietTimingMaster dietTimingMaster") })

@Entity
@Table(name = "m_diet_timing_master", schema = "kitchen")
@SequenceGenerator(name = "m_diet_timing_master_timing_id_seq", sequenceName = "kitchen.m_diet_timing_master_timing_id_seq", allocationSize = 1)
public class DietTimingMaster {
	@Id
	@Column(name = "timing_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_diet_timing_master_timing_id_seq")
	private Integer timingId;

	@Column(name = "timing_type", length = 30)
	private String timingType;

	public Integer getTimingId() {
		return timingId;
	}

	public void setTimingId(Integer timingId) {
		this.timingId = timingId;
	}

	public String getTimingType() {
		return timingType;
	}

	public void setTimingType(String timingType) {
		this.timingType = timingType;
	}

	@Override
	public String toString() {
		return "DietTimingMaster [timingId=" + timingId + ", timingType=" + timingType + "]";
	}

}
