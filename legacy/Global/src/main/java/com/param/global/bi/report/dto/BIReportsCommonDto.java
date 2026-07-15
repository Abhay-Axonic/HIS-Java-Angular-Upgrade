package com.param.global.bi.report.dto;

public class BIReportsCommonDto {
	private Integer offset;
	
	private String fromDate;
	
	private String toDate;
	
	private String period;
	
	private Integer noOfRecordsPerPage;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private boolean source;
	
	private boolean entitlement;
	
	private boolean revenue;
	
	private boolean count;
	
	private boolean patientCount;
	
	private boolean payments;
	
	private boolean outstandings;
	
	private boolean discount;
	
	private boolean expense;
	
	private boolean statusWiseTestCount;
	
	//dashboard
	private boolean bedOccupancy;
	private boolean totalOccupancy;
	private boolean dischargeCount;
	private boolean admissionCount;
	private String crData;
	private Integer orgId;
	
	private Integer doctorId;
	
	public boolean isStatusWiseTestCount() {
		return statusWiseTestCount;
	}

	public void setStatusWiseTestCount(boolean statusWiseTestCount) {
		this.statusWiseTestCount = statusWiseTestCount;
	}

	public boolean isExpense() {
		return expense;
	}

	public void setExpense(boolean expense) {
		this.expense = expense;
	}

	public boolean isDiscount() {
		return discount;
	}

	public void setDiscount(boolean discount) {
		this.discount = discount;
	}

	public boolean isRevenue() {
		return revenue;
	}

	public void setRevenue(boolean revenue) {
		this.revenue = revenue;
	}

	public boolean isCount() {
		return count;
	}

	public void setCount(boolean count) {
		this.count = count;
	}

	public boolean isPatientCount() {
		return patientCount;
	}

	public void setPatientCount(boolean patientCount) {
		this.patientCount = patientCount;
	}

	public boolean isPayments() {
		return payments;
	}

	public void setPayments(boolean payments) {
		this.payments = payments;
	}

	public boolean isOutstandings() {
		return outstandings;
	}

	public void setOutstandings(boolean outstandings) {
		this.outstandings = outstandings;
	}

	public boolean isSource() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	public boolean isEntitlement() {
		return entitlement;
	}

	public void setEntitlement(boolean entitlement) {
		this.entitlement = entitlement;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public boolean isBedOccupancy() {
		return bedOccupancy;
	}

	public void setBedOccupancy(boolean bedOccupancy) {
		this.bedOccupancy = bedOccupancy;
	}

	public boolean isTotalOccupancy() {
		return totalOccupancy;
	}

	public void setTotalOccupancy(boolean totalOccupancy) {
		this.totalOccupancy = totalOccupancy;
	}

	public boolean isDischargeCount() {
		return dischargeCount;
	}

	public void setDischargeCount(boolean dischargeCount) {
		this.dischargeCount = dischargeCount;
	}

	public boolean isAdmissionCount() {
		return admissionCount;
	}

	public void setAdmissionCount(boolean admissionCount) {
		this.admissionCount = admissionCount;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getCrData() {
		return crData;
	}

	public void setCrData(String crData) {
		this.crData = crData;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	
}
