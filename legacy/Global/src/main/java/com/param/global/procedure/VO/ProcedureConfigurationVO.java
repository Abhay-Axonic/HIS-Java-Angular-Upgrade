package com.param.global.procedure.VO;

public class ProcedureConfigurationVO {
	
	private Long procedureConfigId;
	
	private String cptCode;
	private String cptDescription;
	private int duration;
	//private Integer specialityId;
	//private Integer serviceId;
	//private Integer procedureTypeId;
	private Integer anesthesiaTypeId;
	private char status;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private String procedureType;
	private String anesthesiaType;
	private String speciality;
	private String service;
	//private String procedureTypeName;

	
	private ProcedureTypeMasterVO procedureTypeMasterVO;
	
	private SpecialityMasterVO specialityMasterVO;
	
	
	
	

	

	public ProcedureConfigurationVO(Long id, String cptCode, String cptDescription, int duration,
			Integer anesthesiaTypeId, char status, Integer createdBy, Integer orgId, Integer unitId,
			Integer updatedBy, String service, String createdDate, String updatedDate,
			ProcedureTypeMasterVO procedureTypeMasterVO, SpecialityMasterVO specialityMasterVO, String anesthesiaType) {
		this.procedureConfigId = id;
		this.cptCode = cptCode;
		this.cptDescription = cptDescription;
		this.duration = duration;
		this.anesthesiaTypeId = anesthesiaTypeId;
		this.status = status;
		this.orgId = orgId;
		this.unitId = unitId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.anesthesiaType = anesthesiaType;
		this.service = service;
		this.procedureTypeMasterVO = procedureTypeMasterVO;
		this.specialityMasterVO = specialityMasterVO;
	}
	
	
	public ProcedureConfigurationVO(int i, String cptCode, String cptDescription, int duration,
			Integer anesthesiaTypeId, char status, Integer createdBy, Integer orgId, Integer unitId,
			Integer updatedBy, String service, String createdDate, String updatedDate,
			ProcedureTypeMasterVO procedureTypeMasterVO, SpecialityMasterVO specialityMasterVO, String anesthesiaType) {
		this.procedureConfigId = (long) i;
		this.cptCode = cptCode;
		this.cptDescription = cptDescription;
		this.duration = duration;
		this.anesthesiaTypeId = anesthesiaTypeId;
		this.status = status;
		this.orgId = orgId;
		this.unitId = unitId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.anesthesiaType = anesthesiaType;
		this.service = service;
		this.procedureTypeMasterVO = procedureTypeMasterVO;
		this.specialityMasterVO = specialityMasterVO;
	}
	


	public Long getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Long procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}

	public String getCptCode() {
		return cptCode;
	}

	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}

	public String getCptDescription() {
		return cptDescription;
	}

	public void setCptDescription(String cptDescription) {
		this.cptDescription = cptDescription;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Integer getAnesthesiaTypeId() {
		return anesthesiaTypeId;
	}

	public void setAnesthesiaTypeId(Integer anesthesiaTypeId) {
		this.anesthesiaTypeId = anesthesiaTypeId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}

	public String getAnesthesiaType() {
		return anesthesiaType;
	}

	public void setAnesthesiaType(String anesthesiaType) {
		this.anesthesiaType = anesthesiaType;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public ProcedureTypeMasterVO getProcedureTypeMasterVO() {
		return procedureTypeMasterVO;
	}

	public void setProcedureTypeMasterVO(ProcedureTypeMasterVO procedureTypeMasterVO) {
		this.procedureTypeMasterVO = procedureTypeMasterVO;
	}

	public SpecialityMasterVO getSpecialityMasterVO() {
		return specialityMasterVO;
	}

	public void setSpecialityMasterVO(SpecialityMasterVO specialityMasterVO) {
		this.specialityMasterVO = specialityMasterVO;
	}

	
	

}
