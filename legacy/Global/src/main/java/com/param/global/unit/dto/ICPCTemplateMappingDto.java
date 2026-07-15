package com.param.global.unit.dto;

import java.util.List;

public class ICPCTemplateMappingDto {
	
	private Integer icpcTemplateId;
	
	private Integer templateId;
	
	private Integer mIcpcSystemId;
	
	private Integer mIcpcId;
	
	private Character status;
	
	private Integer orgId;
	
	private Integer orgUnitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Character isDeleted;
	
	private Integer icpcSystemId;
	private String icpcSystemDesc;
	private String title;
	private String icpcDesc;
	private Integer noOfRecordsPerPage;
	private Integer offset;
	private Integer templateTypeId;
	private Integer id;
	private String label;
	private List<ICPCTemplateMappingDto> icpcTemplateMappingDtoList;
	private List<ICPCTemplateMappingDto> selectedSystemList;
	private List<ICPCTemplateMappingDto> selectedICPCList;
	private List<Integer> systemIdList;

	private Integer prevTemplateId;


	public Integer getIcpcTemplateId() {
		return icpcTemplateId;
	}

	public void setIcpcTemplateId(Integer icpcTemplateId) {
		this.icpcTemplateId = icpcTemplateId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getmIcpcSystemId() {
		return mIcpcSystemId;
	}

	public void setmIcpcSystemId(Integer mIcpcSystemId) {
		this.mIcpcSystemId = mIcpcSystemId;
	}

	public Integer getmIcpcId() {
		return mIcpcId;
	}

	public void setmIcpcId(Integer mIcpcId) {
		this.mIcpcId = mIcpcId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public String getIcpcSystemDesc() {
		return icpcSystemDesc;
	}

	public void setIcpcSystemDesc(String icpcSystemDesc) {
		this.icpcSystemDesc = icpcSystemDesc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcpcDesc() {
		return icpcDesc;
	}

	public void setIcpcDesc(String icpcDesc) {
		this.icpcDesc = icpcDesc;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
	public List<ICPCTemplateMappingDto> getIcpcTemplateMappingDtoList() {
		return icpcTemplateMappingDtoList;
	}

	public void setIcpcTemplateMappingDtoList(List<ICPCTemplateMappingDto> icpcTemplateMappingDtoList) {
		this.icpcTemplateMappingDtoList = icpcTemplateMappingDtoList;
	}

	public List<Integer> getSystemIdList() {
		return systemIdList;
	}

	public void setSystemIdList(List<Integer> systemIdList) {
		this.systemIdList = systemIdList;
	}

	public List<ICPCTemplateMappingDto> getSelectedSystemList() {
		return selectedSystemList;
	}

	public void setSelectedSystemList(List<ICPCTemplateMappingDto> selectedSystemList) {
		this.selectedSystemList = selectedSystemList;
	}

	public List<ICPCTemplateMappingDto> getSelectedICPCList() {
		return selectedICPCList;
	}

	public void setSelectedICPCList(List<ICPCTemplateMappingDto> selectedICPCList) {
		this.selectedICPCList = selectedICPCList;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public Integer getPrevTemplateId() {
		return prevTemplateId;
	}

	public void setPrevTemplateId(Integer prevTemplateId) {
		this.prevTemplateId = prevTemplateId;
	}

	
}
