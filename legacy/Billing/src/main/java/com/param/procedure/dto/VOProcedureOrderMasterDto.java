package com.param.procedure.dto;

import java.util.List;

import com.param.global.procedure.VO.MedicationDetailsVO;

public class VOProcedureOrderMasterDto {

	private Integer procedureOrderId;
	
	private Integer serviceId;

	private char isPrimarySecondary;
	
	private char isBloodComponentRequired;

	private String procedureType;
	
	private String procedureName;
	
	private Integer operationTheatreTypeId;
	
	private Integer duration;
	
	private Integer roleId;
	
	private String roleName;
	
	private String theatreTypeName;
	
	private List<MedicationDetailsVO> medicationDetailsDto;
	
	private List<MedicationDetailsVO> implantDetailsDto;
	
	private String preferedTime;
	
	private String preferedDateTime;
	
	private Integer orgId;
	
	private Integer unitId;

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public char getIsPrimarySecondary() {
		return isPrimarySecondary;
	}

	public void setIsPrimarySecondary(char isPrimarySecondary) {
		this.isPrimarySecondary = isPrimarySecondary;
	}

	public char getIsBloodComponentRequired() {
		return isBloodComponentRequired;
	}

	public void setIsBloodComponentRequired(char isBloodComponentRequired) {
		this.isBloodComponentRequired = isBloodComponentRequired;
	}

	public String getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	

	public List<MedicationDetailsVO> getMedicationDetailsDto() {
		return medicationDetailsDto;
	}

	public void setMedicationDetailsDto(List<MedicationDetailsVO> medicationDetailsDto) {
		this.medicationDetailsDto = medicationDetailsDto;
	}

	public List<MedicationDetailsVO> getImplantDetailsDto() {
		return implantDetailsDto;
	}

	public void setImplantDetailsDto(List<MedicationDetailsVO> implantDetailsDto) {
		this.implantDetailsDto = implantDetailsDto;
	}

	public Integer getOperationTheatreTypeId() {
		return operationTheatreTypeId;
	}

	public void setOperationTheatreTypeId(Integer operationTheatreTypeId) {
		this.operationTheatreTypeId = operationTheatreTypeId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getTheatreTypeName() {
		return theatreTypeName;
	}

	public void setTheatreTypeName(String theatreTypeName) {
		this.theatreTypeName = theatreTypeName;
	}

	public String getPreferedTime() {
		return preferedTime;
	}

	public void setPreferedTime(String preferedTime) {
		this.preferedTime = preferedTime;
	}

	public String getPreferedDateTime() {
		return preferedDateTime;
	}

	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
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

	
	
	
}
