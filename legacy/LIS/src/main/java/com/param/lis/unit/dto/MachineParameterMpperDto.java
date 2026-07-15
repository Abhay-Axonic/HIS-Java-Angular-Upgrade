package com.param.lis.unit.dto;

import java.util.List;

public class MachineParameterMpperDto {
	
	private Integer machineParameterMpprId;

	private Integer machineId;
	
	private Integer machinParameterId;

	private Integer parameterId;

	private char status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private Integer organisationId;

	private Integer unitId;
	
	private String machineParameterDesc;
	
	private String parameterName;
	
	private String machineName;
	
	private List<MachineParameterMpperDto> machinePathalogyParameterMpperDtoList;
	private List<MachineParameterMpperDto> inactivePathalogyParameterMpperDtoList;

	public Integer getMachineParameterMpprId() {
		return machineParameterMpprId;
	}

	public void setMachineParameterMpprId(Integer machineParameterMpprId) {
		this.machineParameterMpprId = machineParameterMpprId;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public Integer getMachinParameterId() {
		return machinParameterId;
	}

	public void setMachinParameterId(Integer machinParameterId) {
		this.machinParameterId = machinParameterId;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
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

	public String getMachineParameterDesc() {
		return machineParameterDesc;
	}

	public void setMachineParameterDesc(String machineParameterDesc) {
		this.machineParameterDesc = machineParameterDesc;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public String toString() {
		return "MachineParameterMpperDto [machineParameterMpprId=" + machineParameterMpprId + ", machineId=" + machineId
				+ ", machinParameterId=" + machinParameterId + ", parameterId=" + parameterId + ", status=" + status
				+ ", createdDate=" + createdDate + ", createdBy=" + createdBy + ", updatedDate=" + updatedDate
				+ ", updatedBy=" + updatedBy + ", organisationId=" + organisationId + ", unitId=" + unitId
				+ ", machineParameterDesc=" + machineParameterDesc + ", parameterName=" + parameterName + "]";
	}

	
	public List<MachineParameterMpperDto> getMachinePathalogyParameterMpperDtoList() {
		return machinePathalogyParameterMpperDtoList;
	}

	public void setMachinePathalogyParameterMpperDtoList(
			List<MachineParameterMpperDto> machinePathalogyParameterMpperDtoList) {
		this.machinePathalogyParameterMpperDtoList = machinePathalogyParameterMpperDtoList;
	}

	public List<MachineParameterMpperDto> getInactivePathalogyParameterMpperDtoList() {
		return inactivePathalogyParameterMpperDtoList;
	}

	public void setInactivePathalogyParameterMpperDtoList(
			List<MachineParameterMpperDto> inactivePathalogyParameterMpperDtoList) {
		this.inactivePathalogyParameterMpperDtoList = inactivePathalogyParameterMpperDtoList;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	
	

}
