package com.param.opd.coversheet.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*
 * 
 * Created by Tushar
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class MedicationDTO {

	private BigInteger prescriptionId;

	private Integer encounterId;

	private Integer unitId;

	private Integer organizationId;

	private Integer itemId;

	private Integer doseId;

	private Integer roaId;

	private Integer frequencyId;

	private Integer duration;

	private String durationUnit;

	private String startDate;
	private Date startDateNew;

	private String endDate;
	private Date endDateNew;

	private Integer instructionId;
	private Integer addedBy;

	private Integer patientId;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private String itemName;

	private String frequencyDesc;
	

	private String roaDesc;
	
	private String roa;

	private String instructionDesc;

	private String genericName;

	private Double doseDesc;

	private String strengthDesc;

	private Integer doctorId;

	private Integer specialityId;

	private Integer visitTypeId;

	private Integer priorityId;

	private Boolean statflag;

	private Integer orderQty;

	private String encounterDate;

	private Integer quantity;

	private Integer formulationTypeId;
	private String formulationType;

	private Integer genericId;

	private Integer doseUnitId;

	private Boolean isStat;

	private String searchItem;

	private String searchItemFre;

	private String roa_details;

	private Double dose;
	private BigDecimal rate;

	private String medicationStartDate;

	private String medicationEndDate;
	private String name;

	private String doctorName;
	private String stopDate;

	private Integer noOfAdministrationTime;
	private String prescriptionNo;

	private Character isMedicationStopHis;
	private Character isNonFormulary;

	private Integer statusId;
	private Integer statusIdIssue;

	private Integer prescribeDoseUnitId;
	private Integer quantityForDoseCal;
	private Integer strengthForDoseCal;
	private boolean instructionTextFlag;

	private Integer doctorFavoriteId;

	private String isInstructionText;

	private String prescribeDoseUnit;

	// private Integer prescribeDose;
	private Double prescribeDose;
	private String dosageForm;

	private Integer factorId;
	private Integer statusIdPres;

	private Integer formulationDosageMappId;
	private Double strength;
	private Boolean isDischarge;
	private Boolean isDoseCalculationApplicable;
	private String doseCalculationApplicableData;

	private Boolean isOtims;
	private Integer parentSubstituteId;
	private Integer durationCount;

	private String instructionText;

	private String userName;
	private Boolean isNarcotic;
	private Boolean isSubstituteAvail;

	private String addedDate;
	private Date addedDateNew;
	private String addedTime;
	private Integer medicationType;
	private String prescriptionDate;
	private String cimsCode;
	private String cimsType;
	private Character isGeneric;
	private String unit;
	private Double totalRate;
	private String searchGeneric;
	private String priorityCode;
	private String prescriptionDoseUnit;
	private List<MedicationDTO> prescribedDosageUnitList;
	private Character nonFormularyAdvice;
	private String medicationStartDate1;
	private String medicationName;
	private String medicationEndDateFormatted;
	private String medicationEndDate1;
	private String itemStrength;
	private Character isInstruction;
	private Integer formulationCounterIndex;
	private String formulationDosageFromUnit;
	private Integer formulationDosageFromUnitId;
	private Integer formulationDosageToConversionValue;
	private String formulationDosageToUnit;
	private Integer formulationDosageToUnitId;
	private Integer formulationFactorId;
	private Integer counterIndex;
	private String formulationDosage;
	private Integer formulationDosageFromConversionValue;
	private Boolean favorite_status;
	private String doseForDoseCal;
	private Integer conversions;
	private Integer uomTypeId;
	private Integer routeId;
	private Integer prescribedDoseUnitId;
	private boolean isCovtemplate;
	private Integer deptId;
	private Integer covTemplateId;

	private String createdDate1;
	private Integer roa_id;

	private Boolean isDischargeMedication;

	private BigInteger prescriptionDetailsid;
	private Integer prescriptionFrequencyId;
	private Integer admissionId;
	private String justificationStop;
	private char isMedicationStop;
	private Integer issueId;
	
	private String selectedUom;
	private Integer patientWorkbenchId;
	private Integer resultId;
	private Date workbenchAddedDate;
	private String workbenchRemark;
	private Date workbenchEndDate;
	
	private List<MedicationDTO> medicationDTOsList;

	private String frequencyCode;

	public void setCovtemplate(boolean isCovtemplate) {
		this.isCovtemplate = isCovtemplate;
	}

	public String getCreatedDate1() {
		return createdDate1;
	}

	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}

	public String getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(String prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Boolean getIsDischargeMedication() {
		return isDischargeMedication;
	}

	public void setIsDischargeMedication(Boolean isDischargeMedication) {
		this.isDischargeMedication = isDischargeMedication;
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

	public BigInteger getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(BigInteger prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getStrengthDesc() {
		return strengthDesc;
	}

	public void setStrengthDesc(String strengthDesc) {
		this.strengthDesc = strengthDesc;
	}

	public List<MedicationDTO> getMedicationDTOsList() {
		return medicationDTOsList;
	}

	public void setMedicationDTOsList(List<MedicationDTO> medicationDTOsList) {
		this.medicationDTOsList = medicationDTOsList;
	}

	public Double getDoseDesc() {
		return doseDesc;
	}

	public void setDoseDesc(Double doseDesc) {
		this.doseDesc = doseDesc;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getDoseId() {
		return doseId;
	}

	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public Integer getDoseUnitId() {
		return doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}

	public Boolean getIsStat() {
		return isStat;
	}

	public void setIsStat(Boolean isStat) {
		this.isStat = isStat;
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

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
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

	public Integer getRoa_id() {
		return roa_id;
	}

	public void setRoa_id(Integer roa_id) {
		this.roa_id = roa_id;
	}

	public BigInteger getPrescriptionDetailsid() {
		return prescriptionDetailsid;
	}

	public void setPrescriptionDetailsid(BigInteger prescriptionDetailsid) {
		this.prescriptionDetailsid = prescriptionDetailsid;
	}

	public Integer getPrescriptionFrequencyId() {
		return prescriptionFrequencyId;
	}

	public void setPrescriptionFrequencyId(Integer prescriptionFrequencyId) {
		this.prescriptionFrequencyId = prescriptionFrequencyId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
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

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(Character isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getPrescribeDoseUnitId() {
		return prescribeDoseUnitId;
	}

	public void setPrescribeDoseUnitId(Integer prescribeDoseUnitId) {
		this.prescribeDoseUnitId = prescribeDoseUnitId;
	}

	public String getPrescribeDoseUnit() {
		return prescribeDoseUnit;
	}

	public void setPrescribeDoseUnit(String prescribeDoseUnit) {
		this.prescribeDoseUnit = prescribeDoseUnit;
	}

	public Double getPrescribeDose() {
		return prescribeDose;
	}

	public void setPrescribeDose(Double prescribeDose) {
		this.prescribeDose = prescribeDose;
	}

	public String getDosageForm() {
		return dosageForm;
	}

	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
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

	public Integer getStatusIdIssue() {
		return statusIdIssue;
	}

	public void setStatusIdIssue(Integer statusIdIssue) {
		this.statusIdIssue = statusIdIssue;
	}

	public Integer getStatusIdPres() {
		return statusIdPres;
	}

	public void setStatusIdPres(Integer statusIdPres) {
		this.statusIdPres = statusIdPres;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge = isDischarge;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}

	public Boolean getIsSubstituteAvail() {
		return isSubstituteAvail;
	}

	public void setIsSubstituteAvail(Boolean isSubstituteAvail) {
		this.isSubstituteAvail = isSubstituteAvail;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public String getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(String addedTime) {
		this.addedTime = addedTime;
	}

	public Integer getDurationCount() {
		return durationCount;
	}

	public void setDurationCount(Integer durationCount) {
		this.durationCount = durationCount;
	}

	public Integer getMedicationType() {
		return medicationType;
	}

	public void setMedicationType(Integer medicationType) {
		this.medicationType = medicationType;
	}

	public Boolean getIsDoseCalculationApplicable() {
		return isDoseCalculationApplicable;
	}

	public void setIsDoseCalculationApplicable(Boolean isDoseCalculationApplicable) {
		this.isDoseCalculationApplicable = isDoseCalculationApplicable;
	}

	public String getDoseCalculationApplicableData() {
		return doseCalculationApplicableData;
	}

	public void setDoseCalculationApplicableData(String doseCalculationApplicableData) {
		this.doseCalculationApplicableData = doseCalculationApplicableData;
	}

	public String getFormulationType() {
		return formulationType;
	}

	public void setFormulationType(String formulationType) {
		this.formulationType = formulationType;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Date getStartDateNew() {
		return startDateNew;
	}

	public void setStartDateNew(Date startDateNew) {
		this.startDateNew = startDateNew;
	}

	public Date getEndDateNew() {
		return endDateNew;
	}

	public void setEndDateNew(Date endDateNew) {
		this.endDateNew = endDateNew;
	}

	public Date getAddedDateNew() {
		return addedDateNew;
	}

	public void setAddedDateNew(Date addedDateNew) {
		this.addedDateNew = addedDateNew;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public String getCimsCode() {
		return cimsCode;
	}

	public void setCimsCode(String cimsCode) {
		this.cimsCode = cimsCode;
	}

	public String getCimsType() {
		return cimsType;
	}

	public void setCimsType(String cimsType) {
		this.cimsType = cimsType;
	}

	public Character getIsGeneric() {
		return isGeneric;
	}

	public void setIsGeneric(Character isGeneric) {
		this.isGeneric = isGeneric;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(Double totalRate) {
		this.totalRate = totalRate;
	}

	public String getSearchGeneric() {
		return searchGeneric;
	}

	public void setSearchGeneric(String searchGeneric) {
		this.searchGeneric = searchGeneric;
	}

	public String getPriorityCode() {
		return priorityCode;
	}

	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}

	public String getPrescriptionDoseUnit() {
		return prescriptionDoseUnit;
	}

	public void setPrescriptionDoseUnit(String prescriptionDoseUnit) {
		this.prescriptionDoseUnit = prescriptionDoseUnit;
	}

	public List<MedicationDTO> getPrescribedDosageUnitList() {
		return prescribedDosageUnitList;
	}

	public void setPrescribedDosageUnitList(List<MedicationDTO> prescribedDosageUnitList) {
		this.prescribedDosageUnitList = prescribedDosageUnitList;
	}

	public Character getNonFormularyAdvice() {
		return nonFormularyAdvice;
	}

	public void setNonFormularyAdvice(Character nonFormularyAdvice) {
		this.nonFormularyAdvice = nonFormularyAdvice;
	}

	public String getMedicationStartDate1() {
		return medicationStartDate1;
	}

	public void setMedicationStartDate1(String medicationStartDate1) {
		this.medicationStartDate1 = medicationStartDate1;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public String getMedicationEndDateFormatted() {
		return medicationEndDateFormatted;
	}

	public void setMedicationEndDateFormatted(String medicationEndDateFormatted) {
		this.medicationEndDateFormatted = medicationEndDateFormatted;
	}

	public String getMedicationEndDate1() {
		return medicationEndDate1;
	}

	public void setMedicationEndDate1(String medicationEndDate1) {
		this.medicationEndDate1 = medicationEndDate1;
	}

	public String getItemStrength() {
		return itemStrength;
	}

	public void setItemStrength(String itemStrength) {
		this.itemStrength = itemStrength;
	}

	public Character getIsInstruction() {
		return isInstruction;
	}

	public void setIsInstruction(Character isInstruction) {
		this.isInstruction = isInstruction;
	}

	public Integer getFormulationCounterIndex() {
		return formulationCounterIndex;
	}

	public void setFormulationCounterIndex(Integer formulationCounterIndex) {
		this.formulationCounterIndex = formulationCounterIndex;
	}

	public String getFormulationDosageFromUnit() {
		return formulationDosageFromUnit;
	}

	public void setFormulationDosageFromUnit(String formulationDosageFromUnit) {
		this.formulationDosageFromUnit = formulationDosageFromUnit;
	}

	public Integer getFormulationDosageFromUnitId() {
		return formulationDosageFromUnitId;
	}

	public void setFormulationDosageFromUnitId(Integer formulationDosageFromUnitId) {
		this.formulationDosageFromUnitId = formulationDosageFromUnitId;
	}

	public Integer getFormulationDosageToConversionValue() {
		return formulationDosageToConversionValue;
	}

	public void setFormulationDosageToConversionValue(Integer formulationDosageToConversionValue) {
		this.formulationDosageToConversionValue = formulationDosageToConversionValue;
	}

	public String getFormulationDosageToUnit() {
		return formulationDosageToUnit;
	}

	public void setFormulationDosageToUnit(String formulationDosageToUnit) {
		this.formulationDosageToUnit = formulationDosageToUnit;
	}

	public Integer getFormulationDosageToUnitId() {
		return formulationDosageToUnitId;
	}

	public void setFormulationDosageToUnitId(Integer formulationDosageToUnitId) {
		this.formulationDosageToUnitId = formulationDosageToUnitId;
	}

	public Integer getFormulationFactorId() {
		return formulationFactorId;
	}

	public void setFormulationFactorId(Integer formulationFactorId) {
		this.formulationFactorId = formulationFactorId;
	}

	public Integer getCounterIndex() {
		return counterIndex;
	}

	public void setCounterIndex(Integer counterIndex) {
		this.counterIndex = counterIndex;
	}

	public String getFormulationDosage() {
		return formulationDosage;
	}

	public void setFormulationDosage(String formulationDosage) {
		this.formulationDosage = formulationDosage;
	}

	public Integer getFormulationDosageFromConversionValue() {
		return formulationDosageFromConversionValue;
	}

	public void setFormulationDosageFromConversionValue(Integer formulationDosageFromConversionValue) {
		this.formulationDosageFromConversionValue = formulationDosageFromConversionValue;
	}

	public Boolean getFavorite_status() {
		return favorite_status;
	}

	public void setFavorite_status(Boolean favorite_status) {
		this.favorite_status = favorite_status;
	}

	public String getDoseForDoseCal() {
		return doseForDoseCal;
	}

	public void setDoseForDoseCal(String doseForDoseCal) {
		this.doseForDoseCal = doseForDoseCal;
	}

	public Integer getConversions() {
		return conversions;
	}

	public void setConversions(Integer conversions) {
		this.conversions = conversions;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Double getStrength() {
		return strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getPrescribedDoseUnitId() {
		return prescribedDoseUnitId;
	}

	public void setPrescribedDoseUnitId(Integer prescribedDoseUnitId) {
		this.prescribedDoseUnitId = prescribedDoseUnitId;
	}

	public Integer getQuantityForDoseCal() {
		return quantityForDoseCal;
	}

	public void setQuantityForDoseCal(Integer quantityForDoseCal) {
		this.quantityForDoseCal = quantityForDoseCal;
	}

	public Integer getStrengthForDoseCal() {
		return strengthForDoseCal;
	}

	public void setStrengthForDoseCal(Integer strengthForDoseCal) {
		this.strengthForDoseCal = strengthForDoseCal;
	}

	public boolean isInstructionTextFlag() {
		return instructionTextFlag;
	}

	public void setInstructionTextFlag(boolean instructionTextFlag) {
		this.instructionTextFlag = instructionTextFlag;
	}

	public Integer getDoctorFavoriteId() {
		return doctorFavoriteId;
	}

	public void setDoctorFavoriteId(Integer doctorFavoriteId) {
		this.doctorFavoriteId = doctorFavoriteId;
	}

	public String getIsInstructionText() {
		return isInstructionText;
	}

	public void setIsInstructionText(String isInstructionText) {
		this.isInstructionText = isInstructionText;
	}

	public boolean getIsCovtemplate() {
		return isCovtemplate;
	}

	public void setIsCovtemplate(boolean isCovtemplate) {
		this.isCovtemplate = isCovtemplate;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public String getSelectedUom() {
		return selectedUom;
	}

	public void setSelectedUom(String selectedUom) {
		this.selectedUom = selectedUom;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Boolean getStatflag() {
		return statflag;
	}

	public void setStatflag(Boolean statflag) {
		this.statflag = statflag;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}


	public String getFrequencyCode() {
		return frequencyCode;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getRoa() {
		return roa;
	}

	public void setRoa(String roa) {
		this.roa = roa;
	}

	public Integer getPatientWorkbenchId() {
		return patientWorkbenchId;
	}

	public void setPatientWorkbenchId(Integer patientWorkbenchId) {
		this.patientWorkbenchId = patientWorkbenchId;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public Date getWorkbenchAddedDate() {
		return workbenchAddedDate;
	}

	public void setWorkbenchAddedDate(Date workbenchAddedDate) {
		this.workbenchAddedDate = workbenchAddedDate;
	}

	public String getWorkbenchRemark() {
		return workbenchRemark;
	}

	public void setWorkbenchRemark(String workbenchRemark) {
		this.workbenchRemark = workbenchRemark;
	}

	public Date getWorkbenchEndDate() {
		return workbenchEndDate;
	}

	public void setWorkbenchEndDate(Date workbenchEndDate) {
		this.workbenchEndDate = workbenchEndDate;
	}
}
