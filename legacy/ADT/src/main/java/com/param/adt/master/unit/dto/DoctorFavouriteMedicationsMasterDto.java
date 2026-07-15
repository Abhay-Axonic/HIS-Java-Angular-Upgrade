package com.param.adt.master.unit.dto;

import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class DoctorFavouriteMedicationsMasterDto {

    private Integer favouriteMedicationId;

    private Integer doctorId;

    private Integer organizationId;

    private Integer unitId;

    private Integer itemId;

    private Integer roaId;

    private Double dose;

    private Integer doseUnitId;

    private Integer frequencyId;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private String updatedDate;

    private Integer updatedBy;

    private String itemName;

    private String roaDesc;

    private String doseDesc;

    private String frequencyDesc;

    private String genericName;

    private Integer formulationTypeId;

    private Integer genericId;

    private Integer noOfAdministrationTime;

    private Integer serviceMasterId;

    private Integer specialityId;

    private Integer subSpecialityId;

    private String serviceStandardName;

    private String specialityName;

    private String subSpecialityName;

    private Boolean statflag;
    private String instructions;
    private Integer instructionId;
    private Boolean isDischarge;
    private Double prescribedDose;
    private String instructionDesc;
    private String instructionText;
    private Integer prescribedDoseUnitId;
    private Integer factorId;
    private Integer formulationDosageMappId;
    private Boolean isStat;
    private Integer prescribeDoseUnitId;
    private String dosageForm;
    private Integer orderQty;
    private Integer duration;
    private String prescribeDoseUnit;
    private BigDecimal rate;
    private Integer quantity;
    private Integer durationCount;
    private char isMedicationStop;
    private char isNonFormulary;
    private Double strength;
    private String durationUnit;
    private String prescriptionDate;
    private String startDate;
    private String stopDate;
    private Integer storeId;
    private BigInteger count;
    private Boolean isDoseCalculationApplicable;

    private String doseCalculationApplicableData;
    private Integer doctorFavoriteId;

    private Double prescribeDose;

    private List<Integer> doctorIdList;

    private List<Integer> specialityIdList;

    public Double getPrescribeDose() {
        return prescribeDose;
    }

    public void setPrescribeDose(Double prescribeDose) {
        this.prescribeDose = prescribeDose;
    }

    public Integer getFavouriteMedicationId() {
        return favouriteMedicationId;
    }

    public void setFavouriteMedicationId(Integer favouriteMedicationId) {
        this.favouriteMedicationId = favouriteMedicationId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
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

    public Integer getRoaId() {
        return roaId;
    }

    public void setRoaId(Integer roaId) {
        this.roaId = roaId;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRoaDesc() {
        return roaDesc;
    }

    public void setRoaDesc(String roaDesc) {
        this.roaDesc = roaDesc;
    }

    public String getDoseDesc() {
        return doseDesc;
    }

    public void setDoseDesc(String doseDesc) {
        this.doseDesc = doseDesc;
    }

    public String getFrequencyDesc() {
        return frequencyDesc;
    }

    public void setFrequencyDesc(String frequencyDesc) {
        this.frequencyDesc = frequencyDesc;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
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

    public Integer getNoOfAdministrationTime() {
        return noOfAdministrationTime;
    }

    public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
        this.noOfAdministrationTime = noOfAdministrationTime;
    }

    public Integer getServiceMasterId() {
        return serviceMasterId;
    }

    public void setServiceMasterId(Integer serviceMasterId) {
        this.serviceMasterId = serviceMasterId;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public Integer getSubSpecialityId() {
        return subSpecialityId;
    }

    public void setSubSpecialityId(Integer subSpecialityId) {
        this.subSpecialityId = subSpecialityId;
    }

    public String getServiceStandardName() {
        return serviceStandardName;
    }

    public void setServiceStandardName(String serviceStandardName) {
        this.serviceStandardName = serviceStandardName;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getSubSpecialityName() {
        return subSpecialityName;
    }

    public void setSubSpecialityName(String subSpecialityName) {
        this.subSpecialityName = subSpecialityName;
    }

    public Boolean getStatflag() {
        return statflag;
    }

    public void setStatflag(Boolean statflag) {
        this.statflag = statflag;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Integer instructionId) {
        this.instructionId = instructionId;
    }

    public Boolean getDischarge() {
        return isDischarge;
    }

    public void setDischarge(Boolean discharge) {
        isDischarge = discharge;
    }

    public Double getPrescribedDose() {
        return prescribedDose;
    }

    public void setPrescribedDose(Double prescribedDose) {
        this.prescribedDose = prescribedDose;
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

    public Boolean getStat() {
        return isStat;
    }

    public void setStat(Boolean stat) {
        isStat = stat;
    }

    public Integer getPrescribeDoseUnitId() {
        return prescribeDoseUnitId;
    }

    public void setPrescribeDoseUnitId(Integer prescribeDoseUnitId) {
        this.prescribeDoseUnitId = prescribeDoseUnitId;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPrescribeDoseUnit() {
        return prescribeDoseUnit;
    }

    public void setPrescribeDoseUnit(String prescribeDoseUnit) {
        this.prescribeDoseUnit = prescribeDoseUnit;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDurationCount() {
        return durationCount;
    }

    public void setDurationCount(Integer durationCount) {
        this.durationCount = durationCount;
    }

    public char getIsMedicationStop() {
        return isMedicationStop;
    }

    public void setIsMedicationStop(char isMedicationStop) {
        this.isMedicationStop = isMedicationStop;
    }

    public char getIsNonFormulary() {
        return isNonFormulary;
    }

    public void setIsNonFormulary(char isNonFormulary) {
        this.isNonFormulary = isNonFormulary;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
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

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public Boolean getDoseCalculationApplicable() {
        return isDoseCalculationApplicable;
    }

    public void setDoseCalculationApplicable(Boolean doseCalculationApplicable) {
        isDoseCalculationApplicable = doseCalculationApplicable;
    }

    public Integer getDoctorFavoriteId() {
        return doctorFavoriteId;
    }

    public void setDoctorFavoriteId(Integer doctorFavoriteId) {
        this.doctorFavoriteId = doctorFavoriteId;
    }

    public String getDoseCalculationApplicableData() {
        return doseCalculationApplicableData;
    }

    public void setDoseCalculationApplicableData(String doseCalculationApplicableData) {
        this.doseCalculationApplicableData = doseCalculationApplicableData;
    }

    public List<Integer> getDoctorIdList() {
        return doctorIdList;
    }

    public void setDoctorIdList(List<Integer> doctorIdList) {
        this.doctorIdList = doctorIdList;
    }

    public List<Integer> getSpecialityIdList() {
        return specialityIdList;
    }

    public void setSpecialityIdList(List<Integer> specialityIdList) {
        this.specialityIdList = specialityIdList;
    }
}
