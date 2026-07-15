package com.param.global.dto;

public class SpecialityMasterDto {

	private  Character aiDoctor;
	private int specialityId;

	private Integer unitId;

	private String specialityName;

	private String specialityCode;

	private char isSurgicalCode;
	
	private char isSurgicalCodeNew;

	private char isClinicalSpeciality;

	private char status;

	private int createdBy;

	private String createdDate;

	private Integer generalLedgerId;

	private int updatedBy;

	private String updatedDate;

	private Integer organizationId;

	private Integer offset;
	
	private int unitSpecialityMapperId;

	private Integer noOfRecordsPerPage;

	private String generalLedgerName;
	
	private char isRenderingSpeciality;

	private char deptWiseAppointment;
	
	private Character isKioskDept;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private Integer storeId;
	
	private String storeName;
       
	private Integer allowedOverbookingSlots;
	
	private Integer pddDays;
	
	private String specialitySAC;

	private Double newVisitRate;

	private Double followUpVisitRate;

	private Double secondaryVisitRate;

	public String getSpecialitySAC() {
		return specialitySAC;
	}

	public void setSpecialitySAC(String specialitySAC) {
		this.specialitySAC = specialitySAC;
	}

	public Integer getPddDays() {
		return pddDays;
	}

	public void setPddDays(Integer pddDays) {
		this.pddDays = pddDays;
	}

	public Integer getAllowedOverbookingSlots() {
		return allowedOverbookingSlots;
	}

	public void setAllowedOverbookingSlots(Integer allowedOverbookingSlots) {
		this.allowedOverbookingSlots = allowedOverbookingSlots;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	private Character isNursingAssessmentRequired;
	
	public Character getIsNursingAssessmentRequired() {
		return isNursingAssessmentRequired;
	}

	public void setIsNursingAssessmentRequired(Character isNursingAssessmentRequired) {
		this.isNursingAssessmentRequired = isNursingAssessmentRequired;
	}

	public char getIsRenderingSpeciality() {
		return isRenderingSpeciality;
	}

	public void setIsRenderingSpeciality(char isRenderingSpeciality) {
		 this.isRenderingSpeciality = (isRenderingSpeciality == '\u0000') ? 'N' : isRenderingSpeciality;
	}

	public char getIsClinicalSpeciality() {
		return isClinicalSpeciality;
	}

	public void setIsClinicalSpeciality(char isClinicalSpeciality) {
		 this.isClinicalSpeciality = (isClinicalSpeciality == '\u0000') ? 'N' : isClinicalSpeciality;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public int getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public char getIsSurgicalCode() {
		return isSurgicalCode;
	}

	public void setIsSurgicalCode(char isSurgicalCode) {
		this.isSurgicalCode = (isSurgicalCode == '\u0000') ? 'N' : isSurgicalCode;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'N' : status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getGeneralLedgerName() {
		return generalLedgerName;
	}

	public void setGeneralLedgerName(String generalLedgerName) {
		this.generalLedgerName = generalLedgerName;
	}

	public char getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(char deptWiseAppointment) {
		this.deptWiseAppointment = (deptWiseAppointment == '\u0000') ? 'N' : deptWiseAppointment;
	}

	public Character getIsKioskDept() {
		return isKioskDept;
	}

	public void setIsKioskDept(Character isKioskDept) {
		this.isKioskDept = (isKioskDept==null||isKioskDept == '\u0000') ? 'N' : isKioskDept;
	}

	public int getUnitSpecialityMapperId() {
		return unitSpecialityMapperId;
	}

	public void setUnitSpecialityMapperId(int unitSpecialityMapperId) {
		this.unitSpecialityMapperId = unitSpecialityMapperId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public char getIsSurgicalCodeNew() {
		return isSurgicalCodeNew;
	}

	public void setIsSurgicalCodeNew(char isSurgicalCodeNew) {
		this.isSurgicalCodeNew = isSurgicalCodeNew;
	}

    public Double getNewVisitRate() {
        return newVisitRate;
    }

    public void setNewVisitRate(Double newVisitRate) {
        this.newVisitRate = newVisitRate;
    }

    public Double getFollowUpVisitRate() {
        return followUpVisitRate;
    }

    public void setFollowUpVisitRate(Double followUpVisitRate) {
        this.followUpVisitRate = followUpVisitRate;
    }

    public Double getSecondaryVisitRate() {
        return secondaryVisitRate;
    }

    public void setSecondaryVisitRate(Double secondaryVisitRate) {
        this.secondaryVisitRate = secondaryVisitRate;
    }

	public Character getAiDoctor() {
		return aiDoctor;
	}

	public void setAiDoctor(Character aiDoctor) {
		this.aiDoctor = aiDoctor;
	}
}
