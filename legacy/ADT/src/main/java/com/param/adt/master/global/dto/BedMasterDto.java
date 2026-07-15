package com.param.adt.master.global.dto;

import java.math.BigInteger;
import java.util.List;

import com.param.global.dto.MultiSelectDto;

public class BedMasterDto {
	
	private Integer bedId;
	
	private Integer admissionId;
	
	private Integer genderId;

	private Integer unitId;

	private Integer floorId;
	
	private String associateCompanyDesc;

	private String floorName;

	private Integer wardId;

	private String wardName;

	private Integer roomId;

	private Integer bedCategoryId;

	private String bedCategoryDesc;

	private Integer billingBedCategoryId;

	private String bedCode;

	private String bedNumber;

	private Integer bedStatusId;
	
	private Integer nursingStationId;

	private Character isVirtual;

	private Integer createdBy;

	private String createdDate;

	private Character status;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private String billingBedDesc;
	
	private Integer transferTypeId;
	
	private Integer housekeepingId;

	private Integer priorityId;

	private Integer assignedPersonId;
	
	private Integer housekeepingLogId;

	private String nursingStationDesc;
	
	private String priorityDesc;
	
	private Integer housekeepingStatusId;
	
	private String housekeepingStatusDesc;
	
	private String userName;
	
	private Integer rejectionReasonId;

	private String note;

	private String reasonDesc;
	
	private Integer bedCategoryWaitingListId;
	
	private Integer admissionNoteId;

	private String doa;
	
	private BigInteger waitListNumber;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private Character isEarMarking;
	
	private String earmarkingDesc;
	
	private String bedDesc;
	
	private String roomDesc;
	
	private Integer bedTypeId;
	
	private String bedStatusDesc;
	
	private Integer hierarchyId;
	
	private Integer totalScore;
	
	private Character isMovingBed;
	
	private Character isDayCare;
	
	private Integer erCategoryId;
	
	private Character isLaborWard;

	private Integer doctorId;
	
	private Integer itemId;
	
	private List<Integer> wardList;
	
	private List<Integer> bedList;
	
	private Integer radiologySpecialityId;
	
	private Integer hemodialysisSpecialityId;
	
	private Integer labSpecialityId;
	
	private Integer patientId;
	
	private Integer isClinical;

	private boolean enableDD;
	
	private Character isDeath;
	
	private Character isIp;
	private Character isEr;
    private boolean isIcuSelected;

	private String parameterValue;

	private String parameterName;

	private String paramUnit;

	private String sendingMachineName;

	private String resultDateTime;

	private Integer resultId;
	private Integer clinicalInterventionProcedureId;
	private Integer interventionProcessedDoctorId;


	public Character getIsIp() {
		return isIp;
	}

	public void setIsIp(Character isIp) {
		this.isIp = isIp;
	}

	public Integer getIsClinical() {
		return isClinical;
	}

