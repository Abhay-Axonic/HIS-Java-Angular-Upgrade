package com.param.global.dto.global;

import java.math.BigInteger;
import java.util.List;

public class VisitorDto 
{
	private int visitorPatientId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer admissionId;
	
	private String passNumber;
	
	private Integer visitorPatientMapperId;
	
	private String expiryDate;
	
	private Character visitorPassStatus;
	
	private Integer visitorPassTypeId;
	
	private Character status;
	
	private int createdBy;
	
	private int updatedBy;
	
	private String createdDate;

	private String updatedDate;
	
	private String visitorPassTypeDesc;

	private String txtPassNo;
	
	private String txtNewDate;
	
	private String txtOldDate;
	
	private String txtReason;
	
	private Integer visitTypeId;
	
	private Integer bedCategoryId;
	
	private BigInteger noOfPassCount;
	
	private Integer noOfPassAllowed;
	
	private List<VisitorDto> visitorDtosList;
	
	private List<VisitorDto> listVisiterPassMaster;
	
	private int patientId;
	
	
	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getVisitorPatientId() {
		return visitorPatientId;
	}

	public void setVisitorPatientId(int visitorPatientId) {
		this.visitorPatientId = visitorPatientId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getPassNumber() {
		return passNumber;
	}

	public void setPassNumber(String passNumber) {
		this.passNumber = passNumber;
	}

	public Integer getVisitorPatientMapperId() {
		return visitorPatientMapperId;
	}

	public void setVisitorPatientMapperId(Integer visitorPatientMapperId) {
		this.visitorPatientMapperId = visitorPatientMapperId;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Character getVisitorPassStatus() {
		return visitorPassStatus;
	}

	public void setVisitorPassStatus(Character visitorPassStatus) {
		this.visitorPassStatus = visitorPassStatus;
	}

	public Integer getVisitorPassTypeId() {
		return visitorPassTypeId;
	}

	public void setVisitorPassTypeId(Integer visitorPassTypeId) {
		this.visitorPassTypeId = visitorPassTypeId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getVisitorPassTypeDesc() {
		return visitorPassTypeDesc;
	}

	public void setVisitorPassTypeDesc(String visitorPassTypeDesc) {
		this.visitorPassTypeDesc = visitorPassTypeDesc;
	}

	public String getTxtPassNo() {
		return txtPassNo;
	}

	public void setTxtPassNo(String txtPassNo) {
		this.txtPassNo = txtPassNo;
	}

	public String getTxtNewDate() {
		return txtNewDate;
	}

	public void setTxtNewDate(String txtNewDate) {
		this.txtNewDate = txtNewDate;
	}

	public String getTxtOldDate() {
		return txtOldDate;
	}

	public void setTxtOldDate(String txtOldDate) {
		this.txtOldDate = txtOldDate;
	}

	public String getTxtReason() {
		return txtReason;
	}

	public void setTxtReason(String txtReason) {
		this.txtReason = txtReason;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public BigInteger getNoOfPassCount() {
		return noOfPassCount;
	}

	public void setNoOfPassCount(BigInteger noOfPassCount) {
		this.noOfPassCount = noOfPassCount;
	}

	public Integer getNoOfPassAllowed() {
		return noOfPassAllowed;
	}

	public void setNoOfPassAllowed(Integer noOfPassAllowed) {
		this.noOfPassAllowed = noOfPassAllowed;
	}

	public List<VisitorDto> getVisitorDtosList() {
		return visitorDtosList;
	}

	public void setVisitorDtosList(List<VisitorDto> visitorDtosList) {
		this.visitorDtosList = visitorDtosList;
	}

	public List<VisitorDto> getListVisiterPassMaster() {
		return listVisiterPassMaster;
	}

	public void setListVisiterPassMaster(List<VisitorDto> listVisiterPassMaster) {
		this.listVisiterPassMaster = listVisiterPassMaster;
	}
	
}
