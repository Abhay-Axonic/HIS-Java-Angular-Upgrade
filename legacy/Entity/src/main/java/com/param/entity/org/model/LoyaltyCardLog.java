package com.param.entity.org.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_LOYALTI_CARD_LOG_DATA",
			query="select " + 
					"loyalty_card_log_id as \"loyaltyCardLogId\", " + 
					"mstr.loyalty_card_no as \"loyaltyCardNo\", " + 
					"mstr.otp as \"otp\", " + 
					"mstr.amount as \"amount\", " + 
					"mstr.auth_key as \"authKey\", " + 
					"mstr.otp_expired_time as \"otpExpiredTimeInDate\"," + 
					"mstr.otp_count as \"otpCount\", " + 
					"mstr.is_verified as \"isVerified\" " + 
					"from public.t_loyalty_card_log mstr where mstr.loyalty_card_status = 1 and mstr.loyalty_card_no=:cardNo and mstr.auth_key = :authKey")
})
@Entity
@Table(name="t_loyalty_card_log",schema="public")
@SequenceGenerator(name="loyalty_card_log_seq",sequenceName="public.loyalty_card_log_seq",allocationSize=1)
public class LoyaltyCardLog {

	@Id
	@Column(name="loyalty_card_log_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="loyalty_card_log_seq")
	private int loyaltyCardLogId;
	
	@Column(name="loyalty_card_no")
	private String loyaltyCardNo;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="otp")
	private String otp;
	
	@Column(name="otp_count")
	private Integer otpCount;
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name="auth_key")
	private String authKey;
	
	@Column(name="is_verified")
	private char isVerified;
	
	@Column(name="otp_expired_time")
	private Date otpExpiredTime;
	
	@Column(name="card_block_date")
	private Date cardBlockDate;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private char status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="loyalty_card_status")
	private Integer loyaltyCardStatus;
	

	public int getLoyaltyCardLogId() {
		return loyaltyCardLogId;
	}

	public void setLoyaltyCardLogId(int loyaltyCardLogId) {
		this.loyaltyCardLogId = loyaltyCardLogId;
	}

	public String getLoyaltyCardNo() {
		return loyaltyCardNo;
	}

	public void setLoyaltyCardNo(String loyaltyCardNo) {
		this.loyaltyCardNo = loyaltyCardNo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOtpCount() {
		return otpCount;
	}

	public void setOtpCount(Integer otpCount) {
		this.otpCount = otpCount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public char getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(char isVerified) {
		this.isVerified = isVerified;
	}

	public Date getOtpExpiredTime() {
		return otpExpiredTime;
	}

	public void setOtpExpiredTime(Date otpExpiredTime) {
		this.otpExpiredTime = otpExpiredTime;
	}

	public Date getCardBlockDate() {
		return cardBlockDate;
	}

	public void setCardBlockDate(Date cardBlockDate) {
		this.cardBlockDate = cardBlockDate;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public Integer getLoyaltyCardStatus() {
		return loyaltyCardStatus;
	}

	public void setLoyaltyCardStatus(Integer loyaltyCardStatus) {
		this.loyaltyCardStatus = loyaltyCardStatus;
	}
}
