package com.param.global.org.common.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PatientLoyaltyCardMapperDto {
	
private int patientLoyaltyCardMpprId;
	
	private Integer loyaltyCardId;
	
	private Integer patientId;	
	
	private Integer cardConfigurationId;
	
	private Integer patientCategoryId;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private char status;

	private String createdDate;

	private int createdBy;

	private int updatedBy;

	private String updatedDate;
	
	private char isDeleted;
	
	private String cardNo;
	
	private BigDecimal minStartAmount;

	private BigDecimal thresholdAmount;
	
	private BigDecimal minRechargeAmount;
	
	private char isUserDefined;
	
	private double cardAmount;
	private double rechargeAmount;
	
	private char isVerified;
	
	private Integer otp;
	
	private Date otpExpiredTime;
	
	private String firstName;
	private String lastName;

	private String mobileNumber;
	private Integer otpCount;
	private char resendOTP;
	private Date cardBlockDate;
	private String email;
	
	private double consumeAmount;
	
	private String unitName; 
	
	private String authKey;

	public int getPatientLoyaltyCardMpprId() {
		return patientLoyaltyCardMpprId;
	}

	public void setPatientLoyaltyCardMpprId(int patientLoyaltyCardMpprId) {
		this.patientLoyaltyCardMpprId = patientLoyaltyCardMpprId;
	}

	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}


	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		 this.status = (status == '\u0000') ? 'A' : status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		 this.isDeleted =  (isDeleted == '\u0000') ? 'N' : isDeleted;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getCardConfigurationId() {
		return cardConfigurationId;
	}

	public void setCardConfigurationId(Integer cardConfigurationId) {
		this.cardConfigurationId = cardConfigurationId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BigDecimal getMinStartAmount() {
		return minStartAmount;
	}

	public void setMinStartAmount(BigDecimal minStartAmount) {
		this.minStartAmount = minStartAmount;
	}

	public BigDecimal getThresholdAmount() {
		return thresholdAmount;
	}

	public void setThresholdAmount(BigDecimal thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}

	public BigDecimal getMinRechargeAmount() {
		return minRechargeAmount;
	}

	public double getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(double cardAmount) {
		this.cardAmount = cardAmount;
	}

	public char getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(char isVerified) {
		this.isVerified = isVerified;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public Date getOtpExpiredTime() {
		return otpExpiredTime;
	}

	public void setOtpExpiredTime(Date otpExpiredTime) {
		this.otpExpiredTime = otpExpiredTime;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public void setMinRechargeAmount(BigDecimal minRechargeAmount) {
		this.minRechargeAmount = minRechargeAmount;
	}

	public char getIsUserDefined() {
		return isUserDefined;
	}

	public void setIsUserDefined(char isUserDefined) {
		this.isUserDefined = (isUserDefined == '\u0000') ? 'N' : isUserDefined;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Integer getOtpCount() {
		return otpCount;
	}

	public void setOtpCount(Integer otpCount) {
		this.otpCount = otpCount;
	}

	public char getResendOTP() {
		return resendOTP;
	}

	public void setResendOTP(char resendOTP) {
		this.resendOTP = resendOTP;
	}

	public Date getCardBlockDate() {
		return cardBlockDate;
	}

	public void setCardBlockDate(Date cardBlockDate) {
		this.cardBlockDate = cardBlockDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
		
	
}
