package com.param.adt.discharge.dto;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DischargeSummaryDto {
	
	private Integer dischargeSummaryId;

	private Integer dischargeSummaryDetailId;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer departmentId;
	
	private Integer dischargeTemplateId;
	
	private String dischargeFieldDescription;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private String dischargeTemplateName;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer dischargeFieldId;
	
	private String dischargeFieldName;
	
	private Integer organizationId;
	
	private String admissionReason;
	
	private Integer reasonId;
	
	private Character isDischargeSummaryRelease;
	
	private String dischargePdfPath;
	
	private Integer doctorId;
	
	private String searchKeyword;
	
	private List<Integer> rmoMappedWardList;
	
	private Integer visitTypeId;
	
	private Character isDischargeSummHeadReq;
	
	private String isFollowUpAppointment;
	
	private String dischargeFieldTemplateObj;
	
	private Integer dischargeId;
	private Integer dischSumTabColId;
	private String tabColumnJson;

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
	private String pageMargineForWithoutLetterhead;
	private String dischargeSummaryHeaderLogo;
	
	private Integer dischargeSummaryDataViewOptionId;
	private Integer globalOrgId;
	private Integer globalUnitId;
	private List<DischargeSummaryTabularTemplateDataDto> DischargeSummaryTemplateListdata;

	private ByteArrayOutputStream dischargeSummaryPdfBytes;

	public String getAdmissionReason() {
		return admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}

	private List<DischargeSummaryDto> dischargeSummaryTemplateList;

	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}

	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDischargeTemplateId() {
		return dischargeTemplateId;
	}

	public void setDischargeTemplateId(Integer dischargeTemplateId) {
		this.dischargeTemplateId = dischargeTemplateId;
	}

	public String getDischargeFieldDescription() {
		return dischargeFieldDescription;
	}

	public void setDischargeFieldDescription(String dischargeFieldDescription) {
		this.dischargeFieldDescription = dischargeFieldDescription;
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

	public List<DischargeSummaryDto> getDischargeSummaryTemplateList() {
		return dischargeSummaryTemplateList;
	}

	public void setDischargeSummaryTemplateList(List<DischargeSummaryDto> dischargeSummaryTemplateList) {
		this.dischargeSummaryTemplateList = dischargeSummaryTemplateList;
	}

	public Integer getDischargeSummaryDetailId() {
		return dischargeSummaryDetailId;
	}

	public void setDischargeSummaryDetailId(Integer dischargeSummaryDetailId) {
		this.dischargeSummaryDetailId = dischargeSummaryDetailId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Character getIsDischargeSummaryRelease() {
		return isDischargeSummaryRelease;
	}

	public void setIsDischargeSummaryRelease(Character isDischargeSummaryRelease) {
		this.isDischargeSummaryRelease = isDischargeSummaryRelease;
	}

	public String getDischargePdfPath() {
		return dischargePdfPath;
	}

	public void setDischargePdfPath(String dischargePdfPath) {
		this.dischargePdfPath = dischargePdfPath;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Integer getHeaderType() {
		return headerType;
	}

	public void setHeaderType(Integer headerType) {
		this.headerType = headerType;
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

	public String getPageMargineForWithoutLetterhead() {
		return pageMargineForWithoutLetterhead;
	}

	public void setPageMargineForWithoutLetterhead(String pageMargineForWithoutLetterhead) {
		this.pageMargineForWithoutLetterhead = pageMargineForWithoutLetterhead;
	}

	public String getDischargeSummaryHeaderLogo() {
		return dischargeSummaryHeaderLogo;
	}

	public void setDischargeSummaryHeaderLogo(String dischargeSummaryHeaderLogo) {
		this.dischargeSummaryHeaderLogo = dischargeSummaryHeaderLogo;
	}

	public Integer getDischargeSummaryDataViewOptionId() {
		return dischargeSummaryDataViewOptionId;
	}

	public void setDischargeSummaryDataViewOptionId(Integer dischargeSummaryDataViewOptionId) {
		this.dischargeSummaryDataViewOptionId = dischargeSummaryDataViewOptionId;
	}

	public List<DischargeSummaryTabularTemplateDataDto> getDischargeSummaryTemplateListdata() {
		return DischargeSummaryTemplateListdata;
	}

	public void setDischargeSummaryTemplateListdata(
			List<DischargeSummaryTabularTemplateDataDto> dischargeSummaryTemplateListdata) {
		DischargeSummaryTemplateListdata = dischargeSummaryTemplateListdata;
	}

	public ByteArrayOutputStream getDischargeSummaryPdfBytes() {
		return dischargeSummaryPdfBytes;
	}

	public void setDischargeSummaryPdfBytes(ByteArrayOutputStream dischargeSummaryPdfBytes) {
		this.dischargeSummaryPdfBytes = dischargeSummaryPdfBytes;
	}

	public Integer getGlobalOrgId() {
		return globalOrgId;
	}

	public void setGlobalOrgId(Integer globalOrgId) {
		this.globalOrgId = globalOrgId;
	}

	public Integer getGlobalUnitId() {
		return globalUnitId;
	}

	public void setGlobalUnitId(Integer globalUnitId) {
		this.globalUnitId = globalUnitId;
	}
}
