package com.param.global.dto;

import java.time.LocalTime;
import java.util.List;

import com.param.global.common.GlobalCommonDateUtils;

public class IntakeOutputChartMasterDto {
	private Integer intakeOutputChartId;

	private String intakeDate;

	private String intakeTime;
	
	private String intakeTimeStr;
	
	private String outputTimeStr;
	
	private Integer intakeQuantity;
	
	private Integer totalIntake;
	
	private String outputDate;
	
	private String outputTime;
	
	private Integer outputQuantity;
	
	private Integer totalOutput;
	
	private Integer admissionId;
	
	private Integer patientId;
	
	private Integer userId;
	
	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer balance;
	
	private String startIntakeDate;

	private String startOutputDate;

	private String outputShiftName;

	private String intakeShiftName;

	private String startIntakeTime;
	
	private String startOutputTime;

	private String intakeTypeFluidDesc;
	
	private String outputTypeDesc;

	private Integer startIntakeQuantity;

	private Integer startOutputQuantity;
	
	private String fromDate;

	private String toDate;

	private Integer intakeOutputChartDetailsId;
	
	private Integer outputChartDetailsId;
	
	private Integer shiftId;
	
	private Integer intakeShiftId;
	
	private Integer outputShiftId;
	
	private List<IntakeOutputChartDetailsDto> intakeOutputChartMasterDtoList;
	
	private List<IntakeOutputChartMasterDto> intakeOutputChartMasterDetailsList;

	private List<OutputChartDetailsDto> outputChartMasterDtoList;
	
	private List<IntakeOutputChartMasterDto> intakeDateList;
	
	private Boolean isIoStart;
	private String itemName;
	private String intakeFluiddesc;
	private Integer intakeTypeFluidId;
	private Integer intakeFluidId;
	private Integer outputTypeId;
	

	public Integer getIntakeOutputChartId() {
		return intakeOutputChartId;
	}

	public void setIntakeOutputChartId(Integer intakeOutputChartId) {
		this.intakeOutputChartId = intakeOutputChartId;
	}

	public String getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(String intakeDate) {
		this.intakeDate = intakeDate;
	}

	
	public Integer getIntakeQuantity() {
		return intakeQuantity;
	}

	public void setIntakeQuantity(Integer intakeQuantity) {
		this.intakeQuantity = intakeQuantity;
	}

	public Integer getTotalIntake() {
		return totalIntake;
	}

	public void setTotalIntake(Integer totalIntake) {
		this.totalIntake = totalIntake;
	}

	public String getOutputDate() {
		return outputDate;
	}

	public void setOutputDate(String outputDate) {
		this.outputDate = outputDate;
	}

	

	public Integer getOutputQuantity() {
		return outputQuantity;
	}

	public void setOutputQuantity(Integer outputQuantity) {
		this.outputQuantity = outputQuantity;
	}

	public Integer getTotalOutput() {
		return totalOutput;
	}

