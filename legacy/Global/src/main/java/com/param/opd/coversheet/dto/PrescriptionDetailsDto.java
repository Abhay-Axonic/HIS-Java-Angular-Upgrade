package com.param.opd.coversheet.dto;

import java.math.BigInteger;

public class PrescriptionDetailsDto {
	
	private Integer prescriptionDetailsid;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer prescriptionId;
	
	private Integer itemId;
	
	private Integer storeId;
	
	private Integer uomUnitId;
	
	private Integer orderQty;
	
	private Integer pendingQty;
	
	private Integer refillQty;
	
	private Double strength;
	
	private Boolean  isDiscontinued;
	
	private String prescriptionDate;

	private Boolean isPreStop;
	
	private Boolean isIntervention;
	
	private Integer addedBy;
	
	private String addedDate;
	
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
	
	private String startDate;
	
	private String stopDate;
	
	private String instructions;
	
	private Integer consumableMethodId;

	private Integer authorisedBy;

	private String authorisedDate;
	
	private Integer instructionId;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String itemName;
	
	private String genericName;
	
	private Boolean isDischarge;
	
	private String justificationStop;
//	private Integer prescriptionFreqId;
	
	private char isMedicationStop;
	
	private Integer prescriptionFrequencyId;
	
	private Character isNonFormulary;
	
	private Integer infusionSolutionId;
	
//	private Integer prescribedDose;
	private Double prescribedDose;
	
	private Integer prescribedDoseUnitId;
	
    private Integer factorId;
		
	private Integer formulationDosageMappId;
	
	private String instructionDesc;
	
	private String instructionText;
	
	private Integer duration;
	private Integer orderSetId;
	private String durationUnit;
	private Integer durationCount;
	private Integer priorityId;
	private String endDate;
	private BigInteger prescriptionDetailsidNew;

	private Boolean isNarcotic;
	private boolean isAdditive;
	private boolean isSolution;
	private Integer infusionTotalVolume;
	private Integer covTemplateId;
	private Integer roaId;

	private boolean isManuallyAddedItem;
	private Integer oldPrescriptionId;
	private Integer issueDetailsId;
	public Integer getOrderSetId() {
		return orderSetId;
	}

	public void setOrderSetId(Integer orderSetId) {
		this.orderSetId = orderSetId;
	}

	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

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

	public String getAuthorisedDate() {
		return authorisedDate;
	}

	public void setAuthorisedDate(String authorisedDate) {
		this.authorisedDate = authorisedDate;
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

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public Integer getPrescriptionDetailsid() {
		return prescriptionDetailsid;
	}

	public void setPrescriptionDetailsid(Integer prescriptionDetailsid) {
		this.prescriptionDetailsid = prescriptionDetailsid;
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

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
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

	public String getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(String prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
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

	/*public Integer getPrescribedDose() {
		return prescribedDose;
	}

	public void setPrescribedDose(Integer prescribedDose) {
		this.prescribedDose = prescribedDose;
	}*/

	public Integer getPrescribedDoseUnitId() {
		return prescribedDoseUnitId;
	}

	public void setPrescribedDoseUnitId(Integer prescribedDoseUnitId) {
		this.prescribedDoseUnitId = prescribedDoseUnitId;
	}

	public Integer getFactorId() {
		return factorId;
	}

	public void setFactorId(Integer factorId) {
		this.factorId = factorId;
	}


	public Integer getFormulationDosageMappId() {
		return formulationDosageMappId;
	}

	public void setFormulationDosageMappId(Integer formulationDosageMappId) {
		this.formulationDosageMappId = formulationDosageMappId;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public boolean getIsManuallyAddedItem() {
		return isManuallyAddedItem;
	}

	public void setIsManuallyAddedItem(boolean isManuallyAddedItem) {
		this.isManuallyAddedItem = isManuallyAddedItem;
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

	public Integer getDurationCount() {
		return durationCount;
	}

	public void setDurationCount(Integer durationCount) {
		this.durationCount = durationCount;
	}  
	 
	public Double getPrescribedDose() {
		return prescribedDose;
	}

	public void setPrescribedDose(Double prescribedDose) {
		this.prescribedDose = prescribedDose;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public BigInteger getPrescriptionDetailsidNew() {
		return prescriptionDetailsidNew;
	}

	public void setPrescriptionDetailsidNew(BigInteger prescriptionDetailsidNew) {
		this.prescriptionDetailsidNew = prescriptionDetailsidNew;
	}

	public boolean getIsAdditive() {
		return isAdditive;
	}

	public void setIsAdditive(boolean isAdditive) {
		this.isAdditive = isAdditive;
	}

	public boolean getIsSolution() {
		return isSolution;
	}

	public void setIsSolution(boolean isSolution) {
		this.isSolution = isSolution;
	}

	public Integer getInfusionTotalVolume() {
		return infusionTotalVolume;
	}

	public void setInfusionTotalVolume(Integer infusionTotalVolume) {
		this.infusionTotalVolume = infusionTotalVolume;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	public Integer getOldPrescriptionId() {
		return oldPrescriptionId;
	}

	public void setOldPrescriptionId(Integer oldPrescriptionId) {
		this.oldPrescriptionId = oldPrescriptionId;
	}

	public Integer getIssueDetailsId() {
		return issueDetailsId;
	}

	public void setIssueDetailsId(Integer issueDetailsId) {
		this.issueDetailsId = issueDetailsId;
	}
	
	
}
