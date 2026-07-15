package com.param.opd.coversheet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
@Entity
@Table(name = "t_prescription_detail", schema = "pharmacy")
public class PrescriptionDetailsMaster implements Serializable{
	private static long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer prescriptionDetailsId;
	
	@Column(name="prescription_id")
	private Integer prescriptionId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="item_id")
	private Integer itemId;
	
	@Column(name="store_id")
	private Integer storeId;
	
	@Column(name="uom_unit_id")
	private Integer uomUnitId;

	@Column(name="order_qty")
	private Integer orderQty;
	
	@Column(name="pending_qty")
	private Integer pendingQty;
	
	@Column(name="refill_qty")
	private Integer refillQty;
	
	@Column(name="strength")
	private Double strength;
	
	@Column(name="is_discontinued")
	private Boolean isDiscontinued;
	
	@Column(name="discontinue_by")
	private Integer discontinueBy;
	
	@Column(name="discontinue_on")
	private Date discontinueOn;
	
	@Column(name="prescription_date")
	private Date prescriptionDate;
	
	@Column(name="authorised_by")
	private Integer authorisedBy;
	
	@Column(name="authorised_date")
	private Date authorisedDate;
	
	@Column(name="is_pre_stop")
	private Boolean isPreStop;
	
	@Column(name="is_intervention")
	private Boolean isIntervention;
	
	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Date updatedDate; 

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Date addedDate; 
	
	@Column(name="is_deleted")
	private Boolean isDeleted; 
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="status_id")
	private Integer statusId;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="prnflag")
	private Boolean prnflag;
	
	@Column(name="statflag")
	private Boolean statflag;
	
	@Column(name="infusionflag")
	private Boolean infusionflag;
	
	@Column(name="pre_stop_start_date")
	private Date preStopStartDate;
	
	@Column(name="pre_stop_end_date")
	private Date preStopEndDate;
	
	@Column(name="consumable_method_id")
	private Integer consumableMethodId;
	
	@Column(name="prn_start_date")
	private Date prnStartDate;
	
	@Column(name="prn_instruction")
	private String prnInstruction;
	
	@Column(name="uom_type_id")
	private Integer uomTypeId;
	
	@Column(name="instruction_id")
	private Integer instructionId;	
	
	@Column(name="is_discharge_medication")
	private Boolean isDischarge;
	
	@Column(name="generic_name")
	private String genericName;
	
	@Column(name="item_name")
	private String itemName;
	
	@Column(name="is_non_formulary")
	private Character isNonFormulary;
	
	@Column(name="justification_stop")
	private String justificationStop;
	
	@Column(name="is_medication_stop")
	private char isMedicationStop;
	
	@Column(name="dis_presribed_qty")
	private Integer disPrescribedQty;
	
	@Column(name="parent_substitute_id")
	private Integer parentSubstituteId;
	
	
	@Column(name="infusion_solution_id")
	private Integer infusionSolutionId;
	
//	@Column(name="prescribe_dose")
//    private Integer prescribedDose;
	
	@Column(name="prescribe_dose")
    private Double prescribedDose;
	
	@Column(name="prescribe_dose_unit_id")
	private Integer prescribedDoseUnitId;
	
	@Column(name="factor_id")
    private Integer factorId;
	
	@Column(name="formulation_dose_mapp_id")
	private Integer formulationDosageMappId;
	
	@Column(name="instruction_desc")
	private String instructionDesc;
	
	@Column(name="order_set_id")
	private Integer orderSetId;
	
	@Column(name="duration")
	private Integer duration;
	
	@Column(name="duration_unit")
	private String durationUnit;
	
	@Column(name="priority_id")
	private Integer priorityId;
	
	@Column(name="is_solution")
	private boolean isSolution;
	
	@Column(name="is_additive")
	private boolean isAdditive;
	
	@Column(name="infusion_total_volume")
	private Integer infusionTotalVolume;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	public Integer getOrderSetId() {
		return orderSetId;
	}

	public void setOrderSetId(Integer orderSetId) {
		this.orderSetId = orderSetId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Integer getPrescriptionDetailsId() {
		return prescriptionDetailsId;
	}

	public void setPrescriptionDetailsId(Integer prescriptionDetailsId) {
		this.prescriptionDetailsId = prescriptionDetailsId;
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

	public Integer getDiscontinueBy() {
		return discontinueBy;
	}

	public void setDiscontinueBy(Integer discontinueBy) {
		this.discontinueBy = discontinueBy;
	}

	public Date getDiscontinueOn() {
		return discontinueOn;
	}

	public void setDiscontinueOn(Date discontinueOn) {
		this.discontinueOn = discontinueOn;
	}

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Integer getAuthorisedBy() {
		return authorisedBy;
	}

	public void setAuthorisedBy(Integer authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public Date getAuthorisedDate() {
		return authorisedDate;
	}

	public void setAuthorisedDate(Date authorisedDate) {
		this.authorisedDate = authorisedDate;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
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

	public Date getPreStopStartDate() {
		return preStopStartDate;
	}

	public void setPreStopStartDate(Date preStopStartDate) {
		this.preStopStartDate = preStopStartDate;
	}

	public Date getPreStopEndDate() {
		return preStopEndDate;
	}

	public void setPreStopEndDate(Date preStopEndDate) {
		this.preStopEndDate = preStopEndDate;
	}

	public Integer getConsumableMethodId() {
		return consumableMethodId;
	}

	public void setConsumableMethodId(Integer consumableMethodId) {
		this.consumableMethodId = consumableMethodId;
	}

	public Date getPrnStartDate() {
		return prnStartDate;
	}

	public void setPrnStartDate(Date prnStartDate) {
		this.prnStartDate = prnStartDate;
	}

	public String getPrnInstruction() {
		return prnInstruction;
	}

	public void setPrnInstruction(String prnInstruction) {
		this.prnInstruction = prnInstruction;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge = isDischarge;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public Integer getDisPrescribedQty() {
		return disPrescribedQty;
	}

	public void setDisPrescribedQty(Integer disPrescribedQty) {
		this.disPrescribedQty = disPrescribedQty;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}

	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
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

	public boolean getIsSolution() {
		return isSolution;
	}

	public void setIsSolution(boolean isSolution) {
		this.isSolution = isSolution;
	}

	public boolean getIsAdditive() {
		return isAdditive;
	}

	public void setIsAdditive(boolean isAdditive) {
		this.isAdditive = isAdditive;
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
	
	
}