	public void setTotalOutput(Integer totalOutput) {
		this.totalOutput = totalOutput;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public List<IntakeOutputChartDetailsDto> getIntakeOutputChartMasterDtoList() {
		return intakeOutputChartMasterDtoList;
	}

	public void setIntakeOutputChartMasterDtoList(List<IntakeOutputChartDetailsDto> intakeOutputChartMasterDtoList) {
		this.intakeOutputChartMasterDtoList = intakeOutputChartMasterDtoList;
	}

	

	public String getIntakeTimeStr() {
		return intakeTimeStr;
	}

	public void setIntakeTimeStr(String intakeTimeStr) {
		this.intakeTimeStr = intakeTimeStr;
	}

	public String getOutputTimeStr() {
		return outputTimeStr;
	}

	public void setOutputTimeStr(String outputTimeStr) {
		this.outputTimeStr = outputTimeStr;
	}

	public String getIntakeTime() {
		return intakeTime;
	}

	public void setIntakeTime(String intakeTime) {
		this.intakeTime = intakeTime;
	}

	public String getOutputTime() {
		return outputTime;
	}

	public void setOutputTime(String outputTime) {
		this.outputTime = outputTime;
	}

	public List<OutputChartDetailsDto> getOutputChartMasterDtoList() {
		return outputChartMasterDtoList;
	}

	public void setOutputChartMasterDtoList(List<OutputChartDetailsDto> outputChartMasterDtoList) {
		this.outputChartMasterDtoList = outputChartMasterDtoList;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getStartIntakeDate() {
		return startIntakeDate;
	}

	public void setStartIntakeDate(String startIntakeDate) {
		this.startIntakeDate = startIntakeDate;
	}

	public String getStartOutputDate() {
		return startOutputDate;
	}

	public void setStartOutputDate(String startOutputDate) {
		this.startOutputDate = startOutputDate;
	}

	public String getOutputShiftName() {
		return outputShiftName;
	}

	public void setOutputShiftName(String outputShiftName) {
		this.outputShiftName = outputShiftName;
	}

	public String getIntakeShiftName() {
		return intakeShiftName;
	}

	public void setIntakeShiftName(String intakeShiftName) {
		this.intakeShiftName = intakeShiftName;
	}

	public String getStartIntakeTime() {
		return startIntakeTime;
	}

	public void setStartIntakeTime(String startIntakeTime) {
		this.startIntakeTime = startIntakeTime;
	}

	public String getStartOutputTime() {
		return startOutputTime;
	}

	public void setStartOutputTime(String startOutputTime) {
		this.startOutputTime = startOutputTime;
	}

	public String getIntakeTypeFluidDesc() {
		return intakeTypeFluidDesc;
	}

	public void setIntakeTypeFluidDesc(String intakeTypeFluidDesc) {
		this.intakeTypeFluidDesc = intakeTypeFluidDesc;
	}

	public String getOutputTypeDesc() {
		return outputTypeDesc;
	}

	public void setOutputTypeDesc(String outputTypeDesc) {
		this.outputTypeDesc = outputTypeDesc;
	}

	public Integer getStartIntakeQuantity() {
		return startIntakeQuantity;
	}

	public void setStartIntakeQuantity(Integer startIntakeQuantity) {
		this.startIntakeQuantity = startIntakeQuantity;
	}

	public Integer getStartOutputQuantity() {
		return startOutputQuantity;
	}

	public void setStartOutputQuantity(Integer startOutputQuantity) {
		this.startOutputQuantity = startOutputQuantity;
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

	public Integer getIntakeOutputChartDetailsId() {
		return intakeOutputChartDetailsId;
	}

	public void setIntakeOutputChartDetailsId(Integer intakeOutputChartDetailsId) {
		this.intakeOutputChartDetailsId = intakeOutputChartDetailsId;
	}

	public Integer getOutputChartDetailsId() {
		return outputChartDetailsId;
	}

	public void setOutputChartDetailsId(Integer outputChartDetailsId) {
		this.outputChartDetailsId = outputChartDetailsId;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public Integer getIntakeShiftId() {
		return intakeShiftId;
	}

	public void setIntakeShiftId(Integer intakeShiftId) {
		this.intakeShiftId = intakeShiftId;
	}

	public Integer getOutputShiftId() {
		return outputShiftId;
	}

	public void setOutputShiftId(Integer outputShiftId) {
		this.outputShiftId = outputShiftId;
	}

	public List<IntakeOutputChartMasterDto> getIntakeOutputChartMasterDetailsList() {
		return intakeOutputChartMasterDetailsList;
	}

	public void setIntakeOutputChartMasterDetailsList(List<IntakeOutputChartMasterDto> intakeOutputChartMasterDetailsList) {
		this.intakeOutputChartMasterDetailsList = intakeOutputChartMasterDetailsList;
	}

	public List<IntakeOutputChartMasterDto> getIntakeDateList() {
		return intakeDateList;
	}

	public void setIntakeDateList(List<IntakeOutputChartMasterDto> intakeDateList) {
		this.intakeDateList = intakeDateList;
	}

	public Boolean getIsIoStart() {
		return isIoStart;
	}

	public void setIsIoStart(Boolean isIoStart) {
		this.isIoStart = isIoStart;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getIntakeFluiddesc() {
		return intakeFluiddesc;
	}

	public void setIntakeFluiddesc(String intakeFluiddesc) {
		this.intakeFluiddesc = intakeFluiddesc;
	}

	public Integer getIntakeTypeFluidId() {
		return intakeTypeFluidId;
	}

	public void setIntakeTypeFluidId(Integer intakeTypeFluidId) {
		this.intakeTypeFluidId = intakeTypeFluidId;
	}

	public Integer getIntakeFluidId() {
		return intakeFluidId;
	}

	public void setIntakeFluidId(Integer intakeFluidId) {
		this.intakeFluidId = intakeFluidId;
	}

	public Integer getOutputTypeId() {
		return outputTypeId;
	}

	public void setOutputTypeId(Integer outputTypeId) {
		this.outputTypeId = outputTypeId;
	}

	
}
