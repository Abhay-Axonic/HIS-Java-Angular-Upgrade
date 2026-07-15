package com.param.opd.coversheet.dto;

import java.util.List;

import javax.persistence.Column;


public class InfusionSolutionDto {
	private Integer infusionSolutionId;
	
	private Integer itemId;
	
	private Integer genericId;
	
	private Integer strengthUnitId;
	
	private Integer roaId;
	
	private Integer frequencyId;
	
	private Integer priority;
	
	private String startDate;
	
	private String endDate;
	
	private Integer totalVolume;
	
	private Integer duration;
	
	
	private String comments;
	
	private Integer instructionId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	private String solutionName;

	private String genericName;
	
	private List <InfusionAdditiveDto> infusionAdditiveDtosList;
	private List <PrescriptionDetailsDto> PrescriptionDetailsDto;

	private String additiveName;
	
	private Integer solutionItemId;
	
	private Integer additiveItemId;
	
	private String name;
	
	private String solutiongenericName;
	
	private String additivegenericName;
	
	private Integer formulationTypeId;
	
	private Integer solutiongenericId;
	
	private Integer additivegenericId;
	
	private Double solutionVolume;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer volume;
	private Integer doctorId;
	private Integer visitTypeId;

	private String frequencyDesc;
	private String solutionUnit;
	private Double infusionRate;
	private String roaDescription;
	private Double strength;
	private Character isStartFlag;
	private Integer deptId;
	private String justificationStopInfusion;
	private Integer prescriptionId;
	private Integer prescriptionDetailsid;
	private String userName;
	private Integer storeId;

    private List <PrescriptionDetailsDto> prescriptionDetailsList;

	private Integer infusionQuantity;


	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Integer totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
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

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getAdditiveName() {
		return additiveName;
	}

	public void setAdditiveName(String additiveName) {
		this.additiveName = additiveName;
	}

	public Integer getSolutionItemId() {
		return solutionItemId;
	}

	public void setSolutionItemId(Integer solutionItemId) {
		this.solutionItemId = solutionItemId;
	}

	public Integer getAdditiveItemId() {
		return additiveItemId;
	}

	public void setAdditiveItemId(Integer additiveItemId) {
		this.additiveItemId = additiveItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public String getSolutiongenericName() {
		return solutiongenericName;
	}

	public void setSolutiongenericName(String solutiongenericName) {
		this.solutiongenericName = solutiongenericName;
	}

	public String getAdditivegenericName() {
		return additivegenericName;
	}

	public void setAdditivegenericName(String additivegenericName) {
		this.additivegenericName = additivegenericName;
	}

	public Integer getSolutiongenericId() {
		return solutiongenericId;
	}

	public void setSolutiongenericId(Integer solutiongenericId) {
		this.solutiongenericId = solutiongenericId;
	}

	public Integer getAdditivegenericId() {
		return additivegenericId;
	}

	public void setAdditivegenericId(Integer additivegenericId) {
		this.additivegenericId = additivegenericId;
	}
	
	public List<InfusionAdditiveDto> getInfusionAdditiveDtosList() {
		return infusionAdditiveDtosList;
	}

	public void setInfusionAdditiveDtosList(List<InfusionAdditiveDto> infusionAdditiveDtosList) {
		this.infusionAdditiveDtosList = infusionAdditiveDtosList;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public String getSolutionUnit() {
		return solutionUnit;
	}

	public void setSolutionUnit(String solutionUnit) {
		this.solutionUnit = solutionUnit;
	}

	public Double getInfusionRate() {
		return infusionRate;
	}

	public void setInfusionRate(Double infusionRate) {
		this.infusionRate = infusionRate;
	}

	public String getRoaDescription() {
		return roaDescription;
	}

	public void setRoaDescription(String roaDescription) {
		this.roaDescription = roaDescription;
	}

	public Character getIsStartFlag() {
		return isStartFlag;
	}

	public void setIsStartFlag(Character isStartFlag) {
		this.isStartFlag = isStartFlag;
	}

	public String getJustificationStopInfusion() {
		return justificationStopInfusion;
	}

	public void setJustificationStopInfusion(String justificationStopInfusion) {
		this.justificationStopInfusion = justificationStopInfusion;
	}

	public Double getStrength() {
		return strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Double getSolutionVolume() {
		return solutionVolume;
	}

	public void setSolutionVolume(Double solutionVolume) {
		this.solutionVolume = solutionVolume;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public List<PrescriptionDetailsDto> getPrescriptionDetailsDto() {
		return PrescriptionDetailsDto;
	}

	public void setPrescriptionDetailsDto(List<PrescriptionDetailsDto> prescriptionDetailsDto) {
		PrescriptionDetailsDto = prescriptionDetailsDto;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getPrescriptionDetailsid() {
		return prescriptionDetailsid;
	}

	public void setPrescriptionDetailsid(Integer prescriptionDetailsid) {
		this.prescriptionDetailsid = prescriptionDetailsid;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}


	public List<PrescriptionDetailsDto> getPrescriptionDetailsList() {
		return prescriptionDetailsList;
	}

	public void setPrescriptionDetailsList(List<PrescriptionDetailsDto> prescriptionDetailsList) {
		this.prescriptionDetailsList = prescriptionDetailsList;
	}
	
	public Integer getInfusionQuantity() {
		return infusionQuantity;
	}

	public void setInfusionQuantity(Integer infusionQuantity) {
		this.infusionQuantity = infusionQuantity;

	}
	
	
}
