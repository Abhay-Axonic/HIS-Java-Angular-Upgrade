package com.param.opd.coversheet.dto;

import java.util.List;

public class ListOfGeneralExamSystemMaster {
	
	private Integer systemId;
	private char isNADValue;
	private List<ListOfGeneralSystemObservation> listOfGeneralSystemObservation;
	private Integer observationId;

	
	
	
	public char getIsNADValue() {
		return isNADValue;
	}
	public void setIsNADValue(char isNADValue) {
		this.isNADValue = isNADValue;
	}
	public Integer getSystemId() {
		return systemId;
	}
	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}
	public List<ListOfGeneralSystemObservation> getListOfGeneralSystemObservation() {
		return listOfGeneralSystemObservation;
	}
	public void setListOfGeneralSystemObservation(
			List<ListOfGeneralSystemObservation> listOfGeneralSystemObservation) {
		this.listOfGeneralSystemObservation = listOfGeneralSystemObservation;
	}
	public Integer getObservationId() {
		return observationId;
	}
	public void setObservationId(Integer observationId) {
		this.observationId = observationId;
	}
	
	
}
