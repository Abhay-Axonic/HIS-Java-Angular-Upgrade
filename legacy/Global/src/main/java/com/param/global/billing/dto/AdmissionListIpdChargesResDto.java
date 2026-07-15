package com.param.global.billing.dto;

import java.util.Date;
import java.util.List;

public class AdmissionListIpdChargesResDto {
	private Integer admissionId;
	private String admissionNumber;
	private String doa;
	private String patientCategory;
	private String department;
	private String doctorName;
	private String bedBillingCategory;
	private String bedCategory;
	private String paymentEntitlement;
	private Integer patientId;
	private Integer patientCategoryId;
	private Integer departmentId;
	private Integer doctorId;
	private Integer bedBillingCategoryId;
	private Integer bedCategoryId;
	private Integer paymentEntitlementId;
	private String pdd;
	private String uhidNumber;
	private String wardNo;
	private String bedNo;
	private String patientName;
	private String contractName;
	private String companyDesc;
	private String  validTo;
	private Integer payeeId;
	private Integer contractId;
	private Integer dischargeStatusId;
	private Character isFinalBill;
	private Integer patientClassId;
	private String referralTypeName;
	private String referralName;
	private String referralRemark;
	private Integer bedId;
	private Integer wardId;
	private Integer roomId;
	private Integer floorId;
	private String dob;
	private Integer referralId;
	private Integer visitTypeId;
	private Character isMovingBed;
	private Character isTariffRateApplicable;
	private Integer defaultSelfTariffId;
	private Integer selfPayeeId;
	private Integer genderId;
	private List<Integer> tariffIds;
	private String remark;
	private Date doaActual;
	private Date pddActual;

	private Integer preferenceMealId;

	private String preferenceMealName;

	private Integer dietType;

	private String dietTypeDescription;

	public List<Integer> getTariffIds() {
		return tariffIds;
	}

	public void setTariffIds(List<Integer> tariffIds) {
		this.tariffIds = tariffIds;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Character getIsMovingBed() {
		return isMovingBed;
	}

	public void setIsMovingBed(Character isMovingBed) {
		this.isMovingBed = isMovingBed;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public String getReferralTypeName() {
		return referralTypeName;
	}

	public void setReferralTypeName(String referralTypeName) {
		this.referralTypeName = referralTypeName;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public String getReferralRemark() {
		return referralRemark;
	}

	public void setReferralRemark(String referralRemark) {
		this.referralRemark = referralRemark;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}
	
	public Character getIsFinalBill() {
		return isFinalBill;
	}
	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}
	public Integer getDischargeStatusId() {
		return dischargeStatusId;
	}
	public void setDischargeStatusId(Integer dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getCompanyDesc() {
		return companyDesc;
	}
	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPdd() {
		return pdd;
	}
	public void setPdd(String pdd) {
		this.pdd = pdd;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getWardNo() {
		return wardNo;
	}
	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}
	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getBedBillingCategoryId() {
		return bedBillingCategoryId;
	}
	public void setBedBillingCategoryId(Integer bedBillingCategoryId) {
		this.bedBillingCategoryId = bedBillingCategoryId;
	}
	public Integer getBedCategoryId() {
		return bedCategoryId;
	}
	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
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
	public String getAdmissionNumber() {
		return admissionNumber;
	}
	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getPatientCategory() {
		return patientCategory;
	}
	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getBedBillingCategory() {
		return bedBillingCategory;
	}
	public void setBedBillingCategory(String bedBillingCategory) {
		this.bedBillingCategory = bedBillingCategory;
	}
	public String getBedCategory() {
		return bedCategory;
	}
	public void setBedCategory(String bedCategory) {
		this.bedCategory = bedCategory;
	}
	public String getPaymentEntitlement() {
		return paymentEntitlement;
	}
	public void setPaymentEntitlement(String paymentEntitlement) {
		this.paymentEntitlement = paymentEntitlement;
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

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Character getIsTariffRateApplicable() {
		return (isTariffRateApplicable==null?'Y':isTariffRateApplicable);
	}

	public void setIsTariffRateApplicable(Character isTariffRateApplicable) {
		this.isTariffRateApplicable = (isTariffRateApplicable==null)?'Y':isTariffRateApplicable;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDoaActual() {
		return doaActual;
	}

	public void setDoaActual(Date doaActual) {
		this.doaActual = doaActual;
	}

	public Date getPddActual() {
		return pddActual;
	}

	public void setPddActual(Date pddActual) {
		this.pddActual = pddActual;
	}

	public Integer getPreferenceMealId() {
		return preferenceMealId;
	}

	public void setPreferenceMealId(Integer preferenceMealId) {
		this.preferenceMealId = preferenceMealId;
	}

	public String getPreferenceMealName() {
		return preferenceMealName;
	}

	public void setPreferenceMealName(String preferenceMealName) {
		this.preferenceMealName = preferenceMealName;
	}

	public Integer getDietType() {
		return dietType;
	}

	public void setDietType(Integer dietType) {
		this.dietType = dietType;
	}

	public String getDietTypeDescription() {
		return dietTypeDescription;
	}

	public void setDietTypeDescription(String dietTypeDescription) {
		this.dietTypeDescription = dietTypeDescription;
	}
}