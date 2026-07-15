package com.param.adt.discharge.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class DischargeSummaryTabularTemplateDataDto {
	
	private Integer dischargeSummaryTabularTemplateId;
	
	private Integer tableTypeId;
	
	private Integer dischargeTemplateId;
	
	private Integer patientId;
	
	private Integer doctorId;
	
	private Integer admissionId;
	
	private String columnName;

	private Integer coulmnId;
	
	private Character isDischargeSummaryRelease;
	
	private Integer rowId;
	
	private String rowName;
	
	private String inputValue;
	
	private Integer numberOfCoulmn;
	
	private Integer numberOfRow;
	
	private String dischargePdfPath;
	
	private Integer departmentId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer dischargeSummaryId;

	private Integer dischargeSummaryDetailId;
	
	private String dischargeFieldDescription;
	
	private String dischargeTemplateName;
	
	private Integer dischargeFieldId;
	
	private String dischargeFieldName;
	
	private String admissionReason;
	
	private Integer reasonId;
	
	private String searchKeyword;
	
	private List<Integer> rmoMappedWardList;
	
	private Integer visitTypeId;
	
	private Character isDischargeSummHeadReq;
	
	private String isFollowUpAppointment;
	
	private String dischargeFieldTemplateObj;
	
	private Integer dischargeId;
	private Integer dischSumTabColId;
	private String tabColumnJson;
	private String tableName;

	private String dischargeSummaryHeader;
	
	private Boolean isSaveDisable;
	private Boolean disableReleaseBtn;
	private Boolean disableFollowupBtn;
	private Boolean disablePrintBtn;
	
	private String crystalReportUrl;
	private Integer headerType;
	
	private Character isReportAddIntoDischargeSummary;
	
	private String admissionNo;
	
	private List<Integer> rmoMappedDoctorList;
	
	private List<DischargeSumTableColumnsMasterDto> columnsListData;
	
	private Character isHtml; 
	
	
	private List<DischargeSummaryTabularTemplateDataDto> dischargeSummaryTemplateList;
	
	private List<DischargeSummaryTabularTemplateDataDto> DischargeSummaryTemplateListdata;
	
	private Integer dischargeSummaryDataViewOptionId;
	

	public Integer getDischargeSummaryTabularTemplateId() {
		return dischargeSummaryTabularTemplateId;
	}

	public void setDischargeSummaryTabularTemplateId(Integer dischargeSummaryTabularTemplateId) {
		this.dischargeSummaryTabularTemplateId = dischargeSummaryTabularTemplateId;
	}

	public Integer getTableTypeId() {
		return tableTypeId;
	}

	public void setTableTypeId(Integer tableTypeId) {
		this.tableTypeId = tableTypeId;
	}

	public Integer getDischargeTemplateId() {
		return dischargeTemplateId;
	}

	public void setDischargeTemplateId(Integer dischargeTemplateId) {
		this.dischargeTemplateId = dischargeTemplateId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getCoulmnId() {
		return coulmnId;
	}

	public void setCoulmnId(Integer coulmnId) {
		this.coulmnId = coulmnId;
	}

	public Character getIsDischargeSummaryRelease() {
		return isDischargeSummaryRelease;
	}

	public void setIsDischargeSummaryRelease(Character isDischargeSummaryRelease) {
		this.isDischargeSummaryRelease = isDischargeSummaryRelease;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public Integer getNumberOfCoulmn() {
		return numberOfCoulmn;
	}

	public void setNumberOfCoulmn(Integer numberOfCoulmn) {
		this.numberOfCoulmn = numberOfCoulmn;
	}

	public Integer getNumberOfRow() {
		return numberOfRow;
	}

	public void setNumberOfRow(Integer numberOfRow) {
		this.numberOfRow = numberOfRow;
	}

	public String getDischargePdfPath() {
		return dischargePdfPath;
	}

	public void setDischargePdfPath(String dischargePdfPath) {
		this.dischargePdfPath = dischargePdfPath;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}

	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}

	public List<DischargeSummaryTabularTemplateDataDto> getDischargeSummaryTemplateList() {
		return dischargeSummaryTemplateList;
	}

	public void setDischargeSummaryTemplateList(List<DischargeSummaryTabularTemplateDataDto> dischargeSummaryTemplateList) {
		this.dischargeSummaryTemplateList = dischargeSummaryTemplateList;
	}

	public List<DischargeSummaryTabularTemplateDataDto> getDischargeSummaryTemplateListdata() {
		return DischargeSummaryTemplateListdata;
	}

	public void setDischargeSummaryTemplateListdata(
			List<DischargeSummaryTabularTemplateDataDto> dischargeSummaryTemplateListdata) {
		DischargeSummaryTemplateListdata = dischargeSummaryTemplateListdata;
	}

	public Integer getDischargeSummaryDetailId() {
		return dischargeSummaryDetailId;
	}

	public void setDischargeSummaryDetailId(Integer dischargeSummaryDetailId) {
		this.dischargeSummaryDetailId = dischargeSummaryDetailId;
	}

	public String getDischargeFieldDescription() {
		return dischargeFieldDescription;
	}

	public void setDischargeFieldDescription(String dischargeFieldDescription) {
		this.dischargeFieldDescription = dischargeFieldDescription;
	}

	public String getDischargeTemplateName() {
		return dischargeTemplateName;
	}

	public void setDischargeTemplateName(String dischargeTemplateName) {
		this.dischargeTemplateName = dischargeTemplateName;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public String getDischargeFieldName() {
		return dischargeFieldName;
	}

	public void setDischargeFieldName(String dischargeFieldName) {
		this.dischargeFieldName = dischargeFieldName;
	}

	public String getAdmissionReason() {
		return admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public List<Integer> getRmoMappedWardList() {
		return rmoMappedWardList;
	}

	public void setRmoMappedWardList(List<Integer> rmoMappedWardList) {
		this.rmoMappedWardList = rmoMappedWardList;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Character getIsDischargeSummHeadReq() {
		return isDischargeSummHeadReq;
	}

	public void setIsDischargeSummHeadReq(Character isDischargeSummHeadReq) {
		this.isDischargeSummHeadReq = isDischargeSummHeadReq;
	}

	public String getIsFollowUpAppointment() {
		return isFollowUpAppointment;
	}

	public void setIsFollowUpAppointment(String isFollowUpAppointment) {
		this.isFollowUpAppointment = isFollowUpAppointment;
	}

	public String getDischargeFieldTemplateObj() {
		return dischargeFieldTemplateObj;
	}

	public void setDischargeFieldTemplateObj(String dischargeFieldTemplateObj) {
		this.dischargeFieldTemplateObj = dischargeFieldTemplateObj;
	}

	public Integer getDischargeId() {
		return dischargeId;
	}

	public void setDischargeId(Integer dischargeId) {
		this.dischargeId = dischargeId;
	}

	public Integer getDischSumTabColId() {
		return dischSumTabColId;
	}

	public void setDischSumTabColId(Integer dischSumTabColId) {
		this.dischSumTabColId = dischSumTabColId;
	}

	public String getTabColumnJson() {
		return tabColumnJson;
	}

	public void setTabColumnJson(String tabColumnJson) {
		this.tabColumnJson = tabColumnJson;
	}

	public String getDischargeSummaryHeader() {
		return dischargeSummaryHeader;
	}

	public void setDischargeSummaryHeader(String dischargeSummaryHeader) {
		this.dischargeSummaryHeader = dischargeSummaryHeader;
	}

	public Boolean getIsSaveDisable() {
		return isSaveDisable;
	}

	public void setIsSaveDisable(Boolean isSaveDisable) {
		this.isSaveDisable = isSaveDisable;
	}

	public Boolean getDisableReleaseBtn() {
		return disableReleaseBtn;
	}

	public void setDisableReleaseBtn(Boolean disableReleaseBtn) {
		this.disableReleaseBtn = disableReleaseBtn;
	}

	public Boolean getDisableFollowupBtn() {
		return disableFollowupBtn;
	}

	public void setDisableFollowupBtn(Boolean disableFollowupBtn) {
		this.disableFollowupBtn = disableFollowupBtn;
	}

	public Boolean getDisablePrintBtn() {
		return disablePrintBtn;
	}

	public void setDisablePrintBtn(Boolean disablePrintBtn) {
		this.disablePrintBtn = disablePrintBtn;
	}

	public String getCrystalReportUrl() {
		return crystalReportUrl;
	}

	public void setCrystalReportUrl(String crystalReportUrl) {
		this.crystalReportUrl = crystalReportUrl;
	}

	public Integer getHeaderType() {
		return headerType;
	}

	public void setHeaderType(Integer headerType) {
		this.headerType = headerType;
	}

	public Character getIsReportAddIntoDischargeSummary() {
		return isReportAddIntoDischargeSummary;
	}

	public void setIsReportAddIntoDischargeSummary(Character isReportAddIntoDischargeSummary) {
		this.isReportAddIntoDischargeSummary = isReportAddIntoDischargeSummary;
	}

	public String getAdmissionNo() {
		return admissionNo;
	}

	public void setAdmissionNo(String admissionNo) {
		this.admissionNo = admissionNo;
	}

	public List<Integer> getRmoMappedDoctorList() {
		return rmoMappedDoctorList;
	}

	public void setRmoMappedDoctorList(List<Integer> rmoMappedDoctorList) {
		this.rmoMappedDoctorList = rmoMappedDoctorList;
	}

	public List<DischargeSumTableColumnsMasterDto> getColumnsListData() {
		return columnsListData;
	}

	public void setColumnsListData(List<DischargeSumTableColumnsMasterDto> columnsListData) {
		this.columnsListData = columnsListData;
	}

	public Character getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(Character isHtml) {
		this.isHtml = isHtml;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getDischargeSummaryDataViewOptionId() {
		return dischargeSummaryDataViewOptionId;
	}

	public void setDischargeSummaryDataViewOptionId(Integer dischargeSummaryDataViewOptionId) {
		this.dischargeSummaryDataViewOptionId = dischargeSummaryDataViewOptionId;
	}


	
	
}
