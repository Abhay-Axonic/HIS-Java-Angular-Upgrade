package com.param.opd.coversheet.dto;

import java.util.List;

public class MedicationHistoryAppDto {

	private List<MedicationHistoryDto> medicationHistoryList;
	private List<MedicationDTO> orderedMedicationHistory;
	public List<MedicationHistoryDto> getMedicationHistoryList() {
		return medicationHistoryList;
	}
	public void setMedicationHistoryList(List<MedicationHistoryDto> medicationHistoryList) {
		this.medicationHistoryList = medicationHistoryList;
	}
	public List<MedicationDTO> getOrderedMedicationHistory() {
		return orderedMedicationHistory;
	}
	public void setOrderedMedicationHistory(List<MedicationDTO> orderedMedicationHistory) {
		this.orderedMedicationHistory = orderedMedicationHistory;
	}
}
