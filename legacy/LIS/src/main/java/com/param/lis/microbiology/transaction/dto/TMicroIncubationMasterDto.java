package com.param.lis.microbiology.transaction.dto;

import java.util.List;

public class TMicroIncubationMasterDto {


  private Integer tIncubationId;

  private Integer orgId;

  private Integer orgUnitId;

  private Integer labSampleDtlsId;

  private Character isGrowthFound;

  private Character isCompleted;

  private Integer createdBy;

  private Long createdDate;

  private Integer updatedBy;

  private Long updatedDate;

  private Character isDeleted;

  private Long incubationDueTime;
  
  private Integer sensitivityResultId;
  
  private List<TMicroIncubationDetailsMasterDto> listTMicroIncubationDetailsMaster;
  
  private List<SensitivityTestResultDetailsMasterDto> sensitivityTestResultDetailsMasterDto;
  
  private Boolean cultureTest;
  
  private Boolean triggerReleaseMail;
  
  private Integer doctorId;
  
  private Integer patientId;
	
  private String  testDesc;
  
  private Integer incubationType; 
  
  private Character partially;
 
  public Integer gettIncubationId() {
    return tIncubationId;
  }

  public void settIncubationId(Integer tIncubationId) {
    this.tIncubationId = tIncubationId;
  }

  public Integer getOrgId() {
    return orgId;
  }

  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }

  public Integer getOrgUnitId() {
    return orgUnitId;
  }

  public void setOrgUnitId(Integer orgUnitId) {
    this.orgUnitId = orgUnitId;
  }

  public Integer getLabSampleDtlsId() {
    return labSampleDtlsId;
  }

  public void setLabSampleDtlsId(Integer labSampleDtlsId) {
    this.labSampleDtlsId = labSampleDtlsId;
  }

  public Character getIsGrowthFound() {
    return isGrowthFound;
  }

  public void setIsGrowthFound(Character isGrowthFound) {
    this.isGrowthFound = (isGrowthFound == '\u0000') ? 'N' : isGrowthFound;

  }

  public Character getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(Character isCompleted) {
    this.isCompleted = (isCompleted == '\u0000') ? 'N' : isCompleted;
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

  public Integer getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Long getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Long updatedDate) {
    this.updatedDate = updatedDate;
  }

  public Character getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Character isDeleted) {
    this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;

  }

  public List<TMicroIncubationDetailsMasterDto> getListTMicroIncubationDetailsMaster() {
    return listTMicroIncubationDetailsMaster;
  }

  public void setListTMicroIncubationDetailsMaster(
      List<TMicroIncubationDetailsMasterDto> listTMicroIncubationDetailsMaster) {
    this.listTMicroIncubationDetailsMaster = listTMicroIncubationDetailsMaster;
  }

  public Long getIncubationDueTime() {
    return incubationDueTime;
  }

  public void setIncubationDueTime(Long incubationDueTime) {
    this.incubationDueTime = incubationDueTime;
  }

public List<SensitivityTestResultDetailsMasterDto> getSensitivityTestResultDetailsMasterDto() {
	return sensitivityTestResultDetailsMasterDto;
}

public void setSensitivityTestResultDetailsMasterDto(
		List<SensitivityTestResultDetailsMasterDto> sensitivityTestResultDetailsMasterDto) {
	this.sensitivityTestResultDetailsMasterDto = sensitivityTestResultDetailsMasterDto;
}

public Integer getSensitivityResultId() {
	return sensitivityResultId;
}

public void setSensitivityResultId(Integer sensitivityResultId) {
	this.sensitivityResultId = sensitivityResultId;
}

public Boolean getCultureTest() {
	return cultureTest;
}

public void setCultureTest(Boolean cultureTest) {
	this.cultureTest = cultureTest;
}

public Boolean getTriggerReleaseMail() {
	return triggerReleaseMail;
}

public void setTriggerReleaseMail(Boolean triggerReleaseMail) {
	this.triggerReleaseMail = triggerReleaseMail;
}

public Integer getDoctorId() {
	return doctorId;
}

public void setDoctorId(Integer doctorId) {
	this.doctorId = doctorId;
}

public Integer getPatientId() {
	return patientId;
}

public void setPatientId(Integer patientId) {
	this.patientId = patientId;
}

public String getTestDesc() {
	return testDesc;
}

public void setTestDesc(String testDesc) {
	this.testDesc = testDesc;
}

public Integer getIncubationType() {
	return incubationType;
}

public void setIncubationType(Integer incubationType) {
	this.incubationType = incubationType;
}

public Character getPartially() {
	return partially;
}

public void setPartially(Character partially) {
	this.partially = partially;
}



}
