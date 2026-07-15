package com.param.opd.coversheet.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
/*"itemId": 95,
"storeId": 1,
"orderQty": 2,
"doctorId": 1,
"authorisedDate": "2018-12-04 17:12:15",
"doseId": "3",
"doseUnitId": 1,
"frequencyId": 29,
"durationUnit": "Day",
"roaId": 4,
"startDate": "2018-12-06 00:00:00",
"endDate": "2018-12-06 00:00:00",
"status": "A",
"instructionId": "3"*/

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.global.dto.items.BatchResponse;

public class MedicationHistoryDto {
	
	private Integer medicationHistoryId;
	
	private Integer encounterId;
	
	private Integer unitId;
	
	private Integer dose;

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
	
	private Integer patientId;
	
	private Character status;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private String itemName;
	
	private String frequencyDesc;
	
	private String roaDesc;
	private String expiryDate;

	private String instructionDesc;
	private Integer prescriptionDetailId;
	private String genericName;
	
	private Double doseDesc;
	
	private String strengthDesc;
	
	private Integer doctorId;
	
	private Integer orderQty;
	
	private Integer genericId;

	private String name;
	
	private Integer drugId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private Integer routeId;
	
	private String stopDate;
	
	private Integer strength;

	private String description;
	
	private Integer doseUnitId;
	
	private String authorisedDate;
	
	private Integer storeId;

	private Integer priorityId;

	private String doctorName;

	private String prescriptionDate;

	private List<MedicationHistoryDto> medicationHistoryList;
	
	private Integer admissionId;
	
	private Integer addedBy;
	
	private String addedDate;

	private Integer authorisedBy;

	private Boolean statflag;
	
	private Boolean isVaccine;

	private Boolean isDischarge;

	private String patientName;

	private String doseUnit;

	private String specialityName;

	private Integer specialityId;
	
	private String encounterDateString;

	private Integer visitTypeId;
	
	private Integer uomUnitId;
	
	private BigInteger prescriptionId;
	
	private Character isNonFormulary;
	
	private String doa;

	private String pdd;

	private String bedNumber;

	private String uhidNumber;

	private String dob;
	
	private String genderName;
	private Character isMedicationContinue;
	private Character isMedicationStopHis;
	private String justificationStopHis;
	private Character isNoSignificantMedication;
	private BigInteger prescriptionDetailsid;
	private String wardName;
	private String genderCode;
	private String fromDate;
	private String toDate;
	private String searchICPC;
	private String orderDate;
	private String orderTime;
	private String ageString;
	private String genderDesc;
	private String agestring;
	private String serialNumber;
	private Integer uomTypeId;
	private String unitTypeCode;
	private String unitDesc;
	private Integer availableQty;
	private String justificationStop;
	private Integer icpcSyatemId;
	private Integer icpcId;
	private BigInteger batchId;
	private Character isMedicationStop;
	private Integer prescriptionFrequencyId;
	private List<BatchResponse> batch;
	private Integer formulationTypeId;
	private Integer noOfAdministrationTime;
	private Integer parentSubstituteId;
	private Long stockAvailable;
	private String imagePath;
	private String prefixCode;
	private String statusName;
	private String instructionText;
	private Integer statusId;
	private Integer genderId;
	private Integer durationCount;
	private String formulationType;
	private Integer covTemplateId;
	private BigInteger sum;
	private String userName;
	private BigDecimal rate;
	
	private Boolean statFlag;

	private Integer hospitalId;

	private Integer applicationId;
	private String uuid;