	public void setIsClinical(Integer isClinical) {
		this.isClinical = isClinical;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getRadiologySpecialityId() {
		return radiologySpecialityId;
	}

	public void setRadiologySpecialityId(Integer radiologySpecialityId) {
		this.radiologySpecialityId = radiologySpecialityId;
	}

	public Integer getHemodialysisSpecialityId() {
		return hemodialysisSpecialityId;
	}

	public void setHemodialysisSpecialityId(Integer hemodialysisSpecialityId) {
		this.hemodialysisSpecialityId = hemodialysisSpecialityId;
	}

	public Integer getLabSpecialityId() {
		return labSpecialityId;
	}

	public void setLabSpecialityId(Integer labSpecialityId) {
		this.labSpecialityId = labSpecialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
	}

	public List<Integer> getBedList() {
		return bedList;
	}

	public void setBedList(List<Integer> bedList) {
		this.bedList = bedList;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Integer getErCategoryId() {
		return erCategoryId;
	}

	public void setErCategoryId(Integer erCategoryId) {
		this.erCategoryId = erCategoryId;
	}

	public Integer getHierarchyId() {
		return hierarchyId;
	}

	public void setHierarchyId(Integer hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	private List<MultiSelectDto> genderIdList; 
	
	public String getBedStatusDesc() {
		return bedStatusDesc;
	}

	public void setBedStatusDesc(String bedStatusDesc) {
		this.bedStatusDesc = bedStatusDesc;
	}

	public List<MultiSelectDto> getGenderIdList() {
		return genderIdList;
	}

	public void setGenderIdList(List<MultiSelectDto> genderIdList) {
		this.genderIdList = genderIdList;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public String getBedDesc() {
		return bedDesc;
	}

	public void setBedDesc(String bedDesc) {
		this.bedDesc = bedDesc;
	}

	public Character getIsEarMarking() {
		return isEarMarking;
	}

	public void setIsEarMarking(Character isEarMarking) {
		this.isEarMarking = isEarMarking;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getBedCategoryWaitingListId() {
		return bedCategoryWaitingListId;
	}

	public void setBedCategoryWaitingListId(Integer bedCategoryWaitingListId) {
		this.bedCategoryWaitingListId = bedCategoryWaitingListId;
	}

	public Integer getAdmissionNoteId() {
		return admissionNoteId;
	}

	public void setAdmissionNoteId(Integer admissionNoteId) {
		this.admissionNoteId = admissionNoteId;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public BigInteger getWaitListNumber() {
		return waitListNumber;
	}

	public void setWaitListNumber(BigInteger waitListNumber) {
		this.waitListNumber = waitListNumber;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public Integer getRejectionReasonId() {
		return rejectionReasonId;
	}

	public void setRejectionReasonId(Integer rejectionReasonId) {
		this.rejectionReasonId = rejectionReasonId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getHousekeepingId() {
		return housekeepingId;
	}

	public String getNursingStationDesc() {
		return nursingStationDesc;
	}

	public void setNursingStationDesc(String nursingStationDesc) {
		this.nursingStationDesc = nursingStationDesc;
	}

	public String getPriorityDesc() {
		return priorityDesc;
	}

	public void setPriorityDesc(String priorityDesc) {
		this.priorityDesc = priorityDesc;
	}

	public Integer getHousekeepingStatusId() {
		return housekeepingStatusId;
	}

	public void setHousekeepingStatusId(Integer housekeepingStatusId) {
		this.housekeepingStatusId = housekeepingStatusId;
	}

	public String getHousekeepingStatusDesc() {
		return housekeepingStatusDesc;
	}

	public void setHousekeepingStatusDesc(String housekeepingStatusDesc) {
		this.housekeepingStatusDesc = housekeepingStatusDesc;
	}

	public void setHousekeepingId(Integer housekeepingId) {
		this.housekeepingId = housekeepingId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getAssignedPersonId() {
		return assignedPersonId;
	}

	public void setAssignedPersonId(Integer assignedPersonId) {
		this.assignedPersonId = assignedPersonId;
	}

	public Integer getHousekeepingLogId() {
		return housekeepingLogId;
	}

	public void setHousekeepingLogId(Integer housekeepingLogId) {
		this.housekeepingLogId = housekeepingLogId;
	}

	public Integer getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(Integer transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}

	public Integer getBedStatusId() {
		return bedStatusId;
	}

	public void setBedStatusId(Integer bedStatusId) {
		this.bedStatusId = bedStatusId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedCategoryDesc() {
		return bedCategoryDesc;
	}

	public void setBedCategoryDesc(String bedCategoryDesc) {
		this.bedCategoryDesc = bedCategoryDesc;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	
	public Character getIsVirtual() {
		return isVirtual;
	}

	public void setIsVirtual(Character isVirtual) {
		this.isVirtual = isVirtual;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getEarmarkingDesc() {
		return earmarkingDesc;
	}

	public void setEarmarkingDesc(String earmarkingDesc) {
		this.earmarkingDesc = earmarkingDesc;
	}

	public Integer getBedTypeId() {
		return bedTypeId;
	}

	public void setBedTypeId(Integer bedTypeId) {
		this.bedTypeId = bedTypeId;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Character getIsMovingBed() {
		return isMovingBed;
	}

	public void setIsMovingBed(Character isMovingBed) {
		this.isMovingBed = isMovingBed;
	}

	public Character getIsDayCare() {
		return isDayCare;
	}

	public void setIsDayCare(Character isDayCare) {
		this.isDayCare = isDayCare;
	}

	public Character getIsLaborWard() {
		return isLaborWard;
	}

	public void setIsLaborWard(Character isLaborWard) {
		this.isLaborWard = isLaborWard;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public boolean isEnableDD() {
		return enableDD;
	}

	public void setEnableDD(boolean enableDD) {
		this.enableDD = enableDD;
	}	
	
	public Character getIsDeath() {
		return isDeath;
	}

	public void setIsDeath(Character isDeath) {
		this.isDeath = isDeath;
	}

	public Character getIsEr() {
		return isEr;
	}

	public void setIsEr(Character isEr) {
		this.isEr = isEr;
	}

	public String getAssociateCompanyDesc() {
		return associateCompanyDesc;
	}

	public void setAssociateCompanyDesc(String associateCompanyDesc) {
		this.associateCompanyDesc = associateCompanyDesc;
	}

	public Integer getadmissionId() {
		return admissionId;
	}

	public void setadmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public boolean getIsIcuSelected() {
		return isIcuSelected;
	}

	public void setIsIcuSelected(boolean isIcuSelected) {
		this.isIcuSelected = isIcuSelected;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParamUnit() {
		return paramUnit;
	}

	public void setParamUnit(String paramUnit) {
		this.paramUnit = paramUnit;
	}

	public String getSendingMachineName() {
		return sendingMachineName;
	}

	public void setSendingMachineName(String sendingMachineName) {
		this.sendingMachineName = sendingMachineName;
	}

	public String getResultDateTime() {
		return resultDateTime;
	}

	public void setResultDateTime(String resultDateTime) {
		this.resultDateTime = resultDateTime;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public Integer getClinicalInterventionProcedureId() {
		return clinicalInterventionProcedureId;
	}

	public void setClinicalInterventionProcedureId(Integer clinicalInterventionProcedureId) {
		this.clinicalInterventionProcedureId = clinicalInterventionProcedureId;
	}

	public Integer getInterventionProcessedDoctorId() {
		return interventionProcessedDoctorId;
	}

	public void setInterventionProcessedDoctorId(Integer interventionProcessedDoctorId) {
		this.interventionProcessedDoctorId = interventionProcessedDoctorId;
	}
}
