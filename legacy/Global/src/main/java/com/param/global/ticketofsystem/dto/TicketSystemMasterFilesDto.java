package com.param.global.ticketofsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TicketSystemMasterFilesDto {
	
	private Integer  fileId;
	private String fileUrl;
	private Character fileStatus;
	private String fileName;
	private Integer ticketMasterId;
	private Integer unitId;
	private Integer organizationId;
	private Integer userId;
	
	
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	 
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public Integer getTicketMasterId() {
		return ticketMasterId;
	}
	public void setTicketMasterId(Integer ticketMasterId) {
		this.ticketMasterId = ticketMasterId;
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Character getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(Character fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	  

}
