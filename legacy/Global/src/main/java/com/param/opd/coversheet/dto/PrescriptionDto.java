package com.param.opd.coversheet.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


public class PrescriptionDto {

	private Integer prescriptionId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer visitTypeId;
	
	private Integer encounterId;
	
	private Integer doctorId;
	
	private Integer patientId;
	
	private String prescriptionNo;
	
	private String prescriptionDate;
	
	private Boolean isActive;
	
	private Integer addedBy;
	
	private String addedDate;
	
	private Boolean isDeleted;
	
	private Integer statusId;
	
	private Integer priorityId;
	
	private Boolean isValidate;
	
	private Integer authorisedBy;

	private String authorisedDate;
	
	private Boolean isIntervention;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer admissionId;
	
	private Boolean isDischarge;

	private boolean statflag;
	
	private Boolean dischargeMedicationFlag;
	private Boolean reconciliationDone;
	private Integer dose;
	private Integer doseUnitId;
	private Integer frequencyId;
	private Integer instructionId;
	private Integer itemId;
	private Integer orderQty;
	private Integer routeId;
	private String startDate;
	private String itemName;
	private String genericName;

	private String stopDate;
	private Integer storeId;
	private Integer strength;
	private Integer prescriptionDetailsid;
//	private Integer prescriptionFreqId;
	private Integer prescriptionFrequencyId;
	private String specialityName;
	private Integer employeeId;
	private String roaDetails;
	private Integer specialityId;
	private String justificationStop;
	private Character isMedicationStop;
	private Integer deptId;
	private Integer genericId;
	private Integer prescriptionCount;
	private Integer count;
	private Character isNonFormulary;
	private String instructionText;
	private Integer parentSubstituteId;
	private BigDecimal rate;
	private BigInteger countMed;
	private String medicationName;
	private Integer formulationTypeId;
	private Integer strengthUnitId;
	private String unitDesc;
	private Boolean isNarcotic;
	private Boolean isOtims;
	private Boolean isPharmacologicalValidationSkipIp;
	private Boolean isFromOP;
	private Integer drugId;
	private Integer medicationType;
	private Integer covTemplateId;
	private Integer oldPrescriptionId;
	private String reqXml;
	private Integer issueId;
	private Integer issueDetailsId;
	private List<Integer> orderDetailsIds;
	private List<Integer> orderPrescriptionIds;

	private List<MedicationDTO> detailedMedications;

	private List<PatientPersonalHistoryDetailsDto>  personalHistoryCurrentList;

	private List<PatientPersonalHistoryDetailsDto>  personalHistoryOldList;

	private List<MedicationDTO> medicationList;

	private List<PatientFamilyHistoryDto> familyHistoryOldList;

	private List<PatientFamilyHistoryDto> familyHistoryCurrentList;

	private List<PatientSurgicalHistoryDto> surgicalHistoryOldList;

	private List<PatientSurgicalHistoryDto>  surgicalHistoryCurrentList;

	private List<PatientMedicalHistoryDto> medicalHistoryOldList;

	private List<PatientMedicalHistoryDto> medicalHistoryCurrentList;

	private List<PatientTravelHistoryDetailsDto> travelHistoryOldList;

	private List<PatientTravelHistoryDetailsDto> travelHistoryCurrentList;

	private List<ImmunizationHistoryMapperDto> immunizationHistoryOldList;

	private List<ImmunizationHistoryMapperDto> immunizationHistoryCurrentList;

	private List<PatientHabitDetailsDto> patientHabitOldList;

	private List<PatientHabitDetailsDto> patientHabitList;

	private List<NursingNoteDto>  nursingNoteList;

	private List<VaccineImmunizationMapperDto> vaccineMapperList;
	
	private List<String> dates;

	private List<PatientSurgicalHistoryDto> sugicalProcedureList;

	private List<MedicationHistoryDto> detailedMedicationsHistory;

	private List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisListForMedicalHistory;

	private List<PatientTreatmentHistoryDetailsDto> treatmentHistoryCurrentList;

	private List<PatientTreatmentHistoryDetailsDto> treatmentHistoryOldList;

	private List<PatientTreatmentHistoryDetailsDto> listCompletedMedicalProcedures;

	private List<AppointmentVitalMapperDto> patientViatalList;

	private List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisList;

