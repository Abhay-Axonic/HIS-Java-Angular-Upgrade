package com.param.global.dto;

public class PackageListReqDto {
	private Integer packageTypeId;
	private Integer orgId;
	private Integer unitId;
	private String packageCode;
	private String packageName;
	private Integer offset;
	private Integer noOfRecordsPerPage;

	private String searchKeyword;
	
	public Integer getPackageTypeId() {
		return packageTypeId;
	}
	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
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
	public String getPackageCode() {
		return packageCode;
	}
	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}
	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}
