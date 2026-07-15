package com.param.opd.coversheet.dto;

import java.util.List;

public class DrugsListDTO {
	
	private String medicationName;
	private String genericName;
	List<RoaMasterDto> roaMasterDtos;
	
	public String getMedicationName() {
		return medicationName;
	}
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	public List<RoaMasterDto> getRoaMasterDtos() {
		return roaMasterDtos;
	}
	public void setRoaMasterDtos(List<RoaMasterDto> roaMasterDtos) {
		this.roaMasterDtos = roaMasterDtos;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
}
