package com.param.global.procedure.VO;

public class ServiceMasterVO {

	private Integer serviceMasterId;

	private Integer organizationId;

	private String serviceStandardName;

	private String serviceStandardCode;

	private Integer specialityId;
	
	private Integer subSpecialityId;
	
	private Integer unitId;

	private String codificationServiceName;

	private String validFrom;

	private String validTill;
	
	private Integer createdBy;

	private String createdDate;

	private Character status;

	private Integer updatedBy;

	private String updatedDate;
	
	private ServiceMasterDtoVO Speciality;
	
	
	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getCodificationServiceName() {
		return codificationServiceName;
	}

	public void setCodificationServiceName(String codificationServiceName) {
		this.codificationServiceName = codificationServiceName;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTill() {
		return validTill;
	}

	public void setValidTill(String validTill) {
		this.validTill = validTill;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public ServiceMasterDtoVO getSpeciality() {
		return Speciality;
	}

	public void setSpeciality(ServiceMasterDtoVO speciality) {
		Speciality = speciality;
	}

	
	
}
