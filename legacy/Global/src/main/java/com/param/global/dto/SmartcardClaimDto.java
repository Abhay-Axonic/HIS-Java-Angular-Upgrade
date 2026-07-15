package com.param.global.dto;

import java.util.List;

import com.param.billing.global.smartcard.dto.ItemDetailsResponseDto;

public class SmartcardClaimDto {

	private Integer billingMasterId;
	
	private Integer visitTypeId;
	
	private Integer visitId;

	private Integer patientId;

	private String billNumber;

	private String billDate;

	private String visitDate;
	
	private String visitNo;
	
	private String uhidNumber;
	
	private Double totalSelfPayableAmtClaim;
	
	private Double totalCreditPayableAmtClaim;
	
	private Double totalNetPayableAmount;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private String dob;
	
	private String genderName;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	protected boolean pharamcy;

	protected Integer pharamcyServiceId;
	
	protected List<ItemDetailsResponseDto> items;//pharamcyBill
	
	private List<OrderDetailsMasterDto> orderDetailsMasterDtosList;//bill

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public Double getTotalNetPayableAmount() {
		return totalNetPayableAmount;
	}

	public void setTotalNetPayableAmount(Double totalNetPayableAmount) {
		this.totalNetPayableAmount = totalNetPayableAmount;
	}


	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Double getTotalSelfPayableAmtClaim() {
		return totalSelfPayableAmtClaim;
	}

	public void setTotalSelfPayableAmtClaim(Double totalSelfPayableAmtClaim) {
		this.totalSelfPayableAmtClaim = totalSelfPayableAmtClaim;
	}

	public Double getTotalCreditPayableAmtClaim() {
		return totalCreditPayableAmtClaim;
	}

	public void setTotalCreditPayableAmtClaim(Double totalCreditPayableAmtClaim) {
		this.totalCreditPayableAmtClaim = totalCreditPayableAmtClaim;
	}

	public List<OrderDetailsMasterDto> getOrderDetailsMasterDtosList() {
		return orderDetailsMasterDtosList;
	}

	public void setOrderDetailsMasterDtosList(List<OrderDetailsMasterDto> orderDetailsMasterDtosList) {
		this.orderDetailsMasterDtosList = orderDetailsMasterDtosList;
	}

	public Integer getVisitId() {
		return visitId;
	}

	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
	}

	public List<ItemDetailsResponseDto> getItems() {
		return items;
	}

	public void setItems(List<ItemDetailsResponseDto> items) {
		this.items = items;
	}

	public boolean isPharamcy() {
		return pharamcy;
	}

	public void setPharamcy(boolean pharamcy) {
		this.pharamcy = pharamcy;
	}


	public Integer getPharamcyServiceId() {
		return pharamcyServiceId;
	}

	public void setPharamcyServiceId(Integer pharamcyServiceId) {
		this.pharamcyServiceId = pharamcyServiceId;
	}
}
