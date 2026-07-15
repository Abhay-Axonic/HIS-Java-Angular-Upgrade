package com.param.global.otims.dto;

import java.util.List;

public class IntraOperativeDetailsDto {

	private Integer intraOperativeId;
	
	private Integer proReqId;
	
	private String videoUrl;
	
	private Integer procedureOrderId ;
	
	private Integer whoChecklistStatus;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private List<WhoChecklistDto>  whoChecklistDtoList;
	
	private OtCompletionTimeSheetDto otCompletionTimeSheetDto;
	
	private List<ProcedureOrderEquipmentDto>  procedureOrderEquipmentList;
	
	private Integer procedureOrederId;

	public Integer getIntraOperativeId() {
		return intraOperativeId;
	}

	public void setIntraOperativeId(Integer intraOperativeId) {
		this.intraOperativeId = intraOperativeId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Integer getWhoChecklistStatus() {
		return whoChecklistStatus;
	}

	public void setWhoChecklistStatus(Integer whoChecklistStatus) {
		this.whoChecklistStatus = whoChecklistStatus;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public List<WhoChecklistDto> getWhoChecklistDtoList() {
		return whoChecklistDtoList;
	}

	public void setWhoChecklistDtoList(List<WhoChecklistDto> whoChecklistDtoList) {
		this.whoChecklistDtoList = whoChecklistDtoList;
	}

	public OtCompletionTimeSheetDto getOtCompletionTimeSheetDto() {
		return otCompletionTimeSheetDto;
	}

	public void setOtCompletionTimeSheetDto(OtCompletionTimeSheetDto otCompletionTimeSheetDto) {
		this.otCompletionTimeSheetDto = otCompletionTimeSheetDto;
	}

	public List<ProcedureOrderEquipmentDto> getProcedureOrderEquipmentList() {
		return procedureOrderEquipmentList;
	}

	public void setProcedureOrderEquipmentList(List<ProcedureOrderEquipmentDto> procedureOrderEquipmentList) {
		this.procedureOrderEquipmentList = procedureOrderEquipmentList;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getProcedureOrederId() {
		return procedureOrederId;
	}

	public void setProcedureOrederId(Integer procedureOrederId) {
		this.procedureOrederId = procedureOrederId;
	}
	
	
}
