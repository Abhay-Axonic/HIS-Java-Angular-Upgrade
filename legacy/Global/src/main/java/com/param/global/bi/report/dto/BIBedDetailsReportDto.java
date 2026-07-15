package com.param.global.bi.report.dto;

import java.math.BigInteger;

public class BIBedDetailsReportDto {
	
    private Integer organizationId;	
	private Integer unitId;
	private String doa;
	private String bedCategoryDesc; 
	private String percentage;
	private Integer bedCategoryId;
	private String bedOccupancy;
	private String totalOccupancy;
	private BigInteger dischargeCount; 
	private BigInteger admissionCount; 
	private String createdDate;
	
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
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getBedCategoryDesc() {
		return bedCategoryDesc;
	}
	public void setBedCategoryDesc(String bedCategoryDesc) {
		this.bedCategoryDesc = bedCategoryDesc;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public Integer getBedCategoryId() {
		return bedCategoryId;
	}
	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}
	public String getBedOccupancy() {
		return bedOccupancy;
	}
	public void setBedOccupancy(String bedOccupancy) {
		this.bedOccupancy = bedOccupancy;
	}
	public String getTotalOccupancy() {
		return totalOccupancy;
	}
	public void setTotalOccupancy(String totalOccupancy) {
		this.totalOccupancy = totalOccupancy;
	}
	public BigInteger getDischargeCount() {
		return dischargeCount;
	}
	public void setDischargeCount(BigInteger dischargeCount) {
		this.dischargeCount = dischargeCount;
	}
	public BigInteger getAdmissionCount() {
		return admissionCount;
	}
	public void setAdmissionCount(BigInteger admissionCount) {
		this.admissionCount = admissionCount;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	} 
	
	
	
}
