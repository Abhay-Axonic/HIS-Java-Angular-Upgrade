package com.param.global.unit.dto;

import java.util.List;

public class UserRoleMapperDto {

	private Integer userRoleMpprId;
	
	private Character status;
	
	private Integer organisationId;
	
	private Integer unitId;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private Integer roleId;
	
	private Integer userId;
	
	private String userName;
	
	private String password;
	
	private String empCode;
	
	private String empName;
	
	private String roleName;
	
	private Character updateStatus;
	
	private String contactNo;
	
	private String firstName;
	
	private Integer doctorId;
	
	private String emailId;
	
	private String roleIsSelected;
	
	private Integer roleTypeId;
	
	private Integer dashboardId;
	private String dashboardDesc;
	private Integer reportGroupHeaderId;
	private Integer reportId;
	private Boolean  isOtUser;
	private String misReportIds;
	private String reportGroupHeaderName;
	private Boolean isRecoll;
	private boolean isRenderedServiceCancelled;
	private boolean isUpdatePatientReg;
	private String tabAccessIds;
	
	private String tabAccessIdsIPD;
	
	private boolean isUpdateIpdChargesRemark;
	private boolean updateDischargeSummary;
	private Boolean manualMpesa;
	
	private Boolean enableBlockPrint;
	
	private Boolean opdBillCancellation;
	private Boolean ipdBillCancellation;
	private Boolean billSettlement;
	private Boolean ipBillSettlement;
	private Boolean serviceRateEditable;
	private Boolean isForceUpdateServicePackage;
	private Boolean pharmacyBillCancellation;
	private Boolean pharmacyReceiptCancellation;
	private Boolean sampleRecollection;
	private Boolean nursingOpScan;
	private Boolean nursingIpScan;
	private Boolean doctorOpScan;
	private Boolean doctorIpScan;
	private Boolean changePayeeRights;
	
	private Boolean extendPdd;
	private Boolean ipRenderedDateChange;

	private String searchKeyword;

	private Boolean depositReceiptCancellation;
	
		public String getRoleIsSelected() {
		return roleIsSelected;
	}

	public void setRoleIsSelected(String roleIsSelected) {
		this.roleIsSelected = roleIsSelected;
	}

	// for getting the role id  from multiple selection
	private Integer id;
	
	
	private List<UserRoleMapperDto> userRoleMapperList;
	private List<ReportDetailsDto> listMISReports;
	private List<UserRoleMapperDto> selectedUserRoleList;
	private List<UserRoleMapperDto> selectedUserdasboardList;
	private Boolean isRisRecall;
	private Boolean isBillManipulationAllowed;

	
	public Boolean getIsBillManipulationAllowed() {
		return isBillManipulationAllowed;
	}

