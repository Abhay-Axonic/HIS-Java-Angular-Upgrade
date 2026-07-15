package com.param.global.master.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DietTimingMasterDto {

	private Integer timingId;

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

}
