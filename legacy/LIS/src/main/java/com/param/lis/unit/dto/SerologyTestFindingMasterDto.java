package com.param.lis.unit.dto;

public class SerologyTestFindingMasterDto {

	private Integer serologyId;
	private String serologyCode;
	private String serologyTest;
	private Character status;
	private Integer createdBy;
	private Long createdDate;
	private int updatedBy;
	private Long updatedDate;
	private Integer orgId;
	private Integer unitId;

	public Integer getSerologyId() {
		return serologyId;
	}

	public void setSerologyId(Integer serologyId) {
		this.serologyId = serologyId;
	}

	public String getSerologyCode() {
		return serologyCode;
	}

	public void setSerologyCode(String serologyCode) {
		this.serologyCode = serologyCode;
	}

	public String getSerologyTest() {
		return serologyTest;
	}

	public void setSerologyTest(String serologyTest) {
		this.serologyTest = serologyTest;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
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

	@Override
	public String toString() {
		return "SerologyTestMasterDto [serologyId=" + serologyId + ", serologyCode=" + serologyCode + ", serologyTest="
				+ serologyTest + ", status=" + status + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", orgId=" + orgId + ", unitId="
				+ unitId + "]";
	}

}
