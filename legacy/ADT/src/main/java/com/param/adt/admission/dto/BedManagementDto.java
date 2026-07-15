package com.param.adt.admission.dto;

public class BedManagementDto {

	private Integer admissionId;
	private Integer patientId;
	private Integer tPatientId;
	private String admissionNumber;
	private Integer visitTypeId;
	private Integer doctorId;
	private Integer specialityId;
	private String specialityName;
	private String UHIDNumber;
	private String pFirstName;
	private Integer genderId;
	private String genderCode;
	private String dob;
	private String dFirstName;
	private String doa;
	private String pdd;
	private String associateCompanyDesc;
	private Integer paymentEntitlementId;
	private Integer contractId;
	private Integer payeeId;
	private String paymentEntitlementDesc;
	private String bedCategoryDesc;
	private Integer billingBedCategoryId;
	private Integer bedCategoryId;
	private Integer floorId;
	private String bedNumber;
	private Integer bedId;	
	private Integer wardId;	
	private Integer bedStatusId;
	private String wardName;
	private Character isShortLeave;
	private Integer patientCategoryId;
	private Integer dischargeId;
	private String labOrder;
	private String imaging;

	private String imagePath;

	private String nbm;
	private String procedureOrder;
	private String allergy;
	private Integer mewsMapperId;
	private Integer totalScore;
	private Character isLaborWard;
	private Character ishemodialysisward;
	private Integer obgynSpecialityId;
	private Integer hemodialysisSpecialityId;
	private Character isDeath; 
	private Integer roomId;
	private String drugAdministration;
    private Integer dischargeStatusId;
    private Character isEr; 
    private Integer radiologySpecialityId;
    private Integer labSpecialityId;
    private Integer organizationId;
    private Integer unitId;
    private Integer otTransfer;
    private String ageStr;
    private String housekeepingStatusDesc;
    private Integer otStatus;
    private Character isFinalBill; 
    private Integer triageCategoryId;
	private String roomDesc;
	private String pocOrders;
	private Integer interventionRequired;

	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Character getIsLaborWard() {
		return isLaborWard;
	}
	public void setIsLaborWard(Character isLaborWard) {
		this.isLaborWard = isLaborWard;
	}
	public Character getIshemodialysisward() {
		return ishemodialysisward;
	}
	public void setIshemodialysisward(Character ishemodialysisward) {
		this.ishemodialysisward = ishemodialysisward;
	}
	public Integer gettPatientId() {
		return tPatientId;
	}
	public void settPatientId(Integer tPatientId) {
		this.tPatientId = tPatientId;
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
	public String getAdmissionNumber() {
		return admissionNumber;
	}
	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getUHIDNumber() {
		return UHIDNumber;
	}
	public void setUHIDNumber(String uHIDNumber) {
		UHIDNumber = uHIDNumber;
	}
	public String getpFirstName() {
		return pFirstName;
	}
	public void setpFirstName(String pFirstName) {
		this.pFirstName = pFirstName;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getdFirstName() {
		return dFirstName;
	}
	public void setdFirstName(String dFirstName) {
		this.dFirstName = dFirstName;
	}
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getPdd() {
		return pdd;
	}
	public void setPdd(String pdd) {
		this.pdd = pdd;
	}
	public String getassociateCompanyDesc() {
		return associateCompanyDesc;
	}
	public void setassociateCompanyDesc(String associateCompanyDesc) {
		this.associateCompanyDesc = associateCompanyDesc;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}
	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}
	public String getBedCategoryDesc() {
		return bedCategoryDesc;
	}
	public void setBedCategoryDesc(String bedCategoryDesc) {
		this.bedCategoryDesc = bedCategoryDesc;
	}
	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}
	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}
	public Integer getBedCategoryId() {
		return bedCategoryId;
	}
	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}
	public Integer getFloorId() {
		return floorId;
	}
	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}
	public String getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
	public Integer getBedId() {
		return bedId;
	}
	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}
	public Integer getWardId() {
		return wardId;
	}
	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	public Integer getBedStatusId() {
		return bedStatusId;
	}
	public void setBedStatusId(Integer bedStatusId) {
		this.bedStatusId = bedStatusId;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public Character getIsShortLeave() {
		return isShortLeave;
	}
	public void setIsShortLeave(Character isShortLeave) {
		this.isShortLeave = isShortLeave;
	}
	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}
	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}
	public Integer getDischargeId() {
		return dischargeId;
	}
	public void setDischargeId(Integer dischargeId) {
		this.dischargeId = dischargeId;
	}
	public String getLabOrder() {
		return labOrder;
	}
	public void setLabOrder(String labOrder) {
		this.labOrder = labOrder;
	}
	public String getImaging() {
		return imaging;
	}
	public void setImaging(String imaging) {
		this.imaging = imaging;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getNbm() {
		return nbm;
	}
	public void setNbm(String nbm) {
		this.nbm = nbm;
	}
	public String getProcedureOrder() {
		return procedureOrder;
	}
	public void setProcedureOrder(String procedureOrder) {
		this.procedureOrder = procedureOrder;
	}
	public String getAllergy() {
		return allergy;
	}
	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}
	public Integer getMewsMapperId() {
		return mewsMapperId;
	}
	public void setMewsMapperId(Integer mewsMapperId) {
		this.mewsMapperId = mewsMapperId;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getObgynSpecialityId() {
		return obgynSpecialityId;
	}
	public void setObgynSpecialityId(Integer obgynSpecialityId) {
		this.obgynSpecialityId = obgynSpecialityId;
	}
	public Integer getHemodialysisSpecialityId() {
		return hemodialysisSpecialityId;
	}
	public void setHemodialysisSpecialityId(Integer hemodialysisSpecialityId) {
		this.hemodialysisSpecialityId = hemodialysisSpecialityId;
	}
	public Character getIsDeath() {
		return isDeath;
	}
	public void setIsDeath(Character isDeath) {
		this.isDeath = isDeath;
	}
	public String getDrugAdministration() {
		return drugAdministration;
	}
	public void setDrugAdministration(String drugAdministration) {
		this.drugAdministration = drugAdministration;
	}
	public Integer getDischargeStatusId() {
		return dischargeStatusId;
	}
	public void setDischargeStatusId(Integer dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}
	public Character getIsEr() {
		return isEr;
	}
	public void setIsEr(Character isEr) {
		this.isEr = isEr;
	}
	public Integer getRadiologySpecialityId() {
		return radiologySpecialityId;
	}
	public void setRadiologySpecialityId(Integer radiologySpecialityId) {
		this.radiologySpecialityId = radiologySpecialityId;
	}
	public Integer getLabSpecialityId() {
		return labSpecialityId;
	}
	public void setLabSpecialityId(Integer labSpecialityId) {
		this.labSpecialityId = labSpecialityId;
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
	public Integer getOtTransfer() {
		return otTransfer;
	}
	public void setOtTransfer(Integer otTransfer) {
		this.otTransfer = otTransfer;
	}
	public String getAgeStr() {
		return ageStr;
	}
	public void setAgeStr(String ageStr) {
		this.ageStr = ageStr;
	}
	public String getHousekeepingStatusDesc() {
		return housekeepingStatusDesc;
	}
	public void setHousekeepingStatusDesc(String housekeepingStatusDesc) {
		this.housekeepingStatusDesc = housekeepingStatusDesc;
	}
	public Integer getOtStatus() {
		return otStatus;
	}
	public void setOtStatus(Integer otStatus) {
		this.otStatus = otStatus;
	}
	public Character getIsFinalBill() {
		return isFinalBill;
	}
	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}
	public Integer getTriageCategoryId() {
		return triageCategoryId;
	}
	public void setTriageCategoryId(Integer triageCategoryId) {
		this.triageCategoryId = triageCategoryId;
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public String getPocOrders() {
		return pocOrders;
	}

	public void setPocOrders(String pocOrders) {
		this.pocOrders = pocOrders;
	}

	public Integer getInterventionRequired() {
		return interventionRequired;
	}

	public void setInterventionRequired(Integer interventionRequired) {
		this.interventionRequired = interventionRequired;
	}
}
