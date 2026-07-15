package com.param.global.otims.dto;

import java.util.List;

public class SubSequentDetailsDto {
	
	private Integer subSequentDetailsId;
	
	private Integer subSequentProcConfigId;
	
	private Integer sequenceId;
	
	private Integer discPercentage;
	
	private char status;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String sequenceName;
	
	private  List<SubSequentDetailsInfoDto> subSequentDetailsInfoList;
	
	private Boolean isDisabled;
	
	private Boolean skipZeroDiscount;

	public Integer getSubSequentDetailsId() {
		return subSequentDetailsId;
	}

	public void setSubSequentDetailsId(Integer subSequentDetailsId) {
		this.subSequentDetailsId = subSequentDetailsId;
	}

	public Integer getSubSequentProcConfigId() {
		return subSequentProcConfigId;
	}

	public void setSubSequentProcConfigId(Integer subSequentProcConfigId) {
		this.subSequentProcConfigId = subSequentProcConfigId;
	}

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Integer getDiscPercentage() {
		return discPercentage;
	}

	public void setDiscPercentage(Integer discPercentage) {
		this.discPercentage = discPercentage;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public List<SubSequentDetailsInfoDto> getSubSequentDetailsInfoList() {
		return subSequentDetailsInfoList;
	}

	public void setSubSequentDetailsInfoList(List<SubSequentDetailsInfoDto> subSequentDetailsInfoList) {
		this.subSequentDetailsInfoList = subSequentDetailsInfoList;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Boolean getSkipZeroDiscount() {
		return skipZeroDiscount;
	}

	public void setSkipZeroDiscount(Boolean skipZeroDiscount) {
		this.skipZeroDiscount = skipZeroDiscount;
	}

	
}
