package com.param.lis.unit.dto;

public class MicrobiologyTestMasterDto {

  private Integer microTestId;
	 
  private Integer id;

  private String microTestCode;

  private String microTestDesc;
  
  private String code;

  private String desc;

  private Integer orgId;

  private Integer orgUnitId;

  private Integer createdBy;

  private String createdDate;

  private Integer updatedBy;

  private String updatedDate;

  private Character status;
	
  private Integer offset;

  private Integer noOfRecordsPerPage;

  public Integer getMicroTestId() {
    return microTestId;
  }

  public void setMicroTestId(Integer microTestId) {
    this.microTestId = microTestId;
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

  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

 
  public Integer getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Character getStatus() {
    return status;
  }

  public void setStatus(Character status) {
    this.status = (status == '\u0000') ? 'A' : status;
  }

  public String getMicroTestCode() {
    return microTestCode;
  }

  public void setMicroTestCode(String microTestCode) {
    this.microTestCode = microTestCode;
  }

  public String getMicroTestDesc() {
    return microTestDesc;
  }

  public void setMicroTestDesc(String microTestDesc) {
    this.microTestDesc = microTestDesc;
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

public String getCreatedDate() {
	return createdDate;
}

public void setCreatedDate(String createdDate) {
	this.createdDate = createdDate;
}

public String getUpdatedDate() {
	return updatedDate;
}

public void setUpdatedDate(String updatedDate) {
	this.updatedDate = updatedDate;
}

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public String getDesc() {
	return desc;
}

public void setDesc(String desc) {
	this.desc = desc;
}

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}


}