	private List<InfusionSolutionDto> infusionList;

	private List<CoversheetInstructionDto> savedInstructions;

	private List<ConsultDoctorDto> consultOrdersList;

	private List<GeneralExaminationAppoMapperDto> getSavedListOfGeneralExam;
	private List<GeneralExaminationAppoMapperDto> getSavedListOfSystemicExam;

	private List<NursingOrderMapperDto> nursingOrederedListByPatientId;

	private List<ObservationDto> observationList;

	private String descriptiveExamination;

	private String sysDescriptiveExamination;


	public String getSysDescriptiveExamination() {
		return sysDescriptiveExamination;
	}

	public void setSysDescriptiveExamination(String sysDescriptiveExamination) {
		this.sysDescriptiveExamination = sysDescriptiveExamination;
	}

	public String getDescriptiveExamination() {
		return descriptiveExamination;
	}

	public void setDescriptiveExamination(String descriptiveExamination) {
		this.descriptiveExamination = descriptiveExamination;
	}

	public List<GeneralExaminationAppoMapperDto> getGetSavedListOfSystemicExam() {
		return getSavedListOfSystemicExam;
	}

	public void setGetSavedListOfSystemicExam(List<GeneralExaminationAppoMapperDto> getSavedListOfSystemicExam) {
		this.getSavedListOfSystemicExam = getSavedListOfSystemicExam;
	}

	public List<ObservationDto> getObservationList() {
		return observationList;
	}

	public void setObservationList(List<ObservationDto> observationList) {
		this.observationList = observationList;
	}

	public List<NursingOrderMapperDto> getNursingOrederedListByPatientId() {
		return nursingOrederedListByPatientId;
	}

	public void setNursingOrederedListByPatientId(List<NursingOrderMapperDto> nursingOrederedListByPatientId) {
		this.nursingOrederedListByPatientId = nursingOrederedListByPatientId;
	}

	public List<GeneralExaminationAppoMapperDto> getGetSavedListOfGeneralExam() {
		return getSavedListOfGeneralExam;
	}

