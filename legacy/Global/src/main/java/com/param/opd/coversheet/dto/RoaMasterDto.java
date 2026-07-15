package com.param.opd.coversheet.dto;

import java.util.List;

public class RoaMasterDto {
	private int roa_id;
	private String roa_details;
	List<String> dose;

	public int getRoa_id() {
		return roa_id;
	}

	public void setRoa_id(int roa_id) {
		this.roa_id = roa_id;
	}

	public String getRoa_details() {
		return roa_details;
	}

	public void setRoa_details(String roa_details) {
		this.roa_details = roa_details;
	}

	public List<String> getDose() {
		return dose;
	}

	public void setDose(List<String> dose) {
		this.dose = dose;
	}
}
