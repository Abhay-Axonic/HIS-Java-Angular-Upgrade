package com.param.global.pharmacy.dto;

import java.math.BigInteger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class PrescriptionDetailsForOPSalesWorklistDto {

	private Integer prescriptionDetailsid;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private BigInteger prescriptionId;
	
	private Integer itemId;
	
	private Integer storeId;
	
	private Integer uomUnitId;
	
	private Integer orderQty;
	
	private Integer pendingQty;
	
	private Integer refillQty;
	
	private Double strength;
	
	private Boolean  isDiscontinued;
	
	private Timestamp prescriptionDate;

	private Boolean isPreStop;
	private Boolean isNarcotic;
	private Boolean isIntervention;
	
	private Integer addedBy;
	
	private Date addedDate;
	
	private Boolean isDeleted;
	
	private Boolean isActive;
	
	private Integer statusId;	
	
	private String remark;
	
	private Boolean prnflag;
	
	private Boolean statflag;
	
	private Boolean infusionflag;
	
	private Double dose;
	
	private Integer doseUnitId;
	
	private Integer frequencyId;
	 
	private Integer routeId;
	
	private Date startDate;
	
	private Date stopDate;
	
	private String instructions;
	
	private Integer consumableMethodId;

	private Integer authorisedBy;

	private Timestamp authorisedDate;
	
	private Integer instructionId;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String itemName;
	
	private String genericName;
	private Double doseDesc;
	private String strengthDesc;

	private Boolean isDischarge;
	private Boolean isStat;

	private String justificationStop;
//	private Integer prescriptionFreqId;
	private String frequencyDesc;
	
	private String roaDesc;
	
	private String instructionDesc;
	
	
	private Integer doctorId;
	
	private Integer specialityId;
	
	private Integer visitTypeId;
	
	private Integer priorityId;
	
	
	
	private String encounterDate;

	private Integer quantity;
	
	private Integer formulationTypeId;

	private Integer genericId;
	private Integer roaId;

	
	
	private String searchItem;
	
	private String searchItemFre;
	
	private String roa_details;
	
	
	private String medicationStartDate;
	
	private String medicationEndDate;
	
	private String doctorName;

	private Integer noOfAdministrationTime;
	private String prescriptionNo;
	
	private Character isMedicationStopHis;
	
	private char isMedicationStop;
	
	private Integer prescriptionFrequencyId;
	
	private Character isNonFormulary;
	
	private Integer encounterId;
	
	private Integer doseId;
	
	private Integer duration;
	
	private String durationUnit;
	
	private String endDate;
	
	private Integer patientId;
	
	private Character status;
	
	private String createdDate;
	private String instructionText;

	private Integer createdBy;
	private Integer admissionId;
	private Integer parentSubstituteId;
	private Integer parentItemId;

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

	public Integer getAuthorisedBy() {
		return authorisedBy;
	}

	public void setAuthorisedBy(Integer authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getConsumableMethodId() {
		return consumableMethodId;
	}

	public void setConsumableMethodId(Integer consumableMethodId) {
		this.consumableMethodId = consumableMethodId;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
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

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}


	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/*public Integer getPrescriptionDetailsid() {
		return prescriptionDetailsid;
	}

	public void setPrescriptionDetailsid(Integer prescriptionDetailsid) {
		this.prescriptionDetailsid = prescriptionDetailsid;
	}*/
	

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

	

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getPendingQty() {
		return pendingQty;
	}

	public void setPendingQty(Integer pendingQty) {
		this.pendingQty = pendingQty;
	}

	public Integer getRefillQty() {
		return refillQty;
	}

	public void setRefillQty(Integer refillQty) {
		this.refillQty = refillQty;
	}

	public Double getStrength() {
		return strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public Boolean getIsDiscontinued() {
		return isDiscontinued;
	}

	public void setIsDiscontinued(Boolean isDiscontinued) {
		this.isDiscontinued = isDiscontinued;
	}


	public Boolean getIsPreStop() {
		return isPreStop;
	}

	public void setIsPreStop(Boolean isPreStop) {
		this.isPreStop = isPreStop;
	}

	public Boolean getIsIntervention() {
		return isIntervention;
	}

	public void setIsIntervention(Boolean isIntervention) {
		this.isIntervention = isIntervention;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getPrnflag() {
		return prnflag;
	}

	public void setPrnflag(Boolean prnflag) {
		this.prnflag = prnflag;
	}

	public Boolean getStatflag() {
		return statflag;
	}

	public void setStatflag(Boolean statflag) {
		this.statflag = statflag;
	}

	public Boolean getInfusionflag() {
		return infusionflag;
	}

	public void setInfusionflag(Boolean infusionflag) {
		this.infusionflag = infusionflag;
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


	public Integer getPrescriptionFrequencyId() {
		return prescriptionFrequencyId;
	}

	public void setPrescriptionFrequencyId(Integer prescriptionFrequencyId) {
		this.prescriptionFrequencyId = prescriptionFrequencyId;
	}

	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge = isDischarge;
	}

	public Character getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(Character isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public String getJustificationStop() {
		return justificationStop;
	}

	public void setJustificationStop(String justificationStop) {
		this.justificationStop = justificationStop;
	}

	public char getIsMedicationStop() {
		return isMedicationStop;
	}

	public void setIsMedicationStop(char isMedicationStop) {
		this.isMedicationStop = isMedicationStop;
	}

	

	public String getStrengthDesc() {
		return strengthDesc;
	}

	public void setStrengthDesc(String strengthDesc) {
		this.strengthDesc = strengthDesc;
	}

	public Boolean getIsStat() {
		return isStat;
	}

	public void setIsStat(Boolean isStat) {
		this.isStat = isStat;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public String getRoaDesc() {
		return roaDesc;
	}

	public void setRoaDesc(String roaDesc) {
		this.roaDesc = roaDesc;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getSearchItem() {
		return searchItem;
	}

	public void setSearchItem(String searchItem) {
		this.searchItem = searchItem;
	}

	public String getSearchItemFre() {
		return searchItemFre;
	}

	public void setSearchItemFre(String searchItemFre) {
		this.searchItemFre = searchItemFre;
	}

	public String getRoa_details() {
		return roa_details;
	}

	public void setRoa_details(String roa_details) {
		this.roa_details = roa_details;
	}

	public String getMedicationStartDate() {
		return medicationStartDate;
	}

	public void setMedicationStartDate(String medicationStartDate) {
		this.medicationStartDate = medicationStartDate;
	}

	public String getMedicationEndDate() {
		return medicationEndDate;
	}

	public void setMedicationEndDate(String medicationEndDate) {
		this.medicationEndDate = medicationEndDate;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getNoOfAdministrationTime() {
		return noOfAdministrationTime;
	}

	public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
		this.noOfAdministrationTime = noOfAdministrationTime;
	}

	public String getPrescriptionNo() {
		return prescriptionNo;
	}

	public void setPrescriptionNo(String prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}

	public Character getIsMedicationStopHis() {
		return isMedicationStopHis;
	}

	public void setIsMedicationStopHis(Character isMedicationStopHis) {
		this.isMedicationStopHis = isMedicationStopHis;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoseId() {
		return doseId;
	}

	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Double getDoseDesc() {
		return doseDesc;
	}

	public void setDoseDesc(Double doseDesc) {
		this.doseDesc = doseDesc;
	}

	public Integer getPrescriptionDetailsid() {
		return prescriptionDetailsid;
	}

	public void setPrescriptionDetailsid(Integer prescriptionDetailsid) {
		this.prescriptionDetailsid = prescriptionDetailsid;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}

	public Timestamp getAuthorisedDate() {
		return authorisedDate;
	}

	public void setAuthorisedDate(Timestamp authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	public Timestamp getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Timestamp prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public BigInteger getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(BigInteger prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public Integer getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}
	
}