	public void setGetSavedListOfGeneralExam(List<GeneralExaminationAppoMapperDto> getSavedListOfGeneralExam) {
		this.getSavedListOfGeneralExam = getSavedListOfGeneralExam;
	}

	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge = isDischarge;
	}


	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	private List<PrescriptionDetailsDto> prescriptionDetailsDtosList;
	private List<PrescriptionDetailsDto> statPrescriptionDetailsDtosList;
	private List<PrescriptionDetailsDto> normalPrescriptionDetailsDtosList;
	
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Boolean getIsIntervention() {
		return isIntervention;
	}

	public void setIsIntervention(Boolean isIntervention) {
		this.isIntervention = isIntervention;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public Integer getAuthorisedBy() {
		return authorisedBy;
	}

	public void setAuthorisedBy(Integer authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public String getAuthorisedDate() {
		return authorisedDate;
	}

	public void setAuthorisedDate(String authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public List<PrescriptionDetailsDto> getPrescriptionDetailsDtosList() {
		return prescriptionDetailsDtosList;
	}

	public void setPrescriptionDetailsDtosList(List<PrescriptionDetailsDto> prescriptionDetailsDtosList) {
		this.prescriptionDetailsDtosList = prescriptionDetailsDtosList;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getPrescriptionNo() {
		return prescriptionNo;
	}

	public void setPrescriptionNo(String prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}

	public String getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(String prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Boolean getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(Boolean isValidate) {
		this.isValidate = isValidate;
	}

	public Boolean getStatflag() {
		return statflag;
	}

	public void setStatflag(Boolean statflag) {
		this.statflag = statflag;
	}

	public Integer getDose() {
		return dose;
	}

	public void setDose(Integer dose) {
		this.dose = dose;
	}

	public Integer getDoseUnitId() {
		return doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public Integer getPrescriptionDetailsid() {
		return prescriptionDetailsid;
	}

	public void setPrescriptionDetailsid(Integer prescriptionDetailsid) {
		this.prescriptionDetailsid = prescriptionDetailsid;
	}


	public Integer getPrescriptionFrequencyId() {
		return prescriptionFrequencyId;
	}

	public void setPrescriptionFrequencyId(Integer prescriptionFrequencyId) {
		this.prescriptionFrequencyId = prescriptionFrequencyId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Boolean getDischargeMedicationFlag() {
		return dischargeMedicationFlag;
	}

	public void setDischargeMedicationFlag(Boolean dischargeMedicationFlag) {
		this.dischargeMedicationFlag = dischargeMedicationFlag;
	}
	
	public String getRoaDetails() {
		return roaDetails;
	}

	public void setRoaDetails(String roaDetails) {
		this.roaDetails = roaDetails;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}


	public String getJustificationStop() {
		return justificationStop;
	}

	public void setJustificationStop(String justificationStop) {
		this.justificationStop = justificationStop;
	}

	

	public Boolean getReconciliationDone() {
		return reconciliationDone;
	}

	public void setReconciliationDone(Boolean reconciliationDone) {
		this.reconciliationDone = reconciliationDone;
	}

	public Integer getPrescriptionCount() {
		return prescriptionCount;
	}

	public void setPrescriptionCount(Integer prescriptionCount) {
		this.prescriptionCount = prescriptionCount;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Character getIsMedicationStop() {
		return isMedicationStop;
	}

	public void setIsMedicationStop(Character isMedicationStop) {
		this.isMedicationStop = isMedicationStop;
	}

	public Character getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(Character isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}

	public Boolean getIsOtims() {
		return isOtims;
	}

	public void setIsOtims(Boolean isOtims) {
		this.isOtims = isOtims;
	}

	public Boolean getIsPharmacologicalValidationSkipIp() {
		return isPharmacologicalValidationSkipIp;
	}

	public void setIsPharmacologicalValidationSkipIp(Boolean isPharmacologicalValidationSkipIp) {
		this.isPharmacologicalValidationSkipIp = isPharmacologicalValidationSkipIp;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}

	public Boolean getIsFromOP() {
		return isFromOP;
	}

	public void setIsFromOP(Boolean isFromOP) {
		this.isFromOP = isFromOP;
	}

	public List<PrescriptionDetailsDto> getStatPrescriptionDetailsDtosList() {
		return statPrescriptionDetailsDtosList;
	}

	public void setStatPrescriptionDetailsDtosList(List<PrescriptionDetailsDto> statPrescriptionDetailsDtosList) {
		this.statPrescriptionDetailsDtosList = statPrescriptionDetailsDtosList;
	}

	public List<PrescriptionDetailsDto> getNormalPrescriptionDetailsDtosList() {
		return normalPrescriptionDetailsDtosList;
	}

	public void setNormalPrescriptionDetailsDtosList(List<PrescriptionDetailsDto> normalPrescriptionDetailsDtosList) {
		this.normalPrescriptionDetailsDtosList = normalPrescriptionDetailsDtosList;
	}

	

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}


	public BigInteger getCountMed() {
		return countMed;
	}

	public void setCountMed(BigInteger countMed) {
		this.countMed = countMed;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getDrugId() {
		return drugId;
	}

	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}

	public Integer getMedicationType() {
		return medicationType;
	}

	public void setMedicationType(Integer medicationType) {
		this.medicationType = medicationType;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public String getReqXml() {
		return reqXml;
	}

	public void setReqXml(String reqXml) {
		this.reqXml = reqXml;
	}

	public Integer getOldPrescriptionId() {
		return oldPrescriptionId;
	}

	public void setOldPrescriptionId(Integer oldPrescriptionId) {
		this.oldPrescriptionId = oldPrescriptionId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getIssueDetailsId() {
		return issueDetailsId;
	}

	public void setIssueDetailsId(Integer issueDetailsId) {
		this.issueDetailsId = issueDetailsId;
	}

	public List<Integer> getOrderDetailsIds() {
		return orderDetailsIds;
	}

	public void setOrderDetailsIds(List<Integer> orderDetailsIds) {
		this.orderDetailsIds = orderDetailsIds;
	}

	public List<Integer> getOrderPrescriptionIds() {
		return orderPrescriptionIds;
	}

	public void setOrderPrescriptionIds(List<Integer> orderPrescriptionIds) {
		this.orderPrescriptionIds = orderPrescriptionIds;
	}

	public List<PatientPersonalHistoryDetailsDto> getPersonalHistoryCurrentList() {
		return personalHistoryCurrentList;
	}

	public void setPersonalHistoryCurrentList(List<PatientPersonalHistoryDetailsDto> personalHistoryCurrentList) {
		this.personalHistoryCurrentList = personalHistoryCurrentList;
	}

	public List<MedicationDTO> getDetailedMedications() {
		return detailedMedications;
	}

	public void setDetailedMedications(List<MedicationDTO> detailedMedications) {
		this.detailedMedications = detailedMedications;
	}

	public List<MedicationDTO> getMedicationList() {
		return medicationList;
	}

	public void setMedicationList(List<MedicationDTO> medicationList) {
		this.medicationList = medicationList;
	}

	public List<PatientFamilyHistoryDto> getFamilyHistoryOldList() {
		return familyHistoryOldList;
	}

	public void setFamilyHistoryOldList(List<PatientFamilyHistoryDto> familyHistoryOldList) {
		this.familyHistoryOldList = familyHistoryOldList;
	}

	public List<PatientFamilyHistoryDto> getFamilyHistoryCurrentList() {
		return familyHistoryCurrentList;
	}

	public void setFamilyHistoryCurrentList(List<PatientFamilyHistoryDto> familyHistoryCurrentList) {
		this.familyHistoryCurrentList = familyHistoryCurrentList;
	}

	public List<PatientSurgicalHistoryDto> getSurgicalHistoryOldList() {
		return surgicalHistoryOldList;
	}

	public void setSurgicalHistoryOldList(List<PatientSurgicalHistoryDto> surgicalHistoryOldList) {
		this.surgicalHistoryOldList = surgicalHistoryOldList;
	}

	public List<PatientSurgicalHistoryDto> getSurgicalHistoryCurrentList() {
		return surgicalHistoryCurrentList;
	}

	public void setSurgicalHistoryCurrentList(List<PatientSurgicalHistoryDto> surgicalHistoryCurrentList) {
		this.surgicalHistoryCurrentList = surgicalHistoryCurrentList;
	}

	public List<PatientMedicalHistoryDto> getMedicalHistoryOldList() {
		return medicalHistoryOldList;
	}

	public void setMedicalHistoryOldList(List<PatientMedicalHistoryDto> medicalHistoryOldList) {
		this.medicalHistoryOldList = medicalHistoryOldList;
	}

	public List<PatientMedicalHistoryDto> getMedicalHistoryCurrentList() {
		return medicalHistoryCurrentList;
	}

	public void setMedicalHistoryCurrentList(List<PatientMedicalHistoryDto> medicalHistoryCurrentList) {
		this.medicalHistoryCurrentList = medicalHistoryCurrentList;
	}

	public List<PatientTravelHistoryDetailsDto> getTravelHistoryOldList() {
		return travelHistoryOldList;
	}

	public void setTravelHistoryOldList(List<PatientTravelHistoryDetailsDto> travelHistoryOldList) {
		this.travelHistoryOldList = travelHistoryOldList;
	}

	public List<PatientTravelHistoryDetailsDto> getTravelHistoryCurrentList() {
		return travelHistoryCurrentList;
	}

	public void setTravelHistoryCurrentList(List<PatientTravelHistoryDetailsDto> travelHistoryCurrentList) {
		this.travelHistoryCurrentList = travelHistoryCurrentList;
	}

	public List<ImmunizationHistoryMapperDto> getImmunizationHistoryOldList() {
		return immunizationHistoryOldList;
	}

	public void setImmunizationHistoryOldList(List<ImmunizationHistoryMapperDto> immunizationHistoryOldList) {
		this.immunizationHistoryOldList = immunizationHistoryOldList;
	}

	public List<ImmunizationHistoryMapperDto> getImmunizationHistoryCurrentList() {
		return immunizationHistoryCurrentList;
	}

	public void setImmunizationHistoryCurrentList(List<ImmunizationHistoryMapperDto> immunizationHistoryCurrentList) {
		this.immunizationHistoryCurrentList = immunizationHistoryCurrentList;
	}

	public List<PatientHabitDetailsDto> getPatientHabitOldList() {
		return patientHabitOldList;
	}

	public void setPatientHabitOldList(List<PatientHabitDetailsDto> patientHabitOldList) {
		this.patientHabitOldList = patientHabitOldList;
	}

	public List<PatientHabitDetailsDto> getPatientHabitList() {
		return patientHabitList;
	}

	public void setPatientHabitList(List<PatientHabitDetailsDto> patientHabitList) {
		this.patientHabitList = patientHabitList;
	}

	public List<NursingNoteDto> getNursingNoteList() {
		return nursingNoteList;
	}

	public void setNursingNoteList(List<NursingNoteDto> nursingNoteList) {
		this.nursingNoteList = nursingNoteList;
	}

	public List<PatientPersonalHistoryDetailsDto> getPersonalHistoryOldList() {
		return personalHistoryOldList;
	}

	public void setPersonalHistoryOldList(List<PatientPersonalHistoryDetailsDto> personalHistoryOldList) {
		this.personalHistoryOldList = personalHistoryOldList;
	}

	public List<VaccineImmunizationMapperDto> getVaccineMapperList() {
		return vaccineMapperList;
	}

	public void setVaccineMapperList(List<VaccineImmunizationMapperDto> vaccineMapperList) {
		this.vaccineMapperList = vaccineMapperList;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public List<PatientSurgicalHistoryDto> getSugicalProcedureList() {
		return sugicalProcedureList;
	}

	public void setSugicalProcedureList(List<PatientSurgicalHistoryDto> sugicalProcedureList) {
		this.sugicalProcedureList = sugicalProcedureList;
	}

	public List<MedicationHistoryDto> getDetailedMedicationsHistory() {
		return detailedMedicationsHistory;
	}

	public void setDetailedMedicationsHistory(List<MedicationHistoryDto> detailedMedicationsHistory) {
		this.detailedMedicationsHistory = detailedMedicationsHistory;
	}

	public List<DiagnosisPatientAppointmentMapperDto> getPatientDiagnosisListForMedicalHistory() {
		return patientDiagnosisListForMedicalHistory;
	}

	public void setPatientDiagnosisListForMedicalHistory(List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisListForMedicalHistory) {
		this.patientDiagnosisListForMedicalHistory = patientDiagnosisListForMedicalHistory;
	}

	public List<PatientTreatmentHistoryDetailsDto> getTreatmentHistoryCurrentList() {
		return treatmentHistoryCurrentList;
	}

	public void setTreatmentHistoryCurrentList(List<PatientTreatmentHistoryDetailsDto> treatmentHistoryCurrentList) {
		this.treatmentHistoryCurrentList = treatmentHistoryCurrentList;
	}

	public List<PatientTreatmentHistoryDetailsDto> getTreatmentHistoryOldList() {
		return treatmentHistoryOldList;
	}

	public void setTreatmentHistoryOldList(List<PatientTreatmentHistoryDetailsDto> treatmentHistoryOldList) {
		this.treatmentHistoryOldList = treatmentHistoryOldList;
	}

	public List<PatientTreatmentHistoryDetailsDto> getListCompletedMedicalProcedures() {
		return listCompletedMedicalProcedures;
	}

	public void setListCompletedMedicalProcedures(List<PatientTreatmentHistoryDetailsDto> listCompletedMedicalProcedures) {
		this.listCompletedMedicalProcedures = listCompletedMedicalProcedures;
	}

	public List<AppointmentVitalMapperDto> getPatientViatalList() {
		return patientViatalList;
	}

	public void setPatientViatalList(List<AppointmentVitalMapperDto> patientViatalList) {
		this.patientViatalList = patientViatalList;
	}

	public List<DiagnosisPatientAppointmentMapperDto> getPatientDiagnosisList() {
		return patientDiagnosisList;
	}

	public void setPatientDiagnosisList(List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisList) {
		this.patientDiagnosisList = patientDiagnosisList;
	}

	public List<InfusionSolutionDto> getInfusionList() {
		return infusionList;
	}

	public void setInfusionList(List<InfusionSolutionDto> infusionList) {
		this.infusionList = infusionList;
	}

	public List<CoversheetInstructionDto> getSavedInstructions() {
		return savedInstructions;
	}

	public void setSavedInstructions(List<CoversheetInstructionDto> savedInstructions) {
		this.savedInstructions = savedInstructions;
	}

	public List<ConsultDoctorDto> getConsultOrdersList() {
		return consultOrdersList;
	}

	public void setConsultOrdersList(List<ConsultDoctorDto> consultOrdersList) {
		this.consultOrdersList = consultOrdersList;
	}
}