package com.param.entity.model.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_abdm_share", schema = "abdm")
@SequenceGenerator(name = "m_abdm_share_seq", sequenceName = "abdm.m_abdm_share_seq", allocationSize = 1)

public class AbdmShareResponse {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_abdm_share_seq")
	
	@Column(name = "abdm_share_id")
	private Integer abdmShareId;

	@Column(name = "hip_id")
	private String hipId;

	@Column(name = "sbx_id")
	private String sbxId;
	
	@Column(name = "share_json")
	private String shareJson;
	
	@Column(name = "abha_id")
	private String abhaId;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "dob")
	private String dob;
	
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "token_number")
	private Integer tokenNumber;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "expired_date")
	private Date expiredDate;

	public Integer getAbdmShareId() {
		return abdmShareId;
	}

	public void setAbdmShareId(Integer abdmShareId) {
		this.abdmShareId = abdmShareId;
	}

	public String getShareJson() {
		return shareJson;
	}

	public void setShareJson(String shareJson) {
		this.shareJson = shareJson;
	}

	public String getHipId() {
		return hipId;
	}

	public void setHipId(String hipId) {
		this.hipId = hipId;
	}

	public String getSbxId() {
		return sbxId;
	}

	public void setSbxId(String sbxId) {
		this.sbxId = sbxId;
	}

	public String getAbhaId() {
		return abhaId;
	}

	public void setAbhaId(String abhaId) {
		this.abhaId = abhaId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	

}