	public void setIsBillManipulationAllowed(Boolean isBillManipulationAllowed) {
		this.isBillManipulationAllowed = isBillManipulationAllowed;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getUserRoleMpprId() {
		return userRoleMpprId;
	}

	public void setUserRoleMpprId(Integer userRoleMpprId) {
		this.userRoleMpprId = userRoleMpprId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<UserRoleMapperDto> getUserRoleMapperList() {
		return userRoleMapperList;
	}

	public void setUserRoleMapperList(List<UserRoleMapperDto> userRoleMapperList) {
		this.userRoleMapperList = userRoleMapperList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Character getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(Character updateStatus) {
		this.updateStatus = updateStatus;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(Integer roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public Integer getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}

	public List<UserRoleMapperDto> getSelectedUserRoleList() {
		return selectedUserRoleList;
	}

	public void setSelectedUserRoleList(List<UserRoleMapperDto> selectedUserRoleList) {
		this.selectedUserRoleList = selectedUserRoleList;
	}

	public List<UserRoleMapperDto> getSelectedUserdasboardList() {
		return selectedUserdasboardList;
	}

	public void setSelectedUserdasboardList(List<UserRoleMapperDto> selectedUserdasboardList) {
		this.selectedUserdasboardList = selectedUserdasboardList;
	}

	public String getDashboardDesc() {
		return dashboardDesc;
	}

	public void setDashboardDesc(String dashboardDesc) {
		this.dashboardDesc = dashboardDesc;
	}

	public Boolean isOtUser() {
		return isOtUser;
	}

	public void setIsOtUser(Boolean isOtUser) {
		this.isOtUser = (isOtUser == null) ? false : isOtUser;
	}

	public String getMisReportIds() {
		return misReportIds;
	}

	public void setMisReportIds(String misReportIds) {
		this.misReportIds = misReportIds;
	}

	public String getReportGroupHeaderName() {
		return reportGroupHeaderName;
	}

	public void setReportGroupHeaderName(String reportGroupHeaderName) {
		this.reportGroupHeaderName = reportGroupHeaderName;
	}

	public Integer getReportGroupHeaderId() {
		return reportGroupHeaderId;
	}

	public void setReportGroupHeaderId(Integer reportGroupHeaderId) {
		this.reportGroupHeaderId = reportGroupHeaderId;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public List<ReportDetailsDto> getListMISReports() {
		return listMISReports;
	}

	public void setListMISReports(List<ReportDetailsDto> listMISReports) {
		this.listMISReports = listMISReports;
	}

	public Boolean getIsRecoll() {
		return isRecoll;
	}

	public void setIsRecoll(Boolean isRecoll) {
		this.isRecoll = isRecoll;
	}

	public boolean getIsRenderedServiceCancelled() {
		return isRenderedServiceCancelled;
	}

	public void setIsRenderedServiceCancelled(boolean isRenderedServiceCancelled) {
		this.isRenderedServiceCancelled = isRenderedServiceCancelled;
	}

	public boolean getIsUpdatePatientReg() {
		return isUpdatePatientReg;
	}

	public void setIsUpdatePatientReg(boolean isUpdatePatientReg) {
		this.isUpdatePatientReg = isUpdatePatientReg;
	}

	public String getTabAccessIds() {
		return tabAccessIds;
	}

	public void setTabAccessIds(String tabAccessIds) {
		this.tabAccessIds = tabAccessIds;
	}

	public String getTabAccessIdsIPD() {
		return tabAccessIdsIPD;
	}

	public void setTabAccessIdsIPD(String tabAccessIdsIPD) {
		this.tabAccessIdsIPD = tabAccessIdsIPD;
	}

	public boolean getIsUpdateIpdChargesRemark() {
		return isUpdateIpdChargesRemark;
	}

	public void setIsUpdateIpdChargesRemark(boolean isUpdateIpdChargesRemark) {
		this.isUpdateIpdChargesRemark = isUpdateIpdChargesRemark;
	}

	public boolean isUpdateDischargeSummary() {
		return updateDischargeSummary;
	}

	public void setUpdateDischargeSummary(boolean updateDischargeSummary) {
		this.updateDischargeSummary = updateDischargeSummary;
	}

	public Boolean getIsRisRecall() {
		return isRisRecall;
	}

	public void setIsRisRecall(Boolean isRisRecall) {
		this.isRisRecall = isRisRecall;
	}

	public Boolean getManualMpesa() {
		return manualMpesa;
	}

	public void setManualMpesa(Boolean manualMpesa) {
		this.manualMpesa = manualMpesa;
	}

	public Boolean getEnableBlockPrint() {
		return enableBlockPrint;
	}

	public void setEnableBlockPrint(Boolean enableBlockPrint) {
		this.enableBlockPrint = enableBlockPrint;
	}

	public Boolean getOpdBillCancellation() {
		return opdBillCancellation;
	}

	public void setOpdBillCancellation(Boolean opdBillCancellation) {
		this.opdBillCancellation = opdBillCancellation;
	}

	public Boolean getIpdBillCancellation() {
		return ipdBillCancellation;
	}

	public void setIpdBillCancellation(Boolean ipdBillCancellation) {
		this.ipdBillCancellation = ipdBillCancellation;
	}

	public Boolean getBillSettlement() {
		return billSettlement;
	}

	public void setBillSettlement(Boolean billSettlement) {
		this.billSettlement = billSettlement;
	}

	public Boolean getIpBillSettlement() {
		return ipBillSettlement;
	}

	public void setIpBillSettlement(Boolean ipBillSettlement) {
		this.ipBillSettlement = ipBillSettlement;
	}

	public Boolean getServiceRateEditable() {
		return serviceRateEditable;
	}

	public void setServiceRateEditable(Boolean serviceRateEditable) {
		this.serviceRateEditable = serviceRateEditable;
	}

	public Boolean getIsForceUpdateServicePackage() {
		return isForceUpdateServicePackage;
	}

	public void setIsForceUpdateServicePackage(Boolean isForceUpdateServicePackage) {
		this.isForceUpdateServicePackage = isForceUpdateServicePackage;
	}

	public Boolean getPharmacyBillCancellation() {
		return pharmacyBillCancellation;
	}

	public void setPharmacyBillCancellation(Boolean pharmacyBillCancellation) {
		this.pharmacyBillCancellation = pharmacyBillCancellation;
	}

	public Boolean getPharmacyReceiptCancellation() {
		return pharmacyReceiptCancellation;
	}

	public void setPharmacyReceiptCancellation(Boolean pharmacyReceiptCancellation) {
		this.pharmacyReceiptCancellation = pharmacyReceiptCancellation;
	}

	public Boolean getSampleRecollection() {
		return sampleRecollection;
	}

	public void setSampleRecollection(Boolean sampleRecollection) {
		this.sampleRecollection = sampleRecollection;
	}

	public Boolean getNursingOpScan() {
		return nursingOpScan;
	}

	public void setNursingOpScan(Boolean nursingOpScan) {
		this.nursingOpScan = nursingOpScan;
	}

	public Boolean getNursingIpScan() {
		return nursingIpScan;
	}

	public void setNursingIpScan(Boolean nursingIpScan) {
		this.nursingIpScan = nursingIpScan;
	}

	public Boolean getDoctorOpScan() {
		return doctorOpScan;
	}

	public void setDoctorOpScan(Boolean doctorOpScan) {
		this.doctorOpScan = doctorOpScan;
	}

	public Boolean getDoctorIpScan() {
		return doctorIpScan;
	}

	public void setDoctorIpScan(Boolean doctorIpScan) {
		this.doctorIpScan = doctorIpScan;
	}

	public Boolean getChangePayeeRights() {
		return changePayeeRights;
	}

	public void setChangePayeeRights(Boolean changePayeeRights) {
		this.changePayeeRights = changePayeeRights;
	}

	public Boolean getExtendPdd() {
		return extendPdd;
	}

	public void setExtendPdd(Boolean extendPdd) {
		this.extendPdd = extendPdd;
	}

	public Boolean getIpRenderedDateChange() {
		return ipRenderedDateChange;
	}

	public void setIpRenderedDateChange(Boolean ipRenderedDateChange) {
		this.ipRenderedDateChange = ipRenderedDateChange;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Boolean getDepositReceiptCancellation() {
		return depositReceiptCancellation;
	}

	public void setDepositReceiptCancellation(Boolean depositReceiptCancellation) {
		this.depositReceiptCancellation = depositReceiptCancellation;
	}
}
