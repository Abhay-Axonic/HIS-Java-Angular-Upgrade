package com.param.opd.coversheet.dto;

import java.util.List;


public class IpdRylesTubeDto {
	
	private Integer ipdRylesTubeId;
	
	private Integer tubeFeedingProductId;
	
	private Integer doctorId;
	
	private Integer admissionId;
	
	private Integer patientId;
	
	private Integer frequencyId;
	
	private String rylesTubeInstruction;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String frequency;
	
	private String tubeFeedingProductsDesc;
	
	private Integer quantity;

	private List <IpdRylesTubeDto>ipdRylesTubeOrderDtoList;
	
	public Integer getIpdRylesTubeId() {
		return ipdRylesTubeId;
	}

	public void setIpdRylesTubeId(Integer ipdRylesTubeId) {
		this.ipdRylesTubeId = ipdRylesTubeId;
	}

	public Integer getTubeFeedingProductId() {
		return tubeFeedingProductId;
	}

	public void setTubeFeedingProductId(Integer tubeFeedingProductId) {
		this.tubeFeedingProductId = tubeFeedingProductId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getRylesTubeInstruction() {
		return rylesTubeInstruction;
	}

	public void setRylesTubeInstruction(String rylesTubeInstruction) {
		this.rylesTubeInstruction = rylesTubeInstruction;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public List<IpdRylesTubeDto> getIpdRylesTubeOrderDtoList() {
		return ipdRylesTubeOrderDtoList;
	}

	public void setIpdRylesTubeOrderDtoList(List<IpdRylesTubeDto> ipdRylesTubeOrderDtoList) {
		this.ipdRylesTubeOrderDtoList = ipdRylesTubeOrderDtoList;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getTubeFeedingProductsDesc() {
		return tubeFeedingProductsDesc;
	}

	public void setTubeFeedingProductsDesc(String tubeFeedingProductsDesc) {
		this.tubeFeedingProductsDesc = tubeFeedingProductsDesc;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	


}
