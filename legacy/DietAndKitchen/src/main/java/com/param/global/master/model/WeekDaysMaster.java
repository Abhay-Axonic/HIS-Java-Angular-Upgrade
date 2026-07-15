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
		@NamedQuery(name = "WEEK_DAYS_MASTER.findAll", query = "SELECT weekDaysMaster.dayId as dayId, weekDaysMaster.day as day from WeekDaysMaster weekDaysMaster") })

@Entity
@Table(name = "m_week_days_master", schema = "kitchen")
@SequenceGenerator(name = "m_week_days_master_day_id_seq", sequenceName = "kitchen.m_week_days_master_day_id_seq", allocationSize = 1)
public class WeekDaysMaster {
	@Id
	@Column(name = "day_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_week_days_master_day_id_seq")
	private Integer dayId;

	@Column(name = "day", length = 30)
	private String day;

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

}
