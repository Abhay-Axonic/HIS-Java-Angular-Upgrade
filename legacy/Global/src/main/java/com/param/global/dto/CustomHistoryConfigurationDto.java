package com.param.global.dto;

import java.util.List;

public class CustomHistoryConfigurationDto {
	private int id;
	private int specialityId;
 	private String specialityName;
 	private String category;
 	private String remark;
	private Integer visitTypeId;
	private Integer admissionId;
	private Integer encounterId;
 	private char status;
 	private int createdBy;
 	private String createdDate;
 	private List<Integer> specialityIdList;
 	private List<CustomHistoryConfigurationDto> customHistoryConfigurationList;
	public List<CustomHistoryConfigurationDto> getCustomHistoryConfigurationList() {
		return customHistoryConfigurationList;
	}
	public void setCustomHistoryConfigurationList(List<CustomHistoryConfigurationDto> customHistoryConfigurationList) {
		this.customHistoryConfigurationList = customHistoryConfigurationList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public List<Integer> getSpecialityIdList() {
		return specialityIdList;
	}
	public void setSpecialityIdList(List<Integer> specialityIdList) {
		this.specialityIdList = specialityIdList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
 	
}
