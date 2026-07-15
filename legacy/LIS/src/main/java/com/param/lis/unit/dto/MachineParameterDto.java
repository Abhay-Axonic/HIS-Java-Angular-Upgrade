package com.param.lis.unit.dto;

public class MachineParameterDto {
	
	private Integer machinParameterId;

	private Integer machineId;
	
	private String code;

	private String machineParameterDesc;
	
	private String machine;

	private char status;

	private Long  createdDate;

	private Integer createdBy;

	private Long updatedDate;

	private Integer updatedBy;
	
	private Integer organisationId;

	private Integer unitId;
	

	public Integer getMachinParameterId() {
		return machinParameterId;
	}

	public void setMachinParameterId(Integer machinParameterId) {
		this.machinParameterId = machinParameterId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMachineParameterDesc() {
		return machineParameterDesc;
	}

	public void setMachineParameterDesc(String machineParameterDesc) {
		this.machineParameterDesc = machineParameterDesc;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	@Override
	public String toString() {
		return "MachineParameterDto [machinParameterId=" + machinParameterId + ", machineId=" + machineId + ", code="
				+ code + ", machineParameterDesc=" + machineParameterDesc + ", machine=" + machine + ", status="
				+ status + ", createdDate=" + createdDate + ", createdBy=" + createdBy + ", updatedDate=" + updatedDate
				+ ", updatedBy=" + updatedBy + ", organisationId=" + organisationId + ", unitId=" + unitId + "]";
	}
	
	

}