	private String strengthUnit;
	private Double strengthVal;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "issueDetail", cascade = CascadeType.ALL)
	private List<IssueDetailBatchMapper> issueDetailBatchMapper;
	public Integer getMedicationHistoryId() {
		return medicationHistoryId;
	}

	public void setMedicationHistoryId(Integer medicationHistoryId) {
		this.medicationHistoryId = medicationHistoryId;
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

	public Double getDoseDesc() {
		return doseDesc;
	}

	public void setDoseDesc(Double doseDesc) {
		this.doseDesc = doseDesc;
	}

	public String getStrengthDesc() {
		return strengthDesc;
	}

	public void setStrengthDesc(String strengthDesc) {
		this.strengthDesc = strengthDesc;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public List<MedicationHistoryDto> getMedicationHistoryList() {
		return medicationHistoryList;
	}

	public void setMedicationHistoryList(List<MedicationHistoryDto> medicationHistoryList) {
		this.medicationHistoryList = medicationHistoryList;
	}

	public String getName() {
		return name;
	}

	public Integer getDrugId() {
		return drugId;
	}

	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDoseUnitId() {
		return doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}

	public String getAuthorisedDate() {
		return authorisedDate;
	}

	public void setAuthorisedDate(String authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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

	public String getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(String prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Boolean getStatflag() {
		return statflag;
	}

	public void setStatflag(Boolean statflag) {
		this.statflag = statflag;
	}

	public Boolean getIsVaccine() {
		return isVaccine;
	}

	public void setIsVaccine(Boolean isVaccine) {
		this.isVaccine = isVaccine;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getEncounterDateString() {
		return encounterDateString;
	}

	public void setEncounterDateString(String encounterDateString) {
		this.encounterDateString = encounterDateString;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
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

	public Integer getAuthorisedBy() {
		return authorisedBy;
	}

	public void setAuthorisedBy(Integer authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	public Boolean getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Boolean isDischarge) {
		this.isDischarge = isDischarge;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public Integer getStrength() {
		return strength;
	}

	public void setStrength(Integer strength) {
		this.strength = strength;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getDose() {
		return dose;
	}

	public void setDose(Integer dose) {
		this.dose = dose;
	}

	public BigInteger getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(BigInteger prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Character getIsNonFormulary() {
		return isNonFormulary;
	}

	public void setIsNonFormulary(Character isNonFormulary) {
		this.isNonFormulary = isNonFormulary;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public String getPdd() {
		return pdd;
	}

	public void setPdd(String pdd) {
		this.pdd = pdd;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Character getIsMedicationContinue() {
		return isMedicationContinue;
	}

	public void setIsMedicationContinue(Character isMedicationContinue) {
		this.isMedicationContinue = isMedicationContinue;
	}


	public Character getIsMedicationStopHis() {
		return isMedicationStopHis;
	}

	public void setIsMedicationStopHis(Character isMedicationStopHis) {
		this.isMedicationStopHis = isMedicationStopHis;
	}

	public String getJustificationStopHis() {
		return justificationStopHis;
	}

	public void setJustificationStopHis(String justificationStopHis) {
		this.justificationStopHis = justificationStopHis;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getIcpcSyatemId() {
		return icpcSyatemId;
	}

	public void setIcpcSyatemId(Integer icpcSyatemId) {
		this.icpcSyatemId = icpcSyatemId;
	}

	public String getSearchICPC() {
		return searchICPC;
	}

	public void setSearchICPC(String searchICPC) {
		this.searchICPC = searchICPC;
	}

	public Integer getIcpcId() {
		return icpcId;
	}

	public void setIcpcId(Integer icpcId) {
		this.icpcId = icpcId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getGenderDesc() {
		return genderDesc;
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}

	public String getAgestring() {
		return agestring;
	}

	public void setAgestring(String agestring) {
		this.agestring = agestring;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUnitTypeCode() {
		return unitTypeCode;
	}

	public void setUnitTypeCode(String unitTypeCode) {
		this.unitTypeCode = unitTypeCode;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public Integer getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

	public Character getIsMedicationStop() {
		return isMedicationStop;
	}

	public void setIsMedicationStop(Character isMedicationStop) {
		this.isMedicationStop = isMedicationStop;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public Integer getNoOfAdministrationTime() {
		return noOfAdministrationTime;
	}

	public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
		this.noOfAdministrationTime = noOfAdministrationTime;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}

	public String getJustificationStop() {
		return justificationStop;
	}

	public void setJustificationStop(String justificationStop) {
		this.justificationStop = justificationStop;
	}

	public Integer getPrescriptionFrequencyId() {
		return prescriptionFrequencyId;
	}

	public void setPrescriptionFrequencyId(Integer prescriptionFrequencyId) {
		this.prescriptionFrequencyId = prescriptionFrequencyId;
	}

	public BigInteger getPrescriptionDetailsid() {
		return prescriptionDetailsid;
	}

	public void setPrescriptionDetailsid(BigInteger prescriptionDetailsid) {
		this.prescriptionDetailsid = prescriptionDetailsid;
	}

	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public List<IssueDetailBatchMapper> getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(List<IssueDetailBatchMapper> issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Character getIsNoSignificantMedication() {
		return isNoSignificantMedication;
	}

	public void setIsNoSignificantMedication(Character isNoSignificantMedication) {
		this.isNoSignificantMedication = isNoSignificantMedication;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public BigInteger getSum() {
		return sum;
	}

	public void setSum(BigInteger sum) {
		this.sum = sum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getDurationCount() {
		return durationCount;
	}

	public void setDurationCount(Integer durationCount) {
		this.durationCount = durationCount;
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

	public Boolean getStatFlag() {
		return statFlag;
	}

	public void setStatFlag(Boolean statFlag) {
		this.statFlag = statFlag;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(String strengthUnit) {
		this.strengthUnit = strengthUnit;
	}

	public Double getStrengthVal() {
		return strengthVal;
	}

	public void setStrengthVal(Double strengthVal) {
		this.strengthVal = strengthVal;
	}
}
