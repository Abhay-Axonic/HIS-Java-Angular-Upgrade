package com.param.global.otims.dto;

import java.util.List;

public class OtSchedulingTimeDetailsDto {

	private String toDate;
	
	private String fromDate;
	
	private Integer switchOverTime;
	
	private String fromTime;
	
	private String toTime;
	
	private List<Integer> days;

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getSwitchOverTime() {
		return switchOverTime;
	}

	public void setSwitchOverTime(Integer switchOverTime) {
		this.switchOverTime = switchOverTime;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public List<Integer> getDays() {
		return days;
	}

	public void setDays(List<Integer> days) {
		this.days = days;
	}
}